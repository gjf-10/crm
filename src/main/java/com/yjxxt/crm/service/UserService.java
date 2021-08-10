package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.mapper.UserRoleMapper;
import com.yjxxt.crm.query.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserService extends BaseService<User, Integer> {
    @Autowired
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    public UserModel login(String userName, String userPwd){
        isEmptyCheck(userName, userPwd);
        User user = userMapper.selectUserByName(userName);
        isExistUser(user);
        checkPwd(userPwd, user.getUserPwd());
        return buildUserModel(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void changePwd(int userId, String oldPwd, String newPwd, String againPwd){
        // 判断用户是否存在
        User user = userMapper.selectByPrimaryKey(userId);
        isExistUser(user);
        // 判断用户密码是否正确
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)), "用户旧密码输入错误");
        // 判断输入参数是否为空；新密码和确认密码是否一致
        isEmptyCheck(oldPwd, newPwd, againPwd);
        // 设置用户密码为修改后的密码
        user.setUserPwd(Md5Util.encode(newPwd));
        // 更新在数据库中
        userMapper.updateByPrimaryKeySelective(user);
    }

    private void checkPwd(String userPwd, String userPwd1) {
        userPwd = Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(userPwd1), "密码错误");
    }

    private UserModel buildUserModel(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    private void isExistUser(User user) {
        AssertUtil.isTrue(user==null, "用户不存在");
    }

    private void isEmptyCheck(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "密码不能为空");
    }

    private void isEmptyCheck(String oldPwd, String newPwd, String againPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "旧密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(againPwd), "确认密码不能为空");
        AssertUtil.isTrue(oldPwd.equals(newPwd), "新密码和旧密码不能相同");
        AssertUtil.isTrue(!newPwd.equals(againPwd), "确认密码和新密码不一致");
    }

    public List<Map<String, Object>> saleMan(){
        return userMapper.selectAllSales();
    }

    public Map<String, Object> queryAllUser(UserQuery userQuery){
        Map<String, Object> map = new HashMap<>();
        // 构建分页
        PageHelper.startPage(userQuery.getPage(), userQuery.getLimit());
        List<User> plist = userMapper.selectAllUser(userQuery);
        System.out.println(plist);
        PageInfo<User> pageInfo = new PageInfo<>(plist);
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    /*
    1. 参数校验
    * ⽤户名 ⾮空 唯⼀性
    * 邮箱 ⾮空
    * ⼿机号 ⾮空 格式合法
    * 2. 设置默认参数
    * isValid 1
    * creteDate 当前时间
    * updateDate 当前时间
    * userPwd 123456 -> md5加密
    * 3. 执⾏添加，判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(User user){
        // 1. 参数校验，用户名，邮箱，手机号
        AssertUtil.isTrue(user == null, "参数为空");
        AssertUtil.isTrue(userMapper.selectUserByName(user.getUserName()) != null, "用户名已存在");
        paramsCheck(user.getUserName(), user.getEmail(), user.getPhone(), user.getTrueName());
        // 2. 设置默认参数
        user.setIsValid(1);
        user.setUserPwd(Md5Util.encode("123456"));
        user.setUpdateDate(new Date());
        user.setCreateDate(new Date());
        // 3. 执行添加
        AssertUtil.isTrue( userMapper.insertHasKey(user) < 1, "添加失败");
        relateUserRoleAdd(user.getId(), user.getRoleIds());

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void relateUserRoleAdd(Integer id, String roleIds) {

        // 查出当前用户的角色数量
        Integer count = userRoleMapper.countRoleByUserId(id);
        if(count > 0){
            // 有角色就删除所有角色
            AssertUtil.isTrue(userRoleMapper.deleteByUserId(id) != count, "用户角色修改失败");
        }
        // 判断roleIds 是否存在
        List<UserRole> list = new ArrayList<>();
        if(StringUtils.isNotBlank(roleIds)){
            // roleIds 不为空
            String[] arrIds = roleIds.split(",");
            for(String roleId: arrIds){
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(id);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                list.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(list) != arrIds.length, "用户角色添加失败");
        }
    }

    private void paramsCheck(String userName, String email, String phone, String trueName) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(trueName), "用户名真实姓名不能为空");

        AssertUtil.isTrue(StringUtils.isBlank(email), "邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone), "手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号不合法");
    }


    /**
     * 更新⽤户
     * 1. 参数校验
     * id ⾮空 记录必须存在
     * ⽤户名 ⾮空 唯⼀性
     * email ⾮空
     * ⼿机号 ⾮空 格式合法
     * 2. 设置默认参数
     * updateDate
     * 3. 执⾏更新，判断结果
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(User user){
        // 1. 参数校验，用户名，邮箱，手机号
        AssertUtil.isTrue(user == null, "参数为空");
        paramsCheck(user.getUserName(), user.getEmail(), user.getPhone(), user.getTrueName());
        User byId = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(byId == null, "待更新记录不存在");

        // 张三11 id = 1  张三 id = 1  张三 id = 2
        User byName = userMapper.selectUserByName(user.getUserName());
        AssertUtil.isTrue(byName != null && !byName.getId().equals(byId.getId()), "用户名已存在");
        // 2. 设置默认参数
        user.setUpdateDate(new Date());
        // 3. 执行更新
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "更新失败");
        relateUserRoleAdd(user.getId(), user.getRoleIds());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void del(Integer[] ids){
        // 删除根据userId删除所有roleIds
        System.out.println(ids);
        for(int userId : ids){
            Integer count = userRoleMapper.countRoleByUserId(userId);
            if(count > 0){
                AssertUtil.isTrue(userRoleMapper.deleteByUserId(userId) != count, "用户角色删除失败");
            }
        }
        AssertUtil.isTrue(userMapper.deleteBatch(ids) < 1, "删除失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void changeInfo(User user){
        AssertUtil.isTrue(user == null, "传入参数为空");
        AssertUtil.isTrue(userMapper.selectByPrimaryKey(user.getId()) == null,"用户记录不存在");
        user.setUpdateDate(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }
}

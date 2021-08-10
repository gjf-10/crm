package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Modules;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.mapper.ModulesMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.mapper.RoleMapper;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role, Integer> {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModulesMapper modulesMapper;

    public List<Map<String, Object>> queryAllRole(Integer userId){
        return roleMapper.selectAllRole(userId);
    }

    public Map<String, Object> queryAll(RoleQuery roleQuery){

        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(roleQuery.getPage(), roleQuery.getLimit());
        List<Role> roles = roleMapper.selectAll(roleQuery);
        PageInfo<Role> pageInfo = new PageInfo<>(roles);
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void add(Role role) {
        // role 不为null
        AssertUtil.isTrue(role == null,"参数不存在");
        // roleName 为空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "请输入角色名");
        // 角色名已存在
        AssertUtil.isTrue(roleMapper.selectByRoleName(role.getRoleName()) != null, "该角色已近存在");
        // 默认值设置

        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());

        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色添加失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Role role){
        // 判断参数是否存在
        AssertUtil.isTrue(role == null || null == roleMapper.selectByPrimaryKey(role.getId()), "待修改角色不存在");
        // 根据角色名查role
        AssertUtil.isTrue(role.getRoleName() == null, "请输入角色名");
        Role role1 = roleMapper.selectByRoleName(role.getRoleName());
        // 检查角色名是否重复
        AssertUtil.isTrue(role1 != null && !role1.getId().equals(role.getId()), "该角色已经存在");
        // 设置修改时间
        role.setUpdateDate(new Date());
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void del(Integer roleId){
        // roleId 存在
        AssertUtil.isTrue(roleId == null, "待删除角色参数有误");
        // 待删除数据存在
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(roleId) == null, "待删除角色不存在");
        // 更新字段is_valid = 0
        AssertUtil.isTrue(roleMapper.changeToNotValid(roleId) < 1, "删除失败");
        // 待授权角色现在拥有的权限有哪些
        Integer count = permissionMapper.countRolePermissions(roleId);
        if(count != null && count > 0){
            // 删除已存在的所有权限，准备重新添加新权限
            AssertUtil.isTrue(permissionMapper.deleteRolePermissionsByRoleId(roleId)!=count, "角色授权失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void grantRole(Integer roleId, Integer[] mids){
        // 待授权角色是否存
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(roleId) == null, "待授权角色不存在");
        // 待授权角色现在拥有的权限有哪些
        Integer count = permissionMapper.countRolePermissions(roleId);
        if(count != null && count > 0){
            // 删除已存在的所有权限，准备重新添加新权限
            AssertUtil.isTrue(permissionMapper.deleteRolePermissionsByRoleId(roleId)!=count, "角色授权失败");
        }

        // 准备新增加的权限数据
        if(mids != null && mids.length > 0){
            List<Permission> plist = new ArrayList<>();
            for(Integer mid: mids){
                Permission p = new Permission();
                p.setCreateDate(new Date());
                p.setUpdateDate(new Date());
                p.setRoleId(roleId);
                p.setModuleId(mid);
                String aclValue = modulesMapper.selectAclValueByMid(mid);
                AssertUtil.isTrue(StringUtils.isBlank(aclValue), "角色授权失败（权限码获取失败）");
                p.setAclValue(aclValue);
                plist.add(p);
            }
            // 添加权限
            AssertUtil.isTrue(permissionMapper.insertBatch(plist) != plist.size(), "角色授权失败(添加失败)");
        }

    }
}

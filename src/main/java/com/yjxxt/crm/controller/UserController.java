package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotation.PermissionRequired;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.exceptions.ParamsException;
import com.yjxxt.crm.query.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;


    @GetMapping("login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd){
        ResultInfo res = new ResultInfo();
        try{
            UserModel userModel = userService.login(userName, userPwd);

            res.setResult(userModel);
        }catch(ParamsException e){
            e.printStackTrace();
            res.setCode(e.getCode());
            res.setMsg(e.getMsg());
        }catch(Exception e){
            e.printStackTrace();
            res.setCode(500);
            res.setMsg("登录失败");
        }
        return res;
    }

    @PutMapping("updatePassword")
    @ResponseBody
    public ResultInfo changePwd(HttpServletRequest req, String oldPwd, String newPwd, String againPwd){
        ResultInfo res = new ResultInfo();
        try{
            int userId = LoginUserUtil.releaseUserIdFromCookie(req);
            userService.changePwd(userId, oldPwd, newPwd, againPwd);
        }catch(ParamsException e){
            e.printStackTrace();
            res.setCode(e.getCode());
            res.setMsg(e.getMsg());
        }catch(Exception e){
            e.printStackTrace();
            res.setCode(500);
            res.setMsg("密码修改失败");
        }
        return res;
    }

    @RequestMapping("assign_man")
    @ResponseBody
    public List<Map<String, Object>> querySales(){
        return userService.saleMan();
    }

    @RequestMapping("index")
    public String index(){
        return "user/user01";
    }

    @RequestMapping("list")
    @ResponseBody
    @PermissionRequired("601002")
    public Map<String, Object> list(UserQuery userQuery){

        return userService.queryAllUser(userQuery);
    }

    @RequestMapping("add_update")
    public String addOrUpdate(Integer userId, Model model){
        if(userId != null){
            User user = userService.selectByPrimaryKey(userId);
            AssertUtil.isTrue(user == null, "修改用户不存在");
            model.addAttribute("user", user);
        }
        return "user/add_update01";
    }

    @RequestMapping("add")
    @ResponseBody
    @PermissionRequired("601001")
    public ResultInfo add(User user){
        System.out.println("被访问了");
        userService.save(user);
        return success("添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    @PermissionRequired("601003")
    public ResultInfo update(User user){
        System.out.println("被访问l1111111");
        userService.update(user);
        return success("修改成功");
    }

    @RequestMapping("del")
    @ResponseBody
    @PermissionRequired("601004")
    public ResultInfo del(Integer[] ids){
        userService.del(ids);
        return success("删除成功");
    }

    @RequestMapping("toSettingPage")
    public String settingPage(HttpServletRequest req, Model model){
        int i = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(i);
        model.addAttribute("user", user);
        return "user/setting";
    }
    @RequestMapping("changeInfo")
    @ResponseBody
    public ResultInfo changeInfo(User user){
        userService.changeInfo(user);
        return success("修改成功");
    }
}

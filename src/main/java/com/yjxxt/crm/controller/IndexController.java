package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.service.PermissionService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/")
    public String defaultPage(){
        return "hello";
    }

    @RequestMapping("less_permission")
    public String less_permission(){
        return "less_permission";
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest req){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(userId);
        req.setAttribute("user", user);

        // 获取当前用户的所有权限码
        List<String> aclVal = permissionService.queryAllAclValuesByUserId(userId);
        HttpSession session = req.getSession();
        session.setAttribute("aclVal", aclVal);

        return "main";
    }

    @RequestMapping("user/toPasswordPage")
    public String password(){
        return "user/password";
    }
    // 系统界⾯欢迎⻚
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

}

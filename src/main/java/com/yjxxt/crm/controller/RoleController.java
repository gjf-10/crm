package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotation.PermissionRequired;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("toGrantPage")
    public String grantPage(Integer roleId, Model model){
        model.addAttribute("roleId", roleId);
        return "/role/grant_page";
    }

    @RequestMapping("queryAllRoles")
    @ResponseBody
    @PermissionRequired("602002")
    public List<Map<String, Object>> queryAll(Integer userId){
        return roleService.queryAllRole(userId);
    }

    @RequestMapping("index")
    @PermissionRequired("60")
    public String index(){
        return "role/role01";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(RoleQuery roleQuery){
        return roleService.queryAll(roleQuery);
    }

    @RequestMapping("add_update")
    public String add_update(Integer roleId, Model model){
        if(roleId != null){
            Role role = roleService.selectByPrimaryKey(roleId);
            model.addAttribute("role", role);
        }
        return "role/add_update";
    }

    @RequestMapping("add")
    @ResponseBody
    @PermissionRequired("602001")
    public ResultInfo add(Role role){
        roleService.add(role);
        return success("添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    @PermissionRequired("602003")
    public ResultInfo update(Role role){
        roleService.update(role);
        return success("修改成功");
    }

    @RequestMapping("del")
    @ResponseBody
    @PermissionRequired("602004")
    public ResultInfo del(Integer roleId){
        roleService.del(roleId);
        return success("删除成功");
    }

    @RequestMapping("grantRole")
    @ResponseBody
    public ResultInfo grantRole(Integer[] mids, Integer roleId){
        System.out.println(roleId);
        System.out.println( Arrays.toString(mids));
        roleService.grantRole(roleId, mids);
        return success("授权成功");
    }
}

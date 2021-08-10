package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotation.PermissionRequired;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SaleChanceService saleChanceService;

    @RequestMapping("index")
    @PermissionRequired("1010")
    public String index(){
        return "sale/saleChance";
    }

    @RequestMapping("list")
    @ResponseBody
    @PermissionRequired("101001")
    public Map<String, Object> list(SaleChanceQuery query){
        System.out.println(query);
        return saleChanceService.queryList(query);
    }

    @RequestMapping("add_update_page")
    public String addUpdate(Integer id, Model model){
        if(id!=null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance", saleChance);
        }
        return "sale/add_update";
    }

    @PostMapping("add")
    @ResponseBody
    @PermissionRequired("101002")
    public ResultInfo add(HttpServletRequest req, SaleChance dataField){
        // 1. 打印数据
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(userId);
        AssertUtil.isTrue(user == null, "add未登录....");
        dataField.setCreateMan(user.getTrueName());
        saleChanceService.save(dataField);
        return success("添加成功");
    }

    @RequestMapping("change")
    @ResponseBody
    public ResultInfo update(SaleChance dataField){
        saleChanceService.change(dataField);
        return success("修改成功");
    }

    @PostMapping("del")
    @ResponseBody
    @PermissionRequired("101003")
    public ResultInfo delete(Integer[] ids){
        saleChanceService.delete(ids);
        return success("删除成功");
    }

}

package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.Modules;
import com.yjxxt.crm.service.ModulesServices;
import com.yjxxt.crm.treeDto.TreeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("modules")
public class ModulesController extends BaseController {

    @Autowired
    private ModulesServices modulesServices;


    @RequestMapping("ztreeData")
    @ResponseBody
    public List<TreeDto> ztreeData(Integer roleId){
        System.out.println(roleId + "11111111");
        List<TreeDto> treeDtos = modulesServices.ztreeData(roleId);
        treeDtos.forEach(System.out::println);
        return treeDtos;
    }

    @RequestMapping("index")
    public String index(){
        return "modules/module01";
    }

    @RequestMapping("toAddPage")
    public String addPage(Integer grade, Integer parentId, Model model){
        System.out.println(grade + "-->" + parentId);
        model.addAttribute("grade", grade);
        model.addAttribute("parentId", parentId);
        return "modules/add";
    }

    @RequestMapping("toUpdatePage")
    public String updatePage(Integer id, Model model){
        model.addAttribute("module", modulesServices.selectByPrimaryKey(id));
        return "modules/update";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(){
        return modulesServices.selectAllModules();
    }

    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(Modules modules){
        System.out.println(modules);
        modulesServices.save(modules);
        return success("添加成功");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Modules modules){
        System.out.println(modules);
        modulesServices.update(modules);
        return success("修改成功");
    }
}

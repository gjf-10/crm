package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Modules;
import com.yjxxt.crm.mapper.ModulesMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.treeDto.TreeDto;
import com.yjxxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ModulesServices extends BaseService<Modules, Integer> {
    @Resource
    private ModulesMapper modulesMapper;

    @Resource
    private PermissionMapper permissionMapper;

    public List<TreeDto> ztreeData(Integer roleId){
        List<TreeDto> treeDtos = modulesMapper.selectAllModule(roleId);
        Set<Integer> mids = permissionMapper.selectMidByRoleId(roleId);
        System.out.println(mids);
        for(TreeDto tr: treeDtos){
            if(mids.contains(tr.getId())){
                tr.setSelected(true);
            }
        }
        return treeDtos;
    }

    public Map<String,Object> selectAllModules(){
        Map<String, Object> map = new HashMap<>();

        List<Modules> list = modulesMapper.selectModules();
        map.put("code", 0);
        map.put("count", list.size());
        map.put("msg", "success");
        map.put("data", list);

        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Modules modules){
        Integer g = modules.getGrade();
        // 模块名非空判断
        AssertUtil.isTrue(StringUtils.isBlank(modules.getModuleName()), "模块名不能为空");
        // 目录等级 0 1 2 必须有
        Set<Integer> grades = Set.of(0,1,2);
        AssertUtil.isTrue(g == null || !(grades.contains(g)),"目录等级必须不合理");
        // 同一目录等级下模块名不能重复
        int count = modulesMapper.countModuleByNameAndGrade(modules.getModuleName(), modules.getGrade());
        AssertUtil.isTrue(count != 0, "模块名已经存在");
        // 如果是子目录添加需要判断是否有parentId
        if(g > 0){
            Integer parentId = modules.getParentId();
            AssertUtil.isTrue( parentId== null || null == modulesMapper.selectModuleByParentId(parentId) , "请指定上级菜单");
        }
        if(g == 1){
            AssertUtil.isTrue(StringUtils.isBlank(modules.getUrl()), "请指定二级菜单url值");
            AssertUtil.isTrue(null != modulesMapper.queryModuleByGradeAndUrl(g,modules.getUrl()), "二级菜单url不能重复");
        }
        // 权限码必须存在
        AssertUtil.isTrue(StringUtils.isBlank(modules.getOptValue()), "请输入权限码");
        AssertUtil.isTrue(modulesMapper.countModuleByOptValue(modules.getOptValue())>0, "该权限码已存在");
        // 默认值设置
        modules.setIsValid((byte)1);
        modules.setUpdateDate(new Date());
        modules.setCreateDate(new Date());
        // 添加

        AssertUtil.isTrue(modulesMapper.insertSelective(modules) < 1, "添加失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Modules modules){
        // 待修改id是否存在
        AssertUtil.isTrue(modules.getId() == null || modulesMapper.selectByPrimaryKey(modules.getId())== null, "待修改模块不存在");

        Integer g = modules.getGrade();
        // 模块名非空判断
        AssertUtil.isTrue(StringUtils.isBlank(modules.getModuleName()), "模块名不能为空");
        // 目录等级 0 1 2 必须有
        Set<Integer> grades = Set.of(0,1,2);
        AssertUtil.isTrue(g == null || !(grades.contains(g)),"目录等级必须不合理");
        // 同一目录等级下模块名不能重复
        Modules temp = modulesMapper.selectedModuleByNameAndGrade(modules.getModuleName(), modules.getGrade());
        AssertUtil.isTrue(temp!=null && !temp.getId().equals(modules.getId()), "模块名已经存在");
        // 如果是子目录添加需要判断是否有parentId
        if(g > 0){
            Integer parentId = modules.getParentId();
            AssertUtil.isTrue( parentId== null || null == modulesMapper.selectModuleByParentId(parentId) , "请指定上级菜单");
        }
        if(g == 1){
            AssertUtil.isTrue(StringUtils.isBlank(modules.getUrl()), "请指定二级菜单url值");
            AssertUtil.isTrue(null != modulesMapper.queryModuleByGradeAndUrl(g,modules.getUrl()), "二级菜单url不能重复");
        }
        // 权限码必须存在
        AssertUtil.isTrue(StringUtils.isBlank(modules.getOptValue()), "请输入权限码");
        Modules temp01 = modulesMapper.selectModuleByOptValue(modules.getOptValue());
        AssertUtil.isTrue(temp01!=null && !temp01.getOptValue().equals(modules.getOptValue()), "该权限码已存在");
        // 默认值设置
        modules.setUpdateDate(new Date());
        // 添加

        AssertUtil.isTrue(modulesMapper.updateByPrimaryKeySelective(modules) < 1, "修改失败");
    }


}

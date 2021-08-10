package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Modules;
import com.yjxxt.crm.treeDto.TreeDto;

import java.util.List;

public interface ModulesMapper extends BaseMapper<Modules, Integer> {
    List<TreeDto> selectAllModule(Integer roleId);

    String selectAclValueByMid(Integer mid);

    List<Modules> selectModules();

    int countModuleByNameAndGrade(String moduleName, Integer grade);

    Modules selectModuleByParentId(Integer parentId);

    int countModuleByOptValue(String optValue);

    Modules queryModuleByGradeAndUrl(Integer g, String url);

    Modules selectedModuleByNameAndGrade(String moduleName, Integer grade);

    Modules selectModuleByOptValue(String optValue);
}
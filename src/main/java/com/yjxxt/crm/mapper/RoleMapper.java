package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.query.RoleQuery;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    @MapKey("")
    public List<Map<String, Object>> selectAllRole(Integer userId);

    List<Role> selectAll(RoleQuery roleQuery);

    Role selectByRoleName(String roleName);

    Integer changeToNotValid(Integer roleId);
}
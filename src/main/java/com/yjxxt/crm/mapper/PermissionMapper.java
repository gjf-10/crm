package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionMapper extends BaseMapper<Permission, Integer> {

    Integer countRolePermissions(Integer roleId);
    Set<Integer> selectMidByRoleId(Integer roleId);

    Integer deleteRolePermissionsByRoleId(Integer roleId);

    List<String> selectAllAclValuesByUserId(Integer userId);
}
package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.query.UserQuery;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper extends BaseMapper<User, Integer> {
    public User selectUserByName(String userName);
    @MapKey("")
    public List<Map<String, Object>> selectAllSales();
    public List<User> selectAllUser(UserQuery userQuery);
}
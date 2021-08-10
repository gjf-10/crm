package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.query.SaleChanceQuery;

import java.util.List;

public interface SaleChanceMapper extends BaseMapper<SaleChance, Integer> {

    public List<SaleChance> selectByQuery(SaleChanceQuery query);
    public int updateIdsNotValid(Integer[] ids);
}
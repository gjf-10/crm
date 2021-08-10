package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.mapper.SaleChanceMapper;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    public Map<String, Object> queryList(SaleChanceQuery query){
        Map<String, Object> map = new HashMap<>();
        List<SaleChance> saleChances = saleChanceMapper.selectByQuery(query);
        PageHelper.startPage(query.getPage(), query.getLimit());
        PageInfo<SaleChance> listPageInfo = new PageInfo<>(saleChances);
        map.put("data", listPageInfo.getList());
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("count", listPageInfo.getTotal());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(SaleChance dataField){
        /**
         * 营销机会数据添加
         * 1.参数校验
         * customerName:非空
         * linkMan:非空
         * linkPhone:非空 11位手机号
         * 2.设置相关参数默认值
         * state:默认未分配 如果选择分配人 state 为已分配
         * assignTime: 如果选择分配人 时间为当前系统时间
         * devResult:默认未开发 如果选择分配人
         devResult为开发中 0-未开发 1-开发中 2-开发成功 3-开发失败
         * isValid:默认有效数据(1-有效 0-无效)
         * createDate updateDate:默认当前系统时间
         * 3.执行添加 判断结果
         */
        checkParams(dataField.getCustomerName(), dataField.getLinkMan(), dataField.getLinkPhone());
        dataField.setIsValid(1);
        dataField.setState(0);
        dataField.setDevResult(0);
        dataField.setCreateDate(new Date());
        dataField.setUpdateDate(new Date());
        if(StringUtils.isNotBlank(dataField.getAssignMan())){
            dataField.setState(1);
            dataField.setAssignTime(new Date());
            dataField.setDevResult(1);
        }
        saleChanceMapper.insertSelective(dataField);
    }

    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "联系人电话不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "手机号码不合理");
    }

    /*
     * 营销机会数据更新
     * 1.参数校验
     * id:记录必须存在
     * customerName:非空
     * linkMan:非空
     * linkPhone:非空，11位手机号
     * 2. 设置相关参数值
     * updateDate:系统当前时间
     * 原始记录 未分配 修改后改为已分配(由分配人决定)
     * state 0->1
     * assginTime 系统当前时间
     * devResult 0-->1
     * 原始记录 已分配 修改后 为未分配
     * state 1-->0
     * assignTime 待定 null
     * devResult 1-->0
     * 3.执行更新 判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void change(SaleChance saleChance){
        // 1.参数校验
        AssertUtil.isTrue(saleChance == null, "参数异常");
        SaleChance fromDb = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue( fromDb == null, "用户不存在");
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        saleChance.setUpdateDate(new Date());
        if(StringUtils.isBlank(fromDb.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
        }
        if(StringUtils.isNotBlank(fromDb.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(0);
        }
        // 3.执行更新 判断结果
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1, "数据更新失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Integer[] ids){
        AssertUtil.isTrue(ids == null, "请选择要删除的数据！");
        int i = saleChanceMapper.updateIdsNotValid(ids);

        AssertUtil.isTrue(i < 1, "营销机会删除失败");
    }
}

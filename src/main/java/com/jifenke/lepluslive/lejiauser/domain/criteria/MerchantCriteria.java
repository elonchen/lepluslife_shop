package com.jifenke.lepluslive.lejiauser.domain.criteria;

import com.jifenke.lepluslive.partner.domain.entities.Partner;

/**
 * Created by xf on 2017/5/12.
 * 合伙人中心 - 我的好店查询条件
 */
public class MerchantCriteria {
    private Partner partner;        //  合伙人
    private Integer type;           //  0 - 根据每日佣金排序     1- 根据锁定会员数排序
    private Integer orderBy;        //  0 - 正序                 1- 倒序
    private Integer currPage;       //  当前页数

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }
}

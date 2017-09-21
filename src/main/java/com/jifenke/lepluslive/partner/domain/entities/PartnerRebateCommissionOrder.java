package com.jifenke.lepluslive.partner.domain.entities;


import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xf on 17-9-19.
 *         合伙人返佣单
 */
@Entity
@Table(name = "PARTNER_REBATE_COMMISSION_ORDER")
public class PartnerRebateCommissionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Partner partner;            // 天使合伙人 [新]

    @ManyToOne
    private Partner devPartner;         // 发展合伙人

    @ManyToOne
    private LeJiaUser leJiaUser;        // 乐加会员

    private Long dailyIncome = 0L;           // 天使合伙人当日佣金收入

    private Long refundMoney = 0L;           // 当日退款追回分润

    private Long rebateRate = 10L;        // 全局返佣比例，默认10%

    private Long rebateCommision = 0L;    // 本次返佣收入

    private Date rebateDate;            //   返佣日期

    private Date createDate = new Date(); // 创建时间


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRebateDate() {
        return rebateDate;
    }

    public void setRebateDate(Date rebateDate) {
        this.rebateDate = rebateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Partner getDevPartner() {
        return devPartner;
    }

    public void setDevPartner(Partner devPartner) {
        this.devPartner = devPartner;
    }

    public LeJiaUser getLeJiaUser() {
        return leJiaUser;
    }

    public void setLeJiaUser(LeJiaUser leJiaUser) {
        this.leJiaUser = leJiaUser;
    }

    public Long getDailyIncome() {
        return dailyIncome;
    }

    public void setDailyIncome(Long dailyIncome) {
        this.dailyIncome = dailyIncome;
    }

    public Long getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(Long refundMoney) {
        this.refundMoney = refundMoney;
    }

    public Long getRebateRate() {
        return rebateRate;
    }

    public void setRebateRate(Long rebateRate) {
        this.rebateRate = rebateRate;
    }

    public Long getRebateCommision() {
        return rebateCommision;
    }

    public void setRebateCommision(Long rebateCommision) {
        this.rebateCommision = rebateCommision;
    }
}

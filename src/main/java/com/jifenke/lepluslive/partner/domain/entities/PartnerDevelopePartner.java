package com.jifenke.lepluslive.partner.domain.entities;


import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by xf on 17-9-19.
 * 合伙人 - 发展合伙人关联表
 */
@Entity
@Table(name = "PARTNER_DEVELOPE_PARTNER")
public class PartnerDevelopePartner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Partner partner;                 // 天使合伙人 [新]
    @ManyToOne
    private LeJiaUser leJiaUser;             // 会员
    @ManyToOne
    private Partner devPartner;              // 发展合伙人

    private Date createDate = new Date();    // 创建时间

    private Long totalCommission=0L;            // 发展合伙人累计获取佣金收入 1:100

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(Long totalCommission) {
        this.totalCommission = totalCommission;
    }

    public LeJiaUser getLeJiaUser() {
        return leJiaUser;
    }

    public void setLeJiaUser(LeJiaUser leJiaUser) {
        this.leJiaUser = leJiaUser;
    }
}

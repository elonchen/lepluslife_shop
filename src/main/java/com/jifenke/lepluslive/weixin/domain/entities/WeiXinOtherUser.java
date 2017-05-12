package com.jifenke.lepluslive.weixin.domain.entities;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 其他公众号用户微信信息
 * Created by zhangwen on 17/5/11.
 */
@Entity
@Table(name = "WEI_XIN_OTHER_USER")
public class WeiXinOtherUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String openId;

    private Integer source = 1;  //openId所属公众号  1=乐加臻品商城

    @ManyToOne
    private WeiXinUser weiXinUser;

    private Date dateCreated = new Date();

    private Integer subState = 0;   //关注状态 0=从未关注过   1=关注   2=曾经关注现取消关注

    private Date subDate;       //关注时间

    // 0_0_0表示表示普通关注
    // 1_0_123表示通过某一个合伙人的推广二维码，合伙人Id为123
    private String subSource;   //关注来源

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public WeiXinUser getWeiXinUser() {
        return weiXinUser;
    }

    public void setWeiXinUser(WeiXinUser weiXinUser) {
        this.weiXinUser = weiXinUser;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getSubState() {
        return subState;
    }

    public void setSubState(Integer subState) {
        this.subState = subState;
    }

    public Date getSubDate() {
        return subDate;
    }

    public void setSubDate(Date subDate) {
        this.subDate = subDate;
    }

    public String getSubSource() {
        return subSource;
    }

    public void setSubSource(String subSource) {
        this.subSource = subSource;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
}
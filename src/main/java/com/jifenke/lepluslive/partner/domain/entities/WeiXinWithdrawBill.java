package com.jifenke.lepluslive.partner.domain.entities;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinOtherUser;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by xf on 2016/9/18.
 */
@Entity
@Table(name = "WEIXIN_WITHDRAW_BILL")
public class WeiXinWithdrawBill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String withdrawBillSid;

    @ManyToOne
    private Partner partner;

    @ManyToOne
    private WeiXinOtherUser weiXinOtherUser; //微信接口对应的用户

    private Date createdDate;

    private Date completeDate;

    private Integer state;//0申请中 1接口调用完成用户未接收   2已驳回   3已退款 4成功接收 5异常

    private Long totalPrice; //totalPrice = onlineWallet+offlineWallet

    private Long onlineWallet; //总提现金额中线上钱包提现金额

    private Long offlineWallet; //总提现金额中线下钱包提现金额

    private String note;//备注微信接口的返回码

    @Column(unique=true)
    private String mchBillno; //商户订单号 每生成一个结算单就会对应一个唯一随机号 不可修改!!!


    public String getMchBillno() {
        return mchBillno;
    }

    public void setMchBillno(String mchBillno) {
        this.mchBillno = mchBillno;
    }

    public WeiXinOtherUser getWeiXinOtherUser() {
        return weiXinOtherUser;
    }

    public void setWeiXinOtherUser(WeiXinOtherUser weiXinOtherUser) {
        this.weiXinOtherUser = weiXinOtherUser;
    }

    public Long getOnlineWallet() {
        return onlineWallet;
    }

    public void setOnlineWallet(Long onlineWallet) {
        this.onlineWallet = onlineWallet;
    }

    public Long getOfflineWallet() {
        return offlineWallet;
    }

    public void setOfflineWallet(Long offlineWallet) {
        this.offlineWallet = offlineWallet;
    }

    public Integer getState() {
        return state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }


    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getWithdrawBillSid() {
        return withdrawBillSid;
    }

    public void setWithdrawBillSid(String withdrawBillSid) {
        this.withdrawBillSid = withdrawBillSid;
    }

}

package com.jifenke.lepluslive.partner.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 合伙人临时二维码  Created by zhangwen on 2017/5/11.
 */
@Entity
@Table(name = "PARTNER_QR_CODE")
public class PartnerQrCode {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @OneToOne
  private Partner partner;

  private Date dateCreated = new Date();     //创建时间

  private Date dateUpdate;   //最后更新时间  需要根据此时间更新维护二维码

  private Long sceneId;  //场景值ID  临时二维码参数最大值为4294967295

  private String ticket;      //获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码(30天)

  private String downUrl;  //临时二维码海报下载链接

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Date getDateUpdate() {
    return dateUpdate;
  }

  public void setDateUpdate(Date dateUpdate) {
    this.dateUpdate = dateUpdate;
  }

  public Long getSceneId() {
    return sceneId;
  }

  public void setSceneId(Long sceneId) {
    this.sceneId = sceneId;
  }

  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  public String getDownUrl() {
    return downUrl;
  }

  public void setDownUrl(String downUrl) {
    this.downUrl = downUrl;
  }
}

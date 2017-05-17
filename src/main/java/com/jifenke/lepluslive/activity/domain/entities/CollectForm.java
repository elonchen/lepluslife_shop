package com.jifenke.lepluslive.activity.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 收集表单【各种页面信息提交】
 *
 * @author zhangwen【zhangwenit@126.com】 2017/5/16 10:59
 **/
@Entity
@Table(name = "COLLECT_FORM")
public class CollectForm {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long type;   //对应category.id表单类别

  private Date dateCreated = new Date();  //创建时间

  private String content;  //表单内容 JSON格式存储

  private String extra1;  //额外备用字段

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getType() {
    return type;
  }

  public void setType(Long type) {
    this.type = type;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getExtra1() {
    return extra1;
  }

  public void setExtra1(String extra1) {
    this.extra1 = extra1;
  }
}

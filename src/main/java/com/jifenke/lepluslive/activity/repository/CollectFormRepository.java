package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.CollectForm;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 收集表单【各种页面信息提交】
 * Created by zhangwen on 16/8/4.
 */
public interface CollectFormRepository extends JpaRepository<CollectForm, String> {


}

package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.CollectForm;
import com.jifenke.lepluslive.activity.repository.CollectFormRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 收集表单【各种页面信息提交】
 *
 * @author zhangwen【zhangwenit@126.com】 2017/5/16 13:46
 **/
@Service
@Transactional(readOnly = true)
public class CollectFormService {

  @Inject
  private CollectFormRepository repository;

  /**
   * 表单提交  2017/5/16
   *
   * @param type    表单类别
   * @param content 表单JSON值
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void insertForm(Long type, String content) throws Exception {

    CollectForm collectForm = new CollectForm();
    collectForm.setContent(content);
    collectForm.setType(type);

    repository.save(collectForm);
  }


}

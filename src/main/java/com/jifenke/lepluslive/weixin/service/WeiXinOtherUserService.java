package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinOtherUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.repository.WeiXinOtherUserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 其他公众号用户操作
 * Created by zhangwen on 17/5/11.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinOtherUserService {

  @Inject
  private WeiXinOtherUserRepository repository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinOtherUser findByOpenId(String openId) {

    return repository.findByOpenId(openId);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinOtherUser findByWeiXinUser(WeiXinUser weiXinUser) {

    return repository.findByWeiXinUserAndSource(weiXinUser, 1);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveWeiXinOtherUser(WeiXinOtherUser user) {
    repository.saveAndFlush(user);
  }

}

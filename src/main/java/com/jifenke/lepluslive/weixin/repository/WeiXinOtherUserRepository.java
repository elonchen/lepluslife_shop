package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinOtherUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 其他公众号用户
 * Created by zhangwen on 17/5/11.
 */
public interface WeiXinOtherUserRepository extends JpaRepository<WeiXinOtherUser, Long> {

  WeiXinOtherUser findByOpenId(String openId);

  WeiXinOtherUser findByWeiXinUserAndSource(WeiXinUser weiXinUser, Integer source);

}

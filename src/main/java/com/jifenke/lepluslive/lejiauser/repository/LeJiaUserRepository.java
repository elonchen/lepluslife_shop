package com.jifenke.lepluslive.lejiauser.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/3/24.
 */
public interface LeJiaUserRepository extends JpaRepository<LeJiaUser, Long> {

  LeJiaUser findByUserSid(String userSid);

  List<LeJiaUser> findByPhoneNumber(String phoneNumber);

  /**
   * APP获取用户信息  16/09/07
   *
   * @param token 用户唯一标识
   * @return 个人信息
   */
  @Query(value = "SELECT w.nickname,w.head_image_url,u.user_sid,u.phone_number,a.score AS aScore,b.score AS bScore,u.bind_merchant_id FROM le_jia_user u LEFT OUTER JOIN wei_xin_user w ON u.wei_xin_user_id=w.id INNER JOIN scorea a ON a.le_jia_user_id=u.id INNER JOIN scorec b ON b.le_jia_user_id=u.id AND u.user_sid=?1", nativeQuery = true)
  List<Object[]> getUserInfo(String token);


  /**
   *  门店绑定会员数
   */
  @Query(value = "select count(*) from le_jia_user where bind_merchant_id = ?1", nativeQuery = true)
  Long countMerchantBindLeJiaUser(Long merchantId);

  /**
   *  合伙人绑定会员数
   */
  @Query(value = "select count(*) from le_jia_user,wei_xin_user  where wei_xin_user.le_jia_user_id = le_jia_user.id and wei_xin_user.state=1 and bind_partner_id =?1", nativeQuery = true)
  Long countPartnerBindLeJiaUser(Long partnerId);

  /**
   *  合伙人今日绑定会员数
   */
  @Query(value = "select count(*) from le_jia_user where bind_partner_id = ?1 and to_days(create_date) = to_days(now())", nativeQuery = true)
  Long countPartnerDailyBindLeJiaUser(Long partnerId);

  /**
   *  查找当前合伙人下所有会员
   */
  @Query(value="select * from le_jia_user where bind_partner_id  = ?1 limit ?2,10",nativeQuery = true)
  List<LeJiaUser> findByBindPartnerAndPage(Long bindPartner, Integer currPage);
  @Query(value="select wei_xin_user_id,bind_partner_date,phone_number from le_jia_user where bind_partner_id  = ?1 limit ?2,10",nativeQuery = true)
  List<Object[]> findByBindPartnerAndPageSimple(Long bindPartner, Integer currPage);
}

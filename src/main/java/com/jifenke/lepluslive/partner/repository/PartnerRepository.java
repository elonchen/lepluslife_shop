package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Partner findByPartnerSid(String sid);

    Optional<Partner> findByWeiXinUser(WeiXinUser weiXinUser);

    /**
     * 合伙人绑定门店数
     */
    @Query(value = "select count(*) from merchant where partner_id = ?1 and partnerShip != 2", nativeQuery = true)
    Long countParnterBindMerchant(Long id);

    /**
     * 合伙人绑定门店
     * - 门店的日流水
     * - 绑定会员总数
     */
    @Query(value = "select id,`name`," +
            "(select count(*) from le_jia_user where le_jia_user.bind_merchant_id = merchant.id) bind_users," +
            "(select IFNULL(sum(ofl.total_price),0) from off_line_order ofl where ofl.merchant_id = merchant.id  and to_days(ofl.complete_date) = to_days(now()) ) off_Line_price,IFNULL(merchant.user_limit,0)" +
            " from merchant where partner_id=?1 order by off_Line_price desc ", nativeQuery = true)
    List<Object[]> findMerchantsDataByPartnerOrderByAmountDesc(Long partnerId);

    @Query(value = "select id,`name`," +
            "(select count(*) from le_jia_user where le_jia_user.bind_merchant_id = merchant.id) bind_users," +
            "(select IFNULL(sum(ofl.total_price),0) from off_line_order ofl where ofl.merchant_id = merchant.id  and to_days(ofl.complete_date) = to_days(now()) ) off_Line_price,IFNULL(merchant.user_limit,0)" +
            " from merchant where partner_id=?1 order by off_Line_price asc ", nativeQuery = true)
    List<Object[]> findMerchantsDataByPartnerOrderByAmountAsc(Long partnerId);


    @Query(value = "select id,`name`," +
            "(select count(*) from le_jia_user where le_jia_user.bind_merchant_id = merchant.id) bind_users," +
            "(select IFNULL(sum(ofl.total_price),0) from off_line_order ofl where ofl.merchant_id = merchant.id  and to_days(ofl.complete_date) = to_days(now()) ) off_Line_price,IFNULL(merchant.user_limit,0)" +
            " from merchant where partner_id=?1 order by bind_users desc ", nativeQuery = true)
    List<Object[]> findMerchantsDataByPartnerOrderByBindUserDesc(Long partnerId);

    @Query(value = "select id,`name`," +
            "(select count(*) from le_jia_user where le_jia_user.bind_merchant_id = merchant.id) bind_users," +
            "(select IFNULL(sum(ofl.total_price),0) from off_line_order ofl where ofl.merchant_id = merchant.id  and to_days(ofl.complete_date) = to_days(now()) ) off_Line_price,IFNULL(merchant.user_limit,0)" +
            " from merchant where partner_id=?1 order by bind_users asc ", nativeQuery = true)
    List<Object[]> findMerchantsDataByPartnerOrderByBindUserAsc(Long partnerId);
}

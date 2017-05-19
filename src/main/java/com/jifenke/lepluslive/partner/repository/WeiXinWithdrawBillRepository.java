package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.WeiXinWithdrawBill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xf on 2017/5/15.
 */
public interface WeiXinWithdrawBillRepository extends JpaRepository<WeiXinWithdrawBill, Long> {
    @Query(value = "select * from weixin_withdraw_bill where partner_id = ?1  order by created_date DESC limit ?2,10 ", nativeQuery = true)
    List<WeiXinWithdrawBill> findByPartnerAndPage(Long partnerId, Integer currPage);

    /**
     * 0 合伙人平台提现
     * 1 微信公众号提现
     */
    @Query(value = "select * from " +
            "(select 0,state,total_price,created_date from withdraw_bill where partner_id = ?1 " +
            "UNION ALL " +
            "select 1,state,total_price,created_date from weixin_withdraw_bill where partner_id = ?1 ) result " +
            "order by result.created_date desc " +
            "limit ?2,10 ", nativeQuery = true)
    List<Object[]> findBillByPartnerAndPage(Long partnerId,Integer currPage);

    /**
     *  提现总额
     */
    @Query(value = "select IFNULL(sum(total_price),0) from withdraw_bill where partner_id = ?1 ", nativeQuery = true)
    Long sumWithDrawByPartner(Long partnerId);

    @Query(value = "select IFNULL(sum(total_price),0) from weixin_withdraw_bill where partner_id = ?1 ", nativeQuery = true)
    Long sumWxWithDrawByPartner(Long partnerId);

}

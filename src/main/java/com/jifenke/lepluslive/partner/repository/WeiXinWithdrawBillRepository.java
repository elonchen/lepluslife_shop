package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.WeiXinWithdrawBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xf on 2017/5/15.
 */
public interface WeiXinWithdrawBillRepository extends JpaRepository<WeiXinWithdrawBill, Long> {
    @Query(value = "select * from weixin_withdraw_bill where partner_id = ?1  order by created_date DESC limit ?2,10 ", nativeQuery = true)
    List<WeiXinWithdrawBill> findByPartnerAndPage(Long partnerId, Integer currPage);
}

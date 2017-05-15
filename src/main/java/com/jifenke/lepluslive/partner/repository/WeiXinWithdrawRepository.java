package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.WeiXinWithdrawBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by xf on 2017/5/15.
 */
public interface WeiXinWithdrawRepository extends JpaRepository<WeiXinWithdrawBill, Long> {
    List<WeiXinWithdrawBill> findByPartner(Partner partner);
}

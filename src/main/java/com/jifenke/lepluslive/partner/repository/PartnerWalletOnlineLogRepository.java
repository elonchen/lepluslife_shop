package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnlineLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zhangwen on 16/11/05.
 */
public interface PartnerWalletOnlineLogRepository
    extends JpaRepository<PartnerWalletOnlineLog, Long> {
    /**
     * 查询合伙人线上佣金收入 17/05/12
     * 根据时间排序
     */
    List<PartnerWalletOnlineLog> findByPartnerIdOrderByCreateDateDesc(Long partnerId);

    /**
     * 查询合伙人线上佣金收入之和 17/05/12
     * 根据时间排序
     */
    @Query(value="select sum(changeMoney) from partner_wallet_online_log where partner_id = ?1",nativeQuery = true)
    Long countOnlineCommission(Long partnerId);
}

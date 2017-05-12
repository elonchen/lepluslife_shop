package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerWalletLogRepository extends JpaRepository<PartnerWalletLog, Long> {
    /**
     * 查询合伙人线下佣金收入 17/05/12
     * 根据时间排序
     */
    List<PartnerWalletLog> findByParnterIdOrderByCreateDateDesc(Long partnerId);

    /**
     * 查询合伙人线下佣金收入之和 17/05/12
     * 根据时间排序
     */
    @Query(value="select sum(changeMoney) from partner_wallet_log where partner_id = ?1",nativeQuery = true)
    Long countOffLineCommission(Long partnerId);
}

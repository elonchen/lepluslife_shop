package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerQrCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 合伙人临时二维码
 * Created by zhangwen on 17/5/12.
 */
public interface PartnerQrCodeRepository extends JpaRepository<PartnerQrCode, Long> {

  Optional<PartnerQrCode> findByPartner(Partner partner);

}

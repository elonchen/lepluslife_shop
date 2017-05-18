package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerQrCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 合伙人临时二维码
 * Created by zhangwen on 17/5/12.
 */
public interface PartnerQrCodeRepository extends JpaRepository<PartnerQrCode, Long> {

  Optional<PartnerQrCode> findByPartner(Partner partner);

  /**
   * 找出小于某个日期更新的二维码List 2017/5/18
   */
  List<PartnerQrCode> findByDateUpdateLessThan(Date date);

}

package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerDevelopePartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author  by xf on 17-9-20.
 */
@Repository
public interface PartnerDevelopePartnerRepository extends JpaRepository<PartnerDevelopePartner,Long> {
    @Query(nativeQuery = true,value="select * from partner_develope_repository where partner_id = ?1")
    PartnerDevelopePartner findByPartnerId(Long partnerId);
}

package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.partner.domain.entities.WeiXinWithdrawBill;
import com.jifenke.lepluslive.partner.repository.WeiXinWithdrawBillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by xf on 2017/5/15.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinWithdrawBillService {

    @Inject
    private WeiXinWithdrawBillRepository weiXinWithdrawBillRepository;

    /**
     *  合伙人中心 - 提现记录
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<WeiXinWithdrawBill> findByPartnerAndPage(Long partnerId, Integer currPage) {
        return weiXinWithdrawBillRepository.findByPartnerAndPage(partnerId,currPage);
    }


}

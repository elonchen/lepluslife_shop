package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.partner.domain.entities.*;
import com.jifenke.lepluslive.partner.repository.*;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinOtherUser;
import com.jifenke.lepluslive.weixin.repository.WeiXinOtherUserRepository;
import com.jifenke.lepluslive.weixin.service.WeiXinOtherUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xf on 2017/5/15.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinWithdrawBillService {

    @Inject
    private WeiXinWithdrawBillRepository weiXinWithdrawBillRepository;
    @Inject
    private PartnerWalletRepository partnerWalletRepository;
    @Inject
    private PartnerWalletLogRepository partnerWalletLogRepository;
    @Inject
    private PartnerWalletOnlineRepository partnerWalletOnlineRepository;
    @Inject
    private PartnerWalletOnlineLogRepository partnerWalletOnlineLogRepository;

    /**
     *  合伙人中心 - 提现记录
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<Object[]> findByPartnerAndPage(Long partnerId, Integer currPage) {
        return weiXinWithdrawBillRepository.findBillByPartnerAndPage(partnerId,currPage);
    }

    /**
     *  提现总额
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public Long sumWithDrawByPartner(Partner partner) {
        return weiXinWithdrawBillRepository.sumWithDrawByPartner(partner.getId());
    }
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public Long sumWxWithDrawByPartner(Partner partner) {
        return weiXinWithdrawBillRepository.sumWxWithDrawByPartner(partner.getId());
    }

    /**
     *  发起提现   17/15/17
     */
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public boolean createWithdraw(Partner partner,Long price,WeiXinOtherUser otherUser) {
        try {
            // 1 - 判断用户钱包佣金总额是否满足提现的金额
            PartnerWalletOnline walletOnline = partnerWalletOnlineRepository.findByPartner(partner);
            PartnerWallet walletOff = partnerWalletRepository.findByPartner(partner);
            Long sumOnLine = walletOnline == null ? 0L : walletOnline.getAvailableBalance();
            Long sumOffLine = walletOff == null ? 0L : walletOff.getAvailableBalance();
            Long totalCommission = sumOnLine + sumOffLine;
            if(totalCommission<price) {
                return false;                                                     // 余额不足
            }
            // 2 -  生成提现单
            WeiXinWithdrawBill weiXinWithdrawBill = new WeiXinWithdrawBill();
            weiXinWithdrawBill.setPartner(partner);
            Date date = new Date();
            weiXinWithdrawBill.setCreatedDate(date);
            weiXinWithdrawBill.setTotalPrice(price);
            weiXinWithdrawBill.setState(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String orderSid  ="1358860502" + format.format(date) + MvUtil.getRandomNumber(10);
            weiXinWithdrawBill.setMchBillno(orderSid);
            weiXinWithdrawBill.setWithdrawBillSid(orderSid);
            weiXinWithdrawBill.setWeiXinOtherUser(otherUser);
            weiXinWithdrawBillRepository.save(weiXinWithdrawBill);
            // 3 -  修改钱包金额 , 生成日志
            if(sumOnLine>price) {
                Long afterAvail = walletOnline.getAvailableBalance()-price;               //  只减线上钱包
                walletOnline.setAvailableBalance(afterAvail);
                walletOnline.setTotalWithdrawals(walletOnline.getTotalWithdrawals()==null?0+price:walletOff.getTotalWithdrawals()+price);
                walletOnline.setLastUpdate(new Date());
                partnerWalletOnlineRepository.save(walletOnline);
                PartnerWalletOnlineLog onlineLog = new PartnerWalletOnlineLog();
                onlineLog.setBeforeChangeMoney(sumOnLine);
                onlineLog.setAfterChangeMoney(walletOnline.getAvailableBalance());
                onlineLog.setChangeMoney(price);
                onlineLog.setType(16003L);
                onlineLog.setCreateDate(new Date());
                onlineLog.setOrderSid(orderSid);
                onlineLog.setPartnerId(partner.getId());
                partnerWalletOnlineLogRepository.save(onlineLog);
            } else if(sumOnLine<price && sumOnLine!=0) {
                Long onAfterAvail = 0L ;
                Long offAfterAvail = walletOff.getAvailableBalance()-(price-sumOnLine);
                walletOnline.setAvailableBalance(onAfterAvail);                           //  线上钱包
                walletOnline.setTotalWithdrawals(walletOnline.getTotalWithdrawals()==null?0+sumOnLine:walletOff.getTotalWithdrawals()+sumOnLine);
                walletOnline.setLastUpdate(new Date());
                walletOff.setAvailableBalance(offAfterAvail);                             //  线下钱包
                walletOff.setTotalWithdrawals(walletOff.getTotalWithdrawals()==null?0+(price-sumOnLine):walletOff.getTotalWithdrawals()+(price-sumOnLine));
                walletOff.setLastUpdate(new Date());
                partnerWalletOnlineRepository.save(walletOnline);
                partnerWalletRepository.save(walletOff);
                PartnerWalletOnlineLog onlineLog = new PartnerWalletOnlineLog();          //  线上钱包日志
                onlineLog.setBeforeChangeMoney(sumOnLine);
                onlineLog.setAfterChangeMoney(walletOnline.getAvailableBalance());
                onlineLog.setChangeMoney(price);
                onlineLog.setType(16003L);
                onlineLog.setCreateDate(new Date());
                onlineLog.setOrderSid(orderSid);
                onlineLog.setPartnerId(partner.getId());
                PartnerWalletLog offlineLog = new PartnerWalletLog();                    //  线下钱包日志
                offlineLog.setBeforeChangeMoney(sumOffLine);
                offlineLog.setAfterChangeMoney(walletOff.getAvailableBalance());
                offlineLog.setChangeMoney((price-sumOnLine));
                offlineLog.setType(15003L);
                offlineLog.setCreateDate(new Date());
                offlineLog.setOrderSid(orderSid);
                offlineLog.setPartnerId(partner.getId());
                partnerWalletOnlineLogRepository.save(onlineLog);
                partnerWalletLogRepository.save(offlineLog);
            }else {
                Long afterAvail = walletOff.getAvailableBalance()-price;
                walletOff.setAvailableBalance(afterAvail);                               //  只减线下钱包
                walletOff.setTotalWithdrawals(walletOff.getTotalWithdrawals()==null?0+price:walletOff.getTotalWithdrawals()+price);
                walletOff.setLastUpdate(new Date());
                partnerWalletRepository.save(walletOff);
                PartnerWalletLog offlineLog = new PartnerWalletLog();
                offlineLog.setBeforeChangeMoney(sumOffLine);
                offlineLog.setAfterChangeMoney(walletOff.getAvailableBalance());
                offlineLog.setChangeMoney(price);
                offlineLog.setType(15003L);
                offlineLog.setCreateDate(new Date());
                offlineLog.setOrderSid(orderSid);
                offlineLog.setPartnerId(partner.getId());
                partnerWalletLogRepository.save(offlineLog);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

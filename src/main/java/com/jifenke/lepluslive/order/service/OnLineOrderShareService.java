package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletOnline;
import com.jifenke.lepluslive.merchant.service.MerchantScanPayWayService;
import com.jifenke.lepluslive.merchant.service.MerchantWalletOnlineService;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrderShare;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;
import com.jifenke.lepluslive.order.domain.entities.PayOrigin;
import com.jifenke.lepluslive.order.repository.OnLineOrderShareRepository;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnline;
import com.jifenke.lepluslive.partner.service.PartnerWalletOnlineService;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 线上订单分润单 Created by zhangwen on 16/11/05.
 */
@Service
public class OnLineOrderShareService {

  @Inject
  private OnLineOrderShareRepository orderShareRepository;

  @Inject
  private MerchantWalletOnlineService merchantWalletOnlineService;

  @Inject
  private PartnerWalletOnlineService partnerWalletOnlineService;

  @Inject
  private OrderService onlineOrderService;

  @Inject
  private MerchantScanPayWayService merchantScanPayWayService;

  /**
   * 线上订单分润    16/11/05
   *
   * @param orderId 线上订单ID
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void onLineOrderShare(Long orderId) {

    OnLineOrder order = onlineOrderService.findOnLineOrderById(orderId);

    List<OrderDetail> list = order.getOrderDetails();
    Long toMerchant = 0L;
    Merchant merchant = null;
    MerchantWalletOnline merchantWalletOnline = null;
    Long toPartner = 0L;
    Partner partner = null;
    PartnerWalletOnline partnerWalletOnline = null;
    Long toLePlusLife = 0L;
    long type = 16001; //1代表app线上订单分润  2代表公众号线上订单分润
    PayOrigin payOrigin = order.getPayOrigin();
    if (payOrigin.getPayFrom() == 1) { //app
      type = 16006;
    }
    LeJiaUser user = order.getLeJiaUser();
    for (OrderDetail detail : list) {
      ProductSpec spec = detail.getProductSpec();
      toMerchant += spec.getToMerchant() == null ? 0 : spec.getToMerchant();
      toPartner += spec.getToPartner() == null ? 0 : spec.getToPartner();
    }
    //添加分润记录
    if (toMerchant + toPartner > 0) {
      OnLineOrderShare orderShare = new OnLineOrderShare();
      orderShare.setOnLineOrder(order);

      if (user.getBindMerchant() != null && toMerchant > 0) {
        merchant = user.getBindMerchant();
        //分润给绑定商户
        orderShare.setToLockMerchant(toMerchant);
        if (merchant == null || merchant.getPartnership() == 2) {//如果是虚拟商户分润方式改变
          toPartner += toMerchant;
        } else {
          MerchantScanPayWay
              scanPayWay =
              merchantScanPayWayService.findByMerchantId(merchant.getId());
          if (scanPayWay != null && scanPayWay.getOpenOnLineShare() == 0) {
            //没开启线上分润
            toLePlusLife = toMerchant;
            toMerchant = 0L;
          } else {
            merchantWalletOnline =
                merchantWalletOnlineService.findByMercahnt(user.getBindMerchant());
            merchantWalletOnlineService.shareToMerchant(toMerchant, merchant, merchantWalletOnline,
                                                        order.getOrderSid(), type);
            orderShare.setLockMerchant(merchant);
          }
        }
      }
      //对用户绑定合伙人进行分润
      if (user.getBindPartner() != null && toPartner > 0) {
        partnerWalletOnline = partnerWalletOnlineService.findByPartner(user.getBindPartner());
        partner = partnerWalletOnline.getPartner();

        orderShare.setToLockPartner(toPartner);
        //分润给绑定合伙人
        partnerWalletOnlineService
            .shareToPartner(toPartner, partner, partnerWalletOnline, order.getOrderSid(),
                            type);
        orderShare.setLockPartner(partner);
      }
      orderShare.setToLePlusLife(toLePlusLife);
      orderShare.setShareMoney(toMerchant + toPartner + toLePlusLife);
      orderShareRepository.save(orderShare);
    }
  }

}

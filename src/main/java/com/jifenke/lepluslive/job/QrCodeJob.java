package com.jifenke.lepluslive.job;


import com.jifenke.lepluslive.partner.domain.entities.PartnerQrCode;
import com.jifenke.lepluslive.partner.service.PartnerQrCodeService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinOtherUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinOtherUserService;
import com.jifenke.lepluslive.weixin.service.WxTemMsgService;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 合伙人临时二维码即将到期通知 Created by zhangwen on 17/5/18.
 */
@Component
public class QrCodeJob implements Job {

  private static final Logger log = LoggerFactory.getLogger(QrCodeJob.class);


  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    ApplicationContext
        applicationContext = null;
    try {

      applicationContext =
          (ApplicationContext) context.getScheduler().getContext().get("applicationContextKey");
    } catch (SchedulerException e) {
      log.error("jobExecutionContext.getScheduler().getContext() error!", e);
      throw new RuntimeException(e);
    }

    PartnerQrCodeService
        partnerQrCodeService =
        (PartnerQrCodeService) applicationContext.getBean("partnerQrCodeService");
    WeiXinOtherUserService
        weiXinOtherUserService =
        (WeiXinOtherUserService) applicationContext.getBean("weiXinOtherUserService");
    WxTemMsgService
        wxTemMsgService =
        (WxTemMsgService) applicationContext.getBean("wxTemMsgService");

    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, -28);
    Date start = calendar.getTime();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    List<PartnerQrCode> list = partnerQrCodeService.listByDateUpdate(start);

    if (list != null && list.size() > 0) {
      for (PartnerQrCode qrCode : list) {
        WeiXinUser weiXinUser = qrCode.getPartner().getWeiXinUser();
        if (weiXinUser != null) {
          WeiXinOtherUser otherUser = weiXinOtherUserService.findByWeiXinUser(weiXinUser);
          if (otherUser != null && otherUser.getSubState() == 1) {
            calendar.setTime(qrCode.getDateUpdate());
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            //发送即将过期模板消息
            String[] keys = new String[3];
            keys[0] = "推广二维码";
            keys[1] = sdf.format(qrCode.getDateUpdate());
            keys[2] = sdf.format(calendar.getTime());
            wxTemMsgService.sendTemMessage(otherUser.getOpenId(), 9L, keys);
          }
        }
      }
    }

  }
}

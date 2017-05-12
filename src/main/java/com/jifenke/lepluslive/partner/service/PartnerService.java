package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.lejiauser.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.domain.entities.*;
import com.jifenke.lepluslive.partner.repository.*;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.repository.ScoreADetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

/**
 * Created by wcg on 16/6/3.
 */
@Service
public class PartnerService {

    @Inject
    private PartnerRepository partnerRepository;

    @Inject
    private ScoreARepository scoreARepository;

    @Inject
    private ScoreBRepository scoreBRepository;

    @Inject
    private ScoreADetailRepository scoreADetailRepository;

    @Inject
    private ScoreBDetailRepository scoreBDetailRepository;

    @Inject
    private PartnerInfoRepository partnerInfoRepository;

    @Inject
    private PartnerWalletRepository partnerWalletRepository;

    @Inject
    private PartnerScoreLogRepository partnerScoreLogRepository;

    @Inject
    private LeJiaUserRepository leJiaUserRepository;

    @Inject
    private PartnerWalletOnlineLogRepository partnerWalletOnlineLogRepository;

    @Inject
    private PartnerWalletLogRepository partnerWalletLogRepository;

    @Inject
    private MerchantService merchantService;

    private static ReentrantLock lock = new ReentrantLock();

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Map<String, Integer> giveScoreToUser(WeiXinUser weiXinUser, String phoneNumber,
                                                Merchant merchant) {
        Map<String, Integer> map = new HashMap<>();
        LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
        Partner partner = merchant.getPartner();
        PartnerWallet partnerWallet = partnerWalletRepository.findByPartner(partner);
        PartnerInfo partnerInfo = partnerInfoRepository.findByPartner(partner);
        if (partnerInfo.getInviteLimit() == 1) {
            int
                    valueA =
                    partnerInfo.getScoreAType() == 0 ? partnerInfo.getMaxScoreA()
                            :
                            new Random().nextInt(partnerInfo.getMaxScoreA() - partnerInfo.getMinScoreA() + 1)
                                    + partnerInfo.getMinScoreA();
            int
                    valueB =
                    partnerInfo.getScoreBType() == 0 ? partnerInfo.getMaxScoreB() :
                            new Random().nextInt(partnerInfo.getMaxScoreB() - partnerInfo.getMinScoreB() + 1)
                                    + partnerInfo.getMinScoreB();
            Date date = new Date();
            if (partnerWallet.getAvailableScoreA() >= valueA
                    && partnerWallet.getAvailableScoreB() >= valueB) {
                leJiaUser.setPhoneNumber(phoneNumber);
                leJiaUserRepository.save(leJiaUser);
                //合伙人减,会员加
                String sid = "0_" + partner.getPartnerSid();
                if (valueA > 0) {
                    partnerWallet.setAvailableScoreA(partnerWallet.getAvailableScoreA() - valueA);
                    PartnerScoreLog scoreALog = new PartnerScoreLog();
                    scoreALog.setScoreAOrigin(1);
                    scoreALog.setDescription("邀请会员送红包");
                    scoreALog.setSid(leJiaUser.getUserSid());
                    scoreALog.setNumber(-(long) valueA);
                    scoreALog.setType(1);
                    scoreALog.setCreateDate(date);
                    scoreALog.setPartnerId(partner.getId());
                    partnerScoreLogRepository.save(scoreALog);
                    ScoreA scoreA = scoreARepository.findByLeJiaUser(leJiaUser).get(0);
                    scoreA.setScore(scoreA.getScore() + valueA);
                    scoreA.setTotalScore(scoreA.getTotalScore() + valueA);
                    scoreA.setLastUpdateDate(date);
                    scoreARepository.save(scoreA);
                    ScoreADetail scoreADetail = new ScoreADetail();
                    scoreADetail.setNumber(Long.valueOf(String.valueOf(valueA)));
                    scoreADetail.setScoreA(scoreA);
                    scoreADetail.setOperate("注册送礼");
                    scoreADetail.setOrigin(0);
                    scoreADetail.setOrderSid(sid);
                    scoreADetailRepository.save(scoreADetail);
                }
                if (valueB > 0) {
                    partnerWallet.setAvailableScoreB(partnerWallet.getTotalScoreB() - valueB);
                    PartnerScoreLog scoreBLog = new PartnerScoreLog();
                    scoreBLog.setPartnerId(partner.getId());
                    scoreBLog.setType(0);
                    scoreBLog.setNumber(-(long) valueB);
                    scoreBLog.setDescription("邀请会员送积分");
                    scoreBLog.setSid(leJiaUser.getUserSid());
                    scoreBLog.setScoreBOrigin(1);
                    scoreBLog.setCreateDate(date);
                    partnerScoreLogRepository.save(scoreBLog);
                    ScoreB scoreB = scoreBRepository.findByLeJiaUser(leJiaUser);
                    scoreB.setLastUpdateDate(date);
                    scoreB.setScore(scoreB.getScore() + valueB);
                    scoreB.setTotalScore(scoreB.getTotalScore() + valueB);
                    scoreBRepository.save(scoreB);
                    ScoreBDetail scoreBDetail = new ScoreBDetail();
                    scoreBDetail.setNumber((long) valueB);
                    scoreBDetail.setScoreB(scoreB);
                    scoreBDetail.setOperate("注册送礼");
                    scoreBDetail.setOrigin(0);
                    scoreBDetail.setOrderSid(sid);
                    scoreBDetailRepository.save(scoreBDetail);
                }
                partnerWalletRepository.save(partnerWallet);
                map.put("return", 1);
                map.put("scoreA", valueA);
                map.put("scoreB", valueB);
            } else {
                map.put("return", 0);
            }
        } else {
            map.put("return", 0);
        }
        return map;
    }

    public Map<String, Integer> lockGiveScoreToUser(WeiXinUser weiXinUser, String phoneNumber,
                                                    Merchant merchant) {
        lock.lock();
        Map map = giveScoreToUser(weiXinUser, phoneNumber, merchant);
        lock.unlock();
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Partner findPartnerBySid(String sid) {
        return partnerRepository.findByPartnerSid(sid);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Optional findPartnerByWeiXinUser(WeiXinUser weiXinUser) {
        return partnerRepository.findByWeiXinUser(weiXinUser);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Boolean bindWeiXinUser(Partner partner, WeiXinUser weiXinUser) {
        if (partner != null && weiXinUser != null) {
            long partnerUserLimit = leJiaUserRepository.countPartnerBindLeJiaUser(partner.getId());
            LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
            boolean flag = true;
            if (!leJiaUser.getBindPartner().getId().equals(partner.getId())) { //未绑上
                if (partner.getUserLimit() > partnerUserLimit) {
                    Merchant
                            merchant =
                            merchantService.findMerchantByPartnerAndPartnership(partner, 2).get(0);
                    leJiaUser.setBindMerchant(merchant);
                    leJiaUser.setBindPartner(partner);
                    leJiaUserRepository.save(leJiaUser);
                    partner.setWeiXinUser(weiXinUser);
                    partnerRepository.save(partner);
                } else {
                    flag = false;
                }
            } else {
                partner.setWeiXinUser(weiXinUser);
                partnerRepository.save(partner);
            }
            if (flag) {
                new Thread(() -> {
                    String
                            getUrl =
                            "http://www.lepluspay.com/api/partner/bind_wx_user/" + partner.getPartnerSid();
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpGet httpGet = new HttpGet(getUrl);
                    httpGet.addHeader("Content-Type", "text/html;charset=UTF-8");
                    CloseableHttpResponse response = null;
                    try {
                        response = httpclient.execute(httpGet);
                        HttpEntity entity = response.getEntity();
                        EntityUtils.consume(entity);
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            return flag;
        }
        return false;
    }

    /***
     *  合伙人的锁定会员信息  17/05/11
     *  1.合伙人锁定会员；
     *  2.合伙人锁定会员数；
     *  3.今日锁定会员数。
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map findUserByPartner(Partner partner) {
        Map result = new HashMap();
        List<LeJiaUser> bindUsers = leJiaUserRepository.findByBindPartner(partner);
        Long bindCount = leJiaUserRepository.countPartnerBindLeJiaUser(partner.getId());
        Long dailyBindCount = leJiaUserRepository.countPartnerDailyBindLeJiaUser(partner.getId());
        result.put("bindUsers", bindUsers);
        result.put("bindCount", bindCount);
        result.put("dailyBindCount", dailyBindCount);
        return result;
    }

    /**
     *  合伙人佣金记录  17/05/12
     *  1.线下佣金记录
     *  2.线上佣金记录
     *  3.总佣金
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map findPartnerCommisssion(Partner partner) {
        Map result = new HashMap();
        //  佣金记录
        List<PartnerWalletOnlineLog> onlineLogs = partnerWalletOnlineLogRepository.findByPartnerIdOrderByCreateDateDesc(partner.getId());
        List<PartnerWalletLog> offLineLogs = partnerWalletLogRepository.findByParnterIdOrderByCreateDateDesc(partner.getId());
        //  佣金总计
        Long sumOffLine = partnerWalletLogRepository.countOffLineCommission(partner.getId());
        Long sumOnLine = partnerWalletOnlineLogRepository.countOnlineCommission(partner.getId());
        Long totalCommission = (sumOnLine==null?0:sumOnLine)+(sumOffLine==null?0:sumOffLine);
        result.put("onlineLogs",onlineLogs);
        result.put("offLineLogs",offLineLogs);
        result.put("totalCommission",totalCommission);
        return result;
    }

    /**
     *  合伙人的好店信息  17/05/12
     *  1.门店名称
     *  2.每日佣金
     *  3.锁定会员数
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<Object[]> findMerchantDataByPartner(MerchantCriteria merchantCriteria) {
        Partner partner = merchantCriteria.getPartner();
        List<Object[]> list = null;
        if(merchantCriteria.getType()==0) {
            if(merchantCriteria.getOrderBy()==0) {
                list =  partnerRepository.findMerchantsDataByPartnerOrderByAmountAsc(partner.getId());
            }else {
                list =  partnerRepository.findMerchantsDataByPartnerOrderByAmountDesc(partner.getId());
            }
        }else {
            if(merchantCriteria.getOrderBy()==0) {
                list =  partnerRepository.findMerchantsDataByPartnerOrderByBindUserAsc(partner.getId());
            }else {
                list =  partnerRepository.findMerchantsDataByPartnerOrderByBindUserDesc(partner.getId());
            }
        }
        return list;
    }

}

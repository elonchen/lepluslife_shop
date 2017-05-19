<%--
  Created by IntelliJ IDEA.
  User: xf
  Date: 2017/5/15
  Time: 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>合伙人中心</title>
    <link rel="stylesheet" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" href="${leplusShopResource}/partner_center/css/partnerCenter.css">
    <script src="${commonResource}/js/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <!--个人信息-->
    <div class="top">
        <div class="wx-faceImg">
            <div class="img-inner">
                <img src="${partner.weiXinUser.headImageUrl}" alt="">
            </div>
        </div>
        <h3 class="wx-name">${partner.name}</h3>
        <div class="personalInfo">
            <div class="item myMember">
                <div class="info clearfix">
                    <span class="left">我的会员</span>
                    <span class="right"><span class="font">${userCount}</span>/${userLimit}</span>
                </div>
                <div class="progress">
                    <span style="width:${userCount/userLimit*100}%"></span>
                </div>
            </div>
            <div class="line"></div>
            <div class="item myShop">
                <div class="info clearfix">
                    <span class="left">我的好店</span>
                    <span class="right"><span class="font">${merchantCount}</span>/${merchantLimit}</span>
                </div>
                <div class="progress">
                    <span style="width:${merchantCount/merchantLimit*100}%"></span>
                </div>
            </div>

        </div>
        <h3 class="btn-crease">名额不够？点此申请扩容</h3>
    </div>
    <!--提现-->
    <div class="tx clearfix">
        <span class="left">我的佣金：￥${totalCommission/100.0}</span>
        <span class="right btn-tx" onclick="withdrawCenter()">提现</span>
    </div>
    <!--菜单-->
    <div class="menu">
        <div class="item border-bottom border-right" onclick="commissionRecord()">
            <div class="menu-wrapper">
                <div class="icon"><img src="${leplusShopResource}/partner_center/images/yongjinjilu@2x.png" alt=""></div>
                <h3>佣金记录</h3>
            </div>
        </div>
        <div class="item border-bottom border-right" onclick="withdrawRecord()">
            <div class="menu-wrapper">
                <div class="icon"><img src="${leplusShopResource}/partner_center/images/tixainjilu@2x.png" alt=""></div>
                <h3>提现记录</h3>
            </div>
        </div>
        <div class="item item-final border-bottom" onclick="myMember()">
            <div class="menu-wrapper">
                <div class="icon"><img src="${leplusShopResource}/partner_center/images/wodehuiyuan@2x.png" alt=""></div>
                <h3>我的会员</h3>
            </div>
        </div>
        <div class="item border-right" onclick="myShop()">
            <div class="menu-wrapper">
                <div class="icon"><img src="${leplusShopResource}/partner_center/images/wodehaodian@2x.png" alt=""></div>
                <h3>我的好店</h3>
            </div>
        </div>
        <div class="item border-right" onclick="alert('暂未开通！')">
            <div class="menu-wrapper">
                <div class="icon"><img src="${leplusShopResource}/partner_center/images/shourupaiming@2x.png" alt=""></div>
                <h3>收入排名</h3>
            </div>
        </div>
        <div class="item item-final" onclick="customerService()">
            <div class="menu-wrapper">
                <div class="icon"><img src="${leplusShopResource}/partner_center/images/kefuzhongxin@2x.png" alt=""></div>
                <h3>客服中心</h3>
            </div>
        </div>
    </div>
    <!--弹窗-->
    <div class="shadow">
        <div class="popup">
            <div class="people"></div>
            <h3 class="ttl">如需申请扩容，请联系乐加客服</h3>
            <div class="btn">
                <div class="btn-cancel">取消</div>
                <div class="btn-tokefu">前往客服中心</div>
            </div>
        </div>
    </div>
</div>
<script>
    $(".btn-crease").on("touchstart", function (e) {
        popupShow();
        $('.shadow').on('touchstart', function () {
            popupHide();
        });
        e.stopPropagation();//阻止事件向上冒泡
    });
    $(".shadow .popup").on("touchstart", function (e) {
        e.stopPropagation();//阻止事件向上冒泡
    });
    $(".shadow .btn-cancel").on("touchstart", function () {
        popupHide();
    });
    function popupShow() {
        $(".shadow").css("display","block");
        setTimeout(function () {
            $(".shadow").css("opacity",1);
        },0.1);
    }
    function popupHide() {
        $(".shadow").css("opacity",0);
        setTimeout(function () {
            $(".shadow").css("display","none");
        },0.1);
    }
    //  页面跳转
    function withdrawCenter() {
        location.href = "/front/partnerCenter/weixin/withdrawCenter";
    }
    function withdrawRecord() {
        location.href = "/front/partnerCenter/weixin/withdrawRecord";
    }
    function myShop() {
        location.href = "/front/partnerCenter/weixin/myShops";
    }
    function myMember() {
        location.href = "/front/partnerCenter/weixin/myMember";
    }
    function customerService() {
        location.href = "/front/partnerCenter/weixin/customerService";
    }
    function commissionRecord() {
        location.href = "/front/partnerCenter/weixin/commissionRecord";
    }
</script>
</body>
</html>
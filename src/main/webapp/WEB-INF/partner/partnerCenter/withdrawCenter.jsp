<%--
  Created by IntelliJ IDEA.
  User: xf
  Date: 2017/5/15
  Time: 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>提现中心</title>
    <link rel="stylesheet" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" href="${leplusShopResource}/partner_center/css/withdrawCenter.css">
    <script src="${commonResource}/js/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <div class="top">
        <h3 class="ttl">佣金余额（元）</h3>
        <h3 class="money">${totalCommission/100.0}</h3>
    </div>
    <div class="prompt">提现金额将以微信红包的形式发给您当前使用的微信号</div>
    <div class="chooseMoney">
        <div class="item active">
            <div class="moneyWrapper">
                <h3 class="money">50元</h3>
                <div class="icon"><img src="${leplusShopResource}/partner_center/images/50@2x.png" alt=""></div>
            </div>
        </div>
        <div class="item">
            <div class="moneyWrapper">
                <h3 class="money">100元</h3>
                <div class="icon"><img src="${leplusShopResource}/partner_center/images/100@2x.png" alt=""></div>
            </div>
        </div>
        <div class="item">
            <div class="moneyWrapper">
                <h3 class="money">200元</h3>
                <div class="icon"><img src="${leplusShopResource}/partner_center/images/200@2x.png" alt=""></div>
            </div>
        </div>
    </div>
    <div class="btn-tx">确认提现</div>
    <!--弹窗-->
    <div class="shadow">
        <div class="popup">
            <div class="icon"></div>
            <h3 class="ttl">申请已经成功</h3>
            <p class="desc">本笔提现将在1-2个工作日内到账</p>
            <div class="btn-back" onclick="partnerCenter()">返回合伙人中心</div>
            <div class="close"></div>
        </div>
    </div>
</div>
<script>
    var totalCommission = "${totalCommission}";
    alert(totalCommission);
    $(".chooseMoney .item").on("touchstart", function () {
        $(".chooseMoney .item").removeClass("active");
        $(this).addClass("active");
    });

    $(".btn-tx").on("touchstart", function (e) {
       /* popupShow();
        $('.shadow').on('touchstart', function () {
            popupHide();
        });
        e.stopPropagation();//阻止事件向上冒泡*/
        withdraw(10000);
    });
    $(".shadow .popup").on("touchstart", function (e) {
        e.stopPropagation();//阻止事件向上冒泡
    });
    $(".shadow .close").on("touchstart", function () {
        popupHide();
        location.href = "/front/partnerCenter/weixin/";
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
    function withdraw(price) {
        $.ajax({
            type: "post",
            url: "/front/partnerCenter/weixin/withdrawCenter/withdraw",
            data: {price:price},
            success: function (response) {
               if(response.status!=500) {
                   popupShow();
               }else {
                   alert(response.msg);
               }
            }
        });
    }
    function partnerCenter() {
        location.href = "/front/partnerCenter/weixin/";
    }
</script>
</body>
</html>
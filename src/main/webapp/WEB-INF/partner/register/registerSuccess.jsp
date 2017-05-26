<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 2017/5/12
  Time: 下午3:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,minimum-scale=1.0,user-scalable=no">
  <%--<meta name="viewport" id="viewport" content="width=device-width, initial-scale=1">--%>
  <meta name="format-detection" content="telephone=no">
  <meta name="format-detection" content="telephone=yes"/>
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <title>注册成功</title>
  <link rel="stylesheet" href="${leplusShopResource}/partner_register/css/reset.css">
  <link rel="stylesheet" href="${leplusShopResource}/partner_register/css/registerSuccess.css">
</head>
<style>
  .swiper-slide {
    width: 30%;
    background-color: #eaddca;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    border-radius: 5px;
  }

  .swiper-slide > p {
    width: 100%;
    text-align: center;
    color: #8f7655;
    font-size: 0.7rem;
    margin: 10% auto;
    margin-top: 15%;
  }

  .swiper-slide > div {
    width: 60%;
    margin: 10% auto;

  }
</style>
<body>
<section class="bg">
  <div class="blank">
    <div class="a">
      <div class="headRound">
        <img src="${leplusShopResource}/partner_register/img/round.png" alt="">
      </div>
      <div class="headImg">
        <img src="${weiXinUser.headImageUrl}" alt="">
      </div>
      <div class="horn">
        <img src="${leplusShopResource}/partner_register/img/horn.png" alt="">
      </div>
      <p class="name">${weiXinUser.nickname}</p>
      <div class="text fixedClear">
        <div>
          <img src="${leplusShopResource}/partner_register/img/partner.png" alt="">
        </div>
        <div>恭喜您注册成功！</div>
      </div>
      <div>
        <div class="button" onclick="window.location.href='/front/partnerCenter/weixin/'">前往合伙人中心</div>
      </div>
    </div>
  </div>
</section>
</body>
<script src="${leplusShopResource}/partner_register/js/jquery.min.js"></script>
<script>
  $(".bg").css("height",$(window).height() - $(window).width()*0.24 + "px");
</script>
</html>
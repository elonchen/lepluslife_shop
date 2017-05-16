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
    <title>客服中心</title>
    <link rel="stylesheet" href="${commonResource}/css/reset.css">
    <script src="${commonResource}/js/zepto.min.js"></script>
    <link rel="stylesheet" href="${leplusShopResource}/partner_center/css/costomerService.css">
</head>
<body>
<div class="main">
    <div class="window">
        <span class="logo"></span>
        <span class="logo-little logo-left-top"></span>
        <span class="logo-little logo-left-bottom"></span>
        <span class="logo-little logo-right-top"></span>
        <span class="logo-little logo-right-bottom"></span>
        <h3 class="ttl">乐加生活客服微信</h3>
        <div class="rvCode"><img src="${leplusShopResource}/partner_center/img/rvcode.jpg" alt=""></div>
        <h3 class="desc">长按识别二维码</h3>
        <div class="tel">
            <h3 class="tel-ttl">客服电话</h3>
            <p class="tel-num"><a href="tel:400-0412-800">${servicePhone}</a></p>
        </div>
    </div>
    <h3 class="warning">您也可以直接在公众号里留言反馈问题</h3>
</div>
</body>
</html>
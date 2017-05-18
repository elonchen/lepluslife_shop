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
    <title>我的会员</title>
    <link rel="stylesheet" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" href="${leplusShopResource}/partner_center/css/myMember.css">
    <script src="${commonResource}/js/zepto.min.js"></script>
    <script src="${leplusShopResource}/partner_center/js/refresh.js"></script>
</head>
<body>
<div class="main">
    <div class="top">
        <div class="wx-faceImg">
            <div class="img-inner">
                <img src="${partner.weiXinUser.headImageUrl}" alt="">
            </div>
        </div>
        <h3 class="wx-name">${partner.name}</h3>
        <div class="personalInfo">
            <div class="item">
                <p>${data.dailyBindCount}</p>
                <p>今日新增</p>
            </div>
            <div class="item">
                <p>${data.bindCount}</p>
                <p>累计锁定</p>
            </div>

        </div>
    </div>
    <div class="list-wrapper-out">
        <div class="list-wrapper-in" id="content">
            <c:forEach var="user" items="${data.bindUsers}">
                <div class="list">
                    <div class="face-img"><img src="${user.weiXinUser.headImageUrl}" alt=""></div>
                    <div class="desc">
                        <p class="clearfix"><span class="left ttl">${user.weiXinUser.nickname}</span><span class="right time"><fmt:formatDate value="${user.bindPartnerDate}"    pattern="yyyy-MM-dd hh:mm:ss" type="date" dateStyle="long" /></span>
                        </p>
                        <p class="tel">${user.phoneNumber}</p>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
<script>
    //    初始化上啦加载
    Refresh(refreshFun);
    //     全局页数
    var  currPage = 1;
    //     加载时执行的函数
    function refreshFun() {
        var content = document.getElementById("content");
        var newContent = "";
        $.ajax({
            type: "get",
            url: "/front/partnerCenter/weixin/myMember/"+currPage,
            contentType: "application/json",
            success: function (data) {
               var users =  data.data;
               for(var i=0;i<users.length;i++) {
                   newContent+='<div class="list">'+
                               '<div class="face-img"><img src="'+users[i][0].headImageUrl+'" alt=""></div>'+
                               '<div class="desc">'+
                               '<p class="clearfix"><span class="left ttl">'+users[i][0].nickname+'</span><span class="right time">'+users[i][1]+'</span>'+
                               '</p> <p class="tel">'+users[i][2]+'</p>'+
                               '</div></div>';
               }
               content.innerHTML+=newContent;
               currPage = currPage+1;
            }
        });
    }
</script>
</body>
</html>
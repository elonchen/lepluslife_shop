<%--
  Created by IntelliJ IDEA.
  User: xf
  Date: 2017/5/15
  Time: 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>我的好店</title>
    <link rel="stylesheet" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" href="${leplusShopResource}/partner_center/css/myShop.css">
    <script src="${commonResource}/js/zepto.min.js"></script>
    <script src="${leplusShopResource}/partner_center/js/refresh.js"></script>
</head>
<body>
<div class="main">
    <div class="summary clearfix">
        <span class="left">累计锁定</span>
        <span class="right">${totalBind}</span>
    </div>
    <div class="tab">
        <div class="tab-true active logo-focus-up"><span>昨日流水</span></div>
        <div class="tab-line"></div>
        <div class="tab-true logo-init"><span>锁定会员</span></div>
    </div>
    <div class="list-wrapper-out">
        <div class="list-wrapper-in">
            <div class="zuoriliushui" id="dailyCommission">
                <c:forEach var="data" items="${list}">
                    <div class="list">
                        <div class="face-img"><img src="http://www.tiegancrm.com/leplus_shop/partner_center/images/Group4@2x.png" alt=""></div>
                        <div class="desc">
                            <p class="clearfix"><span class="left ttl">${data[1]}</span><span class="right num">锁定会员：<span class="origin-color">${data[2]}</span>/${data[4]}</span>
                            </p>
                            <p class="money">昨日流水：¥${data[3]/100.0}</p>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="suodinghuiyuan" id="bindUsers">

            </div>
        </div>
    </div>
</div>
<script>
    //    tab切换 newState为左右状态值，0为左，1为右
    var oldState = newState = 0;
    //    upDownState为上下状态值，0为上，1为下
    var upDownState = 0;
    $(".tab .tab-true").on("touchstart", function () {
        oldState = newState;
        var index = $(this).index() == 2 ? 1 : 0;
        newState = index;
        if(newState == oldState){
            if ($(this).hasClass("logo-focus-up")) {
                $(this).removeClass("logo-focus-up").addClass("logo-focus-down");
                upDownState = 1;
            } else if ($(this).hasClass("logo-focus-down")) {
                $(this).removeClass("logo-focus-down").addClass("logo-focus-up");
                upDownState = 0;
            }
        }else {
            $(".tab .tab-true").removeClass("active logo-focus-up logo-focus-down");
            $(this).addClass("active logo-focus-up");
            upDownState = 0;
        }
        //  调用方法
        loadData(newState,upDownState);
        $(".list-wrapper-in > div").css("display", "none");
        $(".list-wrapper-in > div").eq(index).css("display", "block");
    });

    //  type  0 - 根据每日佣金排序     1- 根据锁定会员数排序  orderBy 0 - 正序     1- 倒序
    function loadData(type,orderBy) {
        var merchantCriteria={};
        merchantCriteria.type=type;
        merchantCriteria.orderBy=orderBy;
        var cmContent = document.getElementById("dailyCommission");
        var buContent = document.getElementById("bindUsers");
        var newContent = "";
        $.ajax({
            type: "post",
            url: "/front/partnerCenter/myShopsByCriteria",
            data: merchantCriteria,
            success: function (response) {
               var list = response.data;
               for(var i=0;i<list.length;i++) {
                   newContent+='<div class="list"><div class="face-img"><img src="http://www.tiegancrm.com/leplus_shop/partner_center/images/Group4@2x.png" alt=""></div>'+
                       '<div class="desc">'+
                       '<p class="clearfix"><span class="left ttl">'+list[i][1]+'</span><span class="right num">锁定会员：<span class="origin-color">'+list[i][2]+'</span>/'+list[i][4]+'</span>'+
                   '</p><p class="money">昨日流水：¥'+(list[i][3]/100.0)+'</p></div></div>';
               }
               if(type==0){
                   cmContent.innerHTML=newContent;
               }else {
                   buContent.innerHTML=newContent;
               }
            }
        });
    }
</script>
</body>
</html>
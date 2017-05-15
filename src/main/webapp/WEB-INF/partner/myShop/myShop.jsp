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
        <span class="right">34</span>
    </div>
    <div class="tab">
        <div class="tab-true active logo-focus-up"><span>昨日流水</span></div>
        <div class="tab-line"></div>
        <div class="tab-true logo-init"><span>锁定会员</span></div>
    </div>
    <div class="list-wrapper-out">
        <div class="list-wrapper-in">
            <div class="zuoriliushui">
                <div class="list">
                    <div class="face-img"><img src="image/Group4@2x.png" alt=""></div>
                    <div class="desc">
                        <p class="clearfix"><span class="left ttl">一品江南</span><span class="right num">锁定会员：<span class="origin-color">32</span>/100</span>
                        </p>
                        <p class="money">昨日流水：¥200.88</p>
                    </div>
                </div>
                <div class="list">
                    <div class="face-img"><img src="image/Group4@2x.png" alt=""></div>
                    <div class="desc">
                        <p class="clearfix"><span class="left ttl">一品江南</span><span class="right num">锁定会员：<span class="origin-color">32</span>/100</span>
                        </p>
                        <p class="money">昨日流水：¥200.88</p>
                    </div>
                </div>
                <div class="list">
                    <div class="face-img"><img src="image/Group4@2x.png" alt=""></div>
                    <div class="desc">
                        <p class="clearfix"><span class="left ttl">一品江南</span><span class="right num">锁定会员：<span class="origin-color">32</span>/100</span>
                        </p>
                        <p class="money">昨日流水：¥200.88</p>
                    </div>
                </div>
            </div>
            <div class="suodinghuiyuan">
                <div class="list">
                    <div class="face-img"><img src="image/Group4@2x.png" alt=""></div>
                    <div class="desc">
                        <p class="clearfix"><span class="left ttl">一品江南</span><span class="right num">锁定会员：<span class="origin-color">32</span>/100</span>
                        </p>
                        <p class="money">昨日流水：¥200.88</p>
                    </div>
                </div>
                <div class="list">
                    <div class="face-img"><img src="image/Group4@2x.png" alt=""></div>
                    <div class="desc">
                        <p class="clearfix"><span class="left ttl">一品江南</span><span class="right num">锁定会员：<span class="origin-color">32</span>/100</span>
                        </p>
                        <p class="money">昨日流水：¥200.88</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    //    tab切换
    var oldState = newState = 0;
    $(".tab .tab-true").on("touchstart", function () {
        oldState = newState;
        var index = $(this).index() == 2 ? 1 : 0;
        newState = index;
        if(newState == oldState){
            if ($(this).hasClass("logo-focus-up")) {
                $(this).removeClass("logo-focus-up").addClass("logo-focus-down");
            } else if ($(this).hasClass("logo-focus-down")) {
                $(this).removeClass("logo-focus-down").addClass("logo-focus-up");
            }
        }else {
            $(".tab .tab-true").removeClass("active logo-focus-up logo-focus-down");
            $(this).addClass("active logo-focus-up");
        }


        $(".list-wrapper-in > div").css("display", "none");
        $(".list-wrapper-in > div").eq(index).css("display", "block");
    });
    //    初始化上啦加载
    Refresh(refreshFun);
    //     加载时执行的函数
    function refreshFun() {
        alert('上拉加载！')
    }
</script>
</body>
</html>
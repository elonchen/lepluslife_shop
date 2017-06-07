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
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>成为合伙人</title>
    <link rel="stylesheet" href="${leplusShopResource}/partner_register/css/reset.css">
    <link rel="stylesheet" href="${leplusShopResource}/partner_register/css/becomePartner.css">
    <link rel="stylesheet" href="${leplusShopResource}/partner_register/css/swiper.min.css">
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
        font-size: 0.8rem;
        margin: 0 auto;
        margin-top: 15%;
        display: block;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .swiper-slide > div {
        width: 60%;
        margin: 0 auto;
    }

    .w-watch > div {
        float: left;
    }

    .w-watch {
        position: absolute;
        top: 3%;
        left: 5%;
        z-index: 99999999;
    }

    .w-watch > div:first-child {
        width: 14%;
        margin-right: 3%;
    }

    .w-watch > div:last-child {
        font-size: 13px;
        color: #333;
    }

    .w-watch > div:first-child img {
        width: 100%;
        display: block;
    }
</style>
<body>
<div class="clearfix w-watch">
    <div><img src="${leplusShopResource}/partner_register/img/wen.png" alt=""></div>
    <div onclick="window.location.href='http://image.tiegancrm.com/leplus_shop/partner_benefit/index.html';">
        查看合伙人权益
    </div>
</div>
<div class="headRound" class="${leplusShopResource}/partner_register/headRound">
    <img src="${leplusShopResource}/partner_register/img/round.png" alt="">
</div>
<div id="loader"></div>
<div class="nowCost">
    <p>当前消费金额</p>
    <p><span>￥</span>${current/100.0}</p>
</div>
<div class="up">
    <p>消费满额${limit/100.0}元</p>
    <img src="${leplusShopResource}/partner_register/img/up.png" alt="">
</div>
<div class="down">
    <p>当前消费进度</p>
    <img src="${leplusShopResource}/partner_register/img/down.png" alt="">
</div>
<div class="text fixedClear">
    <div>
        <img src="${leplusShopResource}/partner_register/img/partner.png" alt="">
    </div>
    <div>臻品商城消费满<span>${limit/100.0}元</span>即可成为乐+合伙人</div>
</div>
<div class="swiper-container">
    <div class="swiper-wrapper" id="productList">
    </div>
</div>
<div>
    <div class="button" onclick="window.location.href='/front/product/weixin/productIndex'">逛逛商城
    </div>
</div>
</body>

<script src="${leplusShopResource}/partner_register/js/jquery.min.js"></script>
<script src="${leplusShopResource}/partner_register/js/jquery.percentageloader-0.1.min.js"></script>
<script src="${leplusShopResource}/partner_register/js/swiper.jquery.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var $loader;
        var totalKb = ${limit/100.0};
        var kb = 0;
        $loader = $("#loader").percentageLoader({
                                                    width: $(window).width() * 0.43,
                                                    height: $(window).width() * 0.43,
                                                    progress: 0
                                                });

        var animateFunc = function () {
            if (kb > totalKb) {
                kb = totalKb;
            }
            $loader.setProgress(kb / totalKb);
            $loader.setValue(kb.toString() + 'kb');
            if (kb <${current/100.0}) {
                kb += totalKb / 20;
                setTimeout(animateFunc, 5);
            }
        };
        setTimeout(animateFunc, 5);
    });
</script>
<!--滑动-->
<script>
    $.get("/shop/productList?typeId=0&page=0", function (res) {
        res = res.data
        var s = ""
        for (var i = 0; i < 5; i++) {
            s +=
                "<div class='swiper-slide' onclick='window.location.href=\"/front/product/weixin/"
                + res[i].id + "\"'><p>" + res[i].name +
                "</p><div><img src='" + res[i].thumb + "'></div></div>"
        }
        $("#productList").html(s)
        var swiper = new Swiper('.swiper-container', {
            pagination: '.swiper-pagination',
            effect: 'coverflow',
            initialSlide: 2,
            grabCursor: true,
            centeredSlides: true,
            slidesPerView: 'auto',
            coverflow: {
                rotate: 0,
                stretch: 100,
                depth: 200,
                modifier: 1,
                slideShadows: false
            }
        });
    })
</script>
</html>

<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2017/6/9
  Time: 16:14
  关注公众号领取金币
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>会员注册</title>
    <link rel="stylesheet" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" href="${leplusShopResource}user_register/css/register.css">
</head>
<body>
<section class="registering">
    <div class="register">
        <div class="titleImg">
            <img src="${leplusShopResource}user_register/img/titleImg.png" alt="">
        </div>
        <div class="from">
            <div><input type="tel" id="phone" placeholder="请输入手机号"></div>
            <div><input type="number" id="yzm" class="yzm" placeholder="请输入验证码">
                <button class="get" id="button" disabled="disabled" onclick="sendCode(this)">
                    获取验证码
                </button>
            </div>
        </div>
        <div class="button">
            <div id="submit" onclick="doRegister()">立即注册</div>
        </div>
    </div>
</section>
<section class="success">
    <div class="register">
        <div class="titleImg">
            <img src="${leplusShopResource}user_register/img/success.png" alt="">
        </div>
        <p class="text">您已获得8.8金币</p>
        <div class="from toImg">
            <img src="${leplusShopResource}user_register/img/partner.png" alt="">
        </div>
        <div class="button">
            <div>注册成为合伙人</div>
        </div>
    </div>
</section>
<div class="showImg">
    <img src="${leplusShopResource}user_register/img/showImg.png" alt="">
</div>
<section class="main">
    <div class="state jb"><img src="${leplusShopResource}user_register/img/flowergold.png" alt="">
    </div>
    <div class="addAppend">

    </div>
</section>
</body>
<script src="${commonResource}/js/jquery.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
    //count判断是第几次加载
    var imgLength = 10, url = '/merchant/list', gps = {}, shopList = $(".hb");
    var pic = '${resourceUrl}/frontRes/activity/subPage/img/lightning.png', hasHot = 0,
        hasMerchant = 0;
    function initPage() {
        var status = '${status}';
        if (status == 1) {
            $(".headHb").hide();
            $(".hb-text").hide();
            $(".headHbEd").show();
            $(".js").hide();
            $(".sj").show();
            $('#headImg').attr('src', '${resourceUrl}/frontRes/activity/subPage/img/hb3.png');
            hotList();
        } else {
            $(".headHb").show();
        }
    }
    window.onload = initPage;//不要括号
</script>
<script>
    /**
     *
     * 判断手机号input失去焦点/立即注册激活
     *
     */
    setInterval(function () {
        var length = $('.yzm').val().length;
        var phone = $("#phone").val();
        if (phone != '' && phone.match(/\d/g).length === 11 && ((/^1[3|4|5|6|7|8]\d{9}$/.test(
                phone)))) {
            $(".get").removeAttr("disabled").addClass("active");
        } else {
            $(".get").attr("disabled", "disabled").removeClass("active");
        }
//        if (length == 6 && phone != '') {
//            $(".button")
//        } else {
//            $(".button")
//        }
    }, 100);
    /**
     *
     * 获取验证码倒计时
     * @type {string}
     */
    var clock = '';
    var nums = 60;
    var btn;
    function sendCode(thisBtn) {
        var phoneNumber = $("#phone").val();
        if ((!(/^1[3|4|5|6|7|8]\d{9}$/.test(phoneNumber)))) {
            alert("请输入正确的手机号");
            return false
        }
        $('#button').attr('onclick', '');
        $(".get").removeClass("active");
        btn = thisBtn;
        btn.disabled = true; //将按钮置为不可点击
        $(btn).html(nums + '秒重新获取');
        $.post("/user/sendCode", {
            phoneNumber: $("#phone").val(),
            type: 3
        }, function (res) {
            if (res.status != 200) {
                alert(res.msg);
            }
        });
        clock = setInterval(doLoop, 1000); //一秒执行一次
    }
    function doLoop() {
        nums--;
        if (nums > 0) {
            $(btn).html(nums + '秒重新获取');
        } else {
            clearInterval(clock); //清除js定时器
            btn.disabled = false;
            $('#button').attr('onclick', 'sendCode(this)');
            $(btn).html('获取验证码');
            $(".get").removeAttr("disabled").addClass("active");
            nums = 60; //重置时间
        }
    }
    var subSource = '${subSource}';
    function doRegister() {
        $('#submit').attr('onclick', '');
        var phone = $("#phone").val();
        var code = $("#yzm").val();

        $.post("/weixin/subPage/open", {phoneNumber: phone, code: code, subSource: subSource},
               function (data) {
                   if (data.status == 200) {
                       var map = data.data;
                       $("#scoreA").html(map.scoreA / 100);
                       $("#scoreB").html(map.scoreB / 100);
                       hotList();
                       $(".headHb").hide();
                       $(".headHbEd").show();
                       $(".js").hide();
                       $(".sj").show()
                   } else {
                       alert(data.msg);
                       $('#submit').attr('onclick', 'doRegister()');
                   }
               })
    }
</script>
<script>
    addJb();
    $(".state").click(function () {
        if ($(this).hasClass("jb")) {
            $(".addAppend").empty();
            $(this).removeClass("jb").addClass("glj");
            $(".state img").attr("src", "img/reedgold.png");
            addGlj();
        } else {
            $(".addAppend").empty();
            $(this).removeClass("glj").addClass("jb");
            $(".state img").attr("src", "img/flowergold.png");
            addJb();
        }
    });
    function addJb() {
        for (var i = 0; i < 3; i++) {
            $(".addAppend").append(
                $("<div></div>").attr("class", "DIV fixClear").append(
                    $("<div></div>").append(
                        $("<img>").attr("src", "img/can1.png")
                    )
                ).append(
                    $("<div></div>").attr("class", "DIV_").append(
                        $("<p></p>").html("农家自产原生态中华野山蜂蜜")
                    ).append(
                        $("<p></p>").append(
                            $("<span></span>").html("116.00元")
                        ).append(
                            $("<span></span>").html("市场价：128.00元")
                        )
                    ).append(
                        $("<div></div>").attr("class", "fixClear").append(
                            $("<div></div>").attr("class", "fixClear").append(
                                $("<div></div>").append(
                                    $("<img>").attr("src", "img/rob.png")
                                )
                            ).append(
                                $("<div></div>").html("已售6份")
                            )
                        ).append(
                            $("<div></div>").html("马上抢")
                        )
                    )
                )
            )
        }
    }
    function addGlj() {
        for (var i = 0; i < 3; i++) {
            $(".addAppend").append(
                $("<div></div>").attr("class", "DIV fixClear").append(
                    $("<div></div>").append(
                        $("<img>").attr("src", "img/can1.png")
                    )
                ).append(
                    $("<div></div>").attr("class", "glj").append(
                        $("<p></p>").html("农家自产原生态中华野山蜂蜜")
                    ).append(
                        $("<p></p>").attr("class", "star")
                    ).append(
                        $("<div></div>").attr("class", "fixClear").append(
                            $("<div></div>").append(
                                $("<img>").attr("src", "img/food.png")
                            )
                        ).append(
                            $("<div></div>").html("美食")
                        ).append(
                            $("<div></div>").append(
                                $("<img>").attr("src", "img/position.png")
                            )
                        ).append(
                            $("<div></div>").html("朝阳")
                        ).append(
                            $("<div></div>").append(
                                $("<img>").attr("src", "img/juli.png")
                            )
                        ).append(
                            $("<div></div>").html("1600.70km")
                        )
                    )
                )
            )
        }
    }

    $(".star").append(
        $("<img style='width: 13px !important;'>").attr("src", "img/starf.png")
    )
</script>
</html>

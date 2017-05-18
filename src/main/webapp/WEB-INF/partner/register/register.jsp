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
    <title>注册成为合伙人</title>
    <link rel="stylesheet" href="${leplusShopResource}/partner_register/css/reset.css">
    <link rel="stylesheet" href="${leplusShopResource}/partner_register/css/registering.css">
</head>
<body>
<section class="bg">
    <div class="blank">
        <div class="a">
            <div class="headImg">
                <img src="${weiXinUser.headImageUrl}" alt="">
            </div>
            <p class="name">${weiXinUser.nickname}</p>

            <div class="from">
                <div><input type="text" id="name" placeholder="姓名"></div>
                <div><input type="tel" id="phone" placeholder="手机号"
                            value="${weiXinUser.leJiaUser.phoneNumber}"></div>
                <div><input type="number" id="yzm" class="yzm" placeholder="验证码">
                    <button class="get" id="button" disabled="disabled" onclick="sendCode(this)">获取验证码</button>
                </div>
            </div>
            <div>
                <div class="button" id="submit" onclick="doRegister()">立即注册</div>
            </div>
        </div>
    </div>
</section>
</body>
<script src="${leplusShopResource}/partner_register/js/jquery.min.js"></script>
<script>
    $(".bg").css("height", $(window).height() - $(window).width() * 0.27 + "px");

    /**
     *
     * 判断手机号input失去焦点/立即注册激活
     *
     */
    setInterval(function () {
        var length = $('.yzm').val().length;
        var name = $("#name").val();
        var phone = $("#phone").val();
        if (phone != '' && phone.match(/\d/g).length === 11&&((/^1[3|4|5|6|7|8]\d{9}$/.test(phone)))) {
            $(".get").removeAttr("disabled").addClass("active");
        } else {
            $(".get").attr("disabled", "disabled").removeClass("active");
        }
        if (length == 6 && name != '' && phone != '') {
            $(".button").addClass("active");
        } else {
            $(".button").removeClass("active");
        }
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
        $('#button').attr('onclick', '');
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
            nums = 60; //重置时间
        }
    }

    function doRegister(){
        $('#submit').attr('onclick', '');
        var name = $("#name").val();
        var phone = $("#phone").val();
        var code = $("#yzm").val();

        $.post("/front/partner/weixin/doRegister", {name:name,phoneNumber: phone, code: code}, function (res) {
            if (res.status == 200) {
                window.location.href = "/front/partner/weixin/register_success"
            } else {
                $('#submit').attr('onclick', 'doRegister()');
                alert(res.msg)
            }
        })
    }
</script>
</html>

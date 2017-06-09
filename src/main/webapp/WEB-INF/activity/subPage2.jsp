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
<section class="success" style="display: none;">
    <div class="register">
        <div class="titleImg">
            <img id="successImg" src="${leplusShopResource}user_register/img/success.png" alt="">
        </div>
        <p class="text" id="successText">您已获得<span id="scoreC"></span>金币</p>
        <div class="from toImg">
            <img src="${leplusShopResource}user_register/img/partner.png" alt="">
        </div>
        <div class="button">
            <div onclick="window.location.href='/front/partner/weixin/becomePartner';">注册成为合伙人</div>
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
    function drawStar(num, url, merId) {
        for (var i = 0; i < num; i++) {
            $(merId).append($("<div></div>").append(
                $("<img style='width: 13px !important;'>").attr("src", url)));
//            $(merId).append($("<div></div>").append($("<img>").attr("src", url)))
        }
    }
    //商品列表
    function addJb(list, len) {
        for (var i = 0; i < len; i++) {
            $(".addAppend").append(
                $("<div></div>").attr('onclick', 'goProductDetail(' + list[i].id + ')').attr(
                    "class", "DIV fixClear").append(
                    $("<div></div>").append(
                        $("<img>").attr("src", list[i].picture)
                    )
                ).append(
                    $("<div></div>").attr("class", "DIV_").append(
                        $("<p></p>").html(list[i].name)
                    ).append(
                        $("<p></p>").append(
                            $("<span></span>").html(toDecimal(list[i].minPrice / 100) + '元')
                        ).append(
                            $("<span></span>").html("市场价：" + toDecimal(list[i].price / 100) + "元")
                        )
                    ).append(
                        $("<div></div>").attr("class", "fixClear").append(
                            $("<div></div>").attr("class", "fixClear").append(
                                $("<div></div>").append(
                                    $("<img>").attr("src",
                                                    "${leplusShopResource}user_register/img/rob.png")
                                )
                            ).append(
                                $("<div></div>").html("已售" + list[i].saleNumber + "份")
                            )
                        ).append(
                            $("<div></div>").html("马上抢")
                        )
                    )
                )
            )
        }
    }

    //周边商家
    function addGlj(data, len) {

        for (var i = 0; i < len; i++) {
            $(".addAppend").append(
                $("<div></div>").attr("onclick",
                                      "merchantInfo('" + data[i].id + "-"
                                      + data[i].distance + "')").attr("id",
                                                                      "aaa-"
                                                                      + data[i].id
                                                                      + "-"
                                                                      + data[i].distance).attr(
                    "class", "DIV fixClear").append(
                    $("<div></div>").append(
                        $("<img>").attr("src", data[i].picture)
                    )
                ).append(
                    $("<div></div>").attr("class", "glj").append(
                        $("<p></p>").html(data[i].name)
                    ).append(
                        $("<p></p>").attr("class", "star fixClear").attr("id",
                                                                "merchant"
                                                                + data[i].id)
                    ).append(
                        $("<div></div>").attr("class", "fixClear").append(
                            $("<div></div>").append(
                                $("<img>").attr("src",
                                                "${leplusShopResource}user_register/img/food.png")
                            )
                        ).append(
                            $("<div></div>").html(data[i].typeName)
                        ).append(
                            $("<div></div>").append(
                                $("<img>").attr("src",
                                                "${leplusShopResource}user_register/img/position.png")
                            )
                        ).append(
                            $("<div></div>").html(data[i].areaName)
                        ).append(
                            $("<div></div>").append(
                                gps.status
                                == 1
                                    ? $("<img>").attr("src",
                                                      "${leplusShopResource}user_register/img/juli.png")
                                    : ""
                            )
                        ).append(
                            gps.status
                            == 1
                                ? $("<div></div>").html(data[i].distance
                                                        > 1000
                                                            ? ((data[i].distance
                                                                / 1000).toFixed(
                                    1)
                                                               + "km")
                                                            : data[i].distance
                                                              + "m")
                                : ""
                        )
                    )
                ))
            var star = parseInt(data[i].star);
            var merId = "#merchant" + data[i].id;
            if (star > 5) {
                star = 5
            } else if (star < 0) {
                star = 0
            }
            drawStar(star, "${leplusShopResource}user_register/img/starf.png", merId);
            drawStar(5 - star, "${leplusShopResource}user_register/img/no_star.png", merId)
        }
    }
    function hotList() {
        $.ajax({
                   type: "get",
                   url: "/shop/productList?page=1&typeId=0",
                   success: function (data) {
                       var list = data.data;
                       if (list != null) {
                           var length = list.length > 5 ? 5 : list.length;
                           addJb(list, length);
                       }
                   }
               })
    }
    function merchantList() {
        gps.partnership = 1;
        $.ajax('/merchant/list?page=1', {
            dataType: 'json',
            type: 'post',
            data: gps,
            timeout: 10000,
            success: function (data) {
                var imgLength = data.length > 5 ? 5 : data.length;
                addGlj(data, imgLength);
            },
            error: function (xhr, type, errorThrown) {
                console.log(type)
            }
        })
    }
    function getLocation() {
        wx.config({
                      debug: false,
                      appId: '${wxConfig.appId}',
                      timestamp: '${wxConfig.timestamp}',
                      nonceStr: '${wxConfig.noncestr}',
                      signature: '${wxConfig.signature}',
                      jsApiList: ['getLocation']
                  });
        wx.ready(function () {
            wx.getLocation({
                               type: 'wgs84', success: function (res) {
                    var latitude = res.latitude;
                    var longitude = res.longitude;
                    gps.status = 1;
                    gps.lat = latitude;
                    gps.lon = longitude;
                    merchantList()
                }, fail: function (res) {
                    gps.status = 0;
                    merchantList()
                }, cancel: function (res) {
                    gps.status = 0;
                    merchantList()
                }
                           })
        });
        wx.error(function (res) {
        })
    }
    function toDecimal(x) {
        var f = parseFloat(x);
        if (isNaN(f)) {
            return false
        }
        f = Math.round(x * 100) / 100;
        var s = f.toString();
        var rs = s.indexOf('.');
        if (rs < 0) {
            rs = s.length;
            s += '.'
        }
        while (s.length <= rs + 2) {
            s += '0'
        }
        return s
    }

    function merchantInfo(val) {
        var str = val.split('-');
        window.location.href = "/front/shop/weixin/m?id=" + str[0]
                               + "&distance="
                               + str[1] + "&status=" + gps.status;
    }

    function goProductDetail(id) {
        location.href = "/front/product/weixin/" + id;
    }
    $(".state").click(function () {
        if ($(this).hasClass("jb")) { //商家
            $(".addAppend").empty();
            $(this).removeClass("jb").addClass("glj");
            $(".state img").attr("src", "${leplusShopResource}user_register/img/reedgold.png");
            getLocation();
        } else {
            $(".addAppend").empty();
            $(this).removeClass("glj").addClass("jb");
            $(".state img").attr("src", "${leplusShopResource}user_register/img/flowergold.png");
            hotList();
        }
    });
</script>
<script>
    //count判断是第几次加载
    var gps = {}, shopList = $(".hb");
    var pic = '${resourceUrl}/frontRes/activity/subPage/img/lightning.png';
    function initPage() {
        var status = '${status}';
        if (status == 1) {
            $('#successImg').attr('src', '${leplusShopResource}user_register/img/secondIn.png');
            $('#successText').html('无法再次领取会员奖励');
            $(".success").show();
            hotList();
        } else {
            $(".registering").show();
            $(".main").hide();
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
//        var length = $('.yzm').val().length;
        var phone = $("#phone").val();
        if (phone != '' && phone.match(/\d/g).length === 11 && ((/^1[3|4|5|6|7|8]\d{9}$/.test(
                phone)))) {
            $(".get").removeAttr("disabled").addClass("active");
        } else {
            $(".get").attr("disabled", "disabled").removeClass("active");
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
                       $(".registering").hide();
                       $("#scoreC").html(map.scoreC / 100);
                       $(".success").show();
                       $(".main").show();
                       hotList();
                   } else {
                       alert(data.msg);
                       $('#submit').attr('onclick', 'doRegister()');
                   }
               })
    }
</script>

</html>

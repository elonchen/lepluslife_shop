<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/5/16
  Time: 13:52
  邀请商户入驻
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta name="viewport" content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>邀请商户入驻</title>
    <link rel="stylesheet" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" href="${leplusShopResource}partner_invite/css/businessSettled.css">
    <link rel="stylesheet" href="${commonResource}/css/jquery-weui.min.css">
</head>
<body>
<section class="info">
    <div class="fixedClear">
        <div>商户名称</div>
        <div>
            <input type="text" id="name" placeholder="请输入商户名称">
        </div>
    </div>
    <div class="fixedClear">
        <div>所在省市</div>
        <div>
            <input type="text" id='city-picker' placeholder="省市区">
        </div>
        <div><span class="arrowUp"></span></div>
    </div>
    <div class="fixedClear">
        <div>联系电话</div>
        <div>
            <input type="tel" id="tel" placeholder="请输入商户联系电话">
        </div>
    </div>
</section>
<section class="info commission">
    <div class="fixedClear">
        <div>佣金费率</div>
        <div>
            <input type="number" id="rate" placeholder="请输入数字">
        </div>
        <div>%</div>
    </div>
</section>
<section class="button">
    <button>提交确认</button>
</section>
<section class="text">
    <p class="title">邀请商户入驻的具体步骤:</p>
    <hr>
    <ul>
        <li class="fixedClear">
            <div>· 步骤1</div>
            <div>请填写商户名称、所在省市、联系电话及入驻的佣金费率</div>
        </li>
        <li class="fixedClear">
            <div>· 步骤2</div>
            <div>乐+生活将与商户进行合作确认，签约后将该商户锁定为您发展的商户，由乐+生活负责给商户安装POS机及扫码牌</div>
        </li>
        <li class="fixedClear">
            <div>· 步骤3</div>
            <div>该商户产生的每笔佣金订单，您都可获得相应的推广费；推广费与佣金费率成正比，最高可达订单消费额的3%</div>
        </li>
    </ul>
</section>
<section class="layer">
    <div class="success">
        <div>
            <img class="layerImg"
                 src="${leplusShopResource}partner_invite/img/submitSuccessfully.png" alt="">
        </div>
        <p class="layerText">提交成功，乐+客服会尽快处理</p>
        <button class="layerClose">知道了</button>
    </div>
</section>
</body>
<script src="${commonResource}/js/jquery.min.js"></script>
<script src="${commonResource}/js/city-picker.min.js"></script>
<script src="${commonResource}/js/jquery-weui.min.js"></script>
<script src="${commonResource}/js/layer.js"></script>
<script>

    var partner = '${partner.id}';
    /***
     * 城市选择插件
     *
     */
    $("#city-picker").cityPicker({
                                     title: "选择省市区/县",
                                     onChange: function (picker, values, displayValues) {
                                         console.log(values, displayValues);
                                     }
                                 });

    /***
     * layer弹窗插件
     *
     */
    function layerStatus(status) {
        if (status == 1) {
            $(".layerImg").attr("src",
                                "${leplusShopResource}partner_invite/img/submitSuccessfully.png");
            $(".layerText").html("提交成功，乐+客服会尽快处理");
            $(".layerClose").html("知道了");
        } else {
            $(".layerImg").attr("src", "${leplusShopResource}partner_invite/img/becomePartner.png");
            $(".layerText").html("需要先成为合伙人才能锁定门店呦!");
            $(".layerClose").html("我要成为合伙人");
        }
    }

    function layerOpen() {
        layer.open({
                       type: 1,
                       title: false,
                       area: ['80%', '41%'], //宽高
                       content: $(".success")
                   });
    }

    $(".button button").click(function () {
        var name = $('#name').val();
        if (name == null || name === "") {
            alert("请输入商户名称");
            return false
        }
        var tel = $('#tel').val();
        if (tel == null || tel === "") {
            alert("请输入商户联系电话");
            return false
        }
        var rate = $('#rate').val();
        if (rate == null || rate === "") {
            alert("请输入佣金费率");
            return false
        }

        var area = $('#city-picker').val();

        if (area == null || area === '') {
            alert("请选择所在省市");
            return false
        }

        if (partner == null || partner === '') {
            layerStatus(2);
            layerOpen();
            return false
        } else {
            layerStatus(1);
        }
        var content = {};
        content.name = name;
        content.tel = tel;
        content.rate = rate;
        content.area = area;
        content.partner = partner;

        $.post("/front/form/submit", {type: 17001, content: JSON.stringify(content)},
               function (res) {
                   if (res.status === 200) {
                       layerOpen();
                   } else {
                       alert(res.msg);
                   }
               })
    });
    $(".layerClose").click(function () {
        if ($('.layerClose').html() === "我要成为合伙人") {
            window.location.href = '/front/partner/weixin/becomePartner';
        } else {
            layer.closeAll();
        }
    });


</script>
</html>

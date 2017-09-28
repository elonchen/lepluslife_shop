<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/5/15
  Time: 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>佣金记录</title>
    <link rel="stylesheet" href="${commonResource}/css/reset.css">
    <script src="${commonResource}/js/zepto.min.js"></script>
    <link rel="stylesheet" href="${leplusShopResource}/partner_center/css/commissionRecord.css">
    <script src="${leplusShopResource}/partner_center/js/refresh.js"></script>
</head>
<body>
<div class="main">
    <div class="summary clearfix">
        <span class="left">累计佣金</span>
        <span class="right">￥${totalCommission/100.0}</span>
    </div>
    <div class="tab">
        <div class="tab-true active">线上佣金收入</div>
        <div class="tab-line"></div>
        <div class="tab-true">线下佣金收入</div>
    </div>
    <div class="list-wrapper-out">
        <div class="list-wrapper-in">
            <div class="xiansahng" id="onDisplay">
                <c:forEach var="onLineLog" items="${onLineLogs}">
                    <div class="list">
                        <c:if test="${onLineLog.type==16001}">
                            <p class="clearfix desc"><span class="left ttl">锁定会员线上消费</span><span
                                    class="right num">${(onLineLog.afterChangeMoney-onLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${onLineLog.type==16002}">
                            <p class="clearfix desc"><span class="left ttl">锁定会员成为合伙人</span><span
                                    class="right num">${(onLineLog.afterChangeMoney-onLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${onLineLog.type==16003}">
                            <p class="clearfix desc"><span class="left ttl">提现</span><span
                                    class="right num">${(onLineLog.afterChangeMoney-onLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${onLineLog.type==16004}">
                            <p class="clearfix desc"><span class="left ttl">用户未接受微信红包</span><span
                                    class="right num">${(onLineLog.afterChangeMoney-onLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${onLineLog.type==16005}">
                            <p class="clearfix desc"><span class="left ttl">驳回提现请求</span><span
                                    class="right num">${(onLineLog.afterChangeMoney-onLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${onLineLog.type!=16001&&onLineLog.type!=16002&&onLineLog.type!=16003&&onLineLog.type!=16004&&onLineLog.type!=16005}">
                            <p class="clearfix desc"><span class="left ttl">消费记录</span><span
                                    class="right num">${(onLineLog.afterChangeMoney-onLineLog.beforeChangeMoney)/100.0}</span></p>
                        </c:if>
                        <p class="time"><fmt:formatDate value="${onLineLog.createDate}" pattern="yyyy-MM-dd hh:mm:ss"
                                                        type="date" dateStyle="long"/></p>
                    </div>
                </c:forEach>
            </div>
            <div class="xianxia" id="offDisplay">
                <c:forEach var="offLineLog" items="${offLineLogs}">
                    <div class="list">
                        <c:if test="${offLineLog.type==15001}">
                            <p class="clearfix desc"><span class="left ttl">锁定会员线下消费</span><span
                                    class="right num">${(offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${offLineLog.type==15002}">
                            <p class="clearfix desc"><span class="left ttl">锁定门店产生佣金</span><span
                                    class="right num">${(offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${offLineLog.type==15003}">
                            <p class="clearfix desc"><span class="left ttl">提现</span><span
                                    class="right num">${(offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${offLineLog.type==15004}">
                            <p class="clearfix desc"><span class="left ttl"> 用户未接受微信红包</span><span
                                    class="right num">${(offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${offLineLog.type==15005}">
                            <p class="clearfix desc"><span class="left ttl"> 驳回提现请求</span><span
                                    class="right num">${(offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney)/100.0}</span>
                            </p>
                        </c:if>
                        <c:if test="${offLineLog.type!=15001&&offLineLog.type!=15002&&offLineLog.type!=15003&&offLineLog.type!=15004&&offLineLog.type!=15005}">
                            <p class="clearfix desc"><span class="left ttl">消费记录</span><span
                                    class="right num">${(offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney)/100.0}</span></p>
                        </c:if>
                        <p class="time"><fmt:formatDate value="${offLineLog.createDate}" pattern="yyyy-MM-dd hh:mm:ss"
                                                        type="date" dateStyle="long"/></p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<script>
    //    tab切换
    $(".tab .tab-true").on("touchstart", function () {
        $(".tab .tab-true").removeClass("active");
        $(this).addClass("active");
        var index = $(this).index() == 2 ? 1 : 0;
        $(".list-wrapper-in > div").css("display", "none");
        $(".list-wrapper-in > div").eq(index).css("display", "block");
    });
    //    初始化上啦加载
    Refresh(refreshFun);
    //     全局页数
    var currPage = 1;
    //  日期格式化
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()
            //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
                    : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
    //     加载时执行的函数
    function refreshFun() {
        var offContent = document.getElementById("offDisplay");
        var onContent = document.getElementById("onDisplay");
        var newOffContent = "";
        var newOnContent = "";
        $.ajax({
            type: "get",
            url: "/front/partnerCenter/weixin/commissionRecord/" + currPage,
            contentType: "application/json",
            success: function (response) {
                var offLineLogs = response.data.offLineLogs;
                var onLineLogs = response.data.onLineLogs;
                for (var off = 0; off < offLineLogs.length; off++) {
                    newOffContent += '<div class="list"><p class="clearfix desc">';
                    if (offLineLogs[off].type == 15001) {
                        newOffContent += '<span class="left ttl">锁定会员线下消费</span>';
                    } else if (offLineLogs[off].type == 15002) {
                        newOffContent += '<span class="left ttl">锁定门店产生佣金</span>';
                    } else if (offLineLogs[off].type == 15003) {
                        newOffContent += '<span class="left ttl">提现</span>';
                    } else if (offLineLogs[off].type == 15004) {
                        newOffContent += '<span class="left ttl"> 用户未接受微信红包</span>';
                    } else if (offLineLogs[off].type == 15005) {
                        newOffContent += '<span class="left ttl"> 驳回提现请求</span>';
                    } else {
                        newOffContent += '<span class="left ttl"> 历史消费记录</span>';
                    }
                    newOffContent += '<span class="right num">' + (offLineLogs[off].afterChangeMoney - offLineLogs[off].beforeChangeMoney)/100.0 + '</span></p>';
                    newOffContent += '<p class="time">' + new Date(offLineLogs[off].createDate).Format("yyyy-MM-dd HH:mm:ss") + '</p></div>';
                }
                for (var on = 0; on < onLineLogs.length; on++) {
                    newOnContent += '<div class="list"><p class="clearfix desc">';
                    if (onLineLogs[on].type == 16001) {
                        newOnContent += '<span class="left ttl">锁定会员线上消费</span>';
                    } else if (onLineLogs[on].type == 16002) {
                        newOnContent += '<span class="left ttl">锁定会员成为合伙人</span>';
                    } else if (onLineLogs[on].type == 16003) {
                        newOnContent += '<span class="left ttl">提现</span>';
                    } else if (onLineLogs[on].type == 16004) {
                        newOnContent += '<span class="left ttl"> 用户未接受微信红包</span>';
                    } else if (onLineLogs[on].type == 16005) {
                        newOnContent += '<span class="left ttl"> 驳回提现请求</span>';
                    }else {
                        newOffContent += '<span class="left ttl"> 历史消费记录</span>';
                    }
                    newOnContent += '<span class="right num">' + (onLineLogs[on].afterChangeMoney - onLineLogs[on].beforeChangeMoney)/100.0+ '</span></p>';
                    newOnContent += '<p class="time">' + new Date(onLineLogs[on].createDate).Format("yyyy-MM-dd HH:mm:ss") + '</p></div>';
                }
                offContent.innerHTML += newOffContent;
                onContent.innerHTML += newOnContent;
                currPage=currPage+1;
            }
        });
    }
</script>
</body>
</html>
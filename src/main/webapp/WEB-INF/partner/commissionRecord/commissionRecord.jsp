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
            <div class="xiansahng">
                <c:forEach var="onlineLog" items="${onlineLogs}">
                    <div class="list">
                        <c:if test="${onlineLog.type==16001}">
                            <p class="clearfix desc"><span class="left ttl">锁定会员线上消费</span><span
                                    class="right num">${offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney}</span>
                            </p>
                        </c:if>
                        <c:if test="${onlineLog.type==16002}">
                            <p class="clearfix desc"><span class="left ttl">锁定会员成为合伙人</span><span
                                    class="right num">${offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney}</span>
                            </p>
                        </c:if>
                        <c:if test="${onlineLog.type==16003}">
                            <p class="clearfix desc"><span class="left ttl">提现</span><span
                                    class="right num">${offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney}</span>
                            </p>
                        </c:if>
                        <c:if test="${onlineLog.type==16004}">
                            <p class="clearfix desc"><span class="left ttl">用户未接受微信红包</span><span
                                    class="right num">${offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney}</span>
                            </p>
                        </c:if>
                        <c:if test="${onlineLog.type==16005}">
                            <p class="clearfix desc"><span class="left ttl">驳回提现请求</span><span
                                    class="right num">${offLineLog.afterChangeMoney-offLineLog.beforeChangeMoney}</span>
                            </p>
                        </c:if>
                        <p class="time"><fmt:formatDate value="${onlineLog.createDate}" pattern="yyyy-MM-dd hh:mm:ss"
                                                        type="date" dateStyle="long"/></p>
                    </div>
                </c:forEach>
            </div>
            <div class="xianxia">
                <c:forEach var="offLineLog" items="${offLineLogs}">
                    <div class="list">
                        <p class="clearfix desc"><span class="left ttl">测试</span><span
                                class="right num">${onlineLog.afterChangeMoney-onlineLog.beforeChangeMoney}</span></p>
                        <c:if test="${offLineLog.type==15001}">
                        <p class="clearfix desc"><span class="left ttl">锁定会员线下消费</span><span
                                class="right num">${onlineLog.afterChangeMoney-onlineLog.beforeChangeMoney}</span></p>
                        </c:if>
                        <c:if test="${offLineLog.type==15002}">
                            <p class="clearfix desc"><span class="left ttl">锁定门店产生佣金</span><span
                                    class="right num">${onlineLog.afterChangeMoney-onlineLog.beforeChangeMoney}</span>
                            </p>
                        </c:if>
                        <c:if test="${offLineLog.type==15003}">
                            <p class="clearfix desc"><span class="left ttl">提现</span><span
                                    class="right num">${onlineLog.afterChangeMoney-onlineLog.beforeChangeMoney}</span>
                            </p>
                        </c:if>
                        <c:if test="${offLineLog.type==15004}">
                            <p class="clearfix desc"><span class="left ttl"> 用户未接受微信红包</span><span
                                    class="right num">${onlineLog.afterChangeMoney-onlineLog.beforeChangeMoney}</span>
                            </p>
                        </c:if>
                        <c:if test="${offLineLog.type==15005}">
                            <p class="clearfix desc"><span class="left ttl"> 驳回提现请求</span><span
                                    class="right num">${onlineLog.afterChangeMoney-onlineLog.beforeChangeMoney}</span>
                            </p>
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
    var  currPage = 1;
    //     加载时执行的函数
    function refreshFun() {
        var offContent = document.getElementById("xianxia");
        var onContent = document.getElementById("xiansahng");
        var newContent = "";
        $.ajax({
            type: "get",
            url: "/front/partnerCenter/commissionRecord/"+currPage,
            contentType: "application/json",
            success: function (response) {
               var offLineLogs =  response.data.offLineLogs;
               var onLineLogs =  response.data.onLineLogs;
               for (var off;off<offLineLogs.length;off++) {

               }
               for (var off;off<offLineLogs.length;off++) {

               }
               console.log(JSON.stringify(data));
               /* content.innerHTML+=newContent;
                currPage = currPage+1;*/
            }
        });
    }
</script>
</body>
</html>
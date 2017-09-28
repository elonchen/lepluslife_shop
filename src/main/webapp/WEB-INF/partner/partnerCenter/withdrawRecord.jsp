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
    <title>提现记录</title>
    <link rel="stylesheet" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" href="${leplusShopResource}/partner_center/css/withdrawRecord.css">
    <script src="${commonResource}/js/zepto.min.js"></script>
    <script src="${leplusShopResource}/partner_center/js/refresh.js"></script>
</head>
<body>
<div class="main">
    <div class="summary clearfix">
        <span class="left">累计提现</span>
        <span class="right">￥${totalWithDraw/100.0}</span>
    </div>
    <div class="list-wrapper-out">
        <div class="list-wrapper-in" id="content">
            <c:forEach var="record" items="${bills}">
                <div class="list">
                    <p class="clearfix desc">
                        <c:if test="${record[0]==0}">
                            <span class="left ttl">合伙人平台提现</span>
                        </c:if>
                        <c:if test="${record[0]==1}">
                            <span class="left ttl">公众号提现</span>
                        </c:if>
                        <span class="right num">+${record[2]/100.0}</span></p>
                    <p class="clearfix desc">
                        <c:if test="${record[1]==0}">
                             <span class="left mark">提现中</span>
                        </c:if>
                        <c:if test="${record[1]==1||(record[1]!=0&&record[1]!=2)}">
                            <span class="left mark">已到账</span>
                        </c:if>
                        <c:if test="${record[1]==2}">
                            <span class="left mark">未通过</span>
                        </c:if>
                        <span class="right time">${record[3]}</span></p>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
<script>
    var currPage = 1;
    //    初始化上啦加载
    Refresh(refreshFun);
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
        var content = document.getElementById("content");
        var newContent = "";
        $.ajax({
            type: "get",
            url: "/front/partnerCenter/weixin/withdrawRecordByPage/"+currPage,
            contentType: "application/json",
            success: function (data) {
                var list =  data.data;
                for(var i=0;i<list.length;i++) {
                    newContent+=' <div class="list"><p class="clearfix desc">';
                        if(list[i][0]==0) {
                            newContent+='<span class="left ttl">合伙人平台提现</span>';
                        }else {
                            newContent+='<span class="left ttl">公众号提现</span>';
                        }
                        newContent+='<span class="right num">'+(list[i][2]/100.0)+'</span></p>';
                        newContent+='<p class="clearfix desc">';
                        if(list[i][1]==0) {
                            newContent+='<span class="left ttl">提现中</span>';
                        }else if(list[i][1]==1) {
                            newContent+='<span class="left ttl">已到账</span>';
                        }else if(list[i][1]==2) {
                            newContent+='<span class="left ttl">未通过</span>';
                        }else {
                            newContent+='<span class="left ttl">已到账</span>';
                        }
                       newContent+='<span class="right time">'+new Date(list[i][3]).Format("yyyy-MM-dd HH:mm:ss")+'</span></p></div>';
                }
                content.innerHTML+=newContent;
                currPage = currPage+1;
            }
        });
    }
</script>
</body>
</html>
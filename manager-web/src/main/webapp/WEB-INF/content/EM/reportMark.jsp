<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="org.springframework.context.ApplicationContext"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- <META http-equiv="refresh" content="60"> -->
<script type="text/javascript" src="${ctx}/dwr/engine.js"></script>
<script type="text/javascript" src="${ctx}/js/comm/other/table.js"></script>
<script type="text/javascript" src="${ctx}/js/comm/other/sorttable.js"></script>
<!-- <script type='text/javascript' src='项目名称/dwr/util.js'></script> -->
<script type="text/javascript" src="${ctx}/dwr/util.js"></script>
<script language="JavaScript" src="${ctx}/js/EM/g_datetime.js"></script>
<script type="text/javascript" src="${ctx }/js/EM/event.js"></script>
<script type="text/javascript"
	src="${ctx}/dwr/interface/EventSrviceClient.js"></script>
<script type="text/javascript">
function goPage(totalPage) {
	var ip = document.getElementById("ip").value;
	var bureauId = document.getElementById("bureauId").value;
    var mark=document.getElementById("mark").value;
    if(mark > totalPage){
        alert("最大页码值为："+totalPage);
        document.getElementById("mark").value = "";
        document.getElementById("mark").focus();
        return;
    }if(mark <= 0){
        alert("请输入有效的页码值!!");
        document.getElementById("mark").value = "";
        document.getElementById("mark").focus();
        return;
    }else{
        window.location.target="childFrameEvent";
        window.location.href="eventAction.do?method=eventSpecific"+"&mark="+mark+"&faci_ip="+ip+"&bureauId="+bureauId;
    }    
}
function onclick1(){
//	window.location.href = "eventAction.do?method=eventTimeTrend&faci_ip=${faci_ip}&id=${id }";
	window.location.href = "eventAction.do?method=eventTimeTrend&faci_ip=${paraList[0]}&bureauId=${bureauId }&name=${paraList[1]}&init=${paraList[2]}"
        + "&curr=${paraList[3]}&max=${paraList[4]}&min=${paraList[5]}&thre=${paraList[6]}";
}
function onclick2(){
//  window.location.href = "eventAction.do?method=eventSpecific&faci_ip=${faci_ip}&id=${id }";
    window.location.href = "eventAction.do?method=eventSpecific&faci_ip=${paraList[0]}&bureauId=${bureauId }&name=${paraList[1]}&init=${paraList[2]}"
        + "&curr=${paraList[3]}&max=${paraList[4]}&min=${paraList[5]}&thre=${paraList[6]}";
}
function onclick3(){
    window.location.href = "eventAction.do?method=monitoringInformation&faci_ip=${paraList[0]}&bureauId=${bureauId }&name=${paraList[1]}&init=${paraList[2]}"
        + "&curr=${paraList[3]}&max=${paraList[4]}&min=${paraList[5]}&thre=${paraList[6]}";
}
function onclick4(){
    window.location.href = "eventAction.do?method=showListByTag";
}
</script>
<title>无标题文档</title>
<link href="${ctx }/css/comm/other/Newcommon.css" rel="stylesheet"
	type="text/css" />
<link href="${ctx }/css/comm/other/Maincontant.css" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" href="${ctx }/css/comm/other/common.css"
	type="text/css" />
<link rel="stylesheet" href="${ctx }/css/comm/other/boxy.css"
	type="text/css" />
<script type="text/javascript" src="${ctx }/js/comm/other/table.js"></script>
<script type="text/javascript" src="${ctx }/js/comm/other/sorttable.js"></script>
<script type="text/javascript"
	src="${ctx }/js/comm/other/js/jquery-1.2.6.pack.js"></script>
<script type="text/javascript"
	src="${ctx }/js/comm/other/js/jquery.boxy.js"></script>
<script type="text/javascript"
	src="${ctx }/js/comm/other/js/floatdiv.js"></script>
</head>
<body>
<div id="contant" class="mainbg">
<div id="banner" class="grayline overf bannerH">
<li class="act" id="m"><a
	href="${ctx }/eventAction.do?method=showListByTag"><span
	style="background: url(${ctx }/img/comm/other/03.gif) no-repeat; padding-left: 22px;">事件监测</span></a></li>
<li class="nor"><a
	href="${ctx }/eventSelectCondition.do?method=eventSelectCondition&select=safe"><span
	style="background: url(${ctx }/img/comm/other/05375363.gif) no-repeat; padding-left: 22px;">事件查询</span></a></li>
<li class="nor"><a
	href="${ctx }/eventStatisticalAnals.do?method=eventStatisticalAnalysis"><span
	style="background: url(${ctx }/img/comm/other/map01.png) no-repeat; padding-left: 22px;">统计分析</span></a></li>
<li class="nor"><a
	href="${ctx }/eventAction.do?method=eventInTimeSpecific"><span
	style="background: url(${ctx }/img/comm/other/053753177.gif) no-repeat; padding-left: 22px;">实时显示</span></a></li>
<li class="nor"><a
	href="${ctx }/eventCorrelationAction.do?method=eventCorrelation"><span
	style="background: url(${ctx }/img/comm/other/053753177.gif) no-repeat; padding-left: 22px;">事件关联</span></a></li>
<li class="nor"><a
	href="eventStatisticalAnals.do?method=eventDisplay"><span
	style="background: url(${ctx }/img/comm/other/045631195.gif) no-repeat; padding-left: 22px;">事件展示
</span></a></li>
</div>
<div class="contant"><input type="hidden" name="ip" id="ip"
	value="${faci_ip }" /> <input type="hidden" name="bureauId"
	id="bureauId" value="${bureauId }" />
<div id="data" class="greenborder overf pad1 ">
<h2>事件监测---详细信息</h2>
<table id="senfe" sortcol="-1">
	<thead>
		<tr>
			<th>时间</th>
			<th>源IP</th>
			<th>源端口</th>
			<th>目的IP</th>
			<th>目的端口</th>
			<th>威胁等级</th>
			<th>设备类型</th>
			<th>事件类型</th>
			<th>协议类型</th>
			<th>描述</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${eventrealdispList == null}">
			<c:forEach step="1" begin="1" end="9">
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</c:forEach>
		</c:if>
		<c:if test="${listSize < 10}">
			<%
				int i = 0;
			%>
			<c:forEach items="${eventrealdispList}" var="eventrealdisp">
				<%
					i++;
				%>
				<tr>
					<td>${eventrealdisp[2].eventTime }</td>
					<td>${eventrealdisp[2].srcIp }</td>
					<td>${eventrealdisp[2].srcPort }</td>
					<td>${eventrealdisp[2].destIp}</td>
					<td>${eventrealdisp[2].destPort }</td>
					<td>${eventrealdisp[2].threRank}</td>
					<td>${eventrealdisp[2].faciType}</td>
					<td>${eventrealdisp[2].eventType}</td>
					<td>${eventrealdisp[2].prot_type}</td>
					<td>${eventrealdisp[2].descrip}</td>
				</tr>
			</c:forEach>
			<c:forEach step="1" begin="<%=i%>" end="9">
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</c:forEach>
		</c:if>
		<c:if test="${eventrealdispList != null && listSize >= 10}">
			<c:forEach items="${eventrealdispList}" var="eventrealdisp">
				<tr>
					<td>${eventrealdisp[2].eventTime }</td>
					<td>${eventrealdisp[2].srcIp }</td>
					<td>${eventrealdisp[2].srcPort }</td>
					<td>${eventrealdisp[2].destIp}</td>
					<td>${eventrealdisp[2].destPort }</td>
					<td>${eventrealdisp[2].threRank}</td>
					<td>${eventrealdisp[2].faciType}</td>
					<td>${eventrealdisp[2].eventType}</td>
					<td>${eventrealdisp[2].prot_type}</td>
					<td>${eventrealdisp[2].descrip}</td>
				</tr>
			</c:forEach>
		</c:if>
		</td>
		</tr>
	</tbody>
</table>
<!-- 分页 -->
<div class="martop8 Height_t">
<ul id="page">
	<li><a class="first"
		href="eventAction.do?method=eventSpecific&pageno=1&faci_ip=${faci_ip}&bureauId=${bureauId }"></a></li>

	<c:if test="${page.currentPage-1 >= 1}">
		<li><a class="pre"
			href="eventAction.do?method=eventSpecific&pageno=${page.currentPage-1}&faci_ip=${faci_ip}&bureauId=${bureauId }"></a></li>
	</c:if>
	<c:if test="${page.currentPage-1 <= 0}">
		<li><a class="pre"
			href="eventAction.do?method=eventSpecific&pageno=1&faci_ip=${faci_ip}&bureauId=${bureauId }"></a></li>
	</c:if>

	<c:if test="${page.currentPage+1 < page.totalPage}">
		<li><a class="next"
			href="eventAction.do?method=eventSpecific&pageno=${page.currentPage+1}&faci_ip=${faci_ip}&bureauId=${bureauId }"></a></li>
	</c:if>
	<c:if test="${page.currentPage+1 >= page.totalPage}">
		<li><a class="next"
			href="eventAction.do?method=eventSpecific&pageno=${page.totalPage}&faci_ip=${faci_ip}&bureauId=${bureauId }"></a></li>
	</c:if>

	<li><a class="last"
		href="eventAction.do?method=eventSpecific&pageno=${page.totalPage}&faci_ip=${faci_ip}&bureauId=${bureauId }"></a></li>
	<li>共 ${page.totalPage }页 第${page.currentPage }页 跳至 <input
		type="text" size="4" name="mark" id="mark" />页 <input type="button"
		name="go" id="go" value="GO" size="4"
		onclick="goPage(${page.totalPage})" /></li>


	<li  style="float:right;"><input type="button" name="button1" class="an-1" value="事件趋势"
		onclick="onclick1()" /> <input type="button" name="button2"
		class="an-1" value="详细信息" onclick="onclick2()" /> <input type="button"
		name="button3" class="an-1" value="监控信息" onclick="onclick3()" /> <input
		type="button" name="button3" class="an-1" value="返回"
		onclick="onclick4()" />
</li>
</ul>
<br />
      <br />
	  <br />
</div>
</div>
</div>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/comm/other/tablelink.js"></script>
</html>


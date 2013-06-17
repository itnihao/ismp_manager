<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.TreeMap"%>
<%@page import="java.util.Iterator"%>
<%@ page import="edu.sjtu.infosec.ismp.manager.LM.dLog.web.form.SysLogInitQueryBean"%>
<script type="text/javascript" src="${ctx}/js/LM/sysLogValidate.js"></script>
<div  id="data" class="pad1 ">
<form action="sysLogAction.do?method=originalDisplay" method="post" onsubmit="return validate_SysLogQuery();">
  <table>
    <tr>
      <th> �Զ������� </th>
      <tr>
		<td>����</td>
		<td><select name="domain" style="width: 130px;">
			<option id="default" value="all" selected="selected" >--ȫ��--</option>
			<c:forEach items="${sysLogInitQueryBean.domain}" var="domain">
					<option value="${domain.id }" title="${domain.domainName }">${domain.domainName }</option>
			</c:forEach>
		</select></td>
		<td>����ģ��</td>
		<td><select name="facility" style="width: 130px;">
			<option id="default" value="all" selected="selected">--ȫ��--</option>
			<%
				TreeMap<Integer,String> facility = ((SysLogInitQueryBean)request.getAttribute("sysLogInitQueryBean")).getFacility();
				Iterator<Integer> facilityIter = facility.keySet().iterator();	
				for(;facilityIter.hasNext();){
					int facilityKey =  facilityIter.next();
					String facilityValue = facility.get(facilityKey);
			%>
					<option value="<%=facilityKey %>" title="<%=facilityKey %>-<%=facilityValue %>"><%=facilityKey %>-<%=facilityValue %></option>
			<%
				}
			%>
		</select></td>
	</tr>
	<tr>
		<td>��ʼʱ��</td>
		<td><input id="beginDate" name="beginDate" class="Wdate" readonly="readonly"
			type="text" value="${sysLogInitQueryBean.beginDate }"
			onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
		<td>��ֹʱ��</td>
		<td><input id="endDate" name="endDate" class="Wdate" readonly="readonly" type="text"
			value="${sysLogInitQueryBean.endDate }"
			onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
	</tr>
	<tr>
		<td>����IP</td>
		<td><input name="hostname" type="text" size="15" value="${sysLogResponseQueryBean.hostname }" /></td>
		<td>������</td>
		<td><select name="severity" style="width: 130px;">
			<option id="default" value="all" selected="selected">--ȫ��--</option>
			<%
				TreeMap<Integer,String> severity = ((SysLogInitQueryBean)request.getAttribute("sysLogInitQueryBean")).getSeverity();
				Iterator<Integer> severityIter = severity.keySet().iterator();
				for(;severityIter.hasNext();){
					int severitykey =  severityIter.next();
					String severityValue = severity.get(severitykey);
			%>
					<option value="<%=severitykey %>" title="<%=severitykey %>-<%=severityValue %>"><%=severitykey %>-<%=severityValue %></option>
			<%
				}
			%>
		</select></td>
	</tr>
	<tr>
		<td>��Ϣ�ؼ���</td>
		<td colspan="3"><input name="message" type="text" size="50" value="${sysLogResponseQueryBean.message }" /></td>
	</tr>
  </table>
  <div class="paddiv"></div> 
  <input class="submit" type="submit" name="����" value="�ύ" />
  <div class="paddiv"></div>
</form>
</div>

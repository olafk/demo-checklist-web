<%@page import="com.liferay.sales.checklist.ChecklistItem"%>
<%@page import="java.util.List"%>
<%@ include file="init.jsp" %>

<p>
	<b><liferay-ui:message key="demochecklist.caption"/></b>
</p>

<% List<ChecklistItem> checklist = (List<ChecklistItem>) renderRequest.getAttribute("checklist"); %>
<c:forEach items="<%=checklist%>" var="item">
	<c:if test="${item.resolved}">
		<liferay-ui:icon image="checked"/> <liferay-ui:message key="message-${item.name}" arguments="${item.info}"/>
	</c:if>
	<c:if test="${!item.resolved}">
		<liferay-ui:icon image="unchecked"/> <liferay-ui:message key="missing-${item.name}" arguments="${item.info}"/>
		<a href="${item.link}" data-senna-off="true" target="_top" class="btn btn-primary" style="margin-left:1em;"><liferay-ui:message key="fix-it"/></a>
	</c:if>
	<br/>
</c:forEach>
<br/>
<p>
	<liferay-ui:message key="contribution-link"/>
</p>


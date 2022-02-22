<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.sales.checklist.impl.UpdatedPluginChecklistProvider"%>
<%@page import="com.liferay.sales.checklist.api.ChecklistItem"%>
<%@page import="java.util.List"%>
<%@ include file="init.jsp" %>

<p>
	<b><liferay-ui:message key="demochecklist.caption"/></b>
</p>

<% List<ChecklistItem> checklist = (List<ChecklistItem>) renderRequest.getAttribute("checklist"); %>
<c:forEach items="<%=checklist%>" var="item">
	<c:if test="${item.resolved}">
		<liferay-ui:icon image="checked"/> <c:out value="${item.message}"/>
	</c:if>
	<c:if test="${!item.resolved}">
		<liferay-ui:icon image="unchecked"/> <c:out value="${item.message}" escapeXml="false"/>
		<a href="${item.link}" data-senna-off="true" target="_top" class="btn btn-primary" style="margin-left:1em;"><liferay-ui:message key="fix-it"/></a>
	</c:if>
	<br/>
</c:forEach>
<br/>
<p>
	<liferay-ui:message key="contribution-link"/>
</p>
<p>
	<liferay-ui:message key="configure-this-plugin" arguments="<%=UpdatedPluginChecklistProvider.LINK%>"/>
</p>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="wct" uri="http://www.webcurator.org/wct" %>
<%@ page import="org.webcurator.ui.common.Constants" %>

<img src="images/x.gif" alt="" width="1" height="30" border="0" /><br />
<div id="resultsTable">
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td colspan="2"><span class="midtitleGrey">View Task</span></td>
	</tr>
	<tr>
	<td class="tableRowLiteHdr">From:</td>
	<td class="tableRowLite"><c:out value="${notification.sender}"/></td>
	</tr>
	<tr>
	<td class="tableRowLiteHdr">To:</td>
	<td class="tableRowLite"><c:out value="${notification.recipientName}"/></td>
	</tr>
	<tr>
	<td class="tableRowLiteHdr">Subject:</td>
	<td class="tableRowLite"><c:out value="${notification.subject}"/></td>
	</tr>
	<tr>
	<td class="tableRowLiteHdr">Sent:</td>
	<td class="tableRowLite"><wct:date value="${notification.sentDate}" type="fullDateTime"/></td>
	</tr>
	<tr>
	<td class="tableRowLiteHdr">Message:</td>
	<td class="tableRowLite"><c:out value="${notification.message}" escapeXml="false"/></td>
	</tr>
	<tr>
	<td colspan="2" align="center">
	<a href="<%= Constants.CNTRL_INTRAY %>"><img name="_cancel" src="images/generic-btn-cancel.gif" alt="Cancel" width="82" height="23" border="0"></a>
	</td>
	</tr>
	</table>
</div>
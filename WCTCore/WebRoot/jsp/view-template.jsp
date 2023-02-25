<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.webcurator.ui.common.Constants" %>
<%@ page import="org.webcurator.ui.admin.command.TemplateCommand" %>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="authority" uri="http://www.webcurator.org/authority"  %>

	<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<td class="subBoxTextHdr">Template Name:</td>
		<td class="subBoxText" colspan="2">
		<c:out value="${command.templateName}"/>
		</td>
	</tr>
	<tr>
		<td class="subBoxTextHdr" valign=top>Template Description:</td>
		<td class="subBoxText" colspan="2">
		<textarea style="width:400px; height:100px;" name="taTemplateDescription" readonly><c:out value="${command.templateDescription}"/></textarea>
		</td>
	</tr>
	<tr>
		<td class="subBoxTextHdr">Agency:</td>
		<td class="subBoxText" colspan="2">
        <c:forEach items="${agencies}" var="agency">
          <c:if test="${command.agencyOid == agency.oid}">
          	<c:out value="${agency.name}"/>
          </c:if>
        </c:forEach>
		</td>
	</tr>
	<tr>
		<td class="subBoxTextHdr">Template type:</td>
		<td class="subBoxText" colspan="2">
         	<c:out value="${command.templateType}"/>
		</td>
	</tr>
	<c:if test="${command.templateType == command.emailTypeText}">
	<tr>
		<td class="subBoxTextHdr">Template Subject:</td>
		<td class="subBoxText" colspan="2">
         	<c:out value="${command.templateSubject}"/>
		</td>
	</tr>
	<tr>
		<td class="subBoxTextHdr">Template Overwrite From:</td>
		<td class="subBoxText" colspan="2">
		<c:choose>
		<c:when test="${command.templateOverwriteFrom}">
			Yes
		</c:when>
		<c:otherwise>
			No
		</c:otherwise>
		</c:choose>
		
		</td>
	</tr>
	<c:if test="${command.templateOverwriteFrom}">
		<tr>
			<td class="subBoxTextHdr">Template From:</td>
			<td class="subBoxText" colspan="2">
	         	<c:out value="${command.templateFrom}"/>
			</td>
		</tr>
	</c:if>
	<tr>
		<td class="subBoxTextHdr">Template CC:</td>
		<td class="subBoxText" colspan="2">
         	<c:out value="${command.templateCc}"/>
		</td>
	</tr>
	<tr>
		<td class="subBoxTextHdr">Template BCC:</td>
		<td class="subBoxText" colspan="2">
         	<c:out value="${command.templateBcc}"/>
		</td>
	</tr>
	</c:if>
	<tr>
		<td class="subBoxTextHdr" valign=top>Template text:</td>
		<td class="subBoxText">
		<textarea style="width:400px; height:300px;" name="taTemplateText" readonly><c:out value="${command.templateText}"/></textarea>
		</td>
		<td class="subBoxText" valign=top><font color=red size=2>&nbsp;		
		</td>
	</tr>
	<tr>
		<td  class="subBoxText" colspan="3" align="center">
		<a href="<%= Constants.CNTRL_PERMISSION_TEMPLATE %>"><img name="_done" src="images/generic-btn-done.gif" alt="Done" width="82" height="23" border="0"></a>
		</td>
	</tr>
	</table>

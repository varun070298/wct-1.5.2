<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="authority" uri="http://www.webcurator.org/authority" %>
<%@ page import="org.webcurator.ui.agent.command.BandwidthRestrictionsCommand" %>

<%@page import="org.webcurator.domain.model.auth.Privilege"%>
<div id="resultsTable">
<table border="0" width="100%">
  <tr>
    <td>
    <c:set var="count" scope="page" value="0"/>
    <c:forEach items="${daysOfTheWeek}" var="day">    	    	
    	<c:set var="dayRestrictions" scope="page" value="${bandwidthRestrictions[day]}"/>
    	<table width="100%">
		  <tr>
		    <td colspan="2"><c:out value="${day}"/></td>
		  </tr>
		  <tr>
		    <td align="left">00:00:00</td>
		    <td align="right">23:59:59</td>
		  </tr>
		  <tr>
		    <td colspan="2">		    		  
		      <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="black">
		      	<tr>		      	
		  		<c:forEach items="${dayRestrictions}" var="restrictions">
		  		<c:set var="count" scope="page" value="${count + 1}"/>
		  		<td width="<c:out value="${restrictions.dayPercentage}"/>%" align="right">
		  		<authority:hasPrivilege privilege="<%=Privilege.MANAGE_WEB_HARVESTER%>" scope="<%=Privilege.SCOPE_ALL%>">
			  		<form name="bandwidth<c:out value="${count}"/>" method="POST" action="<c:out value="${action}"/>">
			  		<input type="hidden" name="<%=BandwidthRestrictionsCommand.PARAM_OID%>" value="<c:out value="${restrictions.oid}"/>"/>
			  		<input type="hidden" name="<%=BandwidthRestrictionsCommand.PARAM_DAY%>" value="<c:out value="${restrictions.dayOfWeek}"/>"/>			  		
			  		<input type="hidden" name="<%=BandwidthRestrictionsCommand.PARAM_START%>" value="<fmt:formatDate value="${restrictions.startTime}" pattern="HH:mm:ss"/>"/>
			  		<input type="hidden" name="<%=BandwidthRestrictionsCommand.PARAM_END%>" value="<fmt:formatDate value="${restrictions.endTime}" pattern="HH:mm:ss"/>"/>
			  		<input type="hidden" name="<%=BandwidthRestrictionsCommand.PARAM_LIMIT%>" value="<c:out value="${restrictions.bandwidth}"/>"/>
			  		<input type="hidden" name="<%=BandwidthRestrictionsCommand.PARAM_ACTION%>" value="<%=BandwidthRestrictionsCommand.ACTION_EDIT%>"/>
		  			<a href=# onclick="javascript:document.bandwidth<c:out value="${count}"/>.submit(); return false;">		  					  		
		  		</authority:hasPrivilege>
		  		<c:out value="${restrictions.bandwidth}"/>
		  		<authority:hasPrivilege privilege="<%=Privilege.MANAGE_WEB_HARVESTER%>" scope="<%=Privilege.SCOPE_ALL%>">		  		
		  		</a>
		  		</form>
		  		</authority:hasPrivilege>
		  		</td>		  	
		  		</c:forEach>
		  	    </tr>
		  	  </table>
			</td>		    
		  </tr>
    	</table>
    </c:forEach>
	</td>
  </tr>
</table>
</div>

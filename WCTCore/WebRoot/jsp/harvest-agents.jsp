<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="authority" uri="http://www.webcurator.org/authority" %>
<%@taglib prefix="wct" uri="http://www.webcurator.org/wct" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.webcurator.ui.agent.command.ManageHarvestAgentCommand"%>
<%@ page import="org.webcurator.ui.common.Constants"%>
<%@ page import="org.webcurator.domain.model.auth.Privilege"%>
<%
HashMap agents = (HashMap) request.getAttribute(ManageHarvestAgentCommand.MDL_HARVEST_AGENTS);
%>
<authority:hasPrivilege privilege="<%=Privilege.MANAGE_WEB_HARVESTER%>" scope="<%=Privilege.SCOPE_ALL%>">
<div id="resultsTable">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<form name="harvestAgentControl" method="POST" action="curator/agent/harvest-agent.html">
	<tr>			
		<td colspan="3" class="tableRowSep"><img src="images/x.gif" alt="" width="1" height="5" border="0" /></td>
	</tr>
	<tr>
		<td class="subHeadLite" width="50%">Control all allocated Target Instances<input type="hidden" name="<%=ManageHarvestAgentCommand.PARAM_ACTION%>" value="" /></td>
		<td class="subHeadLite"><input type="image" src="images/pause-icon.gif" alt="Pause All" title="Pause All" onclick="javascript:document.harvestAgentControl.<%=ManageHarvestAgentCommand.PARAM_ACTION%>.value='<%=ManageHarvestAgentCommand.ACTION_PAUSE%>'" /></td>
		<td class="subHeadLite"><input type="image" src="images/resume-icon.gif" alt="Resume All" title="Resume All" onclick="javascript:document.harvestAgentControl.<%=ManageHarvestAgentCommand.PARAM_ACTION%>.value='<%=ManageHarvestAgentCommand.ACTION_RESUME%>'" /></td>
	</tr>
	<tr>			
		<td colspan="3" class="tableRowSep"><img src="images/x.gif" alt="" width="1" height="5" border="0" /></td>
	</tr>
	<tr>
		<td class="subHeadLite">Control all Scheduled and Queued Target Instances</td>
		<c:choose>
		<c:when test="${command.queuePaused}">
		<td class="subHeadLite"><input type="image" src="images/calendar.gif" alt="Resume All" title="Resume All" onclick="javascript:document.harvestAgentControl.<%=ManageHarvestAgentCommand.PARAM_ACTION%>.value='<%=ManageHarvestAgentCommand.ACTION_RESUMEQ%>'" /></td>
		<td class="subHeadLite"><font color="red">Queue suspended.</font></td>
		</c:when>
		<c:otherwise>
		<td class="subHeadLite"><input type="image" src="images/calendar-halted.gif" alt="Halt All" title="Halt All" onclick="javascript:document.harvestAgentControl.<%=ManageHarvestAgentCommand.PARAM_ACTION%>.value='<%=ManageHarvestAgentCommand.ACTION_PAUSEQ%>'" /></td>
		<td class="subHeadLite">Queue running.</td>
		</c:otherwise>
		</c:choose>
	</tr>
	<tr>			
		<td colspan="3" class="tableRowSep"><img src="images/x.gif" alt="" width="1" height="5" border="0" /></td>
	</tr>
</form>
</table>
</authority:hasPrivilege>		  		
<div id="resultsTable">
<table width="100%" cellpadding="0" cellspacing="0" border="0">	
	<tr>
		<td colspan="4"><span class="smalltitleGrey">Total Agents: <%=agents.size()%></span></td>
	</tr>
	<tr>
		<td class="tableHeadLite">Name</td>
		<td class="tableHeadDark" colspan="2" align="center">Memory</td>
		<td class="tableHeadLite" align="center">Updated</td>
		<td class="tableHeadDark" colspan="2" align="center"><spring:message code="ui.label.harvestconfig.harvests"/></td>
		<td class="tableHeadLite" colspan="2" align="center">Average</td>
		<td class="tableHeadDark" colspan="2" align="center">Current</td>
		<td class="tableHeadLite" colspan="2" align="center">URLs</td>
		<td class="tableHeadDark" align="center">Data</td>
	</tr>
	<tr>
		<td class="subHeadLite">&nbsp;</td>
		<td class="subHeadDark" align="center">Avail</td>
		<td class="subHeadDark" align="center">Used</td>
		<td class="subHeadLite">&nbsp;</td>
		<td class="subHeadDark" align="center">Max</td>
		<td class="subHeadDark" align="center">Current</td>
		<td class="subHeadLite" align="center">KB/sec</td>
		<td class="subHeadLite" align="center">URI/sec</td>
		<td class="subHeadDark" align="center">KB/sec</td>
		<td class="subHeadDark" align="center">URI/sec</td>
		<td class="subHeadLite" align="center">Saved</td>
		<td class="subHeadLite" align="center">Queued</td>
		<td class="subHeadDark">&nbsp;</td>
	</tr>
	<tr>			
		<td colspan="13" class="tableRowSep"><img src="images/x.gif" alt="" width="1" height="5" border="0" /></td>
	</tr>
	<c:set var="count" scope="page" value="0"/>
	<c:forEach items="${harvestAgents}" var="agent">
		<form name="harvestAgent<c:out value="${count}"/>" method="POST" action="<%=Constants.CNTRL_MNG_AGENTS%>">
		<tr>
			<td class="tableRowLite">
				<input type="hidden" name="<%=ManageHarvestAgentCommand.PARAM_AGENT%>" value="<c:out value="${agent.value.name}"/>" />
				<input type="hidden" name="<%=ManageHarvestAgentCommand.PARAM_ACTION%>" value="<%=ManageHarvestAgentCommand.ACTION_AGENT%>" />
				<a href="#" onclick="javascript:document.harvestAgent<c:out value="${count}"/>.submit(); return false;"><c:out value="${agent.value.name}"/></a>
			<c:if test="${agent.value.memoryWarning == true}">
				&nbsp;(Memory Warning)
			</c:if>
			</td>
			<td class="tableRowDark" align="center"><c:out value="${agent.value.memoryAvailableString}"/></td>
			<td class="tableRowDark" align="center"><c:out value="${agent.value.memoryUsedString}"/></td>
			<td class="tableRowLite" align="center"><wct:date value="${agent.value.lastUpdated}" type="fullDateTime"/></td>
			<td class="tableRowDark" align="center"><c:out value="${agent.value.maxHarvests}"/></td>
			<td class="tableRowDark" align="center"><c:out value="${agent.value.harvesterStatusCount}"/></td>
			<td class="tableRowLite" align="center"><fmt:formatNumber value="${agent.value.averageKBs}" pattern="#.##"/></td>
			<td class="tableRowLite" align="center"><fmt:formatNumber value="${agent.value.averageURIs}" pattern="#.##"/></td>
			<td class="tableRowDark" align="center"><fmt:formatNumber value="${agent.value.currentKBs}" pattern="#.##"/></td>
			<td class="tableRowDark" align="center"><fmt:formatNumber value="${agent.value.currentURIs}" pattern="#.##"/></td>
			<td class="tableRowLite" align="center"><c:out value="${agent.value.urlsDownloaded}"/></td>
			<td class="tableRowLite" align="center"><c:out value="${agent.value.urlsQueued}"/></td>
			<td class="tableRowDark" align="center"><c:out value="${agent.value.dataDownloadedString}"/></td>
		</tr>
		<tr>			
			<td colspan="13" class="tableRowSep"><img src="images/x.gif" alt="" width="1" height="5" border="0" /></td>
		</tr>
		<c:set var="count" scope="page" value="${count + 1}"/>
		</form>
	</c:forEach>

	<form name="harvestAgentDone" method="POST" action="curator/agent/harvest-agent.html">
		<tr>
			<td colspan="13" align="center">
				<input type="hidden" name="<%=ManageHarvestAgentCommand.PARAM_ACTION%>" value="<%=ManageHarvestAgentCommand.ACTION_HOME%>" />
				<img src="images/x.gif" width="1" height="15" border="0" /><br>
				<input type="image" src="images/generic-btn-done.gif" width="82" height="23" alt="Done" value="Done" />
			</td>
		</tr>	
	</form>

</table>
</div>

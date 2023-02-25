<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="authority" uri="http://www.webcurator.org/authority"  %>
<%@taglib prefix="wct" uri="http://www.webcurator.org/wct" %>
<%@page import="org.webcurator.domain.model.auth.Privilege" %>
<%@page import="org.webcurator.ui.target.command.TargetInstanceCommand" %>

<table cellpadding="3" cellspacing="0" border="0">
  <tr>
  	<td colspan="2">
		<input type="hidden" name="<%=TargetInstanceCommand.PARAM_HISTORY_TI_OID%>" value="<c:out value="${command.historyTIOid}"/>" />
    	<input type="hidden" name="<%=TargetInstanceCommand.PARAM_HISTORY_RESULT_ID%>" value="<c:out value="${command.historyResultId}"/>" />
    	<c:choose>
    		<c:when test="${command.historyTIOid eq ''}">
    			&nbsp;
    		</c:when>
    		<c:otherwise>
    			<a href="curator/tools/harvest-history.html?targetInstanceOid=<c:out value="${command.historyTIOid}"/>&harvestResultId=<c:out value="${command.historyResultId}"/>"><font color="red"><b><u>[Return to Harvest History Page]</u></b></font></a>
    		</c:otherwise>
    	</c:choose>    	
  	</td>
  </tr>
  <tr>
    <td class="subBoxTextHdr">Id:</td>
    <td class="subBoxText">
    	<c:out value="${instance.oid}"/>
    </td>
  </tr>
  <tr>
    <td class="subBoxTextHdr"><spring:message code="ui.label.targetinstance.general.targetName"/>:</td>
    <td class="subBoxText">
    	<c:choose>
    		<c:when test="${instance.target.objectType == 0}">
    			<a href="curator/groups/groups.html?targetGroupOid=<c:out value="${instance.target.oid}"/>&mode=view"><c:out value="${instance.target.name}"/></a>
    		</c:when>
    		<c:otherwise>
    			<a href="curator/target/target.html?targetOid=<c:out value="${instance.target.oid}"/>&mode=view"><c:out value="${instance.target.name}"/></a>
    		</c:otherwise>
    	</c:choose>    	
		<input type="hidden" name="<%=TargetInstanceCommand.PARAM_OID%>" value="<c:out value="${command.targetInstanceId}"/>" />
    	<input type="hidden" name="<%=TargetInstanceCommand.PARAM_CMD%>" value="<c:out value="${command.cmd}"/>" />
    </td>
  </tr>
  <tr>
    <td class="subBoxTextHdr">Schedule:</td>
    <td class="subBoxText">
    	<c:choose>
    		<c:when test="${command.cmd eq 'edit' && (instance.state eq 'Scheduled' || instance.state eq 'Queued')}">
    			<input type="text" name="<%=TargetInstanceCommand.PARAM_TIME%>" value="<wct:date value="${command.scheduledTime}" type="fullDateTime"/>" />
    		</c:when>                                                                                                   
    		<c:otherwise>
    			<c:if test="${command.cmd eq 'edit'}">
    			<input type="hidden" name="<%=TargetInstanceCommand.PARAM_TIME%>" value="<wct:date value="${command.scheduledTime}" type="fullDateTime"/>" />
    			</c:if>
    			<fmt:formatDate value="${command.scheduledTime}" pattern="dd/MM/yyyy HH:mm:ss"/>
    		</c:otherwise>
    	</c:choose>    	
    </td>
  </tr>
  <c:if test="${instance.state ne 'Scheduled' && instance.state ne 'Queued'}">
  <tr>
    <td class="subBoxTextHdr">Actual Start:</td>
    <td class="subBoxText">
    	<fmt:formatDate value="${instance.actualStartTime}" pattern="dd/MM/yyyy HH:mm:ss"/>
    </td>
  </tr>
  </c:if>
  <tr>
    <td class="subBoxTextHdr">Priority:</td>
    <td class="subBoxText">
    	<c:choose>
    		<c:when test="${command.cmd eq 'edit' && (instance.state eq 'Scheduled' || instance.state eq 'Queued')}">
    			<select name="<%=TargetInstanceCommand.PARAM_PRI%>">
    			<c:forEach items="${priorities}" var="p">
    				<c:choose>
			    		<c:when test="${command.priority eq p.key}">
			    			<option value="<c:out value="${p.key}"/>" selected="selected"><c:out value="${p.value}"/></option>
			    		</c:when>
			    		<c:otherwise>
			    			<option value="<c:out value="${p.key}"/>"><c:out value="${p.value}"/></option>
			    		</c:otherwise>
			    	</c:choose>
    			</c:forEach>    			
    			</select>	
    		</c:when>
    		<c:otherwise>    
    			<c:forEach items="${priorities}" var="p">
		    		<c:if test="${command.priority eq p.key}">
		    			<c:if test="${command.cmd eq 'edit'}">
	    				<input type="hidden" name="<%=TargetInstanceCommand.PARAM_PRI%>" value="<c:out value="${p.key}"/>" />
		    			</c:if>
			    		<c:out value="${p.value}"/>
		    		</c:if>
    			</c:forEach>			
    		</c:otherwise>
	    </c:choose>
    </td>
  </tr>
  <tr>
    <td class="subBoxTextHdr">Owner:</td>
    <td class="subBoxText">
    	<c:choose>
    		<c:when test="${command.cmd eq 'edit'}">
    			<select name="<%=TargetInstanceCommand.PARAM_OWNER%>">				
				<c:forEach items="${owners}" var="o">
				<c:choose>
					<c:when test="${command.owner eq o.username}">
						<option value="<c:out value="${o.username}"/>" selected="selected"><c:out value="${o.firstname}"/>&nbsp;<c:out value="${o.lastname}"/></option>
					</c:when>
					<c:otherwise>
						<option value="<c:out value="${o.username}"/>"><c:out value="${o.firstname}"/>&nbsp;<c:out value="${o.lastname}"/></option>
					</c:otherwise>
				</c:choose>				
				</c:forEach>
			</select>    			
    		</c:when>
    		<c:otherwise>
    			<c:forEach items="${owners}" var="o">
    			<c:if test="${command.owner eq o.username}">
    				<c:out value="${o.firstname}"/>&nbsp;<c:out value="${o.lastname}"/>
    			</c:if>
    			</c:forEach>
    		</c:otherwise>
    	</c:choose>    	
    </td>
  </tr>
  <tr>
    <td class="subBoxTextHdr">Agency:</td>
    <td class="subBoxText">    	
    	<c:out value="${command.agency}"/>     	
    </td>
  </tr>
  <tr>
    <td class="subBoxTextHdr">State:</td>
    <td class="subBoxText"><input type="hidden" name="<%=TargetInstanceCommand.PARAM_STATE%>" value="<c:out value="${command.state}"/>" /><c:out value="${command.state}"/></td>
  </tr>  
  <tr style="display:${showAQAOption == 1 ? 'block':'none'}">
    <td class="subBoxTextHdr">Use Automated QA:</td>
    <td class="subBoxText">
    	<c:choose>
    		<c:when test="${command.cmd eq 'edit' && (instance.state eq 'Scheduled' || instance.state eq 'Queued')}">
	      		<input type="checkbox" name="<%=TargetInstanceCommand.PARAM_USE_AQA%>" value="true" ${command.useAQA ? 'checked' : ''} />
    		</c:when>
    		<c:otherwise>    
			    ${command.useAQA ? 'Yes' : 'No' }
			    <input type="hidden" name="useAQA" value="<c:out value="${command.useAQA ? 'true' : 'false'}"/>" />
    		</c:otherwise>
	    </c:choose>
    </td>
  </tr>
  <tr>
    <td class="subBoxTextHdr">Bandwidth Percentage:</td>
    <td class="subBoxText">
    	<authority:showControl ownedObject="${instance}" privileges="<%=Privilege.MANAGE_WEB_HARVESTER%>" editMode="${sessionEditMode}">
    		<authority:show>
    			<c:choose>
		    		<c:when test="${command.cmd eq 'edit' && (instance.state eq 'Scheduled' || instance.state eq 'Queued' || instance.state eq 'Paused')}">    			
		    			<input type="text" name="<%=TargetInstanceCommand.PARAM_BW%>" value="<c:out value="${command.bandwidthPercent}"/>" />
		    		</c:when>
		    		<c:otherwise>
		    			<c:if test="${command.cmd eq 'edit'}">
		   				<input type="hidden" name="<%=TargetInstanceCommand.PARAM_BW%>" value="<c:out value="${command.bandwidthPercent}"/>" />
		    			</c:if>
						<c:choose>					
		    				<c:when test="${command.bandwidthPercent == null || command.bandwidthPercent == 0}">    			
		    					Default
		    				</c:when>
		    				<c:otherwise>    		
		    					<c:out value="${command.bandwidthPercent}"/>
		    				</c:otherwise>    		
		    			</c:choose>    			
		    		</c:otherwise>
		    	</c:choose>
    		</authority:show>    	
    		<authority:dont>
    			<c:if test="${command.cmd eq 'edit'}">
   				<input type="hidden" name="<%=TargetInstanceCommand.PARAM_BW%>" value="<c:out value="${command.bandwidthPercent}"/>" />
    			</c:if>
				<c:choose>					
    				<c:when test="${command.bandwidthPercent == null || command.bandwidthPercent == 0}">    			
    					Default
    				</c:when>
    				<c:otherwise>    		
    					<c:out value="${command.bandwidthPercent}"/>
    				</c:otherwise>    		
    			</c:choose>
    		</authority:dont>
    	</authority:showControl>
    </td>
  </tr>  
  <c:if test="${instance.state ne 'Scheduled' && instance.state ne 'Queued'}">
  <tr>
    <td class="subBoxTextHdr">Allocated Bandwidth:</td>
    <td class="subBoxText">
    	<c:out value="${instance.allocatedBandwidth}"/> KB
    </td>
  </tr>
  </c:if>  
  <c:if test="${!empty instance.referenceNumber}">
  <tr>
    <td class="subBoxTextHdr">Reference Number:</td>
    <td class="subBoxText">    	
    	<c:out value="${instance.referenceNumber}"/>     	
    </td>
  </tr>
  </c:if>
  <tr>
    <td class="subBoxTextHdr">Flagged:</td>
    <td class="subBoxText">
		<c:choose>
	   		<c:when test="${command.cmd eq 'edit'}">    			
	   			<input type="checkbox" name="<%=TargetInstanceCommand.PARAM_FLAGGED%>" value="true" ${command.flagged ? 'checked' : ''} />
	   		</c:when>
	   		<c:otherwise>
	   			<input type="checkbox" name="<%=TargetInstanceCommand.PARAM_FLAGGED%>" value="true" ${command.flagged ? 'checked' : ''} disabled />
	   		</c:otherwise>
	   	</c:choose>
    </td>
  </tr>  
  
  <c:if test="${command.cmd ne 'edit'}">
  <authority:hasUserOwnedPriv ownedObject="${instance}" privilege="<%=Privilege.LAUNCH_TARGET_INSTANCE_IMMEDIATE%>" scope="<%=Privilege.SCOPE_AGENCY%>">
  </authority:hasUserOwnedPriv>
  </c:if>
</table>
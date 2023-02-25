<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="authority" uri="http://www.webcurator.org/authority"  %>
<%@taglib prefix="wct" uri="http://www.webcurator.org/wct" %>
<script>
  function selectItem(id) {
	document.getElementById("selectedItem").value = id;
	return true;
  }

  function displayAlerts() {
	  if (document.getElementById("currentState").value == "7") {
		  alert("This target is set to state Completed.\n\nYou must Reinstate this target and then set it to Approved before scheduling a harvest.");
		  return false;
	  }
	  if (document.getElementById("alertText").value.length == 0) {
		return true;
	  }
	  else {
		var answer = confirm ("This target has alerts:\n\n"+document.getElementById("alertText").value)
		if (answer) {
			return true;
		}
		else {
			return false;
		}
	  }
  }

  function harvestNowClicked(chkbox) { 
	  if(chkbox.checked == true) { 
	    return displayAlerts(); 
	  } 
  } 
</script>
			<input type="hidden" id="alertText" name="alertText" value="${alertText}" />
			<input type="hidden" id="currentState" name="currentState" value="${abstractTarget.state}" />
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td align="right">
					<c:if test="${editMode}">
					<authority:hasUserOwnedPriv privilege="${privilegeString}" ownedObject="${abstractTarget}">
						<input type="hidden" id="selectedItem" name="selectedItem">
						<a href="${editControllerUrl}?actionCmd=new" onclick="return displayAlerts();"><img src="images/create-new-btn-red.gif" title="Create a new Schedule" alt="Create a new Schedule" width="82" height="24" border="0"></a>
					</authority:hasUserOwnedPriv>
					</c:if>
					</td>
				</tr>
				<tr>
					<td align="left">
					<c:if test="${editMode}">
					<authority:hasUserOwnedPriv privilege="${privilegeString}" ownedObject="${abstractTarget}">
						<c:choose>
							<c:when test="${abstractTarget.objectType eq 0}">
							    &nbsp;
							</c:when>
							<c:otherwise>
								<b>Harvest Now:&nbsp;</b><input type="checkbox" id="harvestNow" name="harvestNow" onclick="return harvestNowClicked(this);"/>
							</c:otherwise>
						</c:choose>				
					</authority:hasUserOwnedPriv>
					</c:if>
					</td>
				</tr>
			</table>

	<div id="annotationsBox">
	 <img src="images/x.gif" alt="" width="1" height="10" border="0" /><br />
		<table width="100%" cellpadding="3" cellspacing="0" border="0">
			<tr>
				<td class="annotationsHeaderRow">Schedule</td>
				<td class="annotationsHeaderRow">Owner</td>
				<td class="annotationsHeaderRow">Next Scheduled Time</td>
				<td class="annotationsHeaderRow">Action</td>
			</tr>
	
		<c:forEach items="${schedules}" var="schedule" varStatus="status">
		  <tr>
		    <td class="annotationsLiteRow">
		      <c:choose>
		        <c:when test="${schedule.scheduleType <= 0}">
		        <c:out value="${schedule.cronPatternWithoutSeconds}"/>
		        </c:when>
		        <c:otherwise>
		          ${patternMap[schedule.scheduleType].description}
		        </c:otherwise>
		      </c:choose>
		    </td>
		        
		    <td class="annotationsLiteRow"><c:out value="${schedule.owningUser.niceName}"/></td>
		    <td class="annotationsLiteRow"><wct:date value="${schedule.nextExecutionDate}" type="fullDateTime"/></td>
		    <td class="annotationsLiteRow">
		        <a href="${editControllerUrl}?actionCmd=view&selectedItem=${schedule.identity}"><img src="images/action-icon-view.gif" title="View" alt="View" border="0"/></a>
		      <c:if test="${editMode}">		      			      	
		        <authority:hasUserOwnedPriv privilege="${privilegeString}" ownedObject="${abstractTarget}">
		        <img src="images/action-sep-line.gif" border="0" />      
		        <a href="${editControllerUrl}?actionCmd=edit&selectedItem=${schedule.identity}"><img src="images/action-icon-edit.gif" title="Edit" alt="Edit" height="18" width="18" border="0"></a>
		        <img src="images/action-sep-line.gif" border="0" />      
				<input type="image" name="_remove" title="Remove" alt="Remove" src="images/action-icon-delete.gif" height="19" width="18" onclick="selectItem('<c:out value="${schedule.identity}"/>')"/>
		        </authority:hasUserOwnedPriv>
		      </c:if>
		    </td>
		  </tr>
		</c:forEach>	
	</table>
	</div>
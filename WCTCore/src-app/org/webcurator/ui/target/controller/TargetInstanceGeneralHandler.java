/*
 *  Copyright 2006 The National Library of New Zealand
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.webcurator.ui.target.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.webcurator.auth.AuthorityManager;
import org.webcurator.core.agency.AgencyUserManager;
import org.webcurator.core.exceptions.NoPrivilegeException;
import org.webcurator.core.exceptions.WCTRuntimeException;
import org.webcurator.core.harvester.coordinator.HarvestCoordinator;
import org.webcurator.core.scheduler.TargetInstanceManager;
import org.webcurator.core.util.AuthUtil;
import org.webcurator.domain.model.auth.Privilege;
import org.webcurator.domain.model.auth.User;
import org.webcurator.domain.model.core.TargetInstance;
import org.webcurator.domain.model.core.harvester.agent.HarvestAgentStatusDTO;
import org.webcurator.domain.model.dto.UserDTO;
import org.webcurator.ui.common.Constants;
import org.webcurator.ui.target.command.TargetInstanceCommand;
import org.webcurator.ui.util.DateUtils;
import org.webcurator.ui.util.Tab;
import org.webcurator.ui.util.TabHandler;
import org.webcurator.ui.util.TabbedController;
import org.webcurator.ui.util.Utils;
import org.webcurator.ui.util.TabbedController.TabbedModelAndView;

/**
 * The handler for the target instance general tab.
 * @author nwaight
 */
public class TargetInstanceGeneralHandler extends TabHandler {

    private TargetInstanceManager targetInstanceManager;
    private HarvestCoordinator harvestCoordinator;
    private AgencyUserManager agencyUserManager;
    private AuthorityManager authorityManager;
    /** Automatic QA Url */
    private String autoQAUrl = "";
    
    private static Log log = LogFactory.getLog(TargetInstanceGeneralHandler.class);
    
    /**
	 * @param authorityManager the authorityManager to set
	 */
	public void setAuthorityManager(AuthorityManager authorityManager) {
		this.authorityManager = authorityManager;
	}

	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        NumberFormat nf = NumberFormat.getInstance(request.getLocale());        
        binder.registerCustomEditor(java.lang.Long.class, new CustomNumberEditor(java.lang.Long.class, nf, true));
        binder.registerCustomEditor(java.lang.Integer.class, new CustomNumberEditor(java.lang.Integer.class, nf, true));
        binder.registerCustomEditor(java.util.Date.class, DateUtils.get().getFullDateTimeEditor(true));
    }
    
    public void processTab(TabbedController tc, Tab currentTab,
            HttpServletRequest req, HttpServletResponse res, Object comm,
            BindException errors) {
        
    	// process the submit of the tab called on change tab or save
    	TargetInstanceCommand cmd = (TargetInstanceCommand) comm;
    	if (cmd.getCmd().equals(TargetInstanceCommand.ACTION_EDIT)) {
    		TargetInstance ti = (TargetInstance) req.getSession().getAttribute(TargetInstanceCommand.SESSION_TI);
    		
    		ti.setBandwidthPercent(cmd.getBandwidthPercent());
    		ti.setPriority(cmd.getPriority());
    		
    		User owner = agencyUserManager.getUserByUserName(cmd.getOwner());
    		ti.setOwner(owner);
    		
    		ti.setScheduledTime(cmd.getScheduledTime());
    		
    		ti.setFlagged(cmd.getFlagged());
    		ti.setUseAQA(cmd.isUseAQA());
    		
    		req.getSession().setAttribute(TargetInstanceCommand.SESSION_TI, ti);
    	}    	
    }
    
    @SuppressWarnings("unchecked")
    public TabbedModelAndView preProcessNextTab(TabbedController tc,
            Tab nextTabID, HttpServletRequest req, HttpServletResponse res,
            Object comm, BindException errors) {
        
    	// build mav stuff before displaying the tab
        TabbedModelAndView tmav = tc.new TabbedModelAndView();
        
        String fromHistoryTIOid = (String) req.getSession().getAttribute(TargetInstanceCommand.SESSION_HH_TI_OID);
        String fromHistoryResultId = (String) req.getSession().getAttribute(TargetInstanceCommand.SESSION_HH_HR_OID);
        
        Boolean editMode = false;
        TargetInstanceCommand cmd = null;
        TargetInstance ti = null;
        
        if (comm instanceof TargetInstanceCommand) {
        	cmd = (TargetInstanceCommand) comm;
        	if (TargetInstanceCommand.ACTION_EDIT.equals(cmd.getCmd())) {
        		editMode = true;
        	}
        }
        
		ti = (TargetInstance) req.getSession().getAttribute(TargetInstanceCommand.SESSION_TI);
		if (!authorityManager.hasPrivilege(ti, Privilege.MANAGE_TARGET_INSTANCES)) {
			editMode = false;
		}
		//editMode = (Boolean) req.getSession().getAttribute(TargetInstanceCommand.SESSION_MODE);
        			      
		TargetInstanceCommand populatedCommand = new TargetInstanceCommand(ti);        		
		if (editMode) {
			populatedCommand.setCmd(TargetInstanceCommand.ACTION_EDIT);
		}
		if (fromHistoryTIOid != null) {
			populatedCommand.setHistoryTIOid(fromHistoryTIOid);
		}
		if (fromHistoryResultId != null) {
			populatedCommand.setHistoryResultId(fromHistoryResultId);
		}
				
		tmav.addObject(Constants.GBL_CMD_DATA, populatedCommand);
		tmav.addObject(TargetInstanceCommand.MDL_INSTANCE, ti);
		
		int scope = Privilege.SCOPE_NONE;
		if (ti.getOwningUser().equals(AuthUtil.getRemoteUserObject())) {			
			if (authorityManager.hasPrivilege(Privilege.GIVE_OWNERSHIP, Privilege.SCOPE_NONE)) {				
				try {
					scope = authorityManager.getPrivilegeScope(Privilege.GIVE_OWNERSHIP);				
				} 
				catch (NoPrivilegeException e) {
					if (log.isErrorEnabled()) {
						log.error("Failed to get the privilege scope " + e.getMessage(), e);
					}
				}
			}			
		}
		else {			
			if (authorityManager.hasPrivilege(Privilege.TAKE_OWNERSHIP, Privilege.SCOPE_NONE)) {				
				try {
					scope = authorityManager.getPrivilegeScope(Privilege.TAKE_OWNERSHIP);
				} 
				catch (NoPrivilegeException e) {
					if (log.isErrorEnabled()) {
						log.error("Failed to get the privilege scope " + e.getMessage(), e);
					}
				}
			}
		}
		
		List<UserDTO> owners = new ArrayList<UserDTO>();
		if (scope == Privilege.SCOPE_AGENCY) {						
			owners = agencyUserManager.getUserDTOs(AuthUtil.getRemoteUserObject().getAgency().getOid());
		}
		
		if (scope == Privilege.SCOPE_ALL) {			
			owners = agencyUserManager.getUserDTOs();
		}
		
		UserDTO owner = new UserDTO(ti.getOwner().getOid(), ti.getOwner().getUsername(), ti.getOwner().getEmail(), ti.getOwner().isNotificationsByEmail(), ti.getOwner().isTasksByEmail(), ti.getOwner().getTitle(), ti.getOwner().getFirstname(), ti.getOwner().getLastname(), ti.getOwner().getPhone(), ti.getOwner().getAddress(), ti.getOwner().isActive(), ti.getOwner().getAgency().getName(), ti.getOwner().isNotifyOnHarvestWarnings(), ti.getOwner().isNotifyOnGeneral());		
		if (owners.isEmpty() || !owners.contains(owner)) {
			owners.add(owner);
		}
		
        tmav.addObject(TargetInstanceCommand.MDL_OWNERS, owners);
        tmav.addObject(TargetInstanceCommand.MDL_PRIORITIES, ti.getPriorities());
		
        if(autoQAUrl != null && autoQAUrl.length() > 0) {
			tmav.addObject("showAQAOption", 1);
		} else {
			tmav.addObject("showAQAOption", 0);
		}
                                               
        return tmav;        
    }

    public ModelAndView processOther(TabbedController tc, Tab currentTab,
            HttpServletRequest req, HttpServletResponse res, Object comm,
            BindException errors) {
        TargetInstanceCommand cmd = (TargetInstanceCommand) comm;
        if (cmd.getCmd().equals(TargetInstanceCommand.ACTION_HARVEST)) {
            HashMap<String, HarvestAgentStatusDTO> agents = harvestCoordinator.getHarvestAgents();
            TargetInstance ti = targetInstanceManager.getTargetInstance(cmd.getTargetInstanceId());            
            String instanceAgency = ti.getOwner().getAgency().getName(); 
            
            String key = "";
            HarvestAgentStatusDTO agent = null;
            HashMap<String, HarvestAgentStatusDTO> allowedAgents = new HashMap<String, HarvestAgentStatusDTO>();
            Iterator<String> it = agents.keySet().iterator();
            while (it.hasNext()) {
				key = (String) it.next();
				agent = agents.get(key);				
				if (agent.getAllowedAgencies().contains(instanceAgency)
					|| agent.getAllowedAgencies().isEmpty()) {
					allowedAgents.put(key, agent);
				}
			}
            
            ModelAndView mav = new ModelAndView();
            mav.addObject(TargetInstanceCommand.MDL_INSTANCE, ti);
            mav.addObject(Constants.GBL_CMD_DATA, cmd);
            mav.addObject(TargetInstanceCommand.MDL_AGENTS, allowedAgents);
            mav.setViewName(Constants.VIEW_HARVEST_NOW);
            
            return mav;
        }
        else {
            throw new WCTRuntimeException("Unknown command " + cmd.getCmd() + " recieved.");
        }
    }

    /**
     * @param harvestCoordinator The harvestCoordinator to set.
     */
    public void setHarvestCoordinator(HarvestCoordinator harvestCoordinator) {
        this.harvestCoordinator = harvestCoordinator;
    }

    /**
     * @param targetInstanceManager The targetInstanceManager to set.
     */
    public void setTargetInstanceManager(TargetInstanceManager targetInstanceManager) {
        this.targetInstanceManager = targetInstanceManager;
    }

	/**
	 * @param agencyUserManager the agencyUserManager to set
	 */
	public void setAgencyUserManager(AgencyUserManager agencyUserManager) {
		this.agencyUserManager = agencyUserManager;
	}
	
	public void setAutoQAUrl(String autoQAUrl) {
		this.autoQAUrl = autoQAUrl;
	}

	public String getAutoQAUrl() {
		return autoQAUrl;
	}
	
}

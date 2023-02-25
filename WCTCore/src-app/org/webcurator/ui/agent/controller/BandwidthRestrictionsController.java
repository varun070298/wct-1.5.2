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
package org.webcurator.ui.agent.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.webcurator.auth.AuthorityManager;
import org.webcurator.core.exceptions.WCTRuntimeException;
import org.webcurator.core.harvester.coordinator.HarvestCoordinator;
import org.webcurator.core.util.AuthUtil;
import org.webcurator.domain.model.auth.Privilege;
import org.webcurator.domain.model.core.BandwidthRestriction;
import org.webcurator.ui.agent.command.BandwidthRestrictionsCommand;
import org.webcurator.ui.common.Constants;
import org.webcurator.ui.util.DateUtils;

/**
 * The controller for managing the creation, modification 
 * and deletion of bandwidth restrictions.  
 * @author nwaight
 */
public class BandwidthRestrictionsController extends AbstractFormController {
    /** The class the coordinates the harvest agents and holds their states. */
    private HarvestCoordinator harvestCoordinator;
    /** The class that looks up the users privleges. */
    private AuthorityManager authorityManager;
    /** the logger. */
    private Log log;
    
    /** Default Constructor. */
    public BandwidthRestrictionsController() {
        super();
        log = LogFactory.getLog(getClass());
        setCommandClass(BandwidthRestrictionsCommand.class);
    }
    
    /** @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder) */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        NumberFormat nf = NumberFormat.getInstance(request.getLocale());
        binder.registerCustomEditor(java.lang.Long.class, new CustomNumberEditor(java.lang.Long.class, nf, true));
        binder.registerCustomEditor(java.util.Date.class, DateUtils.get().getFullTimeEditor(true));
    }
    
    @Override
    protected ModelAndView showForm(HttpServletRequest aReq,
            HttpServletResponse aResp, BindException aErrors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("show form " + Constants.VIEW_MNG_BANDWIDTH);
        }
        
        return createDefaultModelAndView();
    }

    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest aReq,
            HttpServletResponse aResp, Object aCmd, BindException aErrors)
            throws Exception {
        BandwidthRestrictionsCommand command = (BandwidthRestrictionsCommand) aCmd;
        if (log.isDebugEnabled()) {
            log.debug("process command " + command.getActionCmd());
        }
        
        if (command != null && command.getActionCmd() != null) {
        	if (authorityManager.hasPrivilege(Privilege.MANAGE_WEB_HARVESTER, Privilege.SCOPE_ALL)) {
	            if (command.getActionCmd().equals(BandwidthRestrictionsCommand.ACTION_EDIT)) {
	                return processEditRestriction(aReq, aResp, command, aErrors);
	            }
	            else if (command.getActionCmd().equals(BandwidthRestrictionsCommand.ACTION_SAVE)) {
	                return processSaveRestriction(aReq, aResp, command, aErrors);
	            }
	            else if (command.getActionCmd().equals(BandwidthRestrictionsCommand.ACTION_DELETE)) {
	                return processDeleteRestriction(aReq, aResp, command, aErrors);
	            }
	            else {
	                throw new WCTRuntimeException("Unknown command " + command.getActionCmd() + " recieved.");
	            }
        	}
        	else {
        		if (log.isWarnEnabled()) {
        			log.warn(AuthUtil.getRemoteUser() + " attempted to perform an unautorised operation.");
        		}
        		throw new WCTRuntimeException("You are not authorised to perform the requested operation.");
        	}
        }
                
        throw new WCTRuntimeException("Unknown command recieved.");
    }

    /**
     * @param harvestCoordinator The harvestCoordinator to set.
     */
    public void setHarvestCoordinator(HarvestCoordinator harvestCoordinator) {
        this.harvestCoordinator = harvestCoordinator;
    }

    /**
     * process the edit restriction action.
     * @see AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    private ModelAndView processEditRestriction(HttpServletRequest aReq, HttpServletResponse aResp, BandwidthRestrictionsCommand aCmd, BindException aErrors) throws Exception {
        ModelAndView mav = new ModelAndView();       
        if (aErrors.hasErrors()) {
            mav.addObject(Constants.GBL_CMD_DATA, aErrors.getTarget());
            mav.addObject(Constants.GBL_ERRORS, aErrors);
            mav.addObject(BandwidthRestrictionsCommand.MDL_ACTION, Constants.CNTRL_MNG_BANDWIDTH);
            mav.setViewName(Constants.VIEW_EDIT_BANDWIDTH);

            return mav;
        }
        
        mav.addObject(Constants.GBL_CMD_DATA, aCmd);
        mav.addObject(BandwidthRestrictionsCommand.MDL_ACTION, Constants.CNTRL_MNG_BANDWIDTH);
        mav.setViewName(Constants.VIEW_EDIT_BANDWIDTH);
        
        return mav;
    }
    
    /**
     * process the save restriction action.
     * @see AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    private ModelAndView processSaveRestriction(HttpServletRequest aReq, HttpServletResponse aResp, BandwidthRestrictionsCommand aCmd, BindException aErrors) throws Exception {
        if (aErrors.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.addObject(Constants.GBL_CMD_DATA, aErrors.getTarget());
            mav.addObject(Constants.GBL_ERRORS, aErrors);
            mav.addObject(BandwidthRestrictionsCommand.MDL_ACTION, Constants.CNTRL_MNG_BANDWIDTH);
            mav.setViewName(Constants.VIEW_EDIT_BANDWIDTH);

            return mav;
        }
        
        BandwidthRestriction br = null;
        if (aCmd.getOid() != null) {
            br = harvestCoordinator.getBandwidthRestriction(aCmd.getOid());
        }
        else {
            br = new BandwidthRestriction();
            br.setDayOfWeek(aCmd.getDay());
        }
        
        br.setBandwidth(aCmd.getLimit().longValue());
        br.setStartTime(aCmd.getStart());
        br.setEndTime(aCmd.getEnd());
        
        harvestCoordinator.saveOrUpdate(br);
        
        return createDefaultModelAndView();
    }
    
    /**
     * process the delete restriction action.
     * @see AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    private ModelAndView processDeleteRestriction(HttpServletRequest aReq, HttpServletResponse aResp, BandwidthRestrictionsCommand aCmd, BindException aErrors) throws Exception {
        if (aErrors.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.addObject(Constants.GBL_CMD_DATA, aErrors.getTarget());
            mav.addObject(Constants.GBL_ERRORS, aErrors);
            mav.addObject(BandwidthRestrictionsCommand.MDL_ACTION, Constants.CNTRL_MNG_BANDWIDTH);
            mav.setViewName(Constants.VIEW_EDIT_BANDWIDTH);

            return mav;
        }
        
        BandwidthRestriction br = harvestCoordinator.getBandwidthRestriction(aCmd.getOid());
        harvestCoordinator.delete(br);
        
        return createDefaultModelAndView();
    }
    
    /** 
     * Create the model for displaying the bandwidth restrictions.
     * @return the model and view
     */
    private ModelAndView createDefaultModelAndView() {
        HashMap<String, List<BandwidthRestriction>> restrictions = harvestCoordinator.getBandwidthRestrictions();
        HashMap displayRestrictions = createDisplayBandwidthRestrictions(restrictions);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject(BandwidthRestrictionsCommand.MDL_RESTRICTIONS, displayRestrictions);
        mav.addObject(BandwidthRestrictionsCommand.MDL_DOW, BandwidthRestriction.DOW);
        mav.addObject(BandwidthRestrictionsCommand.MDL_ACTION, Constants.CNTRL_MNG_BANDWIDTH);
        mav.setViewName(Constants.VIEW_MNG_BANDWIDTH);
        
        return mav;
    }
    
    /** 
     * Create a map of bandwidth restrictions that can be easily displayed.
     * @param aRestrictions the saved list of restrictions
     * @return the displayable list of restrictions
     */
    private HashMap createDisplayBandwidthRestrictions(HashMap<String, List<BandwidthRestriction>> aRestrictions) {
        HashMap<String, List<BandwidthRestriction>> display = new HashMap<String, List<BandwidthRestriction>>();
        List<BandwidthRestriction> day = null;
        
        BandwidthRestriction br = null;
        for (int i = 0; i < BandwidthRestriction.DOW.length; i++) {
            day = aRestrictions.get(BandwidthRestriction.DOW[i]);
            if (null == day || day.isEmpty()) {
                br = createDefaultRestriction(BandwidthRestriction.DOW[i]);
                day = new ArrayList<BandwidthRestriction>();
                day.add(br);
                display.put(BandwidthRestriction.DOW[i], day);
            }
            else {
                List<BandwidthRestriction> day2 = createDaysDisplayRestrictions(day);
                display.put(BandwidthRestriction.DOW[i], day2);
            }
        }        
        
        return display;
    }
    
    /** 
     * Create a default bandwidth restriction for the specified day of the week.
     * @param aDay the day of the week to create the restriction for
     * @return the default restriction
     */
    private BandwidthRestriction createDefaultRestriction(String aDay) {        
        BandwidthRestriction br = new BandwidthRestriction();
        br.setBandwidth(0);
        br.setDayOfWeek(aDay);                       
        br.setStartTime(BandwidthRestriction.getDefaultStartTime());
        br.setEndTime(BandwidthRestriction.getDefaultEndTime());
        
        return br;
    }
    
    /**
     * Create a map of bandwidth restirctions to display on the UI.
     * @param aDay the list of the days persisted bandwidth restrictions 
     * @return the list of the days display bandwidth restrictions
     */
    private List<BandwidthRestriction> createDaysDisplayRestrictions(List<BandwidthRestriction> aDay) {
        List<BandwidthRestriction> displayDay = new ArrayList<BandwidthRestriction>();

        Calendar cal = Calendar.getInstance();
        Date dfltStart = BandwidthRestriction.getDefaultStartTime();
        Date dfltEnd = BandwidthRestriction.getDefaultEndTime();
        
        BandwidthRestriction dbr = null;
        BandwidthRestriction br = null;
        Iterator<BandwidthRestriction> it = aDay.iterator();
        while (it.hasNext()) {
            br = it.next();
            if (br.getStartTime().after(dfltStart)) {
                dbr = new BandwidthRestriction();
                dbr.setBandwidth(0);
                dbr.setDayOfWeek(br.getDayOfWeek());
                dbr.setStartTime(dfltStart);
                
                cal.setTime(br.getStartTime());
                cal.add(Calendar.SECOND, -1);
                dbr.setEndTime(cal.getTime());
                    
                displayDay.add(dbr);
            }
            
            displayDay.add(br);
            
            cal.setTime(br.getEndTime());
            cal.add(Calendar.SECOND, 1);
            dfltStart = cal.getTime();
        }
        
        if (br.getEndTime().before(dfltEnd)) {
            dbr = new BandwidthRestriction();
            dbr.setBandwidth(0);
            dbr.setDayOfWeek(br.getDayOfWeek());
            
            cal.setTime(br.getEndTime());
            cal.add(Calendar.SECOND, 1);
            dbr.setStartTime(cal.getTime());            
            
            dbr.setEndTime(dfltEnd);
                
            displayDay.add(dbr);
        }
        
        return displayDay;
    }

	/**
	 * @param authorityManager the authorityManager to set
	 */
	public void setAuthorityManager(AuthorityManager authorityManager) {
		this.authorityManager = authorityManager;
	}    
}

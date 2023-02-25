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
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.webcurator.core.agency.AgencyUserManager;
import org.webcurator.core.common.Environment;
import org.webcurator.core.exceptions.WCTRuntimeException;
import org.webcurator.core.harvester.coordinator.HarvestCoordinator;
import org.webcurator.core.scheduler.TargetInstanceManager;
import org.webcurator.core.util.AuthUtil;
import org.webcurator.core.util.CookieUtils;
import org.webcurator.domain.Pagination;
import org.webcurator.domain.TargetInstanceCriteria;
import org.webcurator.domain.model.auth.Agency;
import org.webcurator.domain.model.auth.User;
import org.webcurator.domain.model.core.Target;
import org.webcurator.domain.model.core.TargetInstance;
import org.webcurator.ui.common.Constants;
import org.webcurator.ui.target.command.TargetInstanceCommand;
import org.webcurator.ui.util.DateUtils;

/**
 * The controller for displaying a list of target instances and 
 * processing commands to filter the list and display the
 * target instances.
 * @author nwaight
 */
public class QueueController extends AbstractFormController {
    /** The manager to use to access the target instance. */
    private TargetInstanceManager targetInstanceManager;
    /** The harvest coordinator for looking at the harvesters. */
    private HarvestCoordinator harvestCoordinator;
    /** the WCT global environment settings. */
    private Environment environment;  
    /** The manager to use for user, role and agency data. */
    private AgencyUserManager agencyUserManager;
    /** the logger. */
    private Log log;
    
	private MessageSource messageSource = null;

    /** Default constructor. */    
    public QueueController() {
        super();
        setCommandClass(TargetInstanceCommand.class);
        log = LogFactory.getLog(getClass());
    }
    
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(java.util.Date.class, DateUtils.get().getFullDateTimeEditor(true));
        
        NumberFormat nf = NumberFormat.getInstance(request.getLocale());
        binder.registerCustomEditor(java.lang.Long.class, new CustomNumberEditor(java.lang.Long.class, nf, true));   
    }
    
    @Override
    protected ModelAndView showForm(HttpServletRequest aReq, HttpServletResponse aResp, BindException aErrors) throws Exception {
    	
    	if (aReq.getParameter(TargetInstanceCommand.REQ_TYPE) != null) {
    		aReq.getSession().removeAttribute(TargetInstanceCommand.SESSION_TI_SEARCH_CRITERIA);
    	}
        return processFilter(aReq, aResp, null, aErrors);
    }

    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest aReq, HttpServletResponse aResp, Object aCmd, BindException aErrors) throws Exception {
        
    	TargetInstanceCommand command = (TargetInstanceCommand) aCmd;
        
    	if (log.isDebugEnabled()) {
            log.debug("process command " + command.getCmd());
        }
        
        if (command != null && command.getCmd() != null) {
            if (command.getCmd().equals(TargetInstanceCommand.ACTION_FILTER)
            	|| command.getCmd().equals(TargetInstanceCommand.ACTION_NEXT)
            	|| command.getCmd().equals(TargetInstanceCommand.ACTION_PREV)
            	|| command.getCmd().equals(TargetInstanceCommand.ACTION_SHOW_PAGE)) {
                return processFilter(aReq, aResp, command, aErrors);
            }
            else if (command.getCmd().equals(TargetInstanceCommand.ACTION_RESET)) { 
            	command.setAgency("");
            	command.setOwner("");
            	command.setFrom(null);
            	command.setTo(null);
            	command.setName("");
            	command.setStates(new HashSet<String>());
            	command.setSearchOid(null);
            	command.setFlagged(false);
            	
                return processFilter(aReq, aResp, command, aErrors);
            }
            else if (command.getCmd().equals(TargetInstanceCommand.ACTION_PAUSE)) {
                return processPause(aReq, aResp, command, aErrors);
            }
            else if (command.getCmd().equals(TargetInstanceCommand.ACTION_RESUME)) {
                return processResume(aReq, aResp, command, aErrors);
            }
            else if (command.getCmd().equals(TargetInstanceCommand.ACTION_ABORT)) {
                return processAbort(aReq, aResp, command, aErrors);
            }
            else if (command.getCmd().equals(TargetInstanceCommand.ACTION_STOP)) {
                return processStop(aReq, aResp, command, aErrors);
            }
            else if (command.getCmd().equals(TargetInstanceCommand.ACTION_DELETE)) {
                return processDelete(aReq, aResp, command, aErrors);
            }
            else {
                throw new WCTRuntimeException("Unknown command " + command.getCmd() + " recieved.");
            }
        }
                
        throw new WCTRuntimeException("Unknown command recieved.");
    }
    
    /**
     * @param aTargetInstanceManager The targetInstanceManager to set.
     */
    public void setTargetInstanceManager(TargetInstanceManager aTargetInstanceManager) {
        targetInstanceManager = aTargetInstanceManager;
    }
    
    /**
     * process the filter target instances action.
     * @see AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @SuppressWarnings("unchecked")
    private ModelAndView processFilter(HttpServletRequest aReq, HttpServletResponse aResp, TargetInstanceCommand aCmd, BindException aErrors) throws Exception {
        
    	ModelAndView mav = new ModelAndView();
        
        TargetInstanceCommand searchCommand = (TargetInstanceCommand) aReq.getSession().getAttribute(TargetInstanceCommand.SESSION_TI_SEARCH_CRITERIA);      
        
        TargetInstanceCriteria criteria = new TargetInstanceCriteria();
        
		// get value of page size cookie
		String currentPageSize = CookieUtils.getPageSize(aReq);

		if (aCmd != null && 
			!aCmd.getCmd().equals(TargetInstanceCommand.ACTION_NEXT) && 
			!aCmd.getCmd().equals(TargetInstanceCommand.ACTION_PREV) && 
			!aCmd.getCmd().equals(TargetInstanceCommand.ACTION_SHOW_PAGE)) {
			copyCommandToCriteria(aCmd, criteria);
			aCmd.setCmd(TargetInstanceCommand.ACTION_FILTER);								
        }
        else if (searchCommand != null) {
        	if (aCmd != null && 
        	    (aCmd.getCmd().equals(TargetInstanceCommand.ACTION_NEXT) || 
        	     aCmd.getCmd().equals(TargetInstanceCommand.ACTION_PREV) || 
        	     aCmd.getCmd().equals(TargetInstanceCommand.ACTION_SHOW_PAGE))) {
        		searchCommand.setCmd(aCmd.getCmd());
        		searchCommand.setPageNo(aCmd.getPageNo());
        		searchCommand.setSelectedPageSize(aCmd.getSelectedPageSize());
        	}

        	if(aCmd == null)
        	{
        		//we have come from another page so update the page 
        		//size in case it has been changed since the searchcommand was saved
        		searchCommand.setSelectedPageSize(currentPageSize);
        	}
        	
        	if (aReq.getParameter(TargetInstanceCommand.REQ_SHOW_SUBMITTED_MSG) != null) {
        		if (aReq.getParameter(TargetInstanceCommand.REQ_SHOW_SUBMITTED_MSG).equals("y") ) {
            		mav.addObject(Constants.GBL_MESSAGES, messageSource.getMessage("ui.label.targetinstance.submitToArchiveStarted", new Object[] {}, Locale.getDefault()));
        		} else {
            		mav.addObject(Constants.GBL_MESSAGES, messageSource.getMessage("ui.label.targetinstance.submitToArchiveFailed", new Object[] {}, Locale.getDefault()));
        		}
        	}

        	copyCommandToCriteria(searchCommand, criteria);        	
			aCmd = searchCommand;			
			aCmd.setCmd(TargetInstanceCommand.ACTION_FILTER);
        }
        else {  
        	aCmd = new TargetInstanceCommand();
        	aCmd.setSelectedPageSize(currentPageSize);

    		Set<String> states = new HashSet<String>();
        	if (TargetInstanceCommand.TYPE_TARGET.equals(aReq.getParameter(TargetInstanceCommand.REQ_TYPE))) {
        		String name = aReq.getParameter(TargetInstanceCommand.PARAM_TARGET_NAME);
            	criteria.setName(name);
            	aCmd.setName(name);
        		aCmd.setStates(states);
        		aCmd.setSortorder(TargetInstanceCommand.SORT_DEFAULT);
        	}
        	else if (TargetInstanceCommand.TYPE_HARVESTED.equals(aReq.getParameter(TargetInstanceCommand.REQ_TYPE))) {        		
        		User user = AuthUtil.getRemoteUserObject();
            	criteria.setOwner(user.getUsername());
            	aCmd.setOwner(user.getUsername());
            	criteria.setAgency(user.getAgency().getName());        	
            	aCmd.setAgency(user.getAgency().getName());
            	
        		states.add(TargetInstance.STATE_HARVESTED);
        		aCmd.setStates(states);
        		aCmd.setSortorder(TargetInstanceCommand.SORT_DEFAULT);
        	}
        	else
        	{
	        	Calendar cal = Calendar.getInstance();
	        	cal.add(Calendar.DATE, environment.getDaysToSchedule() + 1);
	        	cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
	        	cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
	        	cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
	        	criteria.setTo(cal.getTime());
	        	aCmd.setTo(cal.getTime());
        		aCmd.setSortorder(TargetInstanceCommand.SORT_DEFAULT);
	
	        	if (TargetInstanceCommand.TYPE_QUEUE.equals(aReq.getParameter(TargetInstanceCommand.REQ_TYPE))) {        		
	        		states.add(TargetInstance.STATE_SCHEDULED);
	        		states.add(TargetInstance.STATE_QUEUED);
	        		states.add(TargetInstance.STATE_RUNNING);
	        		states.add(TargetInstance.STATE_STOPPING);
	        		states.add(TargetInstance.STATE_PAUSED);
	        		aCmd.setStates(states);
	        	}
	        	else {
	        		cal = Calendar.getInstance();
	            	cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
	            	cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
	            	cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
	            	cal.add(Calendar.DATE, -1);
	            	criteria.setFrom(cal.getTime());
	            	aCmd.setFrom(cal.getTime());
	        		
	        		Iterator<String> it = TargetInstance.getOrderedStates().keySet().iterator();
	        		while (it.hasNext()) {
	        			states.add(it.next());				
	        		}   
	        		
	        		User user = AuthUtil.getRemoteUserObject();
	            	criteria.setOwner(user.getUsername());
	            	aCmd.setOwner(user.getUsername());
	            	criteria.setAgency(user.getAgency().getName());        	
	            	aCmd.setAgency(user.getAgency().getName());
	            	
	            	criteria.setSearchOid(aCmd.getSearchOid());
	        	}
        	}
        	
        	criteria.setStates(states);
        	criteria.setSortorder(aCmd.getSortorder());
        	
        	aCmd.setCmd(TargetInstanceCommand.ACTION_FILTER);
        }                               

		Pagination instances = null;
		if(aCmd.getSelectedPageSize() == null)
		{
			aCmd.setSelectedPageSize(currentPageSize);
		}
		
		if (aCmd.getSelectedPageSize().equals(currentPageSize)) {
			// user has left the page size unchanged..
	        instances = targetInstanceManager.search(criteria, aCmd.getPageNo(), Integer.parseInt(aCmd.getSelectedPageSize()));        
		}
		else {
			// user has selected a new page size, so reset to first page..
	        instances = targetInstanceManager.search(criteria, 0, Integer.parseInt(aCmd.getSelectedPageSize()));        
			// ..then update the page size cookie
			CookieUtils.setPageSize(aResp, aCmd.getSelectedPageSize());
		}

		// we need to populate annotations to determine if targets are alertable.
		if ( instances != null ) {
			for (Iterator<TargetInstance> i = ((List<TargetInstance>) instances.getList()).iterator( ); i.hasNext(); ) {
				TargetInstance ti = i.next();
				ti.setAnnotations(targetInstanceManager.getAnnotations(ti));
			}		
		}
		
		aCmd.setQueuePaused(harvestCoordinator.isQueuePaused());
		
        mav.addObject(TargetInstanceCommand.MDL_INSTANCES, instances);
        
        // Put the instances into the "page" attribute as well for the standard
        // pagination bar.
        mav.addObject("page", instances);
        mav.addObject("action", Constants.CNTRL_TI_QUEUE);
        mav.addObject(Constants.GBL_CMD_DATA, aCmd);
        aReq.getSession().setAttribute(TargetInstanceCommand.SESSION_TI_SEARCH_CRITERIA, aCmd);
        
        List agencies = agencyUserManager.getAgencies();
        Agency selectedAgency = null;
        Agency a = null;
        Iterator<Agency> it = agencies.iterator();
        while (it.hasNext()) {
			a = it.next();
			if (a.getName().equals(aCmd.getAgency())) {
				selectedAgency = a;
				break;
			}			
		}                
        mav.addObject(TargetInstanceCommand.MDL_AGENCIES, agencies);
        
        List owners = null;
        if (selectedAgency != null) {
        	owners = agencyUserManager.getUserDTOs(selectedAgency.getOid());
        }
        else {
        	owners = agencyUserManager.getUserDTOs();
        }        
        mav.addObject(TargetInstanceCommand.MDL_OWNERS, owners);        
        mav.setViewName(Constants.VIEW_TARGET_INSTANCE_QUEUE);
        if (aErrors.hasErrors()) {
        	mav.addObject(Constants.GBL_ERRORS, aErrors);
        }
        
        
        return mav;
    }
    
    /**
     * Copy the search filter data from the command to the criteria
     * @param aCmd the command to copy search criteria from
     * @param aCriteria the criteria object to copy data to
     */
    private void copyCommandToCriteria(TargetInstanceCommand aCmd, TargetInstanceCriteria aCriteria) {
    	aCriteria.setFrom(aCmd.getFrom());
    	aCriteria.setTo(aCmd.getTo());
        
    	if (aCmd.getName() != null && !aCmd.getName().trim().equals("")) {
    		aCriteria.setName(aCmd.getName());
    	}
    	
        if (aCmd.getStates() != null && !aCmd.getStates().isEmpty()) {
        	aCriteria.setStates(aCmd.getStates());	
        }
        
        if (aCmd.getStates() != null && aCmd.getStates().contains(TargetInstance.STATE_RUNNING)) {
        	Set<String> states = aCriteria.getStates();
        	states.add(TargetInstance.STATE_STOPPING);        	
        	aCriteria.setStates(states);
        }        
        
		if (aCmd.getOwner() != null && !aCmd.getOwner().trim().equals("")) {
			aCriteria.setOwner(aCmd.getOwner());
		}            
		
		if (aCmd.getAgency() != null && !aCmd.getAgency().trim().equals("")) {
			aCriteria.setAgency(aCmd.getAgency());
		}
		
		if(aCmd.getSearchOid() != null) {
			aCriteria.setSearchOid(aCmd.getSearchOid());
		}
		
		aCriteria.setFlagged(aCmd.getFlagged());
		aCriteria.setNondisplayonly(aCmd.getNondisplayonly());
		aCriteria.setSortorder(aCmd.getSortorder());
		
		if (aCmd.getCmd().equals(TargetInstanceCommand.ACTION_NEXT)) {
        	aCmd.setPageNo(aCmd.getPageNo() + 1);
        }
        
        if (aCmd.getCmd().equals(TargetInstanceCommand.ACTION_PREV)) {
        	aCmd.setPageNo(aCmd.getPageNo() - 1);
        }
    }
    
    /**
     * process the delete target instance action.
     * @see AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    private ModelAndView processDelete(HttpServletRequest aReq, HttpServletResponse aResp, TargetInstanceCommand aCmd, BindException aErrors) throws Exception {
    	TargetInstance ti = null;    
    	try {
	    	try {
				ti = targetInstanceManager.getTargetInstance(aCmd.getTargetInstanceId());
			} 
	    	catch (RuntimeException e) {
				// assume that the target instance has already been deleted.
	    		ti = null;
			}
	    	
	    	//You can only delete a target instance if it is scheduled or queued
	    	//and if it doesn't have a HarvestStatus (i.e. has not yet begun harvesting)
	    	if (ti != null) {
	    		if (ti.getState() != null && !ti.getState().equals(TargetInstance.STATE_SCHEDULED) && !ti.getState().equals(TargetInstance.STATE_QUEUED)) {
	    			aErrors.reject("target.instance.not.deleteable", new Object[] {ti.getJobName()}, "The target instance may not be deleted."); 
	    		}
	    		else {
	    			if(ti.getStatus() == null)
	    			{
	    				targetInstanceManager.delete(ti);
	    			}
	    			else
	    			{
		    			aErrors.reject("target.instance.not.deleteable", new Object[] {ti.getJobName()}, "The target instance may not be deleted."); 
	    			}
	    		}    		
	    	}
    	}
    	catch(org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException e) { 
    		e.printStackTrace();
    		throw e;
    	}
        
        return processFilter(aReq, aResp, null, aErrors);
    }
    
    /**
     * process the pause target instance action.
     * @see AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    private ModelAndView processPause(HttpServletRequest aReq, HttpServletResponse aResp, TargetInstanceCommand aCmd, BindException aErrors) throws Exception {
        TargetInstance ti = targetInstanceManager.getTargetInstance(aCmd.getTargetInstanceId());        
        harvestCoordinator.pause(ti);
        
        return processFilter(aReq, aResp, null, aErrors);
    }

    /**
     * process the resume target instance action.
     * @see AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    private ModelAndView processResume(HttpServletRequest aReq, HttpServletResponse aResp, TargetInstanceCommand aCmd, BindException aErrors) throws Exception {
        TargetInstance ti = targetInstanceManager.getTargetInstance(aCmd.getTargetInstanceId());
        harvestCoordinator.resume(ti);
        
        return processFilter(aReq, aResp, null, aErrors);
    }
    
    /**
     * process the abort target instance action.
     * @see AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    private ModelAndView processAbort(HttpServletRequest aReq, HttpServletResponse aResp, TargetInstanceCommand aCmd, BindException aErrors) throws Exception {
        TargetInstance ti = targetInstanceManager.getTargetInstance(aCmd.getTargetInstanceId());
        harvestCoordinator.abort(ti);
        
        return processFilter(aReq, aResp, null, aErrors);
    }
    
    /**
     * process the stop target instance action.
     * @see AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    private ModelAndView processStop(HttpServletRequest aReq, HttpServletResponse aResp, TargetInstanceCommand aCmd, BindException aErrors) throws Exception {
        TargetInstance ti = targetInstanceManager.getTargetInstance(aCmd.getTargetInstanceId());
        harvestCoordinator.stop(ti);
        
        return processFilter(aReq, aResp, null, aErrors);
    }
    
    /**
     * @param harvestCoordinator The harvestCoordinator to set.
     */
    public void setHarvestCoordinator(HarvestCoordinator harvestCoordinator) {
        this.harvestCoordinator = harvestCoordinator;
    }

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * @param agencyUserManager the agencyUserManager to set
	 */
	public void setAgencyUserManager(AgencyUserManager agencyUserManager) {
		this.agencyUserManager = agencyUserManager;
	}
	
	/**
	 * @param messageSource The messageSource to set.
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
}
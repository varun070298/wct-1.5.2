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
package org.webcurator.ui.tools.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.webcurator.core.scheduler.TargetInstanceManager;
import org.webcurator.core.targets.TargetManager;
import org.webcurator.domain.TargetInstanceDAO;
import org.webcurator.domain.model.core.HarvestResource;
import org.webcurator.domain.model.core.HarvestResourceDTO;
import org.webcurator.domain.model.core.HarvestResult;
import org.webcurator.domain.model.core.Seed;
import org.webcurator.domain.model.core.TargetInstance;
import org.webcurator.ui.tools.command.QualityReviewToolCommand;

/**
 * The QualityReviewToolController is responsible for displaying the "menu"
 * page where the user can access the other quality review tools.
 * @author bbeaumont
 */
public class QualityReviewToolController extends AbstractCommandController {
    static private Log log = LogFactory.getLog(QualityReviewToolController.class);
    
    private TargetInstanceManager targetInstanceManager;
    private TargetInstanceDAO targetInstanceDao;
    private TargetManager targetManager = null;
    private String archiveUrl = null;
	private HarvestResourceUrlMapper harvestResourceUrlMapper;
	private boolean enableBrowseTool = true;
	private boolean enableAccessTool = false;
    
    
	public QualityReviewToolController() {
		setCommandClass(QualityReviewToolCommand.class);
	}

	@Override
	protected ModelAndView handle(HttpServletRequest req, HttpServletResponse ress, Object comm, BindException error) throws Exception {
        QualityReviewToolCommand cmd = (QualityReviewToolCommand) comm;
        
        TargetInstance ti = targetInstanceManager.getTargetInstance(cmd.getTargetInstanceOid());
        
        //Do not load fully as this loads ALL resources, regardless of whether they're seeds. Causes OutOfMemory for large harvests.
        HarvestResult result = targetInstanceDao.getHarvestResult(cmd.getHarvestResultId(), false);
        
        // v1.2 - The seeds are now against the Target Instance. We should prefer the seeds
        // in the instances over those on the target.
        
        Set<Seed> seeds = targetManager.getSeeds(ti);
        
        // load seedMap list with primary seeds, followed by non-primary seeds.
        List<SeedMapElement> seedMap = new ArrayList<SeedMapElement>();
        for(Seed seed: seeds)
        {
    		load(seedMap, seed, true, result);
        }
        for(Seed seed: seeds)
        {
    		load(seedMap, seed, false, result);
        }
        
        ModelAndView mav = new ModelAndView("quality-review-toc", "command", comm);
        mav.addObject(QualityReviewToolCommand.MDL_SEEDS, seedMap);
        mav.addObject("targetInstanceOid", ti.getOid());
        mav.addObject("archiveUrl", archiveUrl);
        
        return mav;
	}

	private void load(List<SeedMapElement> seedMap, Seed seed, boolean loadPrimary, HarvestResult result) {

		
		if (seed.isPrimary() == loadPrimary) {
        	SeedMapElement element = new SeedMapElement(seed.getSeed());
        	element.setPrimary(loadPrimary);
        	if(enableBrowseTool)
        	{
        		element.setBrowseUrl("curator/tools/browse/" + String.valueOf(result.getOid()) + "/" + seed.getSeed() );
        	}

        	if(enableAccessTool && harvestResourceUrlMapper != null)
        	{
                HarvestResourceDTO hRsr = targetInstanceDao.getHarvestResourceDTO(result.getOid(), seed.getSeed());
        		if(hRsr != null)
        		{
        			element.setAccessUrl(harvestResourceUrlMapper.generateUrl(result, hRsr));
        		}
        		else
        		{
        			log.warn("Cannot find seed '" + seed.getSeed() + "' in harvest result ("+result.getOid()+").");
        		}
        	}
        	seedMap.add(element);
		}
		
	}
	
    /**
     * @param targetInstanceManager The targetInstanceManager to set.
     */
    public void setTargetInstanceManager(TargetInstanceManager targetInstanceManager) {
        this.targetInstanceManager = targetInstanceManager;
    }

	/**
	 * @param targetManager The targetManager to set.
	 */
	public void setTargetManager(TargetManager targetManager) {
		this.targetManager = targetManager;
	}

	public String getArchiveUrl() {
		return archiveUrl;
	}

	public void setArchiveUrl(String archiveUrl) {
		this.archiveUrl = archiveUrl;
	}
	
	public HarvestResourceUrlMapper getHarvestResourceUrlMapper() {
		return harvestResourceUrlMapper;
	}

	public void setHarvestResourceUrlMapper(
			HarvestResourceUrlMapper harvestResourceUrlMapper) {
		this.harvestResourceUrlMapper = harvestResourceUrlMapper;
	}

	public TargetInstanceDAO getTargetInstanceDao() {
		return targetInstanceDao;
	}

	public void setTargetInstanceDao(TargetInstanceDAO targetInstanceDao) {
		this.targetInstanceDao = targetInstanceDao;
	}

	public void setEnableBrowseTool(boolean enableBrowseTool) {
		this.enableBrowseTool = enableBrowseTool;
	}

	public boolean isEnableBrowseTool() {
		return enableBrowseTool;
	}

	public void setEnableAccessTool(boolean enableAccessTool) {
		this.enableAccessTool = enableAccessTool;
	}

	public boolean isEnableAccessTool() {
		return enableAccessTool;
	}
}

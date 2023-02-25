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
package org.webcurator.core.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.webcurator.domain.Pagination;
import org.webcurator.domain.TargetInstanceCriteria;
import org.webcurator.domain.model.auth.User;
import org.webcurator.domain.model.core.AbstractTarget;
import org.webcurator.domain.model.core.Annotation;
import org.webcurator.domain.model.core.HarvestResult;
import org.webcurator.domain.model.core.Schedule;
import org.webcurator.domain.model.core.TargetInstance;
import org.webcurator.domain.model.dto.HarvestHistoryDTO;

/**
 * The interface for managing target instances.
 * @author nwaight
 */
public interface TargetInstanceManager {
	
	/** 
	 * Store the seed history in the SeedHistory table during prepareHarvest.
	 * @return true to store seed history 
	 */
	public boolean getStoreSeedHistory();

	/** 
	 * Return the first page of TargetInstances that meet the specified criteria.
	 * @param aCriteria the criteria to return instances for
	 * @return the first page of TargetInstances 
	 */
    Pagination search(final TargetInstanceCriteria aCriteria);
    
    /**
     * Return the specified page of TargetInstances that meet the specified criteria.
     * @param aCriteria the criteria to return instances for
     * @param aPage the page number to return
     * @param aPageSize the page size to use
     * @return the page of TargetInstances
     */
    Pagination search(final TargetInstanceCriteria aCriteria, final int aPage, final int aPageSize);
    
    /**
     * Return the TargetInstance with the specified primary key.
     * @param aOid the unique id of the TargetInsrtance
     * @return the TargetInstance
     */
    TargetInstance getTargetInstance(Long aOid);
    
    /**
     * Return the TargetInstance with the specified primary key, 
     * if the fully populate flag is set then load its related objects
     * @param aOid the unique id of the TargetInsrtance
     * @param aLoadFully flag to indicate that related objects should be loaded
     * @return the TargetInstance
     */
    TargetInstance getTargetInstance(Long aOid, boolean aLoadFully);
    
    /**
     * Return the the TargetInstance that is the next one off the queue.
     * @return the TargetIntance to be harvested next.
     */
    TargetInstance getNextTargetInstanceToHarvest();
    
    /**
     * Return a list of annotations for the specified TargetInstance
     * @param aTargetInstance the target instance to return annotations for
     * @return the list of annotations
     */
    List<Annotation> getAnnotations(TargetInstance aTargetInstance);
    
    /**
     * Save the specified target instance.
     * @param aTargetInstance the TargetInstance to save
     */
    void save(TargetInstance aTargetInstance);

    /**
     * Save the specified HarvestResult.
     * @param aHarvestResult the HarvestResult to save
     */
    void save(HarvestResult aHarvestResult);    
    
    /**
     * Delete the specified target instance.
     * @param aTargetInstance the TargetInstance to delete
     */
    void delete(TargetInstance aTargetInstance);    
    
    /**
     * Save all of the target instances in the collection.
     * @param aCollection A collection of target instances to be saved.
     */
    void saveAll(Collection<TargetInstance> aCollection);
    
    /**
     * Delete TargetInstances for the specified Target and Schedule
     * @param aTargetOid The target OID.
     * @param aScheduleOid The schedule OID.
     */
    void deleteTargetInstances(Long aTargetOid, Long aScheduleOid);
     
    /**
     * Return a count of target instances owned by the specified user
     * where the target instance is in one of the specified states
     * @param aUser the owner of the target instances to count
     * @param aStates the list of states to count
     * @return the count of target instances
     */
    int countTargetInstances(User aUser, ArrayList<String> aStates);    
    
    /**
     * Return a count of target instances 'owned' by the specified Target
     * @param Oid the oid of the target record from which the TI was derived
     * @return the count of target instances 'owned'
     */
    int countTargetInstancesByTarget(Long Oid);    

    /** 
     * Get the HarvestHistory of a taget.
     * @param targetOid The OID of the Target.
     * @return A list of HarvestHistory objects.
     */
    List<HarvestHistoryDTO> getHarvestHistory(Long targetOid);
    
    /**
     * Retrieve the list of HarvestResults associated with the Target Instance.
     * @param targetInstanceOid The OID of the Target Instance
     * @return A list of the HarvestResults.
     */
    List<HarvestResult> getHarvestResults(Long targetInstanceOid);
    
    /**
     * Set the purged flag and delete any harvest resources.
     * @param aTargetInstance The Target Instance
     */
    void purgeTargetInstance(TargetInstance aTargetInstance);
}

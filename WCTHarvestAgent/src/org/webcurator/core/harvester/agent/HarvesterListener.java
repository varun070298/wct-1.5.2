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
package org.webcurator.core.harvester.agent;

import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.archive.crawler.admin.CrawlJob;
import org.springframework.context.ApplicationContext;
import org.webcurator.core.common.Constants;
import org.webcurator.core.harvester.agent.exception.HarvestAgentException;
import org.webcurator.core.harvester.agent.schedule.SchedulerUtil;
import org.webcurator.core.harvester.coordinator.HarvestCoordinatorNotifier;
import org.webcurator.core.util.ApplicationContextFactory;

/**
 * The HarvesterListener is registered to listen to Heritrix events so that 
 * it can perform tasks on certain state changes. 
 * @author nwaight
 */
public class HarvesterListener implements NotificationListener {
    /** the logger. */
    private Log log;
    
    /** Default Constructor. */
    public HarvesterListener() {
        super();
        log = LogFactory.getLog(getClass());
    }

    /** @see javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object) */
    public void handleNotification(Notification notification, Object handback) {        
        if (log.isInfoEnabled()) {
            log.info("Notification " + notification.getMessage() + " recieved from " + handback);
        }               
        
        ApplicationContext context = ApplicationContextFactory.getWebApplicationContext();
        HarvestAgent agent = (HarvestAgent) context.getBean(Constants.BEAN_HARVEST_AGENT);
        // When the agent moves into the running state grab the settings we need at 
        // Job completion as some of these may no longer be available when the job is finished.
        if (notification.getMessage().equals(CrawlJob.STATUS_RUNNING)) {   
            agent.loadSettings((String) handback);
        }
        
        // Send a heartbeat with the current status to the core.
		try {			
			HarvestCoordinatorNotifier notifier = (HarvestCoordinatorNotifier) context.getBean(Constants.BEAN_NOTIFIER);					
			notifier.heartbeat(agent.getStatus());
		} 
		catch (Exception e) {
			if (log.isWarnEnabled()) {
				log.warn("Failed to send heartbeat on notification " + e.getMessage(), e);
			}
		}
        
		// Schedule the job completion process to be run.
        if (notification.getMessage().equals(CrawlJob.STATUS_FINISHED_ABNORMAL) || 
            notification.getMessage().equals(CrawlJob.STATUS_MISCONFIGURED) ||
            notification.getMessage().startsWith(CrawlJob.STATUS_FINISHED)) {
            
            try {
                SchedulerUtil.scheduleHarvestCompleteJob((String)handback);
            }
            catch (Exception e) {
                throw new HarvestAgentException("Failed to start harvest complete job : " + e.getMessage(), e);
            }

            if (log.isInfoEnabled()) {
                log.info("Running Harvest Complete Job " + handback);
            }
        }
    }
}

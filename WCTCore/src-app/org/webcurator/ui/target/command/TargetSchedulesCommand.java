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
package org.webcurator.ui.target.command;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.webcurator.core.util.Utils;
import org.webcurator.domain.model.core.Schedule;

/**
 * The command object for the target schedules tab.
 * @author bbeaumont
 */
public class TargetSchedulesCommand {
	/** The name of the parameter for Start Date **/
    public static final String PARAM_START_DATE = "startDate";
    /** The name of the parameter for End Date. */
    public static final String PARAM_END_DATE = "endDate";	
    /** The name of the action Parameter. */
    public static final String PARAM_ACTION = "actionCmd";
    
    public static final String PARAM_MINUTES = "minutes";
    public static final String PARAM_HOURS = "hours";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_DAYS_OF_WEEK = "daysOfWeek";
    public static final String PARAM_DAYS_OF_MONTH = "daysOfMonth";
    public static final String PARAM_MONTHS = "months";
    public static final String PARAM_YEARS = "years";
	
    
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_NEW  = "new";
    public static final String ACTION_TEST = "test";
    public static final String ACTION_SAVE = "save";
    public static final String ACTION_CANCEL = "cancel";
    public static final String ACTION_VIEW = "view";
    public static final String ACTION_REFRESH = "refresh";
    
    /** The start date and time of the schedule. */
    private Date startDate;
    /** the end date of the schedule. */
    private Date endDate;
    /** The minutes component of the Cron Expression. **/
    private String minutes;
    /** The hours component of the Cron Expression. **/
    private String hours;
    /** The daysOfWeek component of the Cron Expression. **/
    private String daysOfWeek;
    /** The daysOfMonth component of the Cron Expression. **/
    private String daysOfMonth;
    /** The months component of the Cron Expression. **/
    private String months;
    /** The years component of the Cron Expression. **/
    private String years;
    /** The schedule type */
    private Integer scheduleType;
    /** The selected item index **/
    private String selectedItem; 
    /** The action command. **/
    private String actionCmd;
    /** The time **/
    private Time time;
    /** Is 'Harvest Now' check box checked? **/
    private boolean harvestNow = false;
    
    public static final Pattern TIME_PATTERN = Pattern.compile("^(\\d{1,2}):(\\d{2})$"); 
    public static final NumberFormat MINS_FORMAT = new DecimalFormat("00");
    
    public TargetSchedulesCommand() {
    	startDate = Utils.clearTime(new Date());
    	scheduleType = Schedule.TYPE_MONTHLY;
    	
    	// Set the time to one hour in the future.
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, 1);
    	time = new Time(cal);
    }
    
	/**
	 * @return Returns the actionCmd.
	 */
	public String getActionCmd() {
		return actionCmd;
	}
	/**
	 * @param actionCmd The actionCmd to set.
	 */
	public void setActionCmd(String actionCmd) {
		this.actionCmd = actionCmd;
	}
	/**
	 * @return Returns the daysOfMonth.
	 */
	public String getDaysOfMonth() {
		return daysOfMonth;
	}
	/**
	 * @param daysOfMonth The daysOfMonth to set.
	 */
	public void setDaysOfMonth(String daysOfMonth) {
		this.daysOfMonth = daysOfMonth;
	}
	/**
	 * @return Returns the daysOfWeek.
	 */
	public String getDaysOfWeek() {
		return daysOfWeek;
	}
	/**
	 * @param daysOfWeek The daysOfWeek to set.
	 */
	public void setDaysOfWeek(String daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}
	/**
	 * @return Returns the end.
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The end date to set.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the hours.
	 */
	public String getHours() {
		return hours;
	}
	/**
	 * @param hours The hours to set.
	 */
	public void setHours(String hours) {
		this.hours = hours;
	}
	/**
	 * @return Returns the minutes.
	 */
	public String getMinutes() {
		return minutes;
	}
	/**
	 * @param minutes The minutes to set.
	 */
	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	/**
	 * @return Returns the months.
	 */
	public String getMonths() {
		return months;
	}
	/**
	 * @param months The months to set.
	 */
	public void setMonths(String months) {
		this.months = months;
	}
	/**
	 * @return Returns the start.
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The start date to set.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return Returns the years.
	 */
	public String getYears() {
		return years;
	}
	/**
	 * @param years The years to set.
	 */
	public void setYears(String years) {
		this.years = years;
	}
	
	
	
    
    /**
	 * @return Returns the selectedItem.
	 */
	public String getSelectedItem() {
		return selectedItem;
	}
	/**
	 * @param selectedItem The selectedItem to set.
	 */
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}
	public String getCronExpression() {
		
		if(scheduleType <= Schedule.CUSTOM_SCHEDULE) {
		  	StringBuffer buff = new StringBuffer();
	    	buff.append("00");
	    	buff.append(" ");
	    	buff.append(scheduleType == Schedule.CUSTOM_SCHEDULE ? minutes : time.getMinutes());
	    	buff.append(" ");
	    	buff.append(scheduleType == Schedule.CUSTOM_SCHEDULE ? hours : time.getHours());
	    	buff.append(" ");
	    	buff.append(daysOfMonth);
	    	buff.append(" ");
	    	buff.append(months);
	    	buff.append(" ");
	    	buff.append(daysOfWeek);
	    	buff.append(" ");
	    	buff.append(years);
	    	
	    	return buff.toString();
		}
		else {
			return null;
		}
    }

    public static TargetSchedulesCommand buildFromModel(Schedule schedule) {
    	TargetSchedulesCommand command = new TargetSchedulesCommand();
    	command.setStartDate(schedule.getStartDate());
    	command.setEndDate(schedule.getEndDate());
    	command.setScheduleType(schedule.getScheduleType());
    	
    	StringTokenizer tokens = new StringTokenizer(schedule.getCronPattern(), " ");

    	// Skip the seconds component
    	tokens.nextToken();
    	
    	// Split the expression into its elements.
    	command.setMinutes(tokens.nextToken());
    	command.setHours(tokens.nextToken());
    	command.setDaysOfMonth(tokens.nextToken());
    	command.setMonths(tokens.nextToken());
    	command.setDaysOfWeek(tokens.nextToken());
    	command.setYears(tokens.nextToken());
    	
    	if(command.getScheduleType() < 0) { 
    		int hours = Integer.parseInt(command.getHours());
    		int minutes = Integer.parseInt(command.getMinutes());
    		command.setTime(new Time(hours, minutes));
    	}
    	
    	// Set the oid
    	command.setSelectedItem(schedule.getIdentity());
    	
    	return command;
    	
    }
    
    
    public void updateBusinessModel2(Schedule schedule) {
    	schedule.setStartDate(this.startDate);
    	schedule.setEndDate(this.endDate);
    	schedule.setCronPattern(this.getCronExpression());
    	
    }
    
    
    public Time getTime() {
    	return time;
    }
    
	public void setTime(Time time) {
		this.time = time;
	}
	
	/**
	 * @return Returns the scheduleType.
	 */
	public Integer getScheduleType() {
		return scheduleType;
	}
	/**
	 * @param scheduleType The scheduleType to set.
	 */
	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}

	/**
	 * @return Returns the harvestNow option.
	 */
	public boolean isHarvestNowSet() {
		return harvestNow;
	}

	/**
	 * @param displayTarget The displayTarget to set.
	 */
	public void setHarvestNow(boolean harvestNow) {
		this.harvestNow = harvestNow;
	}
	
    
}


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
package org.webcurator.ui.admin.command;

/**
 * The command object for creating permission templates.
 * @author bprice
 */
public class TemplateCommand {
	/** the constant name of the templates model object. */
    public static final String MDL_TEMPLATES = "templates";
    /** the constant name of the agencies model object. */
    public static final String MDL_AGENCIES = "agencies";
    /** the constant name of the template types model object. */
    public static final String MDL_TEMPLATE_TYPES = "templateTypes";
    
    /** the constant name of the action parameter. */
    public static final String PARAM_ACTION = "action";
    /** the constant name of the template oid parameter. */
    public static final String PARAM_OID = "oid";
    /** the constant name of the template type parameter. */
    public static final String PARAM_TEMPLATE_TYPE = "templateType";
    /** the constant name of the template text parameter. */
    public static final String PARAM_TEMPLATE_TEXT = "templateText";
    /** the constant name of the agency oid parameter. */
    public static final String PARAM_AGENCY_OID = "agencyOid";
    /** the constant name of the template name parameter. */
    public static final String PARAM_TEMPLATE_NAME = "templateName";
    /** the constant name of the template description parameter. */
    public static final String PARAM_TEMPLATE_DESCRIPTION = "templateDescription";
    
    /** the constant name of the templateSubject parameter. */
    public static final String PARAM_TEMPLATE_SUBJECT = "templateSubject";
    /** the constant name of the templateOverwriteFrom parameter. */
    public static final String PARAM_TEMPLATE_OVERWRITE_FROM = "templateOverwriteFrom";
    /** the constant name of the templateFrom parameter. */
    public static final String PARAM_TEMPLATE_FROM = "templateFrom";
    /** the constant name of the templateCc parameter. */
    public static final String PARAM_TEMPLATE_CC = "templateCc";
    /** the constant name of the templateBcc parameter. */
    public static final String PARAM_TEMPLATE_BCC = "templateBcc";
    /** the constant name of the templateBcc parameter. */
    public static final String PARAM_EMAIL_TYPE_TEXT = "emailTypeText";
    
    /** the name of the new template action command. */
    public static final String ACTION_NEW = "new";
    /** the name of the save template action command. */
    public static final String ACTION_SAVE = "save";
    /** the name of the view template action command. */
    public static final String ACTION_VIEW = "view";
    /** the name of the edit template action command. */
    public static final String ACTION_EDIT = "edit";
    /** the name of the delete template action command. */
    public static final String ACTION_DELETE = "delete";
    
    /** the action command field. */
    private String action;
    /** the template type field. */
    private String templateType;
    /** the template text field. */
    private String templateText;
    /** the template name field. */
    private String templateName;
    /** the template description field. */
    private String templateDescription;
    /** the agency oid field. */
    private Long agencyOid;
    /** the template oid field. */
    private Long oid;
    /** the template subject field. */
    private String templateSubject;
    /** the template overwrite From field. */
    private boolean templateOverwriteFrom;
    /** the template from field. */
    private String templateFrom;
    /** the template cc field. */
    private String templateCc;
    /** the template bcc field. */
    private String templateBcc;
    /** the isEmail field. */
    private String emailTypeText;
    
    
    /** Default Constructor. */
    public TemplateCommand() {
    }

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the agencyOid
	 */
	public Long getAgencyOid() {
		return agencyOid;
	}

	/**
	 * @param agencyOid the agencyOid to set
	 */
	public void setAgencyOid(Long agencyOid) {
		this.agencyOid = agencyOid;
	}

	/**
	 * @return the oid
	 */
	public Long getOid() {
		return oid;
	}

	/**
	 * @param oid the oid to set
	 */
	public void setOid(Long oid) {
		this.oid = oid;
	}

	/**
	 * @return the templateDescription
	 */
	public String getTemplateDescription() {
		return templateDescription;
	}

	/**
	 * @param templateDescription the templateDescription to set
	 */
	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the templateText
	 */
	public String getTemplateText() {
		return templateText;
	}

	/**
	 * @param templateText the templateText to set
	 */
	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}

	/**
	 * @return the templateType
	 */
	public String getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType the templateType to set
	 */
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	
	/**
	 * @return the templateSubject
	 */
    public String getTemplateSubject() {
        return templateSubject;
    }

	/**
	 * @param templateSubject the templateSubject to set
	 */
    public void setTemplateSubject(String templateSubject) {
        this.templateSubject = templateSubject;
    }
    
	/**
	 * @return the templateOverwriteFrom
	 */
    public boolean getTemplateOverwriteFrom() {
        return templateOverwriteFrom;
    }

	/**
	 * @param templateOverwriteFrom the templateOverwriteFrom to set
	 */
    public void setTemplateOverwriteFrom(boolean templateOverwriteFrom) {
        this.templateOverwriteFrom = templateOverwriteFrom;
    }
    
	/**
	 * @return the templateFrom
	 */
    public String getTemplateFrom() {
        return templateFrom;
    }

	/**
	 * @param templateFrom the templateFrom to set
	 */
    public void setTemplateFrom(String templateFrom) {
        this.templateFrom = templateFrom;
    }
    
	/**
	 * @return the templateCc
	 */
    public String getTemplateCc() {
        return templateCc;
    }

	/**
	 * @param templateCc the templateCc to set
	 */
    public void setTemplateCc(String templateCc) {
        this.templateCc = templateCc;
    }
    
    
	/**
	 * @return the templateBcc
	 */
    public String getTemplateBcc() {
        return templateBcc;
    }

	/**
	 * @param templateBcc the templateBcc to set
	 */
    public void setTemplateBcc(String templateBcc) {
        this.templateBcc = templateBcc;
    }
    
    /**
	 * @return the emailTypeText
	 */
    public String getEmailTypeText() {
        return emailTypeText;
    }

	/**
	 * @param templateBcc the templateBcc to set
	 */
    public void setEmailTypeText(String emailTypeText) {
        this.emailTypeText = emailTypeText;
    }
    

}

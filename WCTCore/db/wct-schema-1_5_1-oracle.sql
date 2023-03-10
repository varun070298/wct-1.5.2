drop table db_wct.ABSTRACT_TARGET cascade constraints;
drop table db_wct.AGENCY cascade constraints;
drop table db_wct.ANNOTATIONS cascade constraints;
drop table db_wct.ARC_HARVEST_FILE cascade constraints;
drop table db_wct.ARC_HARVEST_RESOURCE cascade constraints;
drop table db_wct.ARC_HARVEST_RESULT cascade constraints;
drop table db_wct.AUTHORISING_AGENT cascade constraints;
drop table db_wct.BANDWIDTH_RESTRICTIONS cascade constraints;
drop table db_wct.DUBLIN_CORE cascade constraints;
drop table db_wct.GROUP_MEMBER cascade constraints;
drop table db_wct.HARVEST_RESOURCE cascade constraints;
drop table db_wct.HARVEST_RESULT cascade constraints;
drop table db_wct.HARVEST_STATUS cascade constraints;
drop table db_wct.HR_MODIFICATION_NOTE cascade constraints;
drop table db_wct.NOTIFICATION cascade constraints;
drop table db_wct.PERMISSION cascade constraints;
drop table db_wct.PERMISSION_EXCLUSION cascade constraints;
drop table db_wct.PERMISSION_TEMPLATE cascade constraints;
drop table db_wct.PERMISSION_URLPATTERN cascade constraints;
drop table db_wct.PO_EXCLUSION_URI cascade constraints;
drop table db_wct.PO_INCLUSION_URI cascade constraints;
drop table db_wct.PROFILE cascade constraints;
drop table db_wct.PROFILE_BASIC_CREDENTIALS cascade constraints;
drop table db_wct.PROFILE_CREDENTIALS cascade constraints;
drop table db_wct.PROFILE_FORM_CREDENTIALS cascade constraints;
drop table db_wct.PROFILE_OVERRIDES cascade constraints;
drop table db_wct.ROLE_PRIVILEGE cascade constraints;
drop table db_wct.SCHEDULE cascade constraints;
drop table db_wct.SEED cascade constraints;
drop table db_wct.SEED_HISTORY cascade constraints;
drop table db_wct.SEED_PERMISSION cascade constraints;
drop table db_wct.SIP_PART_ELEMENT cascade constraints;
drop table db_wct.SITE cascade constraints;
drop table db_wct.SITE_AUTH_AGENCY cascade constraints;
drop table db_wct.TARGET cascade constraints;
drop table db_wct.TARGET_GROUP cascade constraints;
drop table db_wct.TARGET_INSTANCE cascade constraints;
drop table db_wct.TARGET_INSTANCE_ORIG_SEED cascade constraints;
drop table db_wct.TASK cascade constraints;
drop table db_wct.URL_PATTERN cascade constraints;
drop table db_wct.URL_PERMISSION_MAPPING cascade constraints;
drop table db_wct.USER_ROLE cascade constraints;
drop table db_wct.WCTAUDIT cascade constraints;
drop table db_wct.WCTROLE cascade constraints;
drop table db_wct.WCTUSER cascade constraints;
drop table db_wct.WCT_LOGON_DURATION cascade constraints;
drop table db_wct.ID_GENERATOR cascade constraints;
drop view db_wct.ABSTRACT_TARGET_GROUPTYPE_VIEW cascade constraints;
drop view db_wct.ABSTRACT_TARGET_SCHEDULE_VIEW cascade constraints;
drop view db_wct.URL_PERMISSION_MAPPING_VIEW cascade constraints;
create table db_wct.ABSTRACT_TARGET (AT_OID number(19,0) not null, AT_DESC varchar2(4000), AT_NAME varchar2(255), AT_OWNER_ID number(19,0), AT_PROF_OVERRIDE_OID number(19,0), AT_STATE number(10,0), T_PROFILE_ID number(19,0), AT_OBJECT_TYPE number(10,0), AT_CREATION_DATE TIMESTAMP(9), AT_REFERENCE varchar2(255), AT_PROFILE_NOTE varchar2(255), AT_DUBLIN_CORE_OID number(19,0), AT_ACCESS_ZONE number(2,0) default 0 not null, AT_DISPLAY_TARGET number(1,0) default 1 not null, AT_DISPLAY_NOTE varchar2(4000), AT_DISPLAY_CHG_REASON varchar2(1000), primary key (AT_OID));
create table db_wct.AGENCY (AGC_OID number(19,0) not null, AGC_NAME varchar2(80) not null unique, AGC_ADDRESS varchar2(255) not null, AGC_LOGO_URL varchar2(255), AGC_URL varchar2(255), AGC_EMAIL varchar2(80), AGC_FAX varchar2(20), AGC_PHONE varchar2(20), primary key (AGC_OID));
create table db_wct.ANNOTATIONS (AN_OID number(19,0) not null, AN_DATE date not null, AN_NOTE varchar2(1000) not null, AN_USER_OID number(19,0) not null, AN_OBJ_OID number(19,0) not null, AN_OBJ_TYPE varchar2(500) not null, AN_ALERTABLE number(1,0) default 0 not null, primary key (AN_OID));
create table db_wct.ARC_HARVEST_FILE (AHF_OID number(19,0) not null, AHF_COMPRESSED number(1,0) not null, AHF_NAME varchar2(100) not null unique, AHF_ARC_HARVEST_RESULT_ID number(19,0), primary key (AHF_OID));
create table db_wct.ARC_HARVEST_RESOURCE (AHRC_HARVEST_RESOURCE_OID number(19,0) not null, AHRC_RESOURCE_LENGTH number(19,0) not null, AHRC_RESOURCE_OFFSET number(19,0) not null, AHRC_ARC_FILE_NAME varchar2(100) not null, AHRC_COMPRESSED_YN number(1,0) not null, primary key (AHRC_HARVEST_RESOURCE_OID));
create table db_wct.ARC_HARVEST_RESULT (AHRS_HARVEST_RESULT_OID number(19,0) not null, primary key (AHRS_HARVEST_RESULT_OID));
create table db_wct.AUTHORISING_AGENT (AA_OID number(19,0) not null, AA_NAME varchar2(255) unique, AA_ADRESS varchar2(2048), AA_CONTACT varchar2(255), AA_EMAIL varchar2(255), AA_PHONE_NUMBER varchar2(32), AA_DESC varchar2(2048), primary key (AA_OID));
create table db_wct.BANDWIDTH_RESTRICTIONS (BR_OID number(19,0) not null, BR_BANDWIDTH number(19,0) not null, BR_DAY varchar2(9) not null, BR_END_TIME date not null, BR_START_TIME date not null, primary key (BR_OID));
create table db_wct.DUBLIN_CORE (DC_OID number(19,0) not null, DC_CONTRIBUTOR varchar2(255), DC_COVERAGE varchar2(255), DC_CREATOR varchar2(255), DC_DESCRIPTION varchar2(2000), DC_FORMAT varchar2(255), DC_IDENTIFIER varchar2(255), DC_IDENTIFIER_ISBN varchar2(13), DC_IDENTIFIER_ISSN varchar2(9), DC_LANGUAGE varchar2(255), DC_PUBLISHER varchar2(255), DC_RELATION varchar2(255), DC_SOURCE varchar2(255), DC_SUBJECT varchar2(2000), DC_TITLE varchar2(255), DC_TYPE varchar2(50), primary key (DC_OID));
create table db_wct.GROUP_MEMBER (AT_OID number(19,0) not null, GM_CHILD_ID number(19,0), GM_PARENT_ID number(19,0), primary key (AT_OID));
create table db_wct.HARVEST_RESOURCE (HRC_OID number(19,0) not null, HRC_LENGTH number(19,0), HRC_NAME varchar2(1020) not null, HRC_HARVEST_RESULT_OID number(19,0), HRC_STATUS_CODE number(10,0) not null, primary key (HRC_OID));
create table db_wct.HARVEST_RESULT (HR_OID number(19,0) not null, HR_HARVEST_NO number(10,0), HR_TARGET_INSTANCE_ID number(19,0), HR_PROVENANCE_NOTE varchar2(1024) not null, HR_CREATED_DATE date, HR_CREATED_BY_ID number(19,0), HR_STATE number(10,0), HR_DERIVED_FROM number(10,0), HR_INDEX number(10,0), primary key (HR_OID));
create table db_wct.HARVEST_STATUS (HS_OID number(19,0) not null, HS_AVG_KB double precision, HS_AVG_URI double precision, HS_DATA_AMOUNT number(19,0), HS_ELAPSED_TIME number(19,0), HS_JOB_NAME varchar2(500), HS_STATUS varchar2(255), HS_URLS_DOWN number(19,0), HS_URLS_FAILED number(19,0), HS_ALERTS number(10,0), HS_APP_VERSION varchar2(255), HS_HRTX_VERSION varchar2(255), primary key (HS_OID));
create table db_wct.HR_MODIFICATION_NOTE (HMN_HR_OID number(19,0) not null, HMN_NOTE varchar2(2000), HMN_INDEX number(10,0) not null, primary key (HMN_HR_OID, HMN_INDEX));
create table db_wct.NOTIFICATION (NOT_OID number(19,0) not null, NOT_MESSAGE varchar2(2000), NOT_USR_OID number(19,0) not null, NOT_SENDER varchar2(80) not null, NOT_SENT_DATE timestamp(9), NOT_SUBJECT varchar2(255) not null, primary key (NOT_OID));
create table db_wct.PERMISSION (PE_OID number(19,0) not null, PE_ACCESS_STATUS varchar2(255), PE_APPROVED_YN number(1,0), PE_AVAILABLE_YN number(1,0), PE_COPYRIGHT_STATEMENT varchar2(2048), PE_COPYRIGHT_URL varchar2(2048), PE_CREATION_DATE TIMESTAMP(9), PE_END_DATE TIMESTAMP(9), PE_NOTES clob, PE_OPEN_ACCESS_DATE TIMESTAMP(9), PE_PERMISSION_GRANTED_DATE TIMESTAMP(9), PE_PERMISSION_REQUESTED_DATE TIMESTAMP(9), PE_SPECIAL_REQUIREMENTS varchar2(2048), PE_START_DATE TIMESTAMP(9), PE_STATUS number(10,0), PE_AUTH_AGENT_ID number(19,0), PE_SITE_ID number(19,0), PE_QUICK_PICK number(1,0), PE_DISPLAY_NAME varchar2(32), PE_OWNING_AGENCY_ID number(19,0), PE_FILE_REFERENCE varchar2(255), primary key (PE_OID));
create table db_wct.PERMISSION_EXCLUSION (PEX_OID number(19,0) not null, PEX_REASON varchar2(255), PEX_URL varchar2(1024), PEX_PERMISSION_OID number(19,0), PEX_INDEX number(10,0), primary key (PEX_OID));
create table db_wct.PERMISSION_TEMPLATE (PRT_OID number(19,0) not null, PRT_AGC_OID number(19,0) not null, PRT_TEMPLATE_TEXT clob not null, PRT_TEMPLATE_NAME varchar2(80) not null, PRT_TEMPLATE_TYPE varchar2(40) not null, PRT_TEMPLATE_DESC varchar2(255), PRT_TEMPLATE_SUBJECT varchar2(255), PRT_TEMPLATE_OVERWRITE_FROM number(1,0) default 0 not null, PRT_TEMPLATE_FROM varchar2(255), PRT_TEMPLATE_CC varchar2(2048), PRT_TEMPLATE_BCC varchar2(2048), primary key (PRT_OID));
create table db_wct.PERMISSION_URLPATTERN (PU_PERMISSION_ID number(19,0) not null, PU_URLPATTERN_ID number(19,0) not null, primary key (PU_URLPATTERN_ID, PU_PERMISSION_ID));
create table db_wct.PO_EXCLUSION_URI (PEU_PROF_OVER_OID number(19,0) not null, PEU_FILTER varchar2(255), PEU_IX number(10,0) not null, primary key (PEU_PROF_OVER_OID, PEU_IX));
create table db_wct.PO_INCLUSION_URI (PEU_PROF_OVER_OID number(19,0) not null, PEU_FILTER varchar2(255), PEU_IX number(10,0) not null, primary key (PEU_PROF_OVER_OID, PEU_IX));
create table db_wct.PROFILE (P_OID number(19,0) not null, P_VERSION number(10,0) not null, P_DESC varchar2(255), P_NAME varchar2(255), P_PROFILE_STRING clob, P_PROFILE_LEVEL number(10,0), P_STATUS number(10,0), P_DEFAULT number(1,0), P_AGECNY_OID number(19,0), P_ORIG_OID number(19,0), primary key (P_OID));
create table db_wct.PROFILE_BASIC_CREDENTIALS (PBC_PC_OID number(19,0) not null, PBC_REALM varchar2(255), primary key (PBC_PC_OID));
create table db_wct.PROFILE_CREDENTIALS (PC_OID number(19,0) not null, PC_DOMAIN varchar2(255), PC_PASSWORD varchar2(255), PC_USERNAME varchar2(255), PC_PROFILE_OVERIDE_OID number(19,0), PC_INDEX number(10,0), primary key (PC_OID));
create table db_wct.PROFILE_FORM_CREDENTIALS (PRC_PC_OID number(19,0) not null, PFC_METHOD varchar2(4), PFC_LOGIN_URI varchar2(255), PFC_PASSWORD_FIELD varchar2(255), PFC_USERNAME_FIELD varchar2(255), primary key (PRC_PC_OID));
create table db_wct.PROFILE_OVERRIDES (PO_OID number(19,0) not null, PO_EXCL_MIME_TYPES varchar2(255), PO_MAX_BYES number(19,0), PO_MAX_DOCS number(19,0), PO_MAX_HOPS number(10,0), PO_MAX_PATH_DEPTH number(10,0), PO_MAX_TIME_SEC number(19,0), PO_ROBOTS_POLICY varchar2(10), PO_OR_CREDENTIALS number(1,0), PO_OR_EXCL_MIME_TYPES number(1,0), PO_OR_EXCLUSION_URI number(1,0), PO_OR_INCLUSION_URI number(1,0), PO_OR_MAX_BYTES number(1,0), PO_OR_MAX_DOCS number(1,0), PO_OR_MAX_HOPS number(1,0), PO_OR_MAX_PATH_DEPTH number(1,0), PO_OR_MAX_TIME_SEC number(1,0), PO_OR_ROBOTS_POLICY number(1,0), primary key (PO_OID));
create table db_wct.ROLE_PRIVILEGE (PRV_OID number(19,0) not null, PRV_CODE varchar2(40) not null, PRV_ROLE_OID number(19,0), PRV_SCOPE number(10,0) not null, primary key (PRV_OID));
create table db_wct.SCHEDULE (S_OID number(19,0) not null, S_CRON varchar2(255) not null, S_START TIMESTAMP(9) not null, S_END TIMESTAMP(9), S_TARGET_ID number(19,0), S_TYPE number(10,0) not null, S_OWNER_OID number(19,0), S_NEXT_SCHEDULE_TIME TIMESTAMP(9), S_ABSTRACT_TARGET_ID number(19,0), S_LAST_PROCESSED_DATE TIMESTAMP(9) DEFAULT to_timestamp('2001-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'), primary key (S_OID));
create table db_wct.SEED (S_OID number(19,0) not null, S_SEED varchar2(1024), S_TARGET_ID number(19,0), S_PRIMARY number(1,0), primary key (S_OID));
create table db_wct.SEED_HISTORY (SH_OID number(19,0) not null, SH_TI_OID number(19,0), SH_SEED varchar2(1024) not null, SH_PRIMARY number(1,0) not null, primary key (SH_OID));
create table db_wct.SEED_PERMISSION (SP_SEED_ID number(19,0) not null, SP_PERMISSION_ID number(19,0) not null, primary key (SP_SEED_ID, SP_PERMISSION_ID));
create table db_wct.SIP_PART_ELEMENT (SPE_TARGET_INSTANCE_OID number(19,0) not null, SPE_VALUE clob, SPE_KEY varchar2(255) not null, primary key (SPE_TARGET_INSTANCE_OID, SPE_KEY));
create table db_wct.SITE (ST_OID number(19,0) not null, ST_TITLE varchar2(255) not null unique, ST_DESC varchar2(4000), ST_LIBRARY_ORDER_NO varchar2(32), ST_NOTES clob, ST_PUBLISHED number(1,0) not null, ST_ACTIVE number(1,0) not null, ST_OWNING_AGENCY_ID number(19,0), ST_CREATION_DATE TIMESTAMP(9), primary key (ST_OID));
create table db_wct.SITE_AUTH_AGENCY (SA_SITE_ID number(19,0) not null, SA_AGENT_ID number(19,0) not null, primary key (SA_SITE_ID, SA_AGENT_ID));
create table db_wct.TARGET (T_AT_OID number(19,0) not null, T_RUN_ON_APPROVAL number(1,0), T_EVALUATION_NOTE varchar2(1000), T_SELECTION_DATE TIMESTAMP(9), T_SELECTION_NOTE varchar2(1000), T_SELECTION_TYPE varchar2(255), T_HARVEST_TYPE varchar2(255), primary key (T_AT_OID));
create table db_wct.TARGET_GROUP (TG_AT_OID number(19,0) not null, TG_SIP_TYPE number(10,0), TG_START_DATE DATE, TG_END_DATE DATE, TG_OWNERSHIP_METADATA varchar2(255), TG_TYPE varchar2(255), primary key (TG_AT_OID));
create table db_wct.TARGET_INSTANCE (TI_OID number(19,0) not null, TI_VERSION number(10,0) not null, TI_SCHEDULE_ID number(19,0), TI_TARGET_ID number(19,0), TI_PRIORITY number(10,0) not null, TI_SCHEDULED_TIME TIMESTAMP(9) not null, TI_STATE varchar2(50) not null, TI_BANDWIDTH_PERCENT number(10,0), TI_ALLOCATED_BANDWIDTH number(19,0), TI_START_TIME date, TI_OWNER_ID number(19,0), TI_DISPLAY_ORDER number(10,0), TI_PROF_OVERRIDE_OID number(19,0), TI_PURGED number(1,0) not null, TI_ARCHIVE_ID varchar2(40) unique, TI_REFERENCE varchar2(255), TI_HARVEST_SERVER varchar2(255), TI_DISPLAY_TARGET_INSTANCE number(1,0) default 1 not null, TI_DISPLAY_NOTE varchar2(4000), TI_FLAGGED number(1,0) default 0 not null, TI_PROFILE_ID number(19,0), TI_ARCHIVED_TIME date, TI_FIRST_FROM_TARGET number(1,0) default 0 not null, TI_DISPLAY_CHG_REASON varchar2(1000), primary key (TI_OID));
create table db_wct.TARGET_INSTANCE_ORIG_SEED (TIOS_TI_OID number(19,0) not null, TIOS_SEED varchar2(255));
create table db_wct.TASK (TSK_OID number(19,0) not null, TSK_USR_OID number(19,0), TSK_MESSAGE varchar2(2000), TSK_SENDER varchar2(80) not null, TSK_SENT_DATE timestamp(9), TSK_SUBJECT varchar2(255) not null, TSK_PRIVILEGE varchar2(40), TSK_AGC_OID number(19,0) not null, TSK_MSG_TYPE varchar2(40) not null, TSK_RESOURCE_OID number(19,0) not null, TSK_RESOURCE_TYPE varchar2(80) not null, primary key (TSK_OID));
create table db_wct.URL_PATTERN (UP_OID number(19,0) not null, UP_PATTERN varchar2(2048), UP_SITE_ID number(19,0), primary key (UP_OID));
create table db_wct.URL_PERMISSION_MAPPING (UPM_OID number(19,0) not null, UPM_PERMISSION_ID number(19,0), UPM_URL_PATTERN_ID number(19,0), UPM_DOMAIN varchar2(1024), primary key (UPM_OID));
create table db_wct.USER_ROLE (URO_ROL_OID number(19,0) not null, URO_USR_OID number(19,0) not null, primary key (URO_USR_OID, URO_ROL_OID));
create table db_wct.WCTAUDIT (AUD_OID number(19,0) not null, AUD_ACTION varchar2(40) not null, AUD_DATE TIMESTAMP(9) not null, AUD_FIRSTNAME varchar2(50), AUD_LASTNAME varchar2(50), AUD_MESSAGE varchar2(2000) not null, AUD_SUBJECT_TYPE varchar2(255) not null, AUD_USERNAME varchar2(80), AUD_USER_OID number(19,0), AUD_SUBJECT_OID number(19,0), AUD_AGENCY_OID number(19,0), primary key (AUD_OID));
create table db_wct.WCTROLE (ROL_OID number(19,0) not null, ROL_DESCRIPTION varchar2(255), ROL_NAME varchar2(80) not null, ROL_AGENCY_OID number(19,0) not null, primary key (ROL_OID));
create table db_wct.WCTUSER (USR_OID number(19,0) not null, USR_ACTIVE number(1,0) not null, USR_ADDRESS varchar2(200), USR_EMAIL varchar2(100) not null, USR_EXTERNAL_AUTH number(1,0) not null, USR_FIRSTNAME varchar2(50) not null, USR_FORCE_PWD_CHANGE number(1,0) not null, USR_LASTNAME varchar2(50) not null, USR_NOTIFICATIONS_BY_EMAIL number(1,0) not null, USR_PASSWORD varchar2(255), USR_PHONE varchar2(16), USR_TITLE varchar2(10), USR_USERNAME varchar2(80) not null unique, USR_AGC_OID number(19,0) not null, USR_DEACTIVATE_DATE TIMESTAMP(9), USR_TASKS_BY_EMAIL number(1,0) not null, USR_NOTIFY_ON_GENERAL number(1,0) not null, USR_NOTIFY_ON_WARNINGS number(1,0) not null, primary key (USR_OID));
create table db_wct.WCT_LOGON_DURATION (LOGDUR_OID number(19,0) not null, LOGDUR_DURATION number(19,0), LOGDUR_LOGON_TIME TIMESTAMP(9) not null, LOGDUR_LOGOUT_TIME TIMESTAMP(9), LOGDUR_USERNAME varchar2(80), LOGDUR_USER_OID number(19,0) not null, LOGDUR_USER_REALNAME varchar2(100), LOGDUR_SESSION_ID varchar2(32) not null, primary key (LOGDUR_OID));
alter table db_wct.ABSTRACT_TARGET add constraint AT_NAME_AND_TYPE UNIQUE (AT_NAME, AT_OBJECT_TYPE);
alter table db_wct.ABSTRACT_TARGET add constraint FK_AT_DUBLIN_CORE_OID foreign key (AT_DUBLIN_CORE_OID) references db_wct.DUBLIN_CORE;
alter table db_wct.ABSTRACT_TARGET add constraint FK_T_PROF_OVERRIDE_OID foreign key (AT_PROF_OVERRIDE_OID) references db_wct.PROFILE_OVERRIDES;
alter table db_wct.ABSTRACT_TARGET add constraint FKB6DD784E5C2C497 foreign key (AT_OWNER_ID) references db_wct.WCTUSER;
alter table db_wct.ABSTRACT_TARGET add constraint FKB6DD784E3A83A603 foreign key (T_PROFILE_ID) references db_wct.PROFILE;
alter table db_wct.ABSTRACT_TARGET add constraint CHK_ACCESS_ZONE check (AT_ACCESS_ZONE in (0,1,2));
alter table db_wct.ANNOTATIONS add constraint FK_NOTE_USER_OID foreign key (AN_USER_OID) references db_wct.WCTUSER;
alter table db_wct.ARC_HARVEST_FILE add constraint FK_AHR_ARC_HARVEST_RESULT_ID foreign key (AHF_ARC_HARVEST_RESULT_ID) references db_wct.ARC_HARVEST_RESULT;
alter table db_wct.ARC_HARVEST_RESOURCE add constraint FK6D84FEB12FF8F14B foreign key (AHRC_HARVEST_RESOURCE_OID) references db_wct.HARVEST_RESOURCE;
alter table db_wct.ARC_HARVEST_RESULT add constraint FKE39C5380C88A38D9 foreign key (AHRS_HARVEST_RESULT_OID) references db_wct.HARVEST_RESULT;
alter table db_wct.BANDWIDTH_RESTRICTIONS add constraint CHK_DAY check (br_day IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'));
alter table db_wct.BANDWIDTH_RESTRICTIONS add constraint CHK_END_TIME check (br_end_time >= TO_DATE('1972-11-09 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND br_end_time <= TO_DATE('1972-11-09 23:59:59', 'YYYY-MM-DD HH24:MI:SS'));
alter table db_wct.BANDWIDTH_RESTRICTIONS add constraint CHK_START_TIME check (br_start_time >= TO_DATE('1972-11-09 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND br_start_time <= TO_DATE('1972-11-09 23:59:59', 'YYYY-MM-DD HH24:MI:SS'));
alter table db_wct.GROUP_MEMBER add constraint FK_GM_PARENT_ID foreign key (GM_PARENT_ID) references db_wct.TARGET_GROUP;
alter table db_wct.GROUP_MEMBER add constraint FK_GM_CHILD_ID foreign key (GM_CHILD_ID) references db_wct.ABSTRACT_TARGET;
alter table db_wct.HARVEST_RESOURCE add constraint FK5BA2B04431A1C148 foreign key (HRC_HARVEST_RESULT_OID) references db_wct.HARVEST_RESULT;
alter table db_wct.HARVEST_RESULT add constraint FK_HR_CREATED_BY_ID foreign key (HR_CREATED_BY_ID) references db_wct.WCTUSER;
alter table db_wct.HARVEST_RESULT add constraint FK_HRC_TARGET_INSTANCE_ID foreign key (HR_TARGET_INSTANCE_ID) references db_wct.TARGET_INSTANCE;
alter table db_wct.HR_MODIFICATION_NOTE add constraint FKA908CCC03E1474AF foreign key (HMN_HR_OID) references db_wct.HARVEST_RESULT;
alter table db_wct.PERMISSION add constraint FKFE0FB1CFEE52493C foreign key (PE_AUTH_AGENT_ID) references db_wct.AUTHORISING_AGENT;
alter table db_wct.PERMISSION add constraint FKFE0FB1CFA1E5D89A foreign key (PE_OWNING_AGENCY_ID) references db_wct.AGENCY;
alter table db_wct.PERMISSION add constraint FK_PE_SITE_ID foreign key (PE_SITE_ID) references db_wct.SITE;
alter table db_wct.PERMISSION_EXCLUSION add constraint FK2DB3C33EB558CEC7 foreign key (PEX_PERMISSION_OID) references db_wct.PERMISSION;
alter table db_wct.PERMISSION_TEMPLATE add constraint FK_TEMPLATE_AGENCY_OID foreign key (PRT_AGC_OID) references db_wct.AGENCY;
alter table db_wct.PERMISSION_URLPATTERN add constraint PU_FK_1 foreign key (PU_URLPATTERN_ID) references db_wct.URL_PATTERN;
alter table db_wct.PERMISSION_URLPATTERN add constraint PU_FK_2 foreign key (PU_PERMISSION_ID) references db_wct.PERMISSION;
alter table db_wct.PO_EXCLUSION_URI add constraint FKFF4AB0FBBC3C926 foreign key (PEU_PROF_OVER_OID) references db_wct.PROFILE_OVERRIDES;
alter table db_wct.PO_INCLUSION_URI add constraint FK6C665549BC3C926 foreign key (PEU_PROF_OVER_OID) references db_wct.PROFILE_OVERRIDES;
alter table db_wct.PROFILE add constraint FK_P_AGENCY_OID foreign key (P_AGECNY_OID) references db_wct.AGENCY;
alter table db_wct.PROFILE_BASIC_CREDENTIALS add constraint FKE72A5AF5BD9AB61 foreign key (PBC_PC_OID) references db_wct.PROFILE_CREDENTIALS;
alter table db_wct.PROFILE_CREDENTIALS add constraint FK317A252682C63D7F foreign key (PC_PROFILE_OVERIDE_OID) references db_wct.PROFILE_OVERRIDES;
alter table db_wct.PROFILE_FORM_CREDENTIALS add constraint FK6B1303D750209B71 foreign key (PRC_PC_OID) references db_wct.PROFILE_CREDENTIALS;
alter table db_wct.ROLE_PRIVILEGE add constraint FK_PRIV_ROLE_OID foreign key (PRV_ROLE_OID) references db_wct.WCTROLE;
alter table db_wct.SCHEDULE add constraint FK50C8297BE49544D foreign key (S_ABSTRACT_TARGET_ID) references db_wct.ABSTRACT_TARGET;
alter table db_wct.SCHEDULE add constraint FK_S_OWNER_OID foreign key (S_OWNER_OID) references db_wct.WCTUSER;
alter table db_wct.SCHEDULE add constraint FK_S_TARGET_ID foreign key (S_TARGET_ID) references db_wct.ABSTRACT_TARGET;
alter table db_wct.SEED add constraint FK_SEED_TARGET_ID foreign key (S_TARGET_ID) references db_wct.TARGET;
alter table db_wct.SEED_HISTORY add constraint FK_SEED_HISTORY_TI_OID foreign key (SH_TI_OID) references db_wct.TARGET_INSTANCE (TI_OID);
alter table db_wct.SEED_PERMISSION add constraint FK9659805D68A4F4BA foreign key (SP_SEED_ID) references db_wct.SEED;
alter table db_wct.SEED_PERMISSION add constraint FK_SP_PERMISSION_ID foreign key (SP_PERMISSION_ID) references db_wct.PERMISSION;
alter table db_wct.SIP_PART_ELEMENT add constraint FK4998B1F5F51BBD3F foreign key (SPE_TARGET_INSTANCE_OID) references db_wct.TARGET_INSTANCE;
alter table db_wct.SITE add constraint FK_OWNING_AGENCY_ID foreign key (ST_OWNING_AGENCY_ID) references db_wct.AGENCY (AGC_OID);
alter table db_wct.SITE_AUTH_AGENCY add constraint FKDF808D84C049BBEB foreign key (SA_SITE_ID) references db_wct.SITE;
alter table db_wct.SITE_AUTH_AGENCY add constraint FK_SA_AGENT_ID foreign key (SA_AGENT_ID) references db_wct.AUTHORISING_AGENT;
alter table db_wct.TARGET add constraint FK931165917947B83E foreign key (T_AT_OID) references db_wct.ABSTRACT_TARGET;
alter table db_wct.TARGET_GROUP add constraint FKB444963161DABD5F foreign key (TG_AT_OID) references db_wct.ABSTRACT_TARGET;
alter table db_wct.TARGET_INSTANCE add constraint FK_TI_TARGET_ID foreign key (TI_TARGET_ID) references db_wct.ABSTRACT_TARGET;
alter table db_wct.TARGET_INSTANCE add constraint FK_TI_USER_ID foreign key (TI_OWNER_ID) references db_wct.WCTUSER;
alter table db_wct.TARGET_INSTANCE add constraint FK_TI_PROF_OVERRIDE_OID foreign key (TI_PROF_OVERRIDE_OID) references db_wct.PROFILE_OVERRIDES;
alter table db_wct.TARGET_INSTANCE add constraint FK_TI_SCHEDULE_ID foreign key (TI_SCHEDULE_ID) references db_wct.SCHEDULE;
alter table db_wct.TARGET_INSTANCE_ORIG_SEED add constraint FKD47ACFF36748402E foreign key (TIOS_TI_OID) references db_wct.TARGET_INSTANCE;
alter table db_wct.TASK add constraint FK_TASK_AGENCY_OID foreign key (TSK_AGC_OID) references db_wct.AGENCY;
alter table db_wct.URL_PATTERN add constraint FK_UP_SITE_ID foreign key (UP_SITE_ID) references db_wct.SITE;
alter table db_wct.URL_PERMISSION_MAPPING add constraint FK_UPM_URL_PATTERN_ID foreign key (UPM_URL_PATTERN_ID) references db_wct.URL_PATTERN;
alter table db_wct.URL_PERMISSION_MAPPING add constraint FK_UPM_PERMISSION_ID foreign key (UPM_PERMISSION_ID) references db_wct.PERMISSION;
alter table db_wct.USER_ROLE add constraint FK_USERROLE_TO_ROLE foreign key (URO_ROL_OID) references db_wct.WCTROLE;
alter table db_wct.USER_ROLE add constraint FK_USERROLE_TO_USER foreign key (URO_USR_OID) references db_wct.WCTUSER;
alter table db_wct.WCTAUDIT add constraint FK_AUD_USER_OID foreign key (AUD_USER_OID) references db_wct.WCTUSER (USR_OID);
alter table db_wct.WCTAUDIT add constraint FK_AUD_AGENCY_OID foreign key (AUD_AGENCY_OID) references db_wct.AGENCY (AGC_OID);
alter table db_wct.WCTROLE add constraint FK_ROLE_AGENCY_OID foreign key (ROL_AGENCY_OID) references db_wct.AGENCY;
alter table db_wct.WCTUSER add constraint FK_USER_AGENCY_OID foreign key (USR_AGC_OID) references db_wct.AGENCY;
create table db_wct.ID_GENERATOR ( IG_TYPE varchar2(255),  IG_VALUE number(10,0) ) ;
create view db_wct.URL_PERMISSION_MAPPING_VIEW as 
 SELECT upm.upm_oid, upm.upm_domain, p.pe_oid, p.pe_end_date, p.pe_owning_agency_id, up.up_pattern, st.st_active
   FROM db_wct.url_permission_mapping upm
   JOIN db_wct.permission p ON upm.upm_permission_id = p.pe_oid
   JOIN db_wct.url_pattern up ON upm.upm_url_pattern_id = up.up_oid
   JOIN db_wct.site st ON p.pe_site_id = st.st_oid;
create view db_wct.ABSTRACT_TARGET_SCHEDULE_VIEW as 
 SELECT (abt.at_oid || ',') || s.s_oid AS thekey, 
        CASE abt.at_object_type
            WHEN 1 THEN 'Target'
            ELSE 'Group'
        END AS at_object_type_desc, abt.at_name, abt.at_state, u.usr_username, a.agc_name, s.s_oid, s.s_start, s.s_end, s.s_type, s.s_cron
   FROM db_wct.abstract_target abt
   RIGHT JOIN db_wct.schedule s ON s.s_abstract_target_id = abt.at_oid
   JOIN db_wct.wctuser u ON abt.at_owner_id = u.usr_oid
   JOIN db_wct.agency a ON u.usr_agc_oid = a.agc_oid
  ORDER BY abt.at_name, s.s_oid;
create view db_wct.abstract_target_grouptype_view AS 
 SELECT a.at_oid, a.at_desc, a.at_name, a.at_owner_id, a.at_prof_override_oid, a.at_state, a.t_profile_id, a.at_object_type, a.at_creation_date, a.at_reference, a.at_profile_note, a.at_dublin_core_oid, a.at_access_zone, a.at_display_target, a.at_display_note, tg.tg_type
   FROM db_wct.abstract_target a
   LEFT JOIN db_wct.target_group tg ON a.at_oid = tg.tg_at_oid;

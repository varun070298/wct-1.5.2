create user db_wct identified by qdb_wct default tablespace wct_data quota unlimited on wct_data
create user usr_wct identified by qusr_wct  default tablespace wct_data quota unlimited on wct_data
grant create session to usr_wct
grant connect,resource to db_wct

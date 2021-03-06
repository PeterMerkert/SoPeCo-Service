/**
 * Copyright (c) 2014 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.service.configuration;

import java.util.concurrent.TimeUnit;

/**
 * The default service configuration. Here you can find all
 * the URIs for the RESTful service.
 * 
 * @author Peter Merkert
 */
public abstract class ServiceConfiguration {
	
	/**
	 * The Service URL split sign. E.g.
	 * http://host:port/a/b/c, whent the splitting sign is '/',
	 * http://host:port#a#b#c, whent the splitting sign is '#'.
	 */
	public static final String SERVICE_URL_SPLIT = "/";
	
	/**
	 * The package where all the RESTful services are in. Used to setup the {@link JerseyTest}
	 * correctly.
	 */
	public static final String PACKAGE_NAME_REST = "org.sopeco.service.rest";

	/**
	 * The class where the servlet lifecyclelistener is nested in.
	 */
	public static final String PACKAGE_NAME_LIFECYCLELISTENER = "org.sopeco.service.configuration";
	
	// the applications session ID, if needed
	public static final String SESSION_ID 		= "sessionid";
	
	// default user timeout
	public static final long USER_TIMEOUT 		= 600000;

	// timeout to try to connect to a socket
	public static final int SOCKET_TIMEOUT 		= 10000;
	
	// timeunit for the scheduling times can be configured below them
	public static final int SCHEDULING_REPEATE_INTERVAL = 5;
	public static final int SCHEDULING_TIMEOUT_INTERVAL = 5; // waits for the experiment scheduler to finish
	public static final TimeUnit SCHEDULING_TIME_UNIT 	= TimeUnit.SECONDS; // in seconds
	
	// default MeasurementSpecification settings
	public static final String MEASUREMENTENVIRONMENT_ROOTNAME 			= "root";
	public static final String MEASUREMENTENVIRONMENT_DELIMITER 		= "/";
	
	// Database configuration
	public static final String PERSISTENCE_HOST 	= "localhost";
	public static final int PERSISTENCE_PORT 		= 1527;
	public static final String PERSISTENCE_NAME 	= "sopeco-service";
	public static final String PERSISTENCE_USER	 	= "sopeco_service";
	public static final String PERSISTENCE_PASSWORD = "sopeco_service";
	
	// MEC connection configuration
	public static final int MEC_SOCKET_PORT 	= 8089;
	public static final String MEC_SOCKET_HOST 	= "127.0.0.1";
	public static final String MEC_PROTOCOL 	= "socket://";
	
	// settings for database entries
	public static final String SVC_DB_PREFIX = "SPC_SVC"; // users database name
	
	/**
	 * All the REST services are listed down here.
	 * All params with prefix SVC points to paths.
	 * All params with prefix SVCP points to parameter names.
	 */
	private static final String SVCP_TOKEN 					= "token";
	
	public static final String SVC_ACCOUNT 					= "account";
	public static final String SVC_ACCOUNT_CHECK 			= "check";
	public static final String SVC_ACCOUNT_PASSWORD 		= "password";
	public static final String SVC_ACCOUNT_CREATE 			= "create";
	public static final String SVC_ACCOUNT_TOKEN 			= "token";
	public static final String SVC_ACCOUNT_EXISTS 			= "exists";
	public static final String SVC_ACCOUNT_LOGIN 			= "login";
	public static final String SVC_ACCOUNT_LOGOUT 			= "logout";
	public static final String SVC_ACCOUNT_INFO 			= "info";
	public static final String SVC_ACCOUNT_CONNECTED 		= "connected";
	public static final String SVC_ACCOUNT_CUSTOMIZE 		= "customize";
	public static final String SVCP_ACCOUNT_NAME 			= "accountname";
	public static final String SVCP_ACCOUNT_PASSWORD 		= "password";
	public static final String SVCP_ACCOUNT_TOKEN 			= SVCP_TOKEN;
	public static final String SVCP_ACCOUNT_DATABASENAME 	= "dbname";
	public static final String SVCP_ACCOUNT_DATABASEPORT 	= "dbpw";

	public static final String SVC_INFO 					= "info";
	public static final String SVC_INFO_RUNNING 			= "running";
	
	public static final String SVC_SCENARIO 				= "scenario";
	public static final String SVC_SCENARIO_ADD 			= "add";
	public static final String SVC_SCENARIO_LIST	 		= "list";
	public static final String SVC_SCENARIO_DEFINITON 		= "definition";
	public static final String SVC_SCENARIO_DELETE 			= "delete";
	public static final String SVC_SCENARIO_ARCHIVE 		= "archive";
	public static final String SVC_SCENARIO_UPDATE 			= "update";
	public static final String SVC_SCENARIO_XML 			= "xml";
	public static final String SVC_SCENARIO_INSTANCE	 	= "instance";
	public static final String SVC_SCENARIO_INSTANCES 		= "instances";
	public static final String SVCP_SCENARIO_NAME 			= "name";
	public static final String SVCP_SCENARIO_SPECNAME	 	= "specname";
	public static final String SVCP_SCENARIO_TOKEN 			= SVCP_TOKEN;
	public static final String SVCP_SCENARIO_URL 			= "url";
	
	public static final String SVC_MEASUREMENTSPEC 				 = "measurementspecification";
	public static final String SVC_MEASUREMENTSPEC_LIST 		 = "list";
	public static final String SVC_MEASUREMENTSPEC_LISTSPECS 	 = "listspecification";
	public static final String SVC_MEASUREMENTSPEC_CREATE 		 = "create";
	public static final String SVC_MEASUREMENTSPEC_RENAME 		 = "rename";
	public static final String SVC_MEASUREMENTSPEC_NAME 		 = "measurementspecificationname";
	public static final String SVCP_MEASUREMENTSPEC_TOKEN 		 = SVCP_TOKEN;
	public static final String SVCP_MEASUREMENTSPEC_SPECNAME 	 = "specname";
	public static final String SVCP_MEASUREMENTSPEC_SCENARIONAME = "scenarioname";
	public static final String SVCP_MEASUREMENTSPEC_PARAMETERDEF = "parameterdefinition";
	public static final String SVCP_MEASUREMENTSPEC_PARAMETERVAL = "parametervalue";
	public static final String SVCP_MEASUREMENTSPEC_INITIALASSIGNMENT = "initialassignment";
	
	public static final String SVC_MEC 						= "mec";
	public static final String SVC_MEC_STATUS 				= "status";
	public static final String SVC_MEC_VALIDATE 			= "validate";
	public static final String SVC_MEC_PORTREACHABLE		= "reachable";
	public static final String SVC_MEC_LIST 				= "list";
	public static final String SVC_MEC_MED 					= "med";
	public static final String SVC_MEC_SCENARIONAME 		= "scenarioname";
	public static final String SVCP_MEC_TOKEN 				= SVCP_TOKEN;
	public static final String SVCP_MEC_URL 				= "url";
	public static final String SVCP_MEC_HOST 				= "host";
	public static final String SVCP_MEC_PORT 				= "port";
	public static final String SVCP_MEC_ID 					= "id";
	
	public static final String SVC_MED 						= "med";
	public static final String SVC_MED_SET 					= "set";
	public static final String SVC_MED_SET_MEC		 		= "mec";
	public static final String SVC_MED_SET_BLANK 			= "blank";
	public static final String SVC_MED_NAMESPACE 			= "namespace";
	public static final String SVC_MED_NAMESPACE_ADD 		= "add";
	public static final String SVC_MED_NAMESPACE_REMOVE 	= "remove";
	public static final String SVC_MED_NAMESPACE_RENAME 	= "rename";
	public static final String SVC_MED_PARAM 				= "parameter";
	public static final String SVC_MED_PARAM_ADD 			= "add";
	public static final String SVC_MED_PARAM_REMOVE 		= "remove";
	public static final String SVC_MED_PARAM_UPDATE 		= "update";
	public static final String SVC_MED_SCENARIONAME 		= "scenarioname";
	public static final String SVCP_MED_TOKEN 				= SVCP_TOKEN;
	public static final String SVCP_MED_MEC_URL 			= "url";
	public static final String SVCP_MED_NAMESPACE 			= "path"; // namespace path
	public static final String SVCP_MED_NAMESPACE_NEW 		= "newName"; // updated namespace name
	public static final String SVCP_MED_PARAM_NAME 			= "paramname";
	public static final String SVCP_MED_PARAM_NAME_NEW 		= "newparamname";
	public static final String SVCP_MED_PARAM_TYP 			= "paramtype";

	public static final String SVC_EXECUTE 					= "execution";
	public static final String SVC_EXECUTE_ESD 				= "esd";
	public static final String SVC_EXECUTE_SCHEDULE 		= "schedule";
	public static final String SVC_EXECUTE_ENABLE 			= "enable";
	public static final String SVC_EXECUTE_DISABLE 			= "disable";
	public static final String SVC_EXECUTE_ID 				= "id";
	public static final String SVC_EXECUTE_ABORT 			= "abort";
	public static final String SVC_EXECUTE_DETAILS 			= "details";
	public static final String SVC_EXECUTE_MECLOG 			= "meclog";
	public static final String SVC_EXECUTE_EXECUTE 			= "execute";
	public static final String SVC_EXECUTE_STATUS 			= "status";
	public static final String SVCP_EXECUTE_TOKEN 			= SVCP_TOKEN;
	public static final String SVCP_EXECUTE_ID 				= "id";
	public static final String SVCP_EXECUTE_EXPERIMENTSERIES = "experimentseriesname";
	public static final String SVCP_EXECUTE_KEY 			= "key";
	public static final String SVCP_EXECUTE_SCENARIONAME 	= "scenarioname";

	public static final String SVC_RESULT					= "result";
	public static final String SVC_RESULT_DATASETAGGREGATED = "datasetaggregated";
	public static final String SVCP_RESULT_ACCOUNTID 		= "accountid";
	public static final String SVCP_RESULT_DATASETID 		= "datasetid";

	public static final String SVC_ESD 							= "experimentseriesdefinition";
	public static final String SVC_ESD_SCENARIONAME 			= "scenarioname";
	public static final String SVC_ESD_MEASUREMENTSPECNAME 		= "measurementspecname";
	public static final String SVC_ESD_EXPLORATIONSTRATEGY 		= "exporationstrategy";
	public static final String SVC_ESD_TERMINATIONCONDITIONS 	= "terminationcondition";
	public static final String SVC_ESD_EXPERIMENTASSIGNMENTS	= "experimentassignments";
	public static final String SVC_ESD_PREPARATIONASSIGNMENTS 	= "preparationassignments";
	public static final String SVCP_ESD_TOKEN 					= SVCP_TOKEN;
	public static final String SVCP_ESD_EXPSERDEFNAME 			= "experimentseriesdefinitionname";
	public static final String SVCP_ESD_EXPSERDEF 				= "experimentseriesdefinition";
	public static final String SVCP_ESD_EXPLORATIONSTRATEGY 	= "exporationstrategy2";
	public static final String SVCP_ESD_TERMINATIONCONDITION 	= "terminationcondition";
	public static final String SVCP_ESD_NEWEXPSERDEFNAME 		= "newexperimentseriesdefinitionname";
	public static final String SVCP_ESD_EXPERIMENTASSIGNMENT 	= "experimentassignment";
	public static final String SVCP_ESD_PREPARATIONASSIGNMENT 	= "preparationassignment";

}
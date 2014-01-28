package org.sopeco.service.test.rest;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Test;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.service.configuration.ServiceConfiguration;
import org.sopeco.service.persistence.entities.Account;
import org.sopeco.service.persistence.entities.ExecutedExperimentDetails;
import org.sopeco.service.persistence.entities.ScheduledExperiment;
import org.sopeco.service.rest.json.CustomObjectWrapper;
import org.sopeco.service.shared.Message;
import org.sopeco.service.test.configuration.TestConfiguration;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class ExecutionServiceTest extends JerseyTest {

	public ExecutionServiceTest() {
		super();
	}
	
	@Override
	public WebAppDescriptor configure() {
		return new WebAppDescriptor.Builder(TestConfiguration.PACKAGE_NAME_REST)
				.initParam(TestConfiguration.PACKAGE_NAME_POJO, "true")
				.clientConfig(createClientConfig())
				.build();
	}

	/**
	 * Sets the client config for the client. The method adds a special {@link CustomObjectWrapper}
	 * to the normal Jackson wrapper for JSON.
	 * This method is called by {@link configure()}.
	 * 
	 * @return ClientConfig to work with JSON
	 */
	private static ClientConfig createClientConfig() {
		ClientConfig config = new DefaultClientConfig();
	    config.getClasses().add(CustomObjectWrapper.class);
	    return config;
	}

	/**
	 * Removes all scheduled scenarios for the testuser
	 */
	@After
	public void cleanUpDatabase() {
		// connect to test users account
		String accountname = TestConfiguration.TESTACCOUNTNAME;
		String password = TestConfiguration.TESTPASSWORD;
		
		Message m = resource().path(ServiceConfiguration.SVC_ACCOUNT)
							  .path(ServiceConfiguration.SVC_ACCOUNT_LOGIN)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountname)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password)
							  .get(Message.class);
		
		String token = m.getMessage();

		// clean the scheduling list for the user
		resource().path(ServiceConfiguration.SVC_EXECUTE)
				  .path(ServiceConfiguration.SVC_EXECUTE_SCHEDULE)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				  .type(MediaType.APPLICATION_JSON)
				  .delete(Boolean.class);
	}
	
	@Test
	public void testScheduleExperimentDeletion() {
		// connect to test users account
		String accountname = TestConfiguration.TESTACCOUNTNAME;
		String password = TestConfiguration.TESTPASSWORD;
		String measurmentspecificationname = "examplespecname";
		String scenarioname = "examplescenario";
		
		Message m = resource().path(ServiceConfiguration.SVC_ACCOUNT)
							  .path(ServiceConfiguration.SVC_ACCOUNT_LOGIN)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountname)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password)
							  .get(Message.class);
		
		String token = m.getMessage();
		
		Account account = resource().path(ServiceConfiguration.SVC_ACCOUNT)
								    .path(ServiceConfiguration.SVC_ACCOUNT_CONNECTED)
								    .queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, token)
								    .accept(MediaType.APPLICATION_JSON)
								    .get(Account.class);
		
		// add scenario and switch to
		ExperimentSeriesDefinition esd = new ExperimentSeriesDefinition();
		resource().path(ServiceConfiguration.SVC_SCENARIO)
				  .path(ServiceConfiguration.SVC_SCENARIO_ADD)
				  .path(scenarioname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_SPECNAME, measurmentspecificationname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				  .accept(MediaType.APPLICATION_JSON)
				  .type(MediaType.APPLICATION_JSON)
				  .post(Boolean.class, esd);

		resource().path(ServiceConfiguration.SVC_SCENARIO)
				  .path(ServiceConfiguration.SVC_SCENARIO_SWITCH)
				  .path(ServiceConfiguration.SVC_SCENARIO_SWITCH_NAME)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_NAME, scenarioname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				  .accept(MediaType.APPLICATION_JSON)
				  .put(Boolean.class);

		ScenarioDefinition sd = resource().path(ServiceConfiguration.SVC_SCENARIO)
										  .path(ServiceConfiguration.SVC_SCENARIO_CURRENT)
										  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
										  .type(MediaType.APPLICATION_JSON)
										  .get(ScenarioDefinition.class);

		assertEquals(true, sd != null); // the user must have a scenario now
		
		boolean repeating = false;
		String controllerURL = "myCustomURL";
		String label = "myScheduledExperiment";
		long accountId = account.getId();
		
		ScheduledExperiment se = new ScheduledExperiment();
		se.setScenarioDefinition(sd);
		se.setAccountId(accountId);
		se.setControllerUrl(controllerURL);
		se.setRepeating(repeating);
		se.setLabel(label);
		
		Boolean b = resource().path(ServiceConfiguration.SVC_EXECUTE)
							  .path(ServiceConfiguration.SVC_EXECUTE_SCHEDULE)
							  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
							  .accept(MediaType.APPLICATION_JSON)
							  .type(MediaType.APPLICATION_JSON)
							  .post(Boolean.class, se);

		assertEquals(true, b);
		
		b = resource().path(ServiceConfiguration.SVC_EXECUTE)
					  .path(ServiceConfiguration.SVC_EXECUTE_SCHEDULE)
					  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
					  .type(MediaType.APPLICATION_JSON)
					  .delete(Boolean.class);
		
		assertEquals(true, b);
		
		// now try to get the scheduled experiment (which must be empty)
		List<ScheduledExperiment> list = (List<ScheduledExperiment>)resource().path(ServiceConfiguration.SVC_EXECUTE)
																		      .path(ServiceConfiguration.SVC_EXECUTE_SCHEDULE)
																		      .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
																		      .accept(MediaType.APPLICATION_JSON)
																		      .get(new GenericType<List<ScheduledExperiment>>(){});
		
		assertEquals(false, list == null);
		assertEquals(true, list.isEmpty());
		
	}
	
	/**
	 * Adds a {@code ScheduledExperiment} to the scheduler. The list of scheduled experiments
	 * is then requested and checked for the added one.
	 */
	@Test
	public void testScheduleExperiment() {
		// connect to test users account
		String accountname = TestConfiguration.TESTACCOUNTNAME;
		String password = TestConfiguration.TESTPASSWORD;
		String measurmentspecificationname = "examplespecname";
		String scenarioname = "examplescenario";
		
		Message m = resource().path(ServiceConfiguration.SVC_ACCOUNT)
							  .path(ServiceConfiguration.SVC_ACCOUNT_LOGIN)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountname)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password)
							  .get(Message.class);
		
		String token = m.getMessage();

		// account is needed for account id
		Account account = resource().path(ServiceConfiguration.SVC_ACCOUNT)
								    .path(ServiceConfiguration.SVC_ACCOUNT_CONNECTED)
								    .queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, token)
								    .accept(MediaType.APPLICATION_JSON)
								    .get(Account.class);
		
		// add scenario and switch to
		ExperimentSeriesDefinition esd = new ExperimentSeriesDefinition();
		resource().path(ServiceConfiguration.SVC_SCENARIO)
				  .path(ServiceConfiguration.SVC_SCENARIO_ADD)
				  .path(scenarioname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_SPECNAME, measurmentspecificationname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				  .accept(MediaType.APPLICATION_JSON)
				  .type(MediaType.APPLICATION_JSON)
				  .post(Boolean.class, esd);

		resource().path(ServiceConfiguration.SVC_SCENARIO)
				  .path(ServiceConfiguration.SVC_SCENARIO_SWITCH)
				  .path(ServiceConfiguration.SVC_SCENARIO_SWITCH_NAME)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_NAME, scenarioname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				  .accept(MediaType.APPLICATION_JSON)
				  .put(Boolean.class);

		ScenarioDefinition sd = resource().path(ServiceConfiguration.SVC_SCENARIO)
										  .path(ServiceConfiguration.SVC_SCENARIO_CURRENT)
										  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
										  .type(MediaType.APPLICATION_JSON)
										  .get(ScenarioDefinition.class);

		assertEquals(true, sd != null); // the user must have a scenario now
		
		boolean repeating = false;
		String controllerURL = "myCustomURL";
		String label = "myScheduledExperiment";
		long accountId = account.getId();
		boolean active = false;
		
		ScheduledExperiment se = new ScheduledExperiment();
		se.setScenarioDefinition(sd);
		se.setAccountId(accountId);
		se.setControllerUrl(controllerURL);
		se.setRepeating(repeating);
		se.setLabel(label);
		se.setActive(active);
		
		Boolean b = resource().path(ServiceConfiguration.SVC_EXECUTE)
							  .path(ServiceConfiguration.SVC_EXECUTE_SCHEDULE)
							  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
							  .accept(MediaType.APPLICATION_JSON)
							  .type(MediaType.APPLICATION_JSON)
							  .post(Boolean.class, se);

		assertEquals(true, b);

		// get the id of the schedulede experiment
		long id = resource().path(ServiceConfiguration.SVC_EXECUTE)
						    .path(ServiceConfiguration.SVC_EXECUTE_ID)
						    .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
						    .accept(MediaType.APPLICATION_JSON)
						    .type(MediaType.APPLICATION_JSON)
						    .put(long.class, se);
		
		// now try to get the scheduled experiment
		List<ScheduledExperiment> list = (List<ScheduledExperiment>)resource().path(ServiceConfiguration.SVC_EXECUTE)
																		      .path(ServiceConfiguration.SVC_EXECUTE_SCHEDULE)
																		      .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
																		      .accept(MediaType.APPLICATION_JSON)
																		      .get(new GenericType<List<ScheduledExperiment>>(){});
		
		// check the list for my added scenario to execution list
		for (int i = 0; i < list.size(); i++) {
			
			ScheduledExperiment scheduledExperiment = list.get(i);
			
			// if the scenario in the list is the shortly added one, we have to check it's attributes
			if (scheduledExperiment.getId() == id) {
				
				assertEquals(repeating, scheduledExperiment.isRepeating()); // harcoded request @TODO
				assertEquals(controllerURL, scheduledExperiment.getControllerUrl());
				assertEquals(label, scheduledExperiment.getLabel());
				assertEquals(accountId, scheduledExperiment.getAccountId());
				assertEquals(active, scheduledExperiment.isActive());
				
			}
			
		}
		
	}

	
	@Test
	public void testScheduleExperimentEnablingDisabling() {
		// connect to test users account
		String accountname = TestConfiguration.TESTACCOUNTNAME;
		String password = TestConfiguration.TESTPASSWORD;
		String measurmentspecificationname = "examplespecname";
		String scenarioname = "examplescenario";
		
		Message m = resource().path(ServiceConfiguration.SVC_ACCOUNT)
							  .path(ServiceConfiguration.SVC_ACCOUNT_LOGIN)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountname)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password)
							  .get(Message.class);
		
		String token = m.getMessage();

		// account is needed for account id
		Account account = resource().path(ServiceConfiguration.SVC_ACCOUNT)
								    .path(ServiceConfiguration.SVC_ACCOUNT_CONNECTED)
								    .queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, token)
								    .accept(MediaType.APPLICATION_JSON)
								    .get(Account.class);
		
		// add scenario and switch to
		ExperimentSeriesDefinition esd = new ExperimentSeriesDefinition();
		resource().path(ServiceConfiguration.SVC_SCENARIO)
				  .path(ServiceConfiguration.SVC_SCENARIO_ADD)
				  .path(scenarioname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_SPECNAME, measurmentspecificationname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				  .accept(MediaType.APPLICATION_JSON)
				  .type(MediaType.APPLICATION_JSON)
				  .post(Boolean.class, esd);

		resource().path(ServiceConfiguration.SVC_SCENARIO)
				  .path(ServiceConfiguration.SVC_SCENARIO_SWITCH)
				  .path(ServiceConfiguration.SVC_SCENARIO_SWITCH_NAME)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_NAME, scenarioname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				  .accept(MediaType.APPLICATION_JSON)
				  .put(Boolean.class);

		ScenarioDefinition sd = resource().path(ServiceConfiguration.SVC_SCENARIO)
										  .path(ServiceConfiguration.SVC_SCENARIO_CURRENT)
										  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
										  .type(MediaType.APPLICATION_JSON)
										  .get(ScenarioDefinition.class);

		assertEquals(true, sd != null); // the user must have a scenario now
		
		boolean repeating = false;
		String controllerURL = "myCustomURL";
		String label = "myScheduledExperiment";
		long accountId = account.getId();
		boolean scenarioActive = true;
		long addedTime = System.currentTimeMillis();
		
		ScheduledExperiment se = new ScheduledExperiment();
		se.setScenarioDefinition(sd);
		se.setAccountId(accountId);
		se.setControllerUrl(controllerURL);
		se.setRepeating(repeating);
		se.setLabel(label);
		se.setActive(scenarioActive);
		se.setAddedTime(addedTime);
		
		// add to execution list
		Boolean b = resource().path(ServiceConfiguration.SVC_EXECUTE)
							  .path(ServiceConfiguration.SVC_EXECUTE_SCHEDULE)
							  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
							  .accept(MediaType.APPLICATION_JSON)
							  .type(MediaType.APPLICATION_JSON)
							  .post(Boolean.class, se);

		assertEquals(true, b);
		
		// get if for the added scenario
		long tmpID = resource().path(ServiceConfiguration.SVC_EXECUTE)
						    .path(ServiceConfiguration.SVC_EXECUTE_ID)
						    .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
						    .accept(MediaType.APPLICATION_JSON)
						    .type(MediaType.APPLICATION_JSON)
						    .put(long.class, se);
		
		String id = String.valueOf(tmpID);
		
		ScheduledExperiment se2 =  resource().path(ServiceConfiguration.SVC_EXECUTE)
										     .path(id)
										     .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
										     .accept(MediaType.APPLICATION_JSON)
										     .type(MediaType.APPLICATION_JSON)
										     .get(ScheduledExperiment.class);
		
		assertEquals(scenarioActive, se2.isActive());
		
		// now disable the scheduled experiment
		scenarioActive = false;
		
		b = resource().path(ServiceConfiguration.SVC_EXECUTE)
				      .path(id)
				      .path(ServiceConfiguration.SVC_EXECUTE_DISABLE)
				      .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				      .accept(MediaType.APPLICATION_JSON)
				      .type(MediaType.APPLICATION_JSON)
				      .put(Boolean.class);
		
		assertEquals(true, b);
		
		se2 =  resource().path(ServiceConfiguration.SVC_EXECUTE)
					     .path(id)
					     .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
					     .accept(MediaType.APPLICATION_JSON)
					     .type(MediaType.APPLICATION_JSON)
					     .get(ScheduledExperiment.class);

		assertEquals(scenarioActive, se2.isActive());
		
		// now enable the scheulded experiment again
		scenarioActive = true;
		
		b = resource().path(ServiceConfiguration.SVC_EXECUTE)
				      .path(id)
				      .path(ServiceConfiguration.SVC_EXECUTE_ENABLE)
				      .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				      .accept(MediaType.APPLICATION_JSON)
				      .type(MediaType.APPLICATION_JSON)
				      .put(Boolean.class);
		
		assertEquals(true, b);
		
		se2 =  resource().path(ServiceConfiguration.SVC_EXECUTE)
					     .path(id)
					     .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
					     .accept(MediaType.APPLICATION_JSON)
					     .type(MediaType.APPLICATION_JSON)
					     .get(ScheduledExperiment.class);

		assertEquals(scenarioActive, se2.isActive());
		
		// now delete the scheduled experiment
		b = resource().path(ServiceConfiguration.SVC_EXECUTE)
				      .path(id)
				      .path(ServiceConfiguration.SVC_EXECUTE_DELETE)
				      .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				      .accept(MediaType.APPLICATION_JSON)
				      .type(MediaType.APPLICATION_JSON)
				      .delete(Boolean.class);
		
		assertEquals(true, b);
		
		try {
			se2 =  resource().path(ServiceConfiguration.SVC_EXECUTE)
						     .path(id)
						     .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
						     .accept(MediaType.APPLICATION_JSON)
						     .type(MediaType.APPLICATION_JSON)
						     .get(ScheduledExperiment.class);
		} catch (UniformInterfaceException ex) {

            final int status = ex.getResponse().getStatus();
            assertEquals(204, status);
            
        }
		
	}
	
	
	@Test
	public void testGetExecutionDetails() {
		// connect to test users account
		String accountname = TestConfiguration.TESTACCOUNTNAME;
		String password = TestConfiguration.TESTPASSWORD;
		String measurmentspecificationname = "examplespecname";
		String scenarioname = "examplescenario";
		
		Message m = resource().path(ServiceConfiguration.SVC_ACCOUNT)
							  .path(ServiceConfiguration.SVC_ACCOUNT_LOGIN)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountname)
							  .queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password)
							  .get(Message.class);
		
		String token = m.getMessage();
		
		// add scenario and switch to
		ExperimentSeriesDefinition esd = new ExperimentSeriesDefinition();
		resource().path(ServiceConfiguration.SVC_SCENARIO)
				  .path(ServiceConfiguration.SVC_SCENARIO_ADD)
				  .path(scenarioname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_SPECNAME, measurmentspecificationname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				  .accept(MediaType.APPLICATION_JSON)
				  .type(MediaType.APPLICATION_JSON)
				  .post(Boolean.class, esd);

		resource().path(ServiceConfiguration.SVC_SCENARIO)
				  .path(ServiceConfiguration.SVC_SCENARIO_SWITCH)
				  .path(ServiceConfiguration.SVC_SCENARIO_SWITCH_NAME)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_NAME, scenarioname)
				  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
				  .accept(MediaType.APPLICATION_JSON)
				  .put(Boolean.class);

		ScenarioDefinition sd = resource().path(ServiceConfiguration.SVC_SCENARIO)
										  .path(ServiceConfiguration.SVC_SCENARIO_CURRENT)
										  .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
										  .type(MediaType.APPLICATION_JSON)
										  .get(ScenarioDefinition.class);

		assertEquals(true, sd != null); // the user must have a scenario now
		
		List<ExecutedExperimentDetails> eed = resource().path(ServiceConfiguration.SVC_EXECUTE)
													    .path(ServiceConfiguration.SVC_EXECUTE_DETAILS)
													    .queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, token)
													    .accept(MediaType.APPLICATION_JSON)
													    .type(MediaType.APPLICATION_JSON)
													    .get(new GenericType<List<ExecutedExperimentDetails>>(){});

		assertEquals(true, eed.isEmpty());
	}
	
}

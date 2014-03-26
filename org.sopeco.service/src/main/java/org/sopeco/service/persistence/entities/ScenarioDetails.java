/**
 * Copyright (c) 2013 SAP
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
package org.sopeco.service.persistence.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.sopeco.service.configuration.ServiceConfiguration;

/**
 * This class is a relict from the WebUI project. Information is just used, becasue the WebUI
 * sometimes accesses this class and the WebUI should not be changed that much.
 * 
 * @author Peter Merkert
 */
@Entity
public class ScenarioDetails implements Serializable {

	private static final long serialVersionUID = -1830811613436896069L;

	@Id
	@GeneratedValue
	private long Id;
	
	private String scenarioName 		= "";

	private String controllerProtocol 	= ServiceConfiguration.MEC_PROTOCOL;

	private String controllerHost 		= ServiceConfiguration.MEC_SOCKET_HOST;

	private int controllerPort 			= ServiceConfiguration.MEC_SOCKET_PORT;

	private String controllerName 		= "";

	private String selectedSpecification;

	private String selectedExperiment;
	
	private int experimentKey;

	/**
	 * Empty constructor for JSON converting.
	 */
	public ScenarioDetails() {
		
	}
	
	/**
	 * @return the selectedExperiment
	 */
	public String getSelectedExperiment() {
		return selectedExperiment;
	}

	/**
	 * @param pSelectedExperiment
	 *            the selectedExperiment to set
	 */
	public void setSelectedExperiment(String pSelectedExperiment) {
		this.selectedExperiment = pSelectedExperiment;
	}

	/**
	 * @return the scenarioName
	 */
	public String getScenarioName() {
		return scenarioName;
	}

	/**
	 * @param pScenarioName
	 *            the scenarioName to set
	 */
	public void setScenarioName(String pScenarioName) {
		this.scenarioName = pScenarioName;
	}

	/**
	 * @return the controllerProtocol
	 */
	public String getControllerProtocol() {
		return controllerProtocol;
	}

	/**
	 * @param pControllerProtocol
	 *            the controllerProtocol to set
	 */
	public void setControllerProtocol(String pControllerProtocol) {
		this.controllerProtocol = pControllerProtocol;
	}

	/**
	 * @return the controllerHost
	 */
	public String getControllerHost() {
		return controllerHost;
	}

	/**
	 * @param pControllerHost
	 *            the controllerHost to set
	 */
	public void setControllerHost(String pControllerHost) {
		this.controllerHost = pControllerHost;
	}

	/**
	 * @return the controllerPort
	 */
	public int getControllerPort() {
		return controllerPort;
	}

	/**
	 * @param pControllerPort
	 *            the controllerPort to set
	 */
	public void setControllerPort(int pControllerPort) {
		this.controllerPort = pControllerPort;
	}

	/**
	 * @return the controllerName
	 */
	public String getControllerName() {
		return controllerName;
	}

	/**
	 * @param pControllerName
	 *            the controllerName to set
	 */
	public void setControllerName(String pControllerName) {
		this.controllerName = pControllerName;
	}

	/**
	 * @return the selectedSpecification
	 */
	public String getSelectedSpecification() {
		return selectedSpecification;
	}

	/**
	 * @param pSelectedSpecification
	 *            the selectedSpecification to set
	 */
	public void setSelectedSpecification(String pSelectedSpecification) {
		this.selectedSpecification = pSelectedSpecification;
	}

	/**
	 * @return the experimentKey
	 */
	public int getExperimentKey() {
		return experimentKey;
	}

	/**
	 * @param experimentKey
	 *            the epxeriment key to set
	 */
	public void setExperimentKey(int experimentKey) {
		this.experimentKey = experimentKey;
	}
}

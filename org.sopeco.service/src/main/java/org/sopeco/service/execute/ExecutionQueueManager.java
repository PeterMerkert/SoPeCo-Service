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
package org.sopeco.service.execute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The <code>ExecutionQueueManager</code> handles all the {@link ExecutionQueue}s
 * of the SoPeCo Service Layer. <br />
 * Everything is accessed in a static way.
 * 
 * 
 * @author Marius Oehler
 * @author Peter Merkert
 */
public final class ExecutionQueueManager {

	/**
	 * The class is more a utility class and everything is accessed in a static way.
	 */
	private ExecutionQueueManager() {
	};

	private static Map<String, ExecutionQueue> queueMap = new HashMap<String, ExecutionQueue>();

	/**
	 * Returns the {@link ExecutionQueue} corresponding to the given URL.
	 * If the queue does not exist yet, it will be created.
	 * 
	 * @param url the URL to the controller
	 * @return the {@link ExecutionQueue} to the given URL
	 */
	public static ExecutionQueue get(String url) {
		if (!queueMap.containsKey(url)) {
			queueMap.put(url, new ExecutionQueue(url));
		}
		return queueMap.get(url);
	}

	/**
	 * Returns a list with all ExecutionQueues.
	 * 
	 * @return Collection with all queues
	 */
	public static List<ExecutionQueue> getAllQueues() {
		return new ArrayList<ExecutionQueue>(queueMap.values());
	}

	/**
	 * Returns a list with all running ExecutionQueues.
	 * 
	 * @return Collection with all queues
	 */
	public static List<ExecutionQueue> getAllRunningQueues() {
		List<ExecutionQueue> controllerList = new ArrayList<ExecutionQueue>();
		for (ExecutionQueue cq : queueMap.values()) {
			if (cq.isLoaded()) {
				controllerList.add(cq);
			}
		}
		return controllerList;
	}

}
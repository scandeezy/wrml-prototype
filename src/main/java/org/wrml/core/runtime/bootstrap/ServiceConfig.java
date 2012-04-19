/**
 * Copyright (C) 2011 WRML.org <mark@wrml.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wrml.core.runtime.bootstrap;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

public class ServiceConfig 
{	
	private List<JsonNode> apiSpecifications;
	private List<URL> remoteSpecifications;
	
	public ServiceConfig()
	{
		apiSpecifications = new ArrayList<JsonNode>();
	}
	
	public List<JsonNode> getApiSpecifications()
	{
		return apiSpecifications;
	}
	
	public void setApiSpecifications(List<JsonNode> apiSpecifications)
	{
		this.apiSpecifications = apiSpecifications;
	}
	
	public void addApiSpecification(JsonNode value)
	{
		apiSpecifications.add(value);
	}

	public List<URL> getRemoteSpecifications()
    {
	    return remoteSpecifications;
    }
	
	public void addRemoteSpecification(URL spec)
	{
		remoteSpecifications.add(spec);
	}

	public void setRemoteSpecifications(List<URL> remoteSpecifications)
    {
	    this.remoteSpecifications = remoteSpecifications;
    }

}

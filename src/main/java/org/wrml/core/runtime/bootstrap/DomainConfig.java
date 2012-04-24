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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wrml.core.service.APISpecification;

public class DomainConfig
{
	private List<APISpecification> apiSpecifications;
	private Map<String, String> domainConfig;
	
	public DomainConfig()
	{
		apiSpecifications = new ArrayList<APISpecification>();
		domainConfig = new HashMap<String, String>();
	}
	
	public void setApiSpecifications(List<APISpecification> apiSpecifications)
	{
		this.apiSpecifications = apiSpecifications;
	}
	
	public void addApiSpecification(APISpecification spec)
	{
		this.apiSpecifications.add(spec);
	}
	
	public List<APISpecification> getApiSpecifications()
	{
		return this.apiSpecifications;
	}

	/**
     * @return the domainConfig
     */
    public Map<String, String> getDomainConfig()
    {
	    return domainConfig;
    }

	/**
     * @param domainConfig the domainConfig to set
     */
    public void setDomainConfig(Map<String, String> domainConfig)
    {
	    this.domainConfig = domainConfig;
    }

}

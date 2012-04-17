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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ServiceConfig 
{
	private List<URI> apiSpecifications;
	
	public ServiceConfig()
	{
		apiSpecifications = new ArrayList<URI>();
	}
	
	public void setApiSpecifications(List<URI> apiSpecifications)
	{
		this.apiSpecifications = apiSpecifications;
	}
	
	public void addApiSpecification(URI spec)
	{
		this.apiSpecifications.add(spec);
	}
	
	public List<URI> getApiSpecifications()
	{
		return this.apiSpecifications;
	}

}

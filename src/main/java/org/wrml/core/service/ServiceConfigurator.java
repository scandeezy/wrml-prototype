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

package org.wrml.core.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.bootstrap.ServiceConfig;

public class ServiceConfigurator 
{
	private Logger log = LoggerFactory.getLogger(ServiceConfigurator.class);
	
	public static final String APIKEY = "apis";

	private static final String sep = File.separator;
	public static final String WRMLCONFIGLOCATIONTAG = "wrml.configuration.location";
	public static final String WRMLCONFIGFILE = "wrml.config";
	public static final String WRMLCONFIGLOCATIONDEFAULT = sep + "etc" + sep+ "wrml" + sep + WRMLCONFIGFILE;

	private static ServiceConfigurator INSTANCE = null;
	
	private Context context;
	
	private ServiceConfigurator()
	{

	}

	public static ServiceConfigurator getInstance()
	{
		if(INSTANCE == null)
		{
			INSTANCE = new ServiceConfigurator();
		}

		return INSTANCE;
	}
	
	private URL locateConfig(ServletConfig config) throws MalformedURLException
	{
		// Check if we were given a location to look at from the xml
		String location = config.getInitParameter(WRMLCONFIGLOCATIONTAG);
		File configFile = null;

		if(location != null)
		{
			configFile = new File(location);
			log.info("Setting config location to WRMLCONFIGLOCATION: " + WRMLCONFIGLOCATIONTAG);
		}

		if(configFile == null || !configFile.exists())
		{
			location = System.getProperty(WRMLCONFIGLOCATIONTAG);
			if(location != null)
			{
				configFile = new File(location);
				log.info("Setting config location to WRMLCONFIGLOCATIONTAG: " + WRMLCONFIGLOCATIONTAG);
			}
		}

		if(configFile == null || !configFile.exists())
		{
			URL resource = null;
			try
			{
				resource = config.getServletContext().getResource("/WEB-INF/" + WRMLCONFIGFILE);
				if (resource == null)
				{
					resource = config.getServletContext().getResource(WRMLCONFIGFILE);
				}
			} catch (MalformedURLException e)
			{
				log.error("Couldn't parse URL", e);
			}

			if(resource != null)
			{
				log.info("Setting config location to WRMLCONFIGLOCSTIONCLASSPATH: " + WRMLCONFIGFILE);
				
				return resource;
			}
		}

		if(configFile == null || !configFile.exists())
		{
			configFile = new File(WRMLCONFIGLOCATIONDEFAULT);
			log.info("Setting config location to WRMLCONFIGLOCATIONDEFAULT: " + WRMLCONFIGLOCATIONDEFAULT);
		}

		return configFile.toURI().toURL();
	}

	private List<Service> pullInConfig(ServletConfig servletConfig, URL wrmlConfig)
	{
		List<Service> services = new ArrayList<Service>();
		ObjectMapper mapper = new ObjectMapper();

		//BufferedReader in = new BufferedReader(new InputStreamReader(config.openStream()));

		ServiceConfig svcConfig;

		try 
		{
			svcConfig = mapper.readValue(wrmlConfig, ServiceConfig.class);
		}
		catch (IOException e)
		{
			log.error("Unable to read in base service config:" + e);
			e.printStackTrace();

			return null;
		}

		for(URL api : svcConfig.getRemoteSpecifications())
		{
			log.info("Loading api from " + api);

			try
			{
				JsonNode apiConfig = mapper.readValue(api.openStream(), JsonNode.class);
				log.info("loaded remote API specification: " + mapper.writeValueAsString(apiConfig));
				// TODO something
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try
		{
			log.info("CONFIG VALUES: " + mapper.writeValueAsString(svcConfig));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO: Change this mofo
		// Do something to init context
		this.context = new Context(null);

		return services;
	}
	
	public Context getContext()
	{
		return this.context;
	}
	
	public void init(ServletConfig config) throws IOException
	{
		// Check if we were given a location to look at from the xml
		URL configFile = locateConfig(config);

		pullInConfig(config, configFile);
	}

	private void loadAPIs(JsonNode apis)
	{
		Iterator<Entry<String,JsonNode>> apiIterator = apis.getFields();

		// Pull out each config and construct the corresponding API
		while(apiIterator.hasNext())
		{
			Entry<String, JsonNode> apiEntry = apiIterator.next();
		}
	}
}

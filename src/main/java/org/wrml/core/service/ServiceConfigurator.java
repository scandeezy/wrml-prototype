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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.wrml.core.runtime.bootstrap.ServiceConfig;
import org.wrml.core.service.handler.RequestHandler;

public class ServiceConfigurator 
{
	private Logger log = Logger.getLogger(this.getClass().getName());

	public static final String APIKEY = "apis";

	private static final String sep = File.separator;
	public static final String WRMLCONFIGLOCATIONTAG = "wrml.configuration.location";
	public static final String WRMLCONFIGFILE = "wrml.config";
	public static final String WRMLCONFIGLOCATIONDEFAULT = sep + "etc" + sep+ "wrml" + sep + WRMLCONFIGFILE;

	private static ServiceConfigurator INSTANCE = null;

	private ServiceMap serviceMap;

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

	public ServiceMap getServiceMap()
	{
		return serviceMap;
	}

	/*private URL locateConfigURL(ServletConfig servletConfig)
	{
		// Check if we were given a location to look at from the xml
		String location = servletConfig.getInitParameter(WRMLCONFIGLOCATIONTAG);
		URL config = null;
		
		if (null != location)
		{
//			config = new URL(location);
			log.info("Setting config location to WRMLCONFIGLOCATION: " + WRMLCONFIGLOCATIONTAG);

		}
		
		
		URL resource = servletConfig.getServletContext().getResource("/WEB-INF/wrml.config");
		java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(resource.openStream()));

		ObjectMapper mapper = new ObjectMapper();
		ServiceConfig svcConfig = mapper.readValue(in, ServiceConfig.class);
		System.out.println(mapper.writeValueAsString(svcConfig));
		
	}*/
	
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(resource != null)
			{
//				log.info("JCLIFFE: " + resource.getPath());

				// configFile = new File(resource.getFile());

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

	private void pullInConfig(File configFile)
	{
		ObjectMapper mapper = new ObjectMapper();

		try 
		{
			ServiceConfig config = mapper.readValue(configFile, ServiceConfig.class);
			log.info("CONFIG VALUES: " + mapper.writeValueAsString(config));
		} 
		catch (JsonParseException e) 
		{
			log.warning("Unable to read in base service config:" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			RequestHandler.TEST = "fail1 " + e.toString();
		} 
		catch (JsonMappingException e) 
		{
			log.warning("Unable to read in base service config:" + e);

			// TODO Auto-generated catch block
			e.printStackTrace();
			RequestHandler.TEST = "fail2 " + e.toString();
		} 
		catch (IOException e) 
		{
			log.warning("Unable to read in base service config:" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			RequestHandler.TEST = "fail3 " + e.toString();
		}
	}

	private void pullInConfig(URL config) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		
		//BufferedReader in = new BufferedReader(new InputStreamReader(config.openStream()));

		try 
		{
			ServiceConfig svcConfig = mapper.readValue(config, ServiceConfig.class);
			log.info("CONFIG VALUES: " + mapper.writeValueAsString(svcConfig));
		} 
		catch (JsonParseException e) 
		{
			log.warning("Unable to read in base service config:" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			RequestHandler.TEST = "fail1 " + e.toString();
		} 
		catch (JsonMappingException e) 
		{
			log.warning("Unable to read in base service config:" + e);

			// TODO Auto-generated catch block
			e.printStackTrace();
			RequestHandler.TEST = "fail2 " + e.toString();
		} 
		catch (IOException e) 
		{
			log.warning("Unable to read in base service config:" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			RequestHandler.TEST = "fail3 " + e.toString();
		}
	}
	
	public void init(ServletConfig config) throws IOException
	{
		// Check if we were given a location to look at from the xml
		URL configFile = locateConfig(config);

		pullInConfig(configFile);
		
/*		if(configFile.exists())
		{
			serviceMap = null;

			pullInConfig(configFile);

		}
		else
		{
			//boo?
			RequestHandler.TEST = "fail4";
		}*/
	}

	private void loadAPIs(JsonNode apis)
	{
		Iterator<Entry<String,JsonNode>> apiIterator = apis.getFields();

		// Pull out each config and construct the corresponding API
		while(apiIterator.hasNext())
		{
			Entry<String, JsonNode> apiEntry = apiIterator.next();
			RequestHandler.TEST += apiEntry.getKey() + " : " + apiEntry.getValue().asText() + " ";

		}
	}
}

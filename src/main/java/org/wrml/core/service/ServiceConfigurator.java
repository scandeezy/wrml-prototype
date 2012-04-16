package org.wrml.core.service;

import java.io.File;
import java.io.IOException;
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
	public static final String APIKEY = "apis";
	
	public static final String WRMLCONFIGLOCATIONTAG = "wrml.configuration.location";
	public static final String WRMLCONFIGLOCSTIONCLASSPATH = "classpath://wrml.config";
	public static final String WRMLCONFIGLOCATIONDEFAULT = "/etc/wrml/config";
	
	private static ServiceConfigurator INSTANCE = null;
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
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
	
	private File locateConfig(ServletConfig config)
	{
		// Check if we were given a location to look at from the xml
		String location = config.getInitParameter(WRMLCONFIGLOCATIONTAG);
		File configFile = null;
		
		if(location != null)
			configFile = new File(location);
		else
			log.info("config file location is null");
		
		if(configFile == null || !configFile.exists())
		{
			location = System.getProperty(WRMLCONFIGLOCATIONTAG);
			if(location != null)
				configFile = new File(location);
		}
		else
		{
			log.info("config file does not exist");
		}
		
		if(configFile == null || !configFile.exists())
			configFile = new File(WRMLCONFIGLOCSTIONCLASSPATH);
			
		if(!configFile.exists())
			configFile = new File(WRMLCONFIGLOCATIONDEFAULT);
		
		return configFile;
	}
	
	private void pullInConfig(File configFile)
	{
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			ServiceConfig config = mapper.readValue(configFile, ServiceConfig.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			RequestHandler.TEST = "fail1 " + e.toString();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			RequestHandler.TEST = "fail2 " + e.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			RequestHandler.TEST = "fail3 " + e.toString();
		}
	}
	
	public void init(ServletConfig config)
	{
		// Check if we were given a location to look at from the xml
		File configFile = locateConfig(config);
		
		if(configFile.exists())
		{
			serviceMap = null;

			pullInConfig(configFile);
			
			/*
 			//load up
			ObjectMapper mapper = new ObjectMapper();
			try
			{
				JsonNode rootNode = mapper.readValue(configFile,JsonNode.class);
				Iterator<Entry<String, JsonNode>> iter = rootNode.getFields();
				RequestHandler.TEST = "configed: ";
				while(iter.hasNext())
				{
					Entry<String, JsonNode> entry = iter.next();
					RequestHandler.TEST = RequestHandler.TEST + entry.getKey() + ", " + entry.getValue().getTextValue() + " ";
				}
				
				JsonNode apis = rootNode.get(APIKEY);
				loadAPIs(apis);
			} 
			catch (JsonParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				RequestHandler.TEST = "fail1 " + e.toString();
			}
			catch (JsonMappingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				RequestHandler.TEST = "fail2";
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				RequestHandler.TEST = "fail3";
			}
			*/
		}
		else
		{
			//boo?
			RequestHandler.TEST = "fail4";
		}
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

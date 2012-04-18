package org.wrml.core.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.bootstrap.ServiceConfig;
//import org.wrml.core.service.handler.RequestHandler;

public class ServiceConfigurator 
{
	private Logger log = LoggerFactory.getLogger(ServiceConfigurator.class);
	
	public static final String APIKEY = "apis";
	
	public static final String WRMLCONFIGLOCATIONTAG = "wrml.configuration.location";
	public static final String WRMLCONFIGFILE = "wrml.config";
	public static final String WRMLCONFIGLOCATIONDEFAULT = "/etc/wrml/" + WRMLCONFIGFILE;
	
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
	
	private File locateConfig(ServletConfig config)
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
			URL resource = Thread.currentThread().getContextClassLoader().getResource(WRMLCONFIGFILE);
			if(resource != null)
			{
				configFile = new File(resource.getFile());
						
				log.info("Setting config location to WRMLCONFIGLOCSTIONCLASSPATH: " + WRMLCONFIGFILE);
			}
		}
			
		if(configFile == null || !configFile.exists())
		{
			configFile = new File(WRMLCONFIGLOCATIONDEFAULT);
			log.info("Setting config location to WRMLCONFIGLOCATIONDEFAULT: " + WRMLCONFIGLOCATIONDEFAULT);
		}
		
		return configFile;
	}
	
	private List<Service> pullInConfig(File configFile)
	{
		List<Service> services = new ArrayList<Service>();
		ObjectMapper mapper = new ObjectMapper();
		
		try 
		{
			ServiceConfig config = mapper.readValue(configFile, ServiceConfig.class);
			
			for(URI api : config.getApiSpecifications())
			{
				log.info("Loading api from " + api);
				
				// Check if this is a file on disk
				File apiFile = new File(api.toString());
				if(apiFile.exists())
				{
					log.info("Pulling in api config from local disk...");
					JsonNode apiConfig = mapper.readValue(apiFile, JsonNode.class);
					// Do Magic with this thing until we figure out what it needs to be
//					services.add(arg0);
				}
				else // Grab the reference to something probably over the 'net
				{
					URL url = api.toURL();
					// Do Magic with this thing until we figure out what it needs to be
					JsonNode apiConfig = mapper.readValue(url, JsonNode.class);
//					services.add(arg0);
				}
			}
		} 
		catch (JsonParseException e) 
		{
			log.error("Unable to read in base service config", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JsonMappingException e) 
		{
			log.error("Unable to read in base service config", e);
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			log.error("Unable to read in base service config", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return services;
	}
	
	public Context getContext()
	{
		return this.context;
	}
	
	public void init(ServletConfig config)
	{
		// Check if we were given a location to look at from the xml
		File configFile = locateConfig(config);
		
		if(configFile.exists())
		{
			List<Service> services = pullInConfig(configFile);
			this.context = new Context(null);
			log.info("Context initialized");
		}
		else
		{
			//boo?
			log.error("Unexpected case met, config file does not exist");
		}
	}
}

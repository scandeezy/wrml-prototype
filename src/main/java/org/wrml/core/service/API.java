package org.wrml.core.service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class API {
	private static Logger log = LoggerFactory.getLogger(API.class);
	
	private String name;
	// URI Path --> List of Schemas (1:Many)
	private Map<URI,List<URI>> pathToSchemasMap;
	// Schema --> Service (1 : 1)
	private Map<URI, String> schemaToServiceMap;
	// Service --> Jar Download locations (1:Many)
	private Map<String, List<URI>> serviceJars;

	public API()
	{
		this.pathToSchemasMap = new HashMap<URI, List<URI>>();
		this.schemaToServiceMap = new HashMap<URI, String>();
		this.serviceJars = new HashMap<String, List<URI>>();
		this.name = "unknown";
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Map<URI,List<URI>> getPathMap()
	{
		return pathToSchemasMap;
	}
	
	public void setPathMap(Map<URI,List<URI>> pathToSchemasMap)
	{
		this.pathToSchemasMap = pathToSchemasMap;
	}
	
	public void addPathSchema(URI path, URI schema)
	{
		if(!this.pathToSchemasMap.containsKey(path))
		{
			this.pathToSchemasMap.put(path, new ArrayList<URI>());
		}
		if(!this.pathToSchemasMap.get(path).contains(schema))
			this.pathToSchemasMap.get(path).add(schema);
	}
	
	public Map<URI,String> getSchemaMap()
	{
		return this.schemaToServiceMap;
	}
	
	public void setSchemaMap(Map<URI,String> schemaToServiceMap)
	{
		this.schemaToServiceMap = schemaToServiceMap;
	}
	
	public void setSchemaService(URI schema, String Service)
	{
		this.schemaToServiceMap.put(schema, Service);
	}
	
	public Map<String, List<URI>> getServiceJars()
	{
		return this.serviceJars;
	}
	
	public void setServiceJars(Map<String, List<URI>> serviceJars)
	{
		this.serviceJars = serviceJars;
	}
	
	public void setService(String service, List<URI> serviceJars)
	{
		this.serviceJars.put(service, serviceJars);
	}
	
	public void addServiceJar(String service, URI serviceJar)
	{
		if(!this.serviceJars.containsKey(service))
		{
			this.serviceJars.put(service, new ArrayList<URI>());
		}
		if(!this.serviceJars.get(service).contains(serviceJar))
			this.serviceJars.get(service).add(serviceJar);
	}
	
	@JsonCreator
	public static API Create(String jsonString)
	{

		API pc = null;
    	ObjectMapper mapper = new ObjectMapper();
        try 
        {
			pc = mapper.readValue(jsonString, API.class);
		}
        catch (Exception e) 
		{
			log.error("Unable to parse API object\n" + jsonString,e);
		}

	    return pc;
	}
}

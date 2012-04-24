package org.wrml.core.service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APISpecification {
	private static Logger log = LoggerFactory.getLogger(APISpecification.class);
	
	// provides scope, at least; better debug messages
	private String name;
	
	// URL endpoints (when combined with domain)
	// may accept 0+ schemas and route them to different services
	private Map< String, List< Endpoint > >endpoints;
	
	// maps service names to URLs
	private Map<String, URL> services;
	
	private String adminEmail;
	
	private List<String> protocols;

	public APISpecification()
	{
		name = "";
		setEndpoints(new HashMap<String, List<Endpoint>>());
		setServices(new HashMap<String, URL>());
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
		
	/**
     * @return the endpoints
     */
    public Map<String, List<Endpoint>> getEndpoints()
    {
	    return endpoints;
    }

	/**
     * @param endpoints the endpoints to set
     */
    public void setEndpoints(Map<String, List<Endpoint>> endpoints)
    {
	    this.endpoints = endpoints;
    }

	/**
     * @return the services
     */
    public Map<String, URL> getServices()
    {
	    return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(Map< String, URL> services)
    {
	    this.services = services;
    }


	/**
     * @return the adminEmail
     */
    public String getAdminEmail()
    {
	    return adminEmail;
    }

	/**
     * @param adminEmail the adminEmail to set
     */
    public void setAdminEmail(String adminEmail)
    {
	    this.adminEmail = adminEmail;
    }

	/**
     * @return the protocols
     */
    public List<String> getProtocols()
    {
	    return protocols;
    }

	/**
     * @param protocols the protocols to set
     */
    public void setProtocols(List<String> protocols)
    {
	    this.protocols = protocols;
    }

	@JsonCreator
	public static APISpecification Create(String jsonString)
	{

		APISpecification pc = null;
    	ObjectMapper mapper = new ObjectMapper();
        try 
        {
			pc = mapper.readValue(jsonString, APISpecification.class);
		}
        catch (Exception e) 
		{
			log.error("Unable to parse API object\n" + jsonString,e);
		}

	    return pc;
	}
}

package org.wrml.core.service;

import java.net.URL;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
//import java.util.Map;

public class Endpoint {
	private URL schemaURL;
	private String serviceName;
	private String schemaHash;
	private List<String> protocols;
	//private Map<String, String> config;
	
	/**
     * @return the schemaURL
     */
	@JsonProperty("schema")
    public URL getSchemaURL()
    {
        return schemaURL;
    }

	/**
     * @param schemaURL the schemaURL to set
     */
	@JsonProperty("schema")
	public void setSchemaURL(URL schemaURL)
    {
        this.schemaURL = schemaURL;
    }
    
	/**
     * @return the serviceName
     */
	@JsonProperty("service")
	public String getServiceName()
    {
        return serviceName;
    }

	/**
     * @param serviceName the serviceName to set
     */
	@JsonProperty("service")
	public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

	/**
     * @return the schemaHash
     */
    public String getSchemaHash()
    {
        return schemaHash;
    }

	/**
     * @param schemaHash the schemaHash to set
     */
    public void setSchemaHash(String schemaHash)
    {
        this.schemaHash = schemaHash;
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
    
    /**
     * Does not guarantee that the service or schema exists, or hashes properly
     * just weeds out invalid endpoints due to lack of information.
     * @return
     */
    public boolean isValid() 
    {
    	if (serviceName == null || serviceName.isEmpty() || schemaURL == null)
    	{
    		return false;
    	}
    	else
    		return true;
    }

}

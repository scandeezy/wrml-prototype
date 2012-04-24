package org.wrml.core.runtime.bootstrap;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class WRMLConfig
{
	private Map<String, DomainConfig> domains;
	
	private Map<String, String>	wrmlConfig;

	/**
     * @return the domains
     */
    public Map<String, DomainConfig> getDomains()
    {
	    return domains;
    }

	/**
     * @param domains the domains to set
     */
    public void setDomains(Map<String, DomainConfig> domains)
    {
	    this.domains = domains;
    }

	/**
     * @return the wrmlConfig
     */
    @JsonProperty("globalWRMLConfig")
    public Map<String, String> getWrmlConfig()
    {
	    return wrmlConfig;
    }

	/**
     * @param wrmlConfig the wrmlConfig to set
     */
    @JsonProperty("globalWRMLConfig")
    public void setWrmlConfig(Map<String, String> wrmlConfig)
    {
	    this.wrmlConfig = wrmlConfig;
    }
}

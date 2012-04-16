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

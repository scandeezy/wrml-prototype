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

package org.wrml.core.service.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.bootstrap.DomainConfig;
import org.wrml.core.service.APISpecification;
import org.wrml.core.service.AggregatorService;
import org.wrml.core.service.Service;
import org.wrml.core.service.ServiceConfigurator;
import org.wrml.core.service.ServiceMap;

public class RequestHandler extends HttpServlet
{
	private Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	public static String TEST = "it worked!!!!";
	
	private ObjectMapper mapper;
	private AggregatorService serviceMap;
	private Context context;
	
	public void init(ServletConfig config) throws ServletException
	{
	    super.init(config);
	    
	    log.info("Initializing RequestHandler");
	    
	    mapper = new ObjectMapper();
	    
	    // Build the special context
	    // Temp to spit out example config
	    
	    ServiceConfigurator sc = ServiceConfigurator.getInstance();
	    try
        {
	        sc.init(config);
        } 
	    catch (IOException e)
        {
	        log.error("Unable to initialize the Service Configurator", e);
        }
	    
	    this.context = sc.getContext();
	    
	    
	    if(this.context == null)
	    	log.error("Context is null");
	    
//	    serviceMap = new AggregatorService(context);
	}
	
	public Map<String,String> parseMediaType(String mediaType)
	{
		Map<String,String> typeMap = new HashMap<String,String>(5);
		int paramIndex = mediaType.indexOf(";");
		if(paramIndex > -1)
		{
			String[] types = mediaType.split("=");
			typeMap.put("media-type", types[0]);
			
			for(String concat : types)
			{
				// ignore things that don't have key/value pairs
				if(concat.contains("="))
				{
					String[] type = concat.split("=");
					typeMap.put(type[0], type[1]);
				}
			}
		}
		else // There were no extra parameters given in the header
		{
			typeMap.put("media-type", mediaType);
		}
		
		return typeMap;
	}
	
	private void badRequest(String reason, HttpServletResponse resp)
	{
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		PrintWriter out;
		try {
			out = resp.getWriter();
			mapper.writeValue(out, reason);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void mapRequest(String method, String uri, Map<String,String> mediaTypes, HttpServletResponse resp)
	{
		log.debug("Mapping new Request with method");
		// Go off of Schema
		if(mediaTypes.containsKey("schema"))
		{
			URI schema;
			try
			{
				schema = new URI(mediaTypes.get("schema"));
				Service service = context.getService(schema);
				PrintWriter out = resp.getWriter();
				out.println("Found service by schema to map to");
			} 
			catch (URISyntaxException e) 
			{
				log.error("Uri syntax for a map request is incorrect", e);
			}
//			Object obj = null;
//			switch (method) 
//			{
//				case "GET" : obj = service.get(uri); break;
//				default : break;
//			}
			catch (IOException e)
			{
				log.error("IO exception while mapping request", e);
			}
		}
		else // Map to the uri default handler
		{
			Service service = context.getService(uri);
			PrintWriter out;
			
			try 
			{
				out = resp.getWriter();
				out.println("Found service by URI to map to");
			} 
			catch (IOException e) 
			{
				log.error("IO exception while mapping request", e);
			}
		}
	}
	
	protected void service(HttpServletRequest req, HttpServletResponse resp)
	{
		log.debug("Servicing new request");
		String method = req.getMethod();
		String uri = req.getRequestURI();
		
		String media = req.getHeader("media-type");
		Map<String,String> mediaTypes = (media == null) ? new HashMap<String,String>() : parseMediaType(media);
		
		// Map the request to a service
		mapRequest(method, uri, mediaTypes, resp);
		
		TEST = TEST + " Meow";
		
		// We do what we want
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/html");
//		resp.setContentType("application/json");
		
		/*
		ServiceConfig conf = new ServiceConfig();
		List<API> apiSpecifications = new ArrayList<API>();
		API example = new API();
		example.setName("Boris");
		try {
			URI mooseSchema = new URI("http://www.wrml.org/examples/moose");
			URI squirrelSchema = new URI("http://www.wrml.org/examples/squirrel");
			String service1 = "org.wrml.example.exampleService1";
			String service2 = "org.wrml.example.exampleService2";
			
			example.addPathSchema(new URI("/"), mooseSchema);
			example.addPathSchema(new URI("/squirrel"), squirrelSchema);
			
			example.setSchemaService(mooseSchema, service1);
			example.setSchemaService(squirrelSchema, service2);
			
			URI jarLocation = new URI("http://www.wrml.org/dependencies/service.jar");
			List<URI> jarLocations = new ArrayList<URI>();
			jarLocations.add(jarLocation);
			
			example.setService(service1, jarLocations);
			example.setService(service2, jarLocations);
		} catch (URISyntaxException e) {
			log.error("Unable to do my URI thing", e);
		}
		apiSpecifications.add(example);
		conf.setApiSpecifications(apiSpecifications);
		
		PrintWriter out;
		try {
			out = resp.getWriter();
			
			mapper.writeValue(out, conf);
		} catch (IOException e) {
			log.error("Damnit", e);
		}
		*/
		
//		try
//		{
//			PrintWriter out = resp.getWriter();
//
//			if (serviceMap == null || serviceMap.keySet().isEmpty())
//			{
////				mapper.writeValue(out, "serviceMap has an empty keyset");
//				out.println("serviceMap has an empty keyset<br/>");
//				log.info(serviceMap==null?"serviceMap is null":"serviceMap is empty");
//			}
//			else
//			{
//				for (URI key : serviceMap.keySet())
//
//				{
////					mapper.writeValue(out, key + " : " + serviceMap.get(key));
//					out.println(key + " : " + serviceMap.get(key) + "<br/>");
//				}
//			}
//
//			//mapper.writeValue(out, TEST);
//			out.println(TEST);
//			log.info("JCLIFFE: " + TEST);
//			
//		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}

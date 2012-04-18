package org.wrml.core.service.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
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
import org.wrml.core.service.Service;
import org.wrml.core.service.ServiceConfigurator;
import org.wrml.core.service.ServiceMap;

public class RequestHandler extends HttpServlet
{
	private Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	public static String TEST = "it worked!!!!";
	
	private ObjectMapper mapper;
	private Context context;
	
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    
	    log.info("Initializing RequestHandler");
	    
	    mapper = new ObjectMapper();
	    
	    // Build the special context
	    ServiceConfigurator sc = ServiceConfigurator.getInstance();
	    sc.init(config);
	    
	    this.context = sc.getContext();
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
			} 
			catch (URISyntaxException e) 
			{
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Object obj = null;
//			switch (method) 
//			{
//				case "GET" : obj = service.get(uri); break;
//				default : break;
//			}
		}
		else // Map to the uri default handler
		{
			Service service = context.getService(uri);
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
		//resp.setContentType("text/plain");
		resp.setContentType("application/json");
		
		try
		{
			PrintWriter out = resp.getWriter();
			mapper.writeValue(out, TEST);
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

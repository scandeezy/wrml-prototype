package org.wrml.core.service.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.bootstrap.BootstrapConfig;
import org.wrml.core.service.ServiceConfigurator;
import org.wrml.core.service.ServiceMap;

public class RequestHandler extends HttpServlet
{
	public static String TEST = "it worked!!!!";
	
	private ServiceMap serviceMap = null;
	private ObjectMapper mapper;
	
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    
	    mapper = new ObjectMapper();
	    
	    // Build the special context
	    ServiceConfigurator sc = ServiceConfigurator.getInstance();
	    sc.init(config);
	    
	    serviceMap = sc.getServiceMap();
	    
//	    String initial = config.getInitParameter("initial");
//	    try 
//	    {
//	    	TEST = initial;
//	    }
//	    catch (NumberFormatException e)
//	    {
//	    	TEST = "didn't work";
//	    }
	}
	
	protected void service(HttpServletRequest req, HttpServletResponse resp)
	{
		String method = req.getMethod();
		String uri = req.getRequestURI();
		
		TEST = TEST + " Meow";
		
//		if(serviceMap != null)
//		{
//			TEST = "Service Map Created!";
//		}
//		else
//		{
//			TEST = "No Service Map";
//		}
		
		
		// Boo hahahahahaha
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

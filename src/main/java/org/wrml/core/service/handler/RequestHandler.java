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
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.wrml.core.runtime.Context;
import org.wrml.core.service.ServiceConfigurator;
import org.wrml.core.service.ServiceMap;

public class RequestHandler extends HttpServlet
{
	public static String TEST = "it worked!!!!";
	
	private ServiceMap serviceMap = null;
	private ObjectMapper mapper;
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
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
		
		// We do what we want
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/html");
//		resp.setContentType("application/json");

		try
		{
			PrintWriter out = resp.getWriter();

			if (serviceMap == null || serviceMap.keySet().isEmpty())
			{
//				mapper.writeValue(out, "serviceMap has an empty keyset");
				out.println("serviceMap has an empty keyset<br/>");
				log.info(serviceMap==null?"serviceMap is null":"serviceMap is empty");
			}
			else
			{
				for (URI key : serviceMap.keySet())

				{
//					mapper.writeValue(out, key + " : " + serviceMap.get(key));
					out.println(key + " : " + serviceMap.get(key) + "<br/>");
				}
			}

			//mapper.writeValue(out, TEST);
			out.println(TEST);
			log.info("JCLIFFE: " + TEST);
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

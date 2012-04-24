package org.wrml.core.service.defaults;

import java.net.URI;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wrml.core.Model;
import org.wrml.core.runtime.Context;
import org.wrml.core.service.Service;
import org.wrml.core.transformer.Transformer;
import org.wrml.core.www.MediaType;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoService<T> implements Service
{
	private Logger log = LoggerFactory.getLogger(MongoService.class);
	
	private Context context;
	private DBCollection collection;
	
	public MongoService() throws UnknownHostException
	{
		try 
		{
			Mongo mongo = new Mongo("localhost");
			// Get the handle to the database
			DB db = mongo.getDB("default");
			// Authenticate against the database
			db.authenticate("username", "password".toCharArray());
			
			collection = db.getCollection("moose");
		} 
		catch (UnknownHostException e) 
		{
			log.error("Unable to bind to mongo db", e);
			throw e;
		} 
		catch (MongoException e) 
		{
			log.error("Unable to bind to mongo db", e);
			throw e;
		}
	}
	

	public Context getContext() 
	{
		return this.context;
	}

	public void clear() 
	{
		collection.drop();
	}

	public boolean containsKey(Object arg0) 
	{
		BasicDBObject query = new BasicDBObject();
		// TODO Build query
		DBCursor cursor = collection.find(query);
		
		return cursor.count() > 0;
	}

	public boolean containsValue(Object arg0) 
	{
		BasicDBObject query = new BasicDBObject();
		// TODO Build query
		DBCursor cursor = collection.find(query);
		
		return cursor.count() > 0;
	}

	public Set<java.util.Map.Entry<URI, Object>> entrySet() 
	{
		return null;
	}

	public Object get(Object arg0) 
	{
		BasicDBObject query = new BasicDBObject();
		// Build query
		DBCursor cursor = collection.find(query);
		
		return cursor.count() > 0;
	}

	public boolean isEmpty() 
	{
		return collection.getCount() == 0L;
	}

	public Set<URI> keySet() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object put(URI arg0, Object arg1) 
	{
		BasicDBObject obj = new BasicDBObject();
		// TODO build the obj
		
		collection.insert(obj);
		return arg1;
	}

	public void putAll(Map<? extends URI, ? extends Object> arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	public Object remove(Object arg0) 
	{
		BasicDBObject query = new BasicDBObject();
		// Build query
		Object obj = collection.find(query);
		
		collection.remove(query);
		
		return obj;
	}

	public int size() 
	{
		return (int)collection.getCount();
	}

	public Collection<Object> values() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object create(URI collectionId, Object requestEntity,
			MediaType responseType, Model referrer) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(URI resourceId, Object cachedEntity,
			MediaType responseType, Model referrer) {
		// TODO Auto-generated method stub
		return null;
	}

	public Transformer<URI, ?> getIdTransformer() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object put(URI resourceId, Object requestEntity,
			MediaType responseType, Model referrer) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object remove(URI resourceId, MediaType responseType, Model referrer) 
	{
		// TODO Auto-generated method stub
		return null;
	}

}

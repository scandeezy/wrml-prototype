package org.wrml.core.runtime.bootstrap;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.wrml.core.Hyperlink;
import org.wrml.core.Model;
import org.wrml.core.model.Document;
import org.wrml.core.model.DocumentMetadata;
import org.wrml.core.model.DocumentOptions;
import org.wrml.core.model.api.ResourceTemplate;
import org.wrml.core.model.config.Config;
import org.wrml.core.model.schema.Schema;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.ModelGraph;
import org.wrml.core.runtime.event.FieldEventListener;
import org.wrml.core.runtime.event.ModelEventListener;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.www.MediaType;

public class ServiceConfig implements Config {
	private Context context;
	private String etag;
	private Boolean readOnly;
	private Document parent;
	private Long version;
	private Long secondsToLive;
	private Map<String,Object> fields = new HashMap<String,Object>();
	

	public void absorb(Model modelToAbsorb, Model... additionalModelsToAbsorb) {
		// TODO Auto-generated method stub
		
	}

	public boolean addEventListener(ModelEventListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addFieldEventListener(String fieldName,
			FieldEventListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object clickLink(URI rel, Type nativeReturnType,
			Object requestEntity, Map<String, String> hrefParams) {
		// TODO Auto-generated method stub
		return null;
	}

	public void extend(Model modelToExtend, Model... additionalModelsToExtend) {
		// TODO Auto-generated method stub
		
	}

	public void free() {
		// TODO Auto-generated method stub
		
	}

	public Context getContext() {
		return context;
	}

	public Model getDynamicInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getFieldValue(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ObservableMap<URI, Hyperlink> getHyperLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	public MediaType getMediaType() {
		// TODO Auto-generated method stub
		return null;
	}

	public ModelGraph getModelGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	public Type getNativeType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Type[] getNativeTypeParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourceTemplate getResourceTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	public URI getResourceTemplateId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Schema getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	public URI getSchemaId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Model getStaticInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean removeEventListener(ModelEventListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeFieldEventListener(String fieldName,
			FieldEventListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAllFieldsToDefaultValue() {
		// TODO Auto-generated method stub
		
	}

	public void setFieldToDefaultValue(String fieldName) {
		// TODO Auto-generated method stub
		
	}

	public Object setFieldValue(String fieldName, Object fieldValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete() {
		// TODO Auto-generated method stub
		
	}

	public String getEtag() {
		return this.etag;
	}

	public URI getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public DocumentMetadata getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	public DocumentOptions getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	public Document getParent() {
		return parent;
	}

	public Long getSecondsToLive() {
		return this.secondsToLive;
	}

	public Document getSelf() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public String setEtag(String etag) {
		this.etag = etag;
		return this.etag;
	}

	public URI setId(URI id) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		return this.readOnly;
	}

	public Long setSecondsToLive(Long secondsToLive) {
		this.secondsToLive = secondsToLive;
		return this.secondsToLive;
	}

	public Document update() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getVersion() {
		return version;
	}

}

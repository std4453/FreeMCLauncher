package com.std4453.freemclauncher.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StructuredDataObject extends StructuredData {
	protected Map<String, Object> children;

	public StructuredDataObject() {
		this.children = new HashMap<String, Object>();
	}

	public Object getChild(String name) {
		return this.children.get(name);
	}

	public String getString(String name) {
		Object get = getChild(name);
		if (get == null)
			return null;
		if (get instanceof String)
			return (String) get;
		else
			throw new RuntimeException("Child with name \"" + name
					+ "\" is not a String!");
	}

	public StructuredDataObject getStructuredDataObject(String name) {
		Object get = getChild(name);
		if (get == null)
			return null;
		if (get instanceof StructuredDataObject)
			return (StructuredDataObject) get;
		else
			throw new RuntimeException("Child with name \"" + name
					+ "\" is not a StructuredDataObject!");
	}

	public StructuredDataArray getStructuredDataArray(String name) {
		Object get = getChild(name);
		if (get == null)
			return null;
		if (get instanceof StructuredDataArray)
			return (StructuredDataArray) get;
		else
			throw new RuntimeException("Child with name \"" + name
					+ "\" is not a StructuredDataArray!");
	}

	public int getInt(String name) {
		String get = getString(name);
		if (get == null)
			return 0;
		return Integer.parseInt(get);
	}

	public float getFloat(String name) {
		String get = getString(name);
		if (get == null)
			return 0;
		return Float.parseFloat(get);
	}

	public long getLong(String name) {
		String get = getString(name);
		if (get == null)
			return 0;
		return Long.parseLong(get);
	}

	public double getDouble(String name) {
		String get = getString(name);
		if (get == null)
			return 0;
		return Double.parseDouble(get);
	}

	public boolean getBoolean(String name) {
		String get = getString(name);
		if (get == null)
			return false;
		return Boolean.parseBoolean(get);
	}

	public void put(String name, Object object) {
		if (object == null)
			return;
		if (object instanceof String || object instanceof StructuredDataObject
				|| object instanceof StructuredDataArray) {
			this.children.put(name, object);
		} else {
			this.children.put(name, object.toString());
		}
	}
	
	public boolean hasChild(String name) {
		return getChild(name)!=null;
	}

	public void remove(String name) {
		this.children.remove(name);
	}

	public void clear() {
		this.children.clear();
	}

	public Set<Map.Entry<String, Object>> entrySet() {
		return this.children.entrySet();
	}

	public int size() {
		return this.children.size();
	}

	public Set<String> keySet() {
		return this.children.keySet();
	}

	public Collection<Object> values() {
		return this.children.values();
	}
}
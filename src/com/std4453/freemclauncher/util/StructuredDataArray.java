package com.std4453.freemclauncher.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class StructuredDataArray extends StructuredData implements
		Iterable<Object> {
	protected List<Object> children;

	public StructuredDataArray() {
		this.children = new Vector<Object>();
	}

	public Object getChild(int index) {
		return this.children.get(index);
	}

	public String getString(int index) {
		Object get = getChild(index);
		if (get == null)
			return null;
		if (get instanceof String)
			return (String) get;
		else
			throw new RuntimeException("Child with index " + index
					+ " is not a String!");
	}

	public StructuredDataObject getStructuredDataObject(int index) {
		Object get = getChild(index);
		if (get == null)
			return null;
		if (get instanceof StructuredDataObject)
			return (StructuredDataObject) get;
		else
			throw new RuntimeException("Child with index " + index
					+ " is not a StructuredDataObject!");
	}

	public StructuredDataArray getStructuredDataArray(int index) {
		Object get = getChild(index);
		if (get == null)
			return null;
		if (get instanceof StructuredDataArray)
			return (StructuredDataArray) get;
		else
			throw new RuntimeException("Child with index " + index
					+ " is not a StructuredDataArray!");
	}

	public int getInt(int index) {
		String get = getString(index);
		if (get == null)
			return 0;
		return Integer.parseInt(get);
	}

	public float getFloat(int index) {
		String get = getString(index);
		if (get == null)
			return 0;
		return Float.parseFloat(get);
	}

	public long getLong(int index) {
		String get = getString(index);
		if (get == null)
			return 0;
		return Long.parseLong(get);
	}

	public double getDouble(int index) {
		String get = getString(index);
		if (get == null)
			return 0;
		return Double.parseDouble(get);
	}

	public boolean getBoolean(int index) {
		String get = getString(index);
		if (get == null)
			return false;
		return Boolean.parseBoolean(get);
	}

	public void put(int index, Object object) {
		if (object == null)
			return;
		if (object instanceof String || object instanceof StructuredDataObject
				|| object instanceof StructuredDataArray) {
			this.children.add(index, object);
		} else {
			this.children.add(index, object.toString());
		}
	}

	public int size() {
		return this.children.size();
	}

	public void put(Object object) {
		put(size(), object);
	}

	public void remove(int index) {
		this.children.remove(index);
	}

	public void clear() {
		this.children.clear();
	}

	@Override
	public Iterator<Object> iterator() {
		return this.children.iterator();
	}

	public Collection<Object> getChildren() {
		return new Vector<Object>(this.children);
	}
	
	public boolean hasChild(int index) {
		return getChild(index)!=null;
	}
}
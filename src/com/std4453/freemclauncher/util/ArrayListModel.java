package com.std4453.freemclauncher.util;

import java.util.Collection;
import java.util.Vector;

import javax.swing.AbstractListModel;

public class ArrayListModel extends AbstractListModel<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8221536792182356344L;

	protected Vector<String> data;

	public ArrayListModel() {
		this.data = new Vector<String>();
	}

	public ArrayListModel(Collection<String> data) {
		this();
		this.data.addAll(data);
	}

	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public String getElementAt(int index) {
		return data.get(index);
	}

	public void add(int index, String data) {
		this.data.add(index, data);
		this.fireContentsChanged(this, 0, this.data.size() - 1);
	}

	public void add(String data) {
		this.add(getSize(), data);
	}

	public void remove(int index) {
		this.data.remove(index);
		this.fireContentsChanged(this, 0, this.data.size() - 1);
	}

	public void addAll(Collection<String> data) {
		this.data.addAll(data);
		this.fireContentsChanged(this, 0, this.data.size() - 1);
	}
	
	public void clear() {
		this.data.clear();
		this.fireContentsChanged(this, 0, -1);
	}
}

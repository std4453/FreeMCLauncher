package com.std4453.freemclauncher.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class ArrayListModel extends AbstractListModel<String> implements
		ComboBoxModel<String>, Iterable<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8221536792182356344L;

	protected Vector<String> data;
	protected String selected;

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

	@Override
	public void setSelectedItem(Object anItem) {
		if (this.data.contains(anItem))
			selected = (String) anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	public int indexOf(String str) {
		return this.data.indexOf(str);
	}

	public Iterator<String> iterator() {
		return this.data.iterator();
	}
}

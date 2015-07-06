package com.std4453.freemclauncher.util;

public class StructuredData {
	public StructuredDataObject toStructuredDataObject() {
		if (this instanceof StructuredDataObject)
			return (StructuredDataObject) this;
		return null;
	}
	
	public StructuredDataArray toStructuredDataArray() {
		if (this instanceof StructuredDataArray)
			return (StructuredDataArray) this;
		return null;
	}
}

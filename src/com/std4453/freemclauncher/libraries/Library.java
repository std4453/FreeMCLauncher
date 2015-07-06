package com.std4453.freemclauncher.libraries;

import java.util.List;

import com.std4453.freemclauncher.util.StructuredDataArray;
import com.std4453.freemclauncher.util.StructuredDataObject;

public class Library {
	protected String path;
	protected List<SystemFilterRule> rules;

	public Library(StructuredDataObject data) {
		String name = data.getString("name");
		StructuredDataArray rules = data.getStructuredDataArray("rules");

		for (Object object : rules) {
			if (object instanceof StructuredDataObject)
				this.rules.add(new SystemFilterRule(
						(StructuredDataObject) object));
			else
				continue;
		}
	}
}

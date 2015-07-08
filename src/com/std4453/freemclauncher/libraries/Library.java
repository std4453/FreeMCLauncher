package com.std4453.freemclauncher.libraries;

import java.util.List;
import java.util.Vector;

import com.std4453.freemclauncher.util.StructuredDataArray;
import com.std4453.freemclauncher.util.StructuredDataObject;
import com.std4453.freemclauncher.util.SystemNameHelper;

public class Library {
	protected String path;
	protected List<SystemFilterRule> rules;
	protected String natives;

	public Library(StructuredDataObject data) {
		String name = data.getString("name");
		this.path = LibraryPathParser.getLibraryPath(name);

		StructuredDataArray rules = data.getStructuredDataArray("rules");
		this.rules = new Vector<SystemFilterRule>();
		if (rules != null)
			for (Object object : rules) {
				if (object instanceof StructuredDataObject)
					this.rules.add(new SystemFilterRule(
							(StructuredDataObject) object));
				else
					continue;
			}

		StructuredDataObject natives = data.getStructuredDataObject("natives");
		if (natives == null)
			return;
		this.natives="";

		String platformString = natives.getString(SystemNameHelper
				.getSystemName());
		if (platformString == null)
			return;
		this.natives = LibraryPathParser.getNativeName(name, platformString);

		// the "extract" item of the library json object is currently ignored
		// because only the "META_INF" directory is excluded.
	}

	public boolean passed() {
		if (this.rules.isEmpty())
			return true;

		boolean passed = true;
		for (SystemFilterRule rule : this.rules)
			passed &= rule.passed();

		return passed;
	}

	public String getPath() {
		return path;
	}

	public List<SystemFilterRule> getRules() {
		return rules;
	}

	public String getNatives() {
		return natives;
	}
}

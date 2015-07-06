package com.std4453.freemclauncher.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLHelper {
	private static final Logger logger = Logger
			.getLogger("com.std4453.freemclauncher.log.Logger");

	public static Document getDocument(String data) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new ByteArrayInputStream(data.getBytes()));
		} catch (ParserConfigurationException e) {
			logger.log(Level.WARNING, "Exception Caught while parsing xml.", e);
			return null;
		} catch (SAXException e) {
			logger.log(Level.WARNING, "Exception Caught while parsing xml.", e);
			return null;
		} catch (IOException e) {
			logger.log(Level.WARNING, "Exception Caught while parsing xml.", e);
			return null;
		}
		return document;
	}
}

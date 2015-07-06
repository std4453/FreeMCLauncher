package com.std4453.freemclauncher.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StructuredDataHelper {
	public static StructuredData fromJSONObject(JSONObject object) {
		StructuredDataObject sdObject = new StructuredDataObject();

		Object obj;
		for (String key : object.keySet()) {
			obj = object.get(key);
			if (obj instanceof JSONObject)
				sdObject.put(key, fromJSONObject((JSONObject) obj));
			else if (obj instanceof JSONArray)
				sdObject.put(key, fromJSONArray((JSONArray) obj));
			else
				sdObject.put(key, obj);
		}

		return sdObject;
	}

	public static StructuredData fromJSONArray(JSONArray array) {
		StructuredDataArray sdArray = new StructuredDataArray();

		for (Object obj : array) {
			if (obj instanceof JSONObject)
				sdArray.put(fromJSONObject((JSONObject) obj));
			else if (obj instanceof JSONArray)
				sdArray.put(fromJSONArray((JSONArray) obj));
			else
				sdArray.put(obj);
		}

		return sdArray;
	}
	
	public static StructuredData fromXMLDocument(Document document) {
		return fromXMLNode(document);
	}
	
	private static StructuredData fromXMLNode(Node node) {
		StructuredDataObject object=new StructuredDataObject();
		NodeList list=node.getChildNodes();
		Node child;
		String name;
		for (int i=0;i<list.getLength();++i) {
			child=list.item(i);
			name=child.getNodeName();
			
			if (child.getNodeName()=="#text") {
				object.put("#text", node.getNodeValue());
				continue;
			}
			
			if (object.getChild(name)==null)
				object.put(name, new StructuredDataArray());
			object.getStructuredDataArray(name).put(fromXMLNode(child));
		}
		
		StructuredDataObject attr;
		object.put("#attr", attr=new StructuredDataObject());
		NamedNodeMap map=node.getAttributes();
		
		for (int i=0;i<map.getLength();++i) {
			attr.put(map.item(i).getNodeName(), map.item(i).getNodeValue());
		}
		
		return object;
	}
}

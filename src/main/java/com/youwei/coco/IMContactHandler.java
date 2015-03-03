package com.youwei.coco;

import net.sf.json.JSONArray;

public interface IMContactHandler {

	public JSONArray getUserTree();
	
	public JSONArray getChildren(String pid , String parentType);
}

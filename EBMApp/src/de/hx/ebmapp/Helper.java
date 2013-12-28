package de.hx.ebmapp;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class Helper {
	
	public HashMap<String,Integer> renderSymMap(JSONObject jObj){
		HashMap<String,Integer> result = new HashMap<String,Integer>();
		try {
		Iterator keys = jObj.keys();
		while(keys.hasNext()){
			String key = (String) keys.next();
			
				result.put(key, jObj.getInt(key));
			
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}

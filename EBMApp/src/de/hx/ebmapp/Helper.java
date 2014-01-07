package de.hx.ebmapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Helper {
	
	public HashMap<String,Integer> renderSymMap(JSONObject jObj){
		HashMap<String,Integer> result = new HashMap<String,Integer>();
		try {
		Iterator<String> keys = jObj.keys();
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

	public String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

		
	}
	
	public HashMap<String, Integer> renderAddSympMap(JSONObject jObj){
		
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		try {
			JSONArray jArray = jObj.getJSONArray("AdditionalSymptoms");
			for(int i=0;i<jArray.length();i++){
				JSONObject thisJObj = jArray.getJSONObject(i);
				result.put(thisJObj.getString("Name"), thisJObj.getInt("ID"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
}

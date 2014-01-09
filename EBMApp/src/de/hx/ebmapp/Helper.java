package de.hx.ebmapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
	
	public ArrayList<DiseaseModel> renderDiseasesList(JSONObject jObj){
		ArrayList<DiseaseModel> result = new ArrayList<DiseaseModel>();
		JSONArray diseasesArray = null;
		try{
			diseasesArray = jObj.getJSONArray("Diseases");
			for(int i=0;i<diseasesArray.length();i++){
				JSONObject thisJObj = diseasesArray.getJSONObject(i);
				JSONArray thisJArray = thisJObj.getJSONArray("Symptoms");
				ArrayList<SymptomModel> symptomsArray = new ArrayList<SymptomModel>();
				for(int j=0;j<thisJArray.length();j++){
					JSONObject thisSymptomJObj = thisJArray.getJSONObject(j);
					symptomsArray.add(new SymptomModel(thisSymptomJObj.getString("Name"), 
							thisSymptomJObj.getString("Percentage"), thisSymptomJObj.getString("PercentageLong"), 
							thisSymptomJObj.getString("Source"), thisSymptomJObj.getString("URL")));
				}
				JSONArray treatmentJArray = thisJObj.getJSONArray("Treatments");
				JSONObject treatmenJtObject = treatmentJArray.getJSONObject(0);
				TreatmentModel thisTreatment = new TreatmentModel(treatmenJtObject.getString("Description"),
						treatmenJtObject.getString("Name"), treatmenJtObject.getString("Source"), 
						treatmenJtObject.getString("SourceClean"));
				result.add(new DiseaseModel(thisJObj.getString("DeathRate"), 
						thisJObj.getString("ICD10"), thisJObj.getInt("ID"),
						thisJObj.getString("Name"), thisJObj.getString("Probability"),
						thisJObj.getString("Reliability"), symptomsArray, thisTreatment));
			}
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return result;
	}
	
}

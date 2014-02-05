package com.example.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
 
public class JSONParser {
 
    static InputStream is = null;
    static JSONArray jObj = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
 
    }
 
    public JSONArray getJSONFromUrl(String url) {
 
        // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();	//tao mot Client de co the giao dich
            HttpGet httpGet = new HttpGet(url);	//tao mot Post dinh kem url
 
            HttpResponse httpResponse;
			try {
				httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();	//lay tra loi cua Post
	            is = httpEntity.getContent();	//cho thanh InputStream
	            
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	//dung Client de thuc hien Post
            
    
//    	AsyncHttpClient client = new AsyncHttpClient();
//		client.get(url, new JsonHttpResponseHandler(){
//			public void onSuccess(JSONArray response){
////				aa = response.toString();
//				String b = "";
//			}
//		});
         
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
}
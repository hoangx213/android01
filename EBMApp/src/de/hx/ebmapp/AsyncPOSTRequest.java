package de.hx.ebmapp;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncPOSTRequest extends AsyncTask<Object, String, String> {
	
	public static Helper helper = new Helper();
	HashMap<String, Integer> addSympMap;
	
	public String POST(String url, int symId) {
		InputStream inputStream = null;
		String result = "";
		// String thisUrl = url + "/" + videoId;
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("ID", symId);

			// 4. convert JSONObject to JSON to String
			json = jsonObject.toString();

			// ** Alternative way to convert Person object to JSON string usin
			// Jackson Lib
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(person);

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			
			// 10. convert inputstream to string
			if (inputStream != null)
				result = helper.convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		// 11. return result
		return result;
	}

	@Override
	protected String doInBackground(Object... params) {
		return POST((String)params[0], (Integer)params[1]);
	}
	
	protected void onPostExecute(String result){
		
	}
	
	
}



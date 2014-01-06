package de.hx.ebmapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdditionalSympActivity extends Activity {

	String addSympUrl = "http://defaultsym.hydaras.de/api/additionalsymptoms";
	Helper helper = new Helper();
	HashMap<String, Integer> addSympMap = new HashMap<String, Integer>();
	ListView addSympListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additional_symp);
		addSympListView = (ListView) findViewById(R.id.addSympListView);
		Intent intent = getIntent();
		int sympId = intent.getIntExtra("ID", 0);
		AsyncPOSTRequest asyncPOSTTask = new AsyncPOSTRequest();
		String jString = null;
		try {
			jString = asyncPOSTTask.execute(addSympUrl, sympId).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject jObj = null;
		try {
			jObj = new JSONObject(jString);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addSympMap = helper.renderAddSympMap(jObj);
		ArrayList<String> addSympList = new ArrayList<String>(
				addSympMap.keySet());
		addSympListView.setAdapter(new ArrayAdapter<String>(
				getApplicationContext(), R.layout.sym_text_view, addSympList));

	}

}

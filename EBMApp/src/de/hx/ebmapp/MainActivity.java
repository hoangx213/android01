package de.hx.ebmapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	EditText symInputView;
	ListView sympListView;
	HashMap<String, Integer> symMap;
	Helper helper = new Helper();
	String symUrl = "http://defaultsym.hydaras.de/api/mainsymptoms/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		symInputView = (EditText) findViewById(R.id.symInpEditText);
		sympListView = (ListView) findViewById(R.id.symListView);

		symInputView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				ArrayList<String> symList = new ArrayList<String>();
				try {
					AsyncJSONParser aSyncJParser = new AsyncJSONParser();
					String result = aSyncJParser.execute(symUrl + s.toString())
							.get();
					if (result != "[]") {
						JSONObject jObj = new JSONObject(result);
						symMap = helper.renderSymMap(jObj);
						symList = new ArrayList<String>(symMap.keySet());
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				sympListView.setAdapter(new ArrayAdapter<String>(
						getApplicationContext(), R.layout.sym_text_view,
						symList));
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

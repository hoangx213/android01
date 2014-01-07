package de.hx.ebmapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class AdditionalSympActivity extends Activity {

	String addSympUrl = "http://defaultsym.hydaras.de/api/additionalsymptoms";
	Helper helper = new Helper();
	HashMap<String, Integer> addSympMap = new HashMap<String, Integer>();
	ListView addSympListView;

	int sympID;
	String age;
	int sex;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additional_symp);
		addSympListView = (ListView) findViewById(R.id.addSympListView);
		Intent intent = getIntent();
		sympID = intent.getIntExtra("SymptomID", 0);
		age = intent.getStringExtra("Age");
		sex = intent.getIntExtra("Sex", 1);
		AsyncPOSTRequest asyncPOSTTask = new AsyncPOSTRequest();
		String jString = null;
		try {
			jString = asyncPOSTTask.execute(addSympUrl, sympID).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		JSONObject jObj = null;
		try {
			jObj = new JSONObject(jString);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		addSympMap = helper.renderAddSympMap(jObj);
		ArrayList<AddSympRowModel> addSympList = new ArrayList<AddSympRowModel>();
		for (String key : addSympMap.keySet()) {
			addSympList.add(new AddSympRowModel(addSympMap.get(key).toString(),
					key));
		}

		addSympListView.setAdapter(new AddSympListViewAdapter(addSympList));

	}

	protected class AddSympListViewAdapter extends
			ArrayAdapter<AddSympRowModel> {
		ArrayList<AddSympRowModel> addSympListOfAdapter;

		public AddSympListViewAdapter(ArrayList<AddSympRowModel> addSympList) {
			super(AdditionalSympActivity.this,
					R.layout.additional_symp_listview_elem,
					R.id.addSympTextView, addSympList);

			this.addSympListOfAdapter = addSympList;
		}

		public class ViewHolder {
			public RadioGroup radioGroup;

			ViewHolder(View base) {
				this.radioGroup = (RadioGroup) base
						.findViewById(R.id.selectRadioGroup);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);
			ViewHolder viewHolder = (ViewHolder) row.getTag();
			if (viewHolder == null) {
				viewHolder = new ViewHolder(row);
				row.setTag(viewHolder);
				
				viewHolder.radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						Integer myPosition = (Integer) group.getTag();
						AddSympRowModel addSymp = addSympListOfAdapter.get(myPosition);
						addSymp.checkedID = checkedId;
					}
				});
				
			}
			viewHolder.radioGroup.setTag(Integer.valueOf(position));
			AddSympRowModel addSymp = addSympListOfAdapter.get(position);
			viewHolder.radioGroup.check(addSymp.checkedID);
			return row;
		}
	}

	class AddSympRowModel {
		String addSympID;
		String name;
		int checkedID = R.id.selectRadioNull;

		AddSympRowModel(String addSympID, String name) {
			this.addSympID = addSympID;
			this.name = name;
		}
		
		public String toString(){
			return name;
		}
	}

}

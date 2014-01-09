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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DiseasesActivity extends Activity {

	HashMap<String, Boolean> symptoms;
	String age;
	int sex;
	int symptomID;

	String updateAdditionalUrl = "http://defaultsym.hydaras.de/api/updateadditionals";
	Helper helper = new Helper();
	ListView deseasesListView;
	TextView totalReliabilityTextView;
	
	ArrayList<DiseaseModel> diseasesList;
	String totalReliability;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diseases_page);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		symptoms = (HashMap<String, Boolean>) bundle
				.getSerializable("Symptoms");
		age = bundle.getString("Age");
		sex = bundle.getInt("Sex");
		symptomID = bundle.getInt("SymptomID");

		deseasesListView = (ListView) findViewById(R.id.deseasesListView);
		totalReliabilityTextView = (TextView) findViewById(R.id.totalReliability);
		

		AsyncPOSTRequest asyncPOSTTask = new AsyncPOSTRequest();
		String jString = null;
		try {
			jString = asyncPOSTTask.execute(updateAdditionalUrl, symptomID,
					sex, age, symptoms).get();
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
		try {
			totalReliability = jObj.getString("Reliability");
			totalReliabilityTextView.setText(totalReliability);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		diseasesList = helper.renderDiseasesList(jObj);
		deseasesListView.setAdapter(new DiseasesListAdapter(diseasesList));
		
		deseasesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(DiseasesActivity.this, TherapyActivity.class);
				DiseaseModel clickedDisease = diseasesList.get(position);
				intent.putExtra("Treatment", clickedDisease.treatment);
				intent.putParcelableArrayListExtra("Symptoms", clickedDisease.symptomsArray);
				intent.putExtra("Name", clickedDisease.name);
				intent.putExtra("Code", clickedDisease.code);
				startActivity(intent);
			}
		});
	}

	class DiseasesListAdapter extends ArrayAdapter<DiseaseModel> {
		ArrayList<DiseaseModel> diseasesArray;

		public DiseasesListAdapter(ArrayList<DiseaseModel> diseasesArray) {
			super(DiseasesActivity.this, R.layout.disease_listview_elem,
					R.id.diseaseNameTextView,diseasesArray);
			this.diseasesArray = diseasesArray;
		}

		class ViewHolder {
			TextView proabilityTextView, diseaseNameTextView,
					reliabilityTextView, deathRateTextView;
			ViewHolder(View base){
				this.proabilityTextView = (TextView)base.findViewById(R.id.probabilitytextView);
				this.diseaseNameTextView = (TextView)base.findViewById(R.id.diseaseNameTextView);
				this.reliabilityTextView = (TextView)base.findViewById(R.id.reliabilityTextView);
				this.deathRateTextView = (TextView)base.findViewById(R.id.deathRateTextView);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);
			ViewHolder holder = (ViewHolder)row.getTag();
			if(holder == null){
				holder = new ViewHolder(row);
				row.setTag(holder);
			}
			DiseaseModel thisDisease = diseasesArray.get(position);
			holder.proabilityTextView.setText(thisDisease.probability);
			holder.reliabilityTextView.setText(thisDisease.reliability);
			holder.deathRateTextView.setText(thisDisease.deathRate);
			return row;
		}
	}

}

package de.hx.ebmapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EvidenzDataActivity extends Activity {

	ListView symptomListView;
	ArrayList<SymptomModel> symptomList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evidenz_data_page);
		symptomListView = (ListView) findViewById(R.id.evidenzDataSymptomListView);

		Intent intent = getIntent();
		symptomList = intent.getParcelableArrayListExtra("Symptoms");
		symptomListView.setAdapter(new EvidenzDataListAdapter(symptomList));

	}

	class EvidenzDataListAdapter extends ArrayAdapter<SymptomModel> {
		ArrayList<SymptomModel> symptomList;

		EvidenzDataListAdapter(ArrayList<SymptomModel> symptomList) {
			super(EvidenzDataActivity.this, R.layout.symptom_listview_elem,
					R.id.evidenzDataSympNameTextView, symptomList);
			this.symptomList = symptomList;
		}

		class ViewHolder {

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);
			((TextView) row.findViewById(R.id.evidenzDataPercentageTextView))
					.setText(symptomList.get(position).percentage);
			return row;
		}
	}
}

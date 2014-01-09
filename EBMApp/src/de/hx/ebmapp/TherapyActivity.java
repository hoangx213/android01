package de.hx.ebmapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TherapyActivity extends Activity {

	TextView diseaseCodeTextView;
	TextView diseaseNameTextView;
	Button toEvidenzDateButton;
	TextView sourceTextView;
	TextView googleSearchTextView;

	ArrayList<SymptomModel> symptomsList;
	TreatmentModel treatment;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.therapy_page);
		Intent intent = getIntent();
		symptomsList = intent.getParcelableArrayListExtra("Symptoms");
		String diseaseName = intent.getStringExtra("Name");
		String diseaseCode = intent.getStringExtra("Code");
		treatment = (TreatmentModel) intent.getSerializableExtra("Treatment");

		diseaseNameTextView = (TextView) findViewById(R.id.diseaseNameTherapyTextView);
		diseaseCodeTextView = (TextView) findViewById(R.id.diseaseCodeTherapyTextView);
		toEvidenzDateButton = (Button) findViewById(R.id.toEvidenzDateButton);
		sourceTextView = (TextView) findViewById(R.id.sourceTextView);
		googleSearchTextView = (TextView) findViewById(R.id.googleSearchTextView);

		diseaseCodeTextView.setText(diseaseCode);
		diseaseNameTextView.setText(diseaseName);

		toEvidenzDateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(TherapyActivity.this,
						EvidenzDataActivity.class);
				newIntent.putParcelableArrayListExtra("Symptoms", symptomsList);
				startActivity(newIntent);
			}
		});

		sourceTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(TherapyActivity.this,
						WebViewActivity.class);
				newIntent.putExtra("Source", treatment.source);
				startActivity(newIntent);
				// WebView webView = (WebView)findViewById(R.id.webView);
				// webView.loadUrl("http://wikipedia.de");
			}
		});

		googleSearchTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(TherapyActivity.this,
						WebViewActivity.class);
				newIntent.putExtra("Name", treatment.name);
				startActivity(newIntent);
				// WebView webView = (WebView)findViewById(R.id.webView);
				// webView.loadUrl("http://wikipedia.de");
			}
		});
	}

}

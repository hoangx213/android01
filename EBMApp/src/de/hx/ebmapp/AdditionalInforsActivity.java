package de.hx.ebmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioGroup;

public class AdditionalInforsActivity extends Activity implements OnClickListener{

	int sympId;
	RadioGroup sexRadioGroupView;
//	RadioButton radioButtonFemale;
//	RadioButton radioButtonMale;
	EditText ageEditTextView;
	Button nextButtonView;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.additional_infors_page);
	    Intent intent = getIntent();
	    sympId = intent.getIntExtra("ID", 1000);
	    sexRadioGroupView = (RadioGroup)findViewById(R.id.sexRadioGroup);
	    ageEditTextView = (EditText)findViewById(R.id.ageEditText);
	    nextButtonView = (Button)findViewById(R.id.nextButton);
	    nextButtonView.setOnClickListener(this);
//	    radioButtonFemale = (RadioButton)findViewById(R.id.femaleRadio);
//	    radioButtonMale = (RadioButton)findViewById(R.id.maleRadio);
	}
	@Override
	public void onClick(View arg0) {
		String age = ageEditTextView.getText().toString();
		int checkedRadioButton = sexRadioGroupView.getCheckedRadioButtonId();
		int sex;
		switch(checkedRadioButton){
			case R.id.femaleRadio : 	sex = 1;
					break;
			case R.id.maleRadio : 		sex = 2;
					break;
			default: 					sex = 1;
					break;
		}
		Intent intent = new Intent(this, AdditionalSympActivity.class);
		intent.putExtra("Age", age).putExtra("Sex", sex).putExtra("SymptomID", sympId);
		startActivity(intent);
	}
	
	
	
	

}

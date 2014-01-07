package de.hx.ebmapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import de.hx.ebmapp.AdditionalSympActivity.AddSympListViewAdapter.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
		addSympListView.setAdapter(new AddSympListViewAdapter(
				this, R.layout.additional_symp_listview_elem, addSympList));

	}
	
	protected class AddSympListViewAdapter extends ArrayAdapter<String>{
		private final LayoutInflater mInflater;
		Activity context;
		int res;
		ArrayList<String> addSympList;

		public AddSympListViewAdapter(Activity context, int viewResourceId,
				ArrayList<String> addSympList) {
			super(context, viewResourceId, addSympList);
			mInflater = (LayoutInflater) (context.getLayoutInflater());
			this.context = context;
			this.res = viewResourceId;
			this.addSympList = addSympList;
		}

		public class ViewHolder {
			public TextView addSymp;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			if (rowView == null) {
				rowView = mInflater.inflate(res, null);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.addSymp = (TextView) rowView
						.findViewById(R.id.addSympTextView);
				rowView.setTag(viewHolder);
			}
			String thisSymp = addSympList.get(position);
			ViewHolder holder = (ViewHolder) rowView.getTag();
			holder.addSymp.setText(thisSymp);
			return rowView;
		}
	}


}

package com.example.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class VideoDetailActivity extends FragmentActivity implements
		OnClickListener, OnScrollListener {

	private final static String DESCRIPTION = "Description";
	private final static String FILE = "File";
	private final static String IMAGE = "Image";
	private final static String TITLE = "Title";
	private final static String ID = "ID";
	private final static String NAME = "Name";
	private final static String MESSAGE = "Message";

	private String title, descr, fileurl;
	private String videoId, videoCommentListUrl;
	private Bitmap bm;

	private TextView titleView, descrView;
	private ImageView detailImageView;
	private ListView commentListView;

	private int currentPage, totalPages;
	private String previousPage, nextPage;

	private static String commentListUrl = "http://deux.k9.l/vms_test/";
	private static String commentPOSTUrl = "http://deux.k9.l/vms_test/comment/add";
	private static String currentCommentListUrl;

	private boolean loadingItems = false;
	private boolean firstLoadingTime = true;

	private ArrayList<Comment> commentList = new ArrayList<Comment>();
	private CommentListAdapter commentListAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_detail_activity);
		Intent intent = getIntent();
		videoId = intent.getStringExtra(ID);
		videoCommentListUrl = commentListUrl + "comment/comments/" + videoId;
		commentListView = (ListView) findViewById(R.id.commentListView);

		bm = BitmapFactory.decodeByteArray(intent.getByteArrayExtra(IMAGE), 0,
				intent.getByteArrayExtra(IMAGE).length);
		title = intent.getStringExtra(TITLE);
		descr = intent.getStringExtra(DESCRIPTION);
		fileurl = intent.getStringExtra(FILE);

		View headerView = View
				.inflate(this, R.layout.video_detail_header, null);
		titleView = (TextView) headerView.findViewById(R.id.detailTitleView);
		titleView.setText(title);
		descrView = (TextView) headerView
				.findViewById(R.id.detailDescriptioinView);
		descrView.setText(descr);
		detailImageView = (ImageView) headerView
				.findViewById(R.id.detailImageView);

		detailImageView.setImageBitmap(bm);
		commentListView.addHeaderView(headerView);
		new JSONCommentListParser().execute(videoCommentListUrl);
		detailImageView.setOnClickListener(this);

		Button commentButtonView = (Button) findViewById(R.id.commentButtonView);
		commentButtonView.setOnClickListener(this);

	}

	@SuppressLint("ValidFragment")
	public class WriteCommentDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					VideoDetailActivity.this);
			// LayoutInflater inflater = this.getLayoutInflater();
			final View commentForm = View.inflate(VideoDetailActivity.this,
					R.layout.comment_form, null);
			builder.setView(commentForm);
			builder.setTitle("Write comment");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							String name = ((TextView) commentForm
									.findViewById(R.id.editText1)).getText()
									.toString();
							String message = ((TextView) commentForm
									.findViewById(R.id.editText2)).getText()
									.toString();
							pushCommentToServer(name, message);
							Thread thread = new Thread(null, loadNewComments);
							thread.start();
							dialog.dismiss();

						}

					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});

			return builder.create();

		}

	}

	// public Runnable loadNewComments = new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// new JSONCommentListParser().execute(videoCommentListUrl);
	// }
	// };

	public void pushCommentToServer(String name, String message) {
		new HttpAsyncTask().execute(commentPOSTUrl, videoId, name, message);
	}

	public static String POST(String url, String videoId, String name,
			String message) {
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
			jsonObject.accumulate("ID", Integer.valueOf(videoId));
			jsonObject.accumulate("Name", name);
			jsonObject.accumulate("Message", message);

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
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		// 11. return result
		return result;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {

			return POST(params[0], params[1], params[2], params[3]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
		}
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == (View) findViewById(R.id.detailImageView)) {
			Intent intent = new Intent(this, ViewVideo.class);
			intent.putExtra(FILE, fileurl);
			startActivity(intent);
		}
		if (v == (View) findViewById(R.id.commentButtonView)) {
			// Toast.makeText(getApplicationContext(), "Comment",
			// Toast.LENGTH_LONG).show();
			DialogFragment newFragment = new WriteCommentDialogFragment();
			newFragment.show(getSupportFragmentManager(), "write_comment");
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		int lastItemOnScreen = firstVisibleItem + visibleItemCount;
		if ((lastItemOnScreen == totalItemCount) && !loadingItems
				&& nextPage != "false") {
			// TODO call loadItemsThread
			Thread thread = new Thread(null, loadMoreComments);
			thread.start();
		}

	}

	Runnable loadMoreComments = new Runnable() {

		@Override
		public void run() {
			loadingItems = true;
			// TODO Auto-generated method stub
			videoCommentListUrl = commentListUrl + nextPage;
			try {
				ArrayList<Comment> newCommentList = renderCommentList((new JSONCommentListParser()
						.execute(videoCommentListUrl)).get());
				commentList.addAll(newCommentList);
				runOnUiThread(returnRes);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	Runnable loadNewComments = new Runnable() {

		@Override
		public void run() {
			loadingItems = true;
			// TODO Auto-generated method stub

			try {
				ArrayList<Comment> newCommentList = renderCommentList((new JSONCommentListParser()
						.execute(videoCommentListUrl)).get());
				newCommentList.removeAll(commentList);
				commentList.addAll(0,newCommentList);
				while (nextPage != "false") {
					videoCommentListUrl = commentListUrl + nextPage;
					newCommentList = renderCommentList((new JSONCommentListParser()
							.execute(videoCommentListUrl)).get());
					commentList.addAll(0,newCommentList);
				}
				runOnUiThread(returnRes);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			commentListAdapter.notifyDataSetChanged();
			loadingItems = false;
		}
	};

	public class JSONCommentListParser extends
			AsyncTask<String, String, String> {

		private InputStream is;
		private String json;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);

			HttpResponse httpResponse;
			try {
				httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
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

			return json;

		}

		@Override
		protected void onPostExecute(String result) {
			if (firstLoadingTime) {
				firstLoadingTime = false;
				commentList = renderCommentList(result);
				commentListAdapter = new CommentListAdapter(
						VideoDetailActivity.this, R.layout.comment_row,
						commentList);
				commentListView.setAdapter(commentListAdapter);
				commentListView.setOnScrollListener(VideoDetailActivity.this);
			}
		}
	}

	public ArrayList<Comment> renderCommentList(String jString) {
		JSONObject jObject = null;
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		try {
			jObject = new JSONObject(jString);
			JSONObject pages = jObject.getJSONObject("Pages");
			currentPage = pages.getInt("Current");
			totalPages = pages.getInt("Total");
			previousPage = pages.getString("Previous");
			nextPage = pages.getString("Next");

			JSONArray commentArray = jObject.getJSONArray("Comments");
			for (int i = 0; i < commentArray.length(); i++) {
				JSONObject jObjectComment = commentArray.getJSONObject(i);
				Comment comment = new Comment(jObjectComment.getString(ID),
						jObjectComment.getString(NAME),
						jObjectComment.getString(MESSAGE));
				commentList.add(comment);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commentList;
	}

	public class CommentListAdapter extends ArrayAdapter<Comment> {
		private final LayoutInflater mInflater;
		Activity context;
		int res;
		ArrayList<Comment> commentList;

		public CommentListAdapter(Activity context, int viewResourceId,
				ArrayList<Comment> commentList) {
			super(context, viewResourceId, commentList);
			mInflater = (LayoutInflater) (context.getLayoutInflater());
			this.context = context;
			this.res = viewResourceId;
			this.commentList = commentList;
		}

		public class ViewHolder {
			public TextView name;
			public TextView message;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			if (rowView == null) {
				rowView = mInflater.inflate(res, null);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.name = (TextView) rowView
						.findViewById(R.id.nameView);
				viewHolder.message = (TextView) rowView
						.findViewById(R.id.messageView);
				rowView.setTag(viewHolder);
			}
			Comment thisComment = commentList.get(position);
			ViewHolder holder = (ViewHolder) rowView.getTag();
			holder.name.setText(thisComment.getName());
			holder.message.setText(thisComment.getMessage());
			return rowView;
		}
	}

}

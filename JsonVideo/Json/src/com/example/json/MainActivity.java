package com.example.json;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	// Video Pages Infos
	private String nextPage, previousPage;
	private int totalPages, currentPage;
	private boolean loadingItems = false;
	private LruCache<String, Bitmap> mMemoryCache;

	// Adapter
	JSONArrayAdapter videoArrayAdapter;
	// Data for ListView
	ArrayList<HashMap<String, String>> videoList = new ArrayList<HashMap<String, String>>();

	// url to the JSON
	private static String url = "http://deux.k9.l/vms_test/";

	// JSON Node names
	private final static String DESCRIPTION = "Description";
	private final static String FILE = "File";
	private final static String IMAGE = "Image";
	private final static String TITLE = "Title";
	private final static String ID = "ID";
	
	private ListView videoListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	    // Use 1/8th of the available memory for this memory cache.
	    final int cacheSize = maxMemory / 2;

	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        @SuppressLint("NewApi")
			@Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	            return bitmap.getByteCount() / 1024;
	        }
	    };
		videoListView = (ListView) findViewById(R.id.videoListView);
//	    View loading_spinner = View
//				.inflate(this, R.layout.loading_progress, null);
//	    videoListView.addFooterView(loading_spinner);
	    
		// JSON Parser
		AsyncJSONParser asyncJParser = new AsyncJSONParser();

		JSONObject jObject = null;
		try {
			String jsonInString = null;
			jsonInString = asyncJParser.execute(url + "video/json").get();
			jObject = new JSONObject(jsonInString);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		videoList.addAll(renderVideoList(jObject));
		videoArrayAdapter = new JSONArrayAdapter(this,
				R.layout.item_row, videoList);
		renderVideoRows();

	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap){
		if(getBitmapFromMemoryCache(key) == null)
			mMemoryCache.put(key, bitmap);
	}
	
	public Bitmap getBitmapFromMemoryCache(String key){
		return mMemoryCache.get(key);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public ArrayList<HashMap<String, String>> renderVideoList(JSONObject jObject) {
		ArrayList<HashMap<String, String>> newVideoList = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject pages = jObject.getJSONObject("Pages");

			nextPage = pages.getString("Next");
			previousPage = pages.getString("Previous");
			totalPages = pages.getInt("Total");
			currentPage = pages.getInt("Current");

			JSONArray videoArray = jObject.getJSONArray("Videos");
			for (int i = 0; i < videoArray.length(); i++) {
				JSONObject jObjektVideo;

				jObjektVideo = videoArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				String description = jObjektVideo.getString(DESCRIPTION);
				String file = jObjektVideo.getString(FILE);
				String image = jObjektVideo.getString(IMAGE);
				String videoId = jObjektVideo.getString(ID);
				String tittle = jObjektVideo.getString(TITLE);
				

				map.put(DESCRIPTION, description);
				map.put(FILE, file);
				map.put(IMAGE, image);
				map.put(TITLE, tittle);
				map.put(ID, videoId);

				newVideoList.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newVideoList;
	}

	public void renderVideoRows() {


		videoListView.setAdapter(videoArrayAdapter);
		videoListView.setScrollingCacheEnabled(false);
		videoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, VideoDetailActivity.class);
				HashMap<String, String> item = ((JSONArrayAdapter)videoListView.getAdapter()).getItem(position);
				String fileurl = item.get(FILE);
				String title = item.get(TITLE);
				String descr = item.get(DESCRIPTION);
				String videoId = item.get(ID);
				Bitmap bm = getBitmapFromMemoryCache(((JSONArrayAdapter)videoListView.getAdapter()).getItem(position).get(IMAGE));
				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.PNG, 50, bs);
				
//				intent.putExtra("fileurl", fileurl);
				intent.putExtra(TITLE, title);
				intent.putExtra(DESCRIPTION, descr);
				intent.putExtra(IMAGE, bs.toByteArray());
				intent.putExtra(FILE, fileurl);
				intent.putExtra(ID, videoId);
				startActivity(intent);
			}
		});

		videoListView.setOnScrollListener(new OnScrollListener() {

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
					Thread thread = new Thread(null, loadItems);
					thread.start();
				}

			}
		});
	}

	private Runnable loadItems = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			loadingItems = true;
			String newUrl = url + nextPage;
			AsyncJSONParser asyncJParser = new AsyncJSONParser();
			try {
				JSONObject newJObject = new JSONObject(asyncJParser.execute(
						newUrl).get());
				ArrayList<HashMap<String, String>> newVideoList = renderVideoList(newJObject);
				// for(int i=0;i<newVideoList.size();i++){
				// // ListView videoListView = (ListView)
				// findViewById(R.id.videoListView);
				// videoArrayAdapter.add(newVideoList.get(i));
				// }
				videoList.addAll(newVideoList);
				runOnUiThread(returnRes);
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
		}
	};

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			videoArrayAdapter.notifyDataSetChanged();
			loadingItems = false;
		}

	};

	protected class AsyncJSONParser extends AsyncTask<String, String, String> {

		private InputStream is;
		private String json;

		@Override
		protected String doInBackground(String... arg) {

			String url = arg[0];
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
			// JSONObject jObject = null;
			// try {
			// jObject = new JSONObject(result);
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// renderVideoList(jObject);
			// renderVideoRows();
		}

	}

	protected class JSONArrayAdapter extends ArrayAdapter<HashMap<String, String>> {

		Activity context;
		int res;
		ArrayList<HashMap<String, String>> items;

		public JSONArrayAdapter(Activity context, int resource,
				ArrayList<HashMap<String, String>> videoList) {
			super(context, resource, videoList);
			this.context = context;
			this.res = resource;
			this.items = videoList;
		}
		
		public class ViewHolder{
			public TextView title;
			public ImageView image;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			if (rowView == null) {
				LayoutInflater inflater = (context
						.getLayoutInflater());
				rowView = inflater.inflate(res, null);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.title = (TextView) rowView
						.findViewById(R.id.tittleView);
				viewHolder.image = (ImageView) rowView
						.findViewById(R.id.imageView);
				rowView.setTag(viewHolder);
			}
			Map<String, String> map = new HashMap<String, String>();
			map = (Map<String, String>) items.get(position);
			String title = map.get(TITLE);
//			String description = map.get(DESCRIPTION);
//			String file = map.get(FILE);
			String imageUrl = map.get(IMAGE);
//			TextView titteView = (TextView) rowView
//					.findViewById(R.id.tittleView);
//			titteView.setText(tittle);
//			ImageView imageView = (ImageView) rowView
//					.findViewById(R.id.imageView);
//			TextView videoTimeView = (TextView) rowView
//					.findViewById(R.id.videoTimeView);
			// videoTimeView.setText(renderTimeOfVideo(file));
			ViewHolder holder = (ViewHolder) rowView.getTag();
			holder.title.setText(title);
			
			loadBitmap(imageUrl, holder.image);
			
			return rowView;
		}
		
		public void loadBitmap(String imageUrl, ImageView imageView){
			Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
			if(bitmap != null){
				imageView.setImageBitmap(bitmap);
			}
			else {
				imageView.setImageResource(R.drawable.placeholder);
				new AsyncImageLoader().execute(imageUrl, imageView);
			}
		}

		public String renderTimeOfVideo(String videoUrl) {
			FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
			String uri = "http://deux.k9.l/vms_test/assets/Uploads/Members/3/VideoFiles/01.mp4";
			retriever.setDataSource(videoUrl);
			String time = retriever
					.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
			long timeInmillisec = Long.parseLong(time);
			long duration = timeInmillisec / 1000;
			long hours = duration / 3600;
			long minutes = (duration - hours * 3600) / 60;
			long seconds = duration - (hours * 3600 + minutes * 60);

			return "" + hours + ":" + minutes + ":" + seconds;
		}

	}

	protected class AsyncImageLoader extends AsyncTask<Object, String, Bitmap> {

		
		ImageView imageView = null;
		// ImageView imageView;

		@Override
		protected Bitmap doInBackground(Object... args) {
			// imageView = (ImageView) args[1];
			Bitmap image = null;
			try {
				image = downloadBitmap((String) args[0]);
				addBitmapToMemoryCache((String) args[0], image);
			} catch (Exception e) {
				e.printStackTrace();
			}
			imageView = (ImageView) args[1];
			return image;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			imageView.setImageBitmap(result);
		}

		private Bitmap downloadBitmap(String url) {
			Bitmap image = null;
			// initilize the default HTTP client object
			final DefaultHttpClient client = new DefaultHttpClient();

			// forming a HttoGet request
			final HttpGet getRequest = new HttpGet(url);
			try {

				HttpResponse response = client.execute(getRequest);

				// check 200 OK for success
				final int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.w("ImageDownloader", "Error " + statusCode
							+ " while retrieving bitmap from " + url);
					return null;

				}

				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					try {
						// getting contents from the stream
						inputStream = entity.getContent();

						// decoding stream data back into image Bitmap that
						// android understands
						image = BitmapFactory.decodeStream(inputStream);

					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (Exception e) {
				// You Could provide a more explicit error message for
				// IOException
				getRequest.abort();
				Log.e("ImageDownloader", "Something went wrong while"
						+ " retrieving bitmap from " + url + e.toString());
			}

			return image;
		}
	}

}

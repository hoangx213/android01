package com.example.json;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class ViewVideo extends Activity {

	private final static String FILE = "File";
	private String fileurl;
	private static final int INSERT_ID = Menu.FIRST;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	    setContentView(R.layout.video_view);
	    System.gc();
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        fileurl = extras.getString(FILE);
        VideoView vv = (VideoView) findViewById(R.id.videoView);      
        
        vv.setVideoURI(Uri.parse(fileurl));
        vv.setMediaController(new MediaController(this));
        vv.requestFocus();
        vv.start();
	}
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      super.onCreateOptionsMenu(menu);
	      menu.add(0, INSERT_ID, 0,"FullScreen");

	      return true;
	  }

	  @Override
	  public boolean onMenuItemSelected(int featureId, MenuItem item) {
	      switch(item.getItemId()) {
	      case INSERT_ID:
	          createNote();
	      }
	      return true;
	  }

	  private void createNote() {
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
	                             WindowManager.LayoutParams.FLAG_FULLSCREEN);  
	  }

}

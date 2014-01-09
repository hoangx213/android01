package de.hx.ebmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

	WebView webView;
	String url;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.disease_web_page);
	    
	    Intent intent = getIntent();
	    if(intent.getStringExtra("Source")!=null){
	    	url = intent.getStringExtra("Source");
	    }
	    else if(intent.getStringExtra("Name")!=null){
	    	url = "https://www.google.de/#q=" + intent.getStringExtra("Name");
	    }
	    webView = (WebView)findViewById(R.id.webView);
	    webView.setWebViewClient(new WebViewClient());
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.loadUrl(url);
	    // TODO Auto-generated method stub
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
	        webView.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}

}

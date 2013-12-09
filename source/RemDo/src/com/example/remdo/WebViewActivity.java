package com.example.remdo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	private WebView webView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
	    // Get the uri from the intent
	    Intent intent = getIntent();
	    String uriToDisplay = intent.getStringExtra(MainActivity.DEVICE_URL);
		
		webView = (WebView) findViewById(R.id.webView1);
		//webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(uriToDisplay);
		
		//WebView myWebView = (WebView) this.findViewById(R.id.textView1);
		//myWebView.loadUrl(uriToDisplay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

}

package com.example.remdo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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
		//webView.loadUrl(uriToDisplay);
		
		
	    HttpClient httpclient = new DefaultHttpClient();
	    //HttpPost httppost = new HttpPost("http://a_site.com/logintest.aspx");
	    HttpPost httppost = new HttpPost(uriToDisplay);

	    try {
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("txtUsername", "admin"));
	        nameValuePairs.add(new BasicNameValuePair("txtPassword", "opendomo"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        HttpResponse response = httpclient.execute(httppost);
	        String responseAsText = EntityUtils.toString(response.getEntity());
	        webView.loadUrl(responseAsText);

	    } catch (ClientProtocolException e) {

	    } catch (IOException e) {

	    }
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

}

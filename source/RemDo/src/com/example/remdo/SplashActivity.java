package com.example.remdo;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

import android.view.Menu;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGHT = 1000;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable(){
        	
			@Override
            public void run() {
            	gotoDeviceConfig();
            }
        }, SPLASH_DISPLAY_LENGHT);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}


	
	public void gotoDeviceConfig() {
		Intent intent = new Intent(this, EditDeviceActivity.class);
		startActivity(intent);
	}
}


/* ************************************************************************  
 * RemoteDomo is an Android native application to remotely control domotics systems
 * Copyright © 2014 Gerard Torrents Vinaixa
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <www.gnu.org/licenses/>.
 * ************************************************************************/
package com.remdo.app;

import com.remdo.app.R;
import com.remdo.app.R.layout;
import com.remdo.app.R.menu;

import remdo.sqlite.helper.DatabaseHelper;
import remdo.sqlite.model.*;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

import android.view.Menu;
import android.view.Window;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGHT = 1000;
	private DatabaseHelper dh;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		this.dh = new DatabaseHelper(getApplicationContext());
		
        new Handler().postDelayed(new Runnable(){
        	
            public void run() {
            	if (!dh.hasDevices())
            	{
            		gotoDeviceConfig();
            	}
            	else
            	{
            		gotoMainActivity();
            	}
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
		finish();//close current activity, no longer usefully
	}
	
	public void gotoMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();//close current activity, no longer usefully
	}
}


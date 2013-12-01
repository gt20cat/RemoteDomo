/* ************************************************************************  
 * RemoteDomo is an Android native application to remotely control domotics systems
 * Copyright © 2013 Gerard Torrents Vinaixa
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ************************************************************************/
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


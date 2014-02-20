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
package com.remdo.app;

import java.util.ArrayList;
import java.util.List;

import remdo.sqlite.helper.DatabaseHelper;
import remdo.sqlite.model.Device;

import com.remdo.app.R;
import com.remdo.app.R.id;
import com.remdo.app.R.layout;
import com.remdo.app.R.menu;
import com.remdo.app.R.string;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.webkit.URLUtil;



public class EditDeviceActivity extends FragmentActivity  implements OnItemSelectedListener, OnClickListener  {
	private DatabaseHelper dh;     
	
	private String mDeviceName;
	private String mHost;
	private String mUser;
	private String mPassword;
	private int location;
	private int deviceType;
	
	//Referencias UI
	private EditText mDeviceNameView;
	private EditText mHostView;
	private EditText mUserView;
	private EditText mPasswordView;
	private TextView mLoginStatusMessageView;
	private DialogFragment dialog; 
	Device currentDevice;
	boolean EditMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_device);
		this.dh = new DatabaseHelper(this);
	    Intent intent = getIntent();
	    Long deviceId = intent.getLongExtra("DEVICE_ID",-1);
	    
		mDeviceNameView  = (EditText) findViewById(R.id.edit_devicename);
		mHostView = (EditText) findViewById(R.id.edit_host);
		mUserView = (EditText) findViewById(R.id.edit_user);
		mPasswordView = (EditText) findViewById(R.id.edit_password);
        Button save = (Button) findViewById(R.id.button_accept);
		save.setOnClickListener(this);
		        
	    if (deviceId == -1)
	    {			
			EditMode = false;
			save.setText(R.string.save);
	    }
	    else
	    {
			currentDevice = dh.getDevicebyID(deviceId);
			
			mDeviceNameView.setText(currentDevice.name,TextView.BufferType.EDITABLE);
			mHostView.setText(currentDevice.url,TextView.BufferType.EDITABLE);
			mUserView.setText(currentDevice.usr,TextView.BufferType.EDITABLE);
			mPasswordView.setText(currentDevice.pwd,TextView.BufferType.EDITABLE);
			EditMode =  true;
			save.setText(R.string.edit);
	    }
	    setsnniper();

	}
	
    private void setsnniper() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        
        // Spinner click listener
        spinner.setOnItemSelectedListener((OnItemSelectedListener) this);
        
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        
        if(dh.ODNetworkExists())
        {
        	if(EditMode == true)
        	{
		        if(currentDevice.odType == 1)
		        {
		        	categories.add("ODNetwork");
		        	categories.add("ODControl");
		        }
		        else
		        {
		        	categories.add("ODControl");
		        }
        	}
        	else
        	{
        		categories.add("ODControl");
        	}
        }
        else
        {
        	if(EditMode == true)
        	{
        		categories.add("ODControl");
        		categories.add("ODNetwork");
        	}
        	else
        	{
		        categories.add("ODNetwork");
		        categories.add("ODControl");
        	}
        }
        
		
        // Creating adapter for spinner
		ArrayAdapter<String> odTypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
		
		// Drop down layout style - list view with radio button
		odTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// attaching data adapter to spinner
		spinner.setAdapter(odTypesAdapter);
		//odTypesAdapter.setDropDownViewResource(currentDevice.odType);
		
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// On selecting a spinner item
    	
		String item = parent.getItemAtPosition(position).toString();
		  
		deviceType = dh.getODDeviceType(item).id;

		
	}
	
	public void onClick(View v){
		
		if (checkFields())
		{
			switch(v.getId()){
			case R.id.button_accept:
				View editText1 = (EditText) findViewById(R.id.edit_devicename);
				View editText2 = (EditText) findViewById(R.id.edit_host);
				View editText3 = (EditText) findViewById(R.id.edit_user);
				View editText4 = (EditText) findViewById(R.id.edit_password);	
				String myEditText1=((TextView) editText1).getText().toString();
				String myEditText2=((TextView) editText2).getText().toString();
				String myEditText3=((TextView) editText3).getText().toString();
				String myEditText4=((TextView) editText4).getText().toString();
				
				if (currentDevice == null)
					currentDevice = new Device();
				
				currentDevice.name = myEditText1;
				currentDevice.url = myEditText2;
				currentDevice.usr = myEditText3;
				currentDevice.pwd = myEditText4;
				currentDevice.location = location;
				currentDevice.odType = deviceType;
				
	
			try
			{
				if (EditMode == false)
				{
					this.dh.createDevice(currentDevice);
				}
				else
				{
					int result = this.dh.updateDevice(currentDevice);

				}
			}
			
			catch(Exception ex)
			{
				String msg = ex.getMessage();
			}

	
	            break;
	
			}
			if (EditMode == false)
			{
				DialogFragment  newdialog = new AddNewDeviceFragment();
				newdialog.show(getSupportFragmentManager(), "newdevice");
			}
			else
			{
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			}
				
		}
	} 


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_device, menu);
		return true;
	}
	
	/**
	 * Check if fields match defined rules and if deviceName or url already exist
	 * @return
	 */
	public boolean checkFields() {
		
		mDeviceName = mDeviceNameView.getText().toString();
		mHost = mHostView.getText().toString();
		mUser = mUserView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		
		// check device name field
		if (TextUtils.isEmpty(mDeviceName)) {
			mDeviceNameView.setError(getString(R.string.error_required_field));
			focusView = mDeviceNameView;
			cancel = true;
		}
		if (EditMode == false && dh.DeviceExists(mDeviceName,"name")) {
			mHostView.setError(getString(R.string.error_existing_device));
			if (!cancel) {
				focusView = mDeviceNameView;
				cancel = true;
			}
		}
		
		// check server field
		if (TextUtils.isEmpty(mHost)) {
			mHostView.setError(getString(R.string.error_required_field));
			if (!cancel) {
				focusView = mHostView;
				cancel = true;
			}
		}
		if (!URLUtil.isValidUrl(mHost)){
			mHostView.setError(getString(R.string.error_invalid_url));
			focusView = mHostView;
			cancel = true;
		}
		if (EditMode == false && dh.DeviceExists(mHost,"url")) {
			mHostView.setError(getString(R.string.error_existing_device));
			if (!cancel) {
				focusView = mHostView;
				cancel = true;
			}
		}
		
		// check user field
		if (TextUtils.isEmpty(mUser)) {
			mUserView.setError(getString(R.string.error_required_field));
			if (!cancel) {
				focusView = mUserView;
				cancel = true;
			}			
		} 
		
		// check password field
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_required_field));
			if (!cancel) {
				focusView = mPasswordView;
				cancel = true;
			}			
		} 
		
		if (cancel) {
			// on error do not save the device
			focusView.requestFocus();
			return false;
		} else {
				return true;
		}
	}
	
    public void onUserSelectValue(boolean result) {

		if (result)
		{
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}

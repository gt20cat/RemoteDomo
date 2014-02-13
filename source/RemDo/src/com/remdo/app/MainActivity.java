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

import com.remdo.app.R;
import com.remdo.app.R.id;
import com.remdo.app.R.layout;
import com.remdo.app.R.menu;

import remdo.sqlite.helper.DatabaseHelper;
import remdo.sqlite.model.*;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	public final static String DEVICE_URL = "";
	public final static String DEVICE_USER = "";
	public final static String DEVICE_PASSWORD = "";
	public final static Long DEVICE_ID =(long) 0;
	private  ArrayAdapter<String> adapter;
	ListView deviceList;
	public int idToModify; 
	DatabaseHelper dm;
	
	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	String[] devices;
	String selectedItem;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try
		{
			dm = new DatabaseHelper(getApplicationContext());
		      names2 = dm.selectDevicesAll();
	
		      devices=new String[names2.size()]; 
	
			int x=0;
			String stg;
	
			for (String[] name : names2) {
				stg = name[1];
	
				devices[x]=stg;
				x++;
			}
		}
		catch(Exception ex)
		{
			String aux = ex.getMessage();
			
		}
		adapter = new ArrayAdapter<String>(   
				this,R.layout.device_list_item,   
				devices);

        deviceList=(ListView)findViewById(R.id.deviceList);

        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(new OnItemClickListener(){
        	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				openWebView(devices[position]);
				
			}
        	
        });
        registerForContextMenu(deviceList);
		
	}
	

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		//getMenuInflater().inflate(R.menu.context_device_config, menu);
		getMenuInflater().inflate(R.menu.option_device_new, menu);
		return true;
	}
	
	
	public void openWebView(String pDeviceName) {
		Intent intent = new Intent(this, WebViewActivity.class);
		
		dm = new DatabaseHelper(this);
		
		String url = dm.getDeviceParamByName(pDeviceName,"url");
		String usr = dm.getDeviceParamByName(pDeviceName,"usr");
		String pwd = dm.getDeviceParamByName(pDeviceName,"pwd");
		
		intent.putExtra("DEVICE_URL", url);
		intent.putExtra("DEVICE_USER", usr);
		intent.putExtra("DEVICE_PASSWORD", pwd);
		startActivity(intent);
	}
	
	@Override
	//this is the main activity we cannot go back 
	public void onBackPressed() 
	{
	        moveTaskToBack(true);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	  if (v.getId()==R.id.deviceList) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(devices[info.position]);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_device_config, menu);
	    }
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    
		dm = new DatabaseHelper(this);
		
		String DeviceId = dm.getDeviceParamByName(devices[info.position],"id");
		selectedItem = devices[info.position];
	    
	    switch (item.getItemId()) {
	        case R.id.editOption: //edit
	            editDevice( Integer.parseInt(DeviceId));
	            return true;
	        case R.id.deleteOption: //delete
	            deleteDevice(Integer.parseInt(DeviceId));
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.newOption:
	            CallNewDeviceActivity();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void CallNewDeviceActivity() {
		Intent intent = new Intent(this, EditDeviceActivity.class);
		startActivity(intent);
	}


	private void deleteDevice(final long idDevice) {

		new AlertDialog.Builder(this)
		.setMessage(R.string.delete_warning)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {

		    	dm.deleteDevice((int)idDevice);
		    	try
		    	{
	            adapter.remove(selectedItem);
		    	}
		    	catch(Exception ex)
		    	{
		    		
		    		
		    	}
		    	
	            adapter.notifyDataSetChanged();
	    		Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
	            /*Intent intent = getIntent();
	            finish();
	            startActivity(intent);*/
	    		
		    }})
		 .setNegativeButton(android.R.string.no, null).show();		
	}
	

	private void editDevice(long id) {

		Intent intent = new Intent(this, EditDeviceActivity.class);
		intent.putExtra("DEVICE_ID", id);
		
		startActivity(intent);
		
	}

}

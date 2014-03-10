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
import java.util.Calendar;
import java.util.List;

import remdo.services.GeopositioningService;
import remdo.services.NotificationService;

import com.remdo.app.R;
import com.remdo.app.R.id;
import com.remdo.app.R.layout;
import com.remdo.app.R.menu;

import remdo.sqlite.helper.DatabaseHelper;
import remdo.sqlite.model.*;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * MainActivity class implements RemoteDomo interface features, diplays alerts preview, all configured OD devices and services state.
 */
public class MainActivity extends FragmentActivity {

	public final static String DEVICE_URL = "";
	public final static String DEVICE_USER = "";
	public final static String DEVICE_PASSWORD = "";
	public final static Long DEVICE_ID =(long) 0;
	private  ArrayAdapter<String> adapter;
	
	private static boolean GeoEnabled = false;
	private static int mGeoInterval;
	private static boolean AlertsEnabled = false;
	private static int mAlertsInterval;
	
	private static PendingIntent pendingGeoIntent;
	private static PendingIntent pendingAlertsIntent;
	
	private static boolean AlertsEnables = true;
	ListView deviceList;
	TextView GeoState;
	TextView AlertsState;
	public int idToModify; 
	DatabaseHelper dm;
	
	List<String[]> list = new ArrayList<String[]>();
	List<String[]> names2 =null ;
	String[] devices;
	String selectedItem;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        
        checkServices();
             
        GeoState =(TextView)findViewById(R.id.tv_geo_footer);
        registerForContextMenu(GeoState);
        AlertsState =(TextView)findViewById(R.id.tv_alerts_footer);
        registerForContextMenu(AlertsState);

	}
	/**
	 * This method checks services state, and configure MainActivity layout to be displayed accordingly.
	 */
    private void checkServices() {
        TextView TVGeo=(TextView)findViewById(R.id.tv_geo_footer);
        TextView TVAlerts=(TextView)findViewById(R.id.tv_alerts_footer);
        	    
	    //Comprobamos si el servicio GEO esta iniciado
	    Intent myIntent = new Intent(this, GeopositioningService.class);
        pendingGeoIntent = PendingIntent.getService(this, 0, myIntent, PendingIntent.FLAG_NO_CREATE);
        if (pendingGeoIntent == null) {
        	TVGeo.setTextColor(this.getResources().getColor(R.color.Red));
        	TVGeo.setText(R.string.geo_off);
        	GeoEnabled = false;
        } else {
        	TVGeo.setTextColor(this.getResources().getColor(R.color.Black));
        	TVGeo.setText(R.string.geo_on);
        	GeoEnabled= true;
        }	
        
	    //Comprobamos si el servicio Alerts esta  iniciado
	    myIntent = new Intent(this, NotificationService.class);
	    pendingAlertsIntent = PendingIntent.getService(this, 0, myIntent, PendingIntent.FLAG_NO_CREATE);
        if (pendingAlertsIntent == null) {
        	TVAlerts.setTextColor(this.getResources().getColor(R.color.Red));
        	TVAlerts.setText(R.string.alerts_off);
        	AlertsEnabled = false;
        } else {
        	TVAlerts.setTextColor(this.getResources().getColor(R.color.Black));
        	TVAlerts.setText(R.string.alerts_on);
        	AlertsEnabled= true;
        }
		
	}

	public void onTvGeoFooterClick(View v) {
		
		TextView TVGeo=(TextView)findViewById(R.id.tv_geo_footer);
        if (!GeoEnabled) {
        	if (isGPSorNetworkEnabled())
        	{
        		enableGeopositioning();
        		TVGeo.setTextColor(this.getResources().getColor(R.color.Black));
        		TVGeo.setText(R.string.geo_on);
        		GeoEnabled=true;
        	}
        	else
        	{
        		showAlertGPSDisabled();
        	}
        } else {
        	disableGeopositioning();
        	TVGeo.setTextColor(this.getResources().getColor(R.color.Red));
        	TVGeo.setText(R.string.geo_off);
        	GeoEnabled=false;

        }
    }
	
	public void onTvAlertsFooterClick(View v)
	{
		TextView TVAlerts=(TextView)findViewById(R.id.tv_alerts_footer);
        if (!AlertsEnabled) {
        	enableNotificationsService();
        	TVAlerts.setTextColor(this.getResources().getColor(R.color.Black));
        	TVAlerts.setText(R.string.alerts_on);
        	AlertsEnabled=true;
        }
        else
        {
        	disableNotificationsService();
			TVAlerts.setTextColor(this.getResources().getColor(R.color.Red));
			TVAlerts.setText(R.string.alerts_off);
			AlertsEnabled=true;
        }
		
	}
    
  //region GEO utils
	
    /**
     * Anables geopositioning service with configured minutes span in Services database
     */
	private void enableGeopositioning() {
	    mGeoInterval = dm.getServcieMinutes("Geo");
		long milisegundos = mGeoInterval * 60 * 1000;
						
		Intent intent = new Intent(this, GeopositioningService.class);
        pendingGeoIntent = PendingIntent.getService(this, 0, intent, 0);
		
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); 
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), milisegundos, pendingGeoIntent);
 
        Toast.makeText(this, getString(R.string.started_geo_service), Toast.LENGTH_LONG).show();
	}
	
	private void disableGeopositioning() {
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingGeoIntent);
        pendingGeoIntent = null;        
		
        Toast.makeText(this, getString(R.string.stopped_geo_service), Toast.LENGTH_LONG).show();
	}
	
	private Boolean isGPSorNetworkEnabled() {
		Boolean enabled = false;
		
		//Accedemos a los servicios de localización del sistema
		LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		
		//Obtenemos el estado del GPS y de la red
		Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

         if (isGPSEnabled || isNetworkEnabled) 
        	 enabled = true;
         
         return enabled;
	}
	
	private void showAlertGPSDisabled() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.error));
		builder.setMessage(getString(R.string.gps_not_enabled));
		builder.setCancelable(false);       
		builder.setIcon(R.drawable.ic_alerts_and_warnings);    
		builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});   
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	//endregion GEO

	//Region Alerts
	/**
	 * Enables Alerts service with configured minutes span in Services database
	 */
	private void enableNotificationsService() {
		mAlertsInterval = dm.getServcieMinutes("Alerts");
		long milisegundos = mAlertsInterval * 60 * 1000;
			
		Intent intent = new Intent(this, NotificationService.class);
        pendingAlertsIntent = PendingIntent.getService(this, 0, intent, 0);
		
		
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); 
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), milisegundos, pendingAlertsIntent);
 
        Toast.makeText(this, getString(R.string.started_alerts_service), Toast.LENGTH_LONG).show();
	}
	
	private void disableNotificationsService() {
		 AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingAlertsIntent);
        pendingAlertsIntent = null;        
		
        Toast.makeText(this, getString(R.string.stopped_alerts_service), Toast.LENGTH_LONG).show();
	}
	
	//End region Alerts
	
	/**
	 * This method call to WebView activity in order to display pDeviceName
	 * @param pDeviceName device name displayed in device list
	 */
	public void openWebView(String pDeviceName) {
		Intent intent = new Intent(this, WebViewActivity.class);
		
		dm = new DatabaseHelper(this);
		
		String url = dm.getDeviceParamByName(pDeviceName,"url");
		String usr = dm.getDeviceParamByName(pDeviceName,"usr");
		String pwd = dm.getDeviceParamByName(pDeviceName,"pwd");
		String odTypeId = dm.getDeviceParamByName(pDeviceName,"odTypeId");

		intent.putExtra("DEVICE_URL", url);
		intent.putExtra("DEVICE_USER", usr);
		intent.putExtra("DEVICE_PASSWORD", pwd);
		intent.putExtra("DEVICE_ODTYPEID", Integer.parseInt(odTypeId));
				
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
	  if (v.getId()==R.id.tv_geo_footer) {		  
			DialogFragment  newdialog = new ServiceConfigFragment();
		    Bundle args = new Bundle();
		    args.putInt("service", R.id.tv_geo_footer);
		    args.putInt("minutes", dm.getServcieMinutes("Geo"));
			newdialog.setArguments(args);
			newdialog.show(getSupportFragmentManager(),null);
		  
		}
	  if (v.getId()==R.id.tv_alerts_footer) {
			DialogFragment  newdialog = new ServiceConfigFragment();
		    Bundle args = new Bundle();
		    args.putInt("service", R.id.tv_alerts_footer);
		    args.putInt("minutes", dm.getServcieMinutes("Alerts"));
			newdialog.setArguments(args);
			newdialog.show(getSupportFragmentManager(), null);

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
	        case R.id.newOption:
	            CallNewDeviceActivity();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	private void CallNewDeviceActivity() {
		Intent intent = new Intent(this, EditDeviceActivity.class);
		startActivity(intent);
	}

	/**
	 * Removes from database specified device
	 * @param idDevice identifier of the device to be removed.
	 */
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
	    		
		    }})
		 .setNegativeButton(android.R.string.no, null).show();		
	}
	
	/**
	 * Calls to EditDeviceActivity in order to edit specified device configuration
	 * @param id Identifier of the device to be edited.
	 */
	private void editDevice(long id) {

		Intent intent = new Intent(this, EditDeviceActivity.class);
		intent.putExtra("DEVICE_ID", id);
		
		startActivity(intent);
		
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

	public void restartServcie(int serviceButtonId, int minutes) {
		
    	dm = new DatabaseHelper(getApplicationContext());
    	   	
	    switch (serviceButtonId) {
	    case R.id.tv_alerts_footer:
	    	dm.updateServiceConfig("Alerts",minutes);
	    	disableNotificationsService();
	    	AlertsState.invalidate();
	    	checkServices();
	    	enableNotificationsService();
	    break;
	    case R.id.tv_geo_footer:
	    	dm.updateServiceConfig("Geo",minutes);
	    	disableGeopositioning();
	    	GeoState.invalidate();
	    	checkServices();
	    	enableGeopositioning();
	    break;
	    
	    }
	 }
		
}

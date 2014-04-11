package com.remdo.app;

import java.util.ArrayList;
import java.util.List;

import com.opendomo.notifier.Event;

import remdo.sqlite.helper.DatabaseHelper;
import remdo.sqlite.model.EventCategory;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AlertsListActivity extends FragmentActivity {
	
private  ArrayAdapter<String> adapter;
	
	ListView activityList;
	TextView DeleteAll;
	TextView MarkAllAsRead; 
	DatabaseHelper dm;
	String category;
	
	List<String[]> list = new ArrayList<String[]>();
	List<Event> events =null ;
	String[] categoriNames;
	String selectedItem;
	
	private static TextView tvDeleteAlertsAll;
	private static TextView tvReadAlertsAll;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alerts_list);
		
	    Intent intent = getIntent();
		category = intent.getStringExtra("CATEGORY_NAME");
		
		TextView tvTitle = (TextView)findViewById(R.id.categoryList);
		tvTitle.setText(category);
		
		try
		{
			dm = new DatabaseHelper(getApplicationContext());
			events = dm.selectActivityByCategory(category);
	
			categoriNames=new String[events.size()]; 
	
			int x=0;
			String stg;
	
			for (Event ev : events) {
				if (ev.getRead()== 1)
				{
					stg = ev.getDay() + " - "+ ev.getTime() + "\r\n"+ ev.getMessage();
				}
				else
				{
					stg ="NEW \r\n" + ev.getDay() + " - "+ ev.getTime() + "\r\n"+ ev.getMessage();
					
				}
	
				categoriNames[x]=stg;
				x++;
			}
		}
		catch(Exception ex)
		{
			String aux = ex.getMessage();
			
		}
		adapter = new ArrayAdapter<String>(   
				this,R.layout.device_list_item,   
				categoriNames);

		activityList=(ListView)findViewById(R.id.alertsList);

		activityList.setAdapter(adapter);
        
        try
        {
        tvDeleteAlertsAll = (TextView)findViewById(R.id.tv_delete_alerts_all);
        registerForContextMenu(tvDeleteAlertsAll);
        tvReadAlertsAll = (TextView)findViewById(R.id.tv_read_alerts_all);
        registerForContextMenu(tvReadAlertsAll);
		}
		catch(Exception ex)
		{
			String aux = ex.getMessage();
			
		}
        
		try
		{
			dm = new DatabaseHelper(getApplicationContext());
			dm.markActivityReadByCategory(category);
	
		}
		catch(Exception ex)
		{
			String aux = ex.getMessage();
			
		}
        
        
	}
	
	public void onTvDeleteAllClick(View v) {		
		dm = new DatabaseHelper(getApplicationContext());
		dm.deleteAlertsByCategory(category);
		finish();
		startActivity(getIntent());
		
	}

}

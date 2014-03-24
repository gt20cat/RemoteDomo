package com.remdo.app;

import java.util.ArrayList;
import java.util.List;

import remdo.sqlite.helper.DatabaseHelper;
import remdo.sqlite.model.EventCategory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AlertsCategoryActivity extends FragmentActivity {

	
	private  ArrayAdapter<String> adapter;
	
	ListView categoryList;
	TextView DeleteAll;
	TextView MarkAllAsRead;
	public int CategoryToOpen; 
	DatabaseHelper dm;
	
	List<String[]> list = new ArrayList<String[]>();
	List<EventCategory> categories =null ;
	String[] categoriNames;
	String selectedItem;
	
	private static TextView tvDeleteAll;
	private static TextView tvReadAll;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alerts_category);
		
		try
		{
			dm = new DatabaseHelper(getApplicationContext());
			categories = dm.selectCategoriesAll();
	
			categoriNames=new String[categories.size()]; 
	
			int x=0;
			String stg;
	
			for (EventCategory cat : categories) {
				stg = cat.unreadAlerts + " - "+ cat.name;
	
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

		categoryList=(ListView)findViewById(R.id.categoryList);

		categoryList.setAdapter(adapter);
		categoryList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				openAlertsListActivity(categoriNames[position]);
			}
        });
        registerForContextMenu(categoryList);
        
        
        tvDeleteAll = (TextView)findViewById(R.id.tv_delete_all);
        registerForContextMenu(tvDeleteAll);
        tvReadAll = (TextView)findViewById(R.id.tv_read_all);
        registerForContextMenu(tvReadAll);
        
	}

	protected void openAlertsListActivity(String string) {
		// TODO Auto-generated method stub
		
	}
	
}

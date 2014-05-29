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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package com.remdo.app;

import java.util.ArrayList;
import java.util.List;

import com.remdo.adapters.Category;
import com.remdo.adapters.CategoryAdapter;

import remdo.sqlite.helper.DatabaseHelper;
import remdo.sqlite.model.EventCategory;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AlertsCategoryActivity extends FragmentActivity {

	
	private  CategoryAdapter adapter;
	
	ListView categoryList;
	TextView DeleteAll;
	TextView MarkAllAsRead;
	public int CategoryToOpen; 
	DatabaseHelper dm;
	
	List<String[]> list = new ArrayList<String[]>();
	List<EventCategory> categories =null ;
	EventCategory[] categoriNames;
	String selectedItem;
	
	private static TextView tvDeleteAll;
	private static TextView tvReadAll;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alerts_category);
		super.onCreate(savedInstanceState);
		TextView tvTitle = (TextView)findViewById(R.id.selectionCategory);
		tvTitle.setText(R.string.alerts_cotegories_title);
		
		try
		{
			dm = new DatabaseHelper(getApplicationContext());
			categories = dm.selectCategoriesAll();
	
			categoriNames = categories.toArray(new EventCategory[categories.size()]);

		}
		catch(Exception ex)
		{
			String aux = ex.getMessage();
			
		}
		adapter = new CategoryAdapter(this,
				R.layout.category_list_item,   
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
        
        /*http://www.ezzylearning.com/tutorial.aspx?tid=1763429*/
        
        tvDeleteAll = (TextView)findViewById(R.id.tv_delete_all);
        registerForContextMenu(tvDeleteAll);
        tvReadAll = (TextView)findViewById(R.id.tv_read_all);
        registerForContextMenu(tvReadAll);
		Log.v("Example", "onCreate");
		getIntent().setAction("Already created");
		
	}
	
	protected void onResume()
	{

	    String action = getIntent().getAction();
	    // Prevent endless loop by adding a unique action, don't restart if action is present
	    if(action == null || !action.equals("Already created")) {
		
	        Log.v("Example", "Force restart");
	        Intent intent = new Intent(this, AlertsCategoryActivity.class);
	        startActivity(intent);
	        finish();
	    }
	    // Remove the unique action so the next time onResume is called it will restart
	    else
	        getIntent().setAction(null);

	    super.onResume();
		
	}
	

	protected void openAlertsListActivity(EventCategory cat) {
		
		Intent intent = new Intent(this, AlertsListActivity.class);
		
		intent.putExtra("CATEGORY_NAME", cat.name);
				
		startActivity(intent);
		
	}
	
	public void onTvDeleteAllClick(View v) {		
		dm = new DatabaseHelper(getApplicationContext());
		dm.deleteAlertsAll();
		finish();
		startActivity(getIntent());
		
	}
	public void onTvReadAllClick(View v) {
		
		dm = new DatabaseHelper(getApplicationContext());
		dm.markAsReadAlertsAll();
		finish();
		startActivity(getIntent());
	}
	
	
}

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

import remdo.sqlite.helper.DatabaseHelper;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * @author GerardT
 * Class to handle warnings on application flow
 */

public class ServiceConfigFragment extends DialogFragment {
	private int serviceButtonId;
	private int minutes;
	private LayoutInflater inflater;
	private View v;
    @SuppressLint("NewApi")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
    	String Title = "";
    	serviceButtonId = getArguments().getInt("service");
    	minutes = getArguments().getInt("minutes");
    		
    	if (serviceButtonId == R.id.tv_geo_footer)
    		Title = getString(R.string.geo_title) + ' ' + getString(R.string.configuration);
    	else
    		Title = getString(R.string.alerts_title) + ' ' + getString(R.string.configuration);
    	
    	inflater = getActivity().getLayoutInflater();
    	v = inflater.inflate(R.layout.service_config_dialog, null) ;
		EditText et = (EditText)v.findViewById(R.id.etMinutes);
		et.setText(String.valueOf(minutes));
    	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.service_config_message)
        		.setTitle(Title)
        		.setView(v)
                .setPositiveButton(R.string.restart_service, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	Intent intent = getActivity().getIntent();
                	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                	
                	startActivity(intent);
                	MainActivity callingActivity = (MainActivity) getActivity();
                	
                	EditText et = (EditText)v.findViewById(R.id.etMinutes);
                	
                		callingActivity.restartServcie(serviceButtonId,Integer.parseInt(et.getText().toString()));      
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	MainActivity callingActivity = (MainActivity) getActivity();
                    callingActivity.onUserSelectValue(false);
                   }
               });

        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

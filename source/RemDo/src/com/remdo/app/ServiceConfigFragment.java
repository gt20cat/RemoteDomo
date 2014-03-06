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

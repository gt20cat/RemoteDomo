package com.example.remdo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;



public class EditDeviceActivity extends FragmentActivity  implements OnClickListener {
	private DataManipulator dh;     
	
	private String mDeviceName;
	private String mHost;
	private String mUser;
	private String mPassword;
	
	//Referencias UI
	private EditText mDeviceNameView;
	private EditText mHostView;
	private EditText mUserView;
	private EditText mPasswordView;
	private TextView mLoginStatusMessageView;
	private DialogFragment dialog; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_device);
		
        View save = findViewById(R.id.button_accept);
		save.setOnClickListener(this);
		
		//Referencias UI
		mDeviceNameView  = (EditText) findViewById(R.id.edit_devicename);
		mHostView = (EditText) findViewById(R.id.edit_host);
		mUserView = (EditText) findViewById(R.id.edit_user);
		mPasswordView = (EditText) findViewById(R.id.edit_password);


		
		/*Codigo comentado, es mejor hacer qye la clase implements OnClickListener
		 * Button buttonAccept = (Button)findViewById(R.id.button_accept);
		buttonAccept.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v) {
        		attemptSave();
        	}
        });	*/
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
	
				this.dh = new DataManipulator(this);
				this.dh.insert(myEditText1,myEditText2,myEditText3,myEditText4);
	
	
	            break;
	
			}
			DialogFragment  newdialog = new AddNewDeviceFragment();
			newdialog.show(getSupportFragmentManager(), "newdevice");
					
			//showDialog(DIALOG_ID);
		}
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_device, menu);
		return true;
	}
	
	public boolean checkFields() {
		
		mDeviceName = mDeviceNameView.getText().toString();
		mHost = mHostView.getText().toString();
		mUser = mUserView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		
		
		// check server field
		if (TextUtils.isEmpty(mDeviceName)) {
			mDeviceNameView.setError(getString(R.string.error_required_field));
			focusView = mDeviceNameView;
			cancel = true;
		}
		
		// check server field
		if (TextUtils.isEmpty(mHost)) {
			mHostView.setError(getString(R.string.error_required_field));
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
			// Si se produce un error no realizamos el login
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
	
	
/*	protected final Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		switch(id) {
		case DIALOG_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Information saved successfully ! Add Another Info?")
			.setCancelable(false)
			.setPositiveButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					EditDeviceActivity.this.finish();

              }
			})
			.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create(); 
			dialog = alert;
			break;

		default:

		}
		return dialog;
	}*/


	
	

}

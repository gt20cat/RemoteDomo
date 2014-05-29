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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author GerardT
 * Class to handle warnings on application flow
 */

public class WarningFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	 
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	 
	    builder.setTitle("Information")
	            .setIcon(
	                    getResources().getDrawable(
	                            android.R.drawable.ic_dialog_info))
	            .setMessage("Message saved as draft.")
	            .setNeutralButton(android.R.string.ok, new OnClickListener() {
	 
	                @Override
	                public void onClick(DialogInterface arg0, int arg1) {
	 
	                }
	            });
	 
	    return builder.create();
	}

}

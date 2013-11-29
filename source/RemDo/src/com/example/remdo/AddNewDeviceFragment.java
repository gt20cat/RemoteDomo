package com.example.remdo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AddNewDeviceFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.add_new_device)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
               		EditDeviceActivity callingActivity = (EditDeviceActivity) getActivity();
                    callingActivity.onUserSelectValue(true);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
              		EditDeviceActivity callingActivity = (EditDeviceActivity) getActivity();
                    callingActivity.onUserSelectValue(false);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
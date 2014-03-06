/* ************************************************************************  
 * Copyright © 2013 Maite Calpe Miravet 
 * ************************************************************************ 
 * This file is part of OpenDomo Notifier.
 *
 * OpenDomo Notifier is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License v3 
 * published by the Free Software Foundation.
 *
 * OpenDomo Notifier is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License v3 for more details.
 *
 * You should have received a copy of the GNU General Public License v3
 * along with OpenDomo Notifier. If not, see <http://www.gnu.org/licenses/>
 * ************************************************************************/

package com.opendomo.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertUtilities {
	
	/**
	 * Muestra una sencilla alerta.
	 * 
	 * @param context - Contexto desde el que se lanza la alerta.
	 * @param title	- Título de la alerta.
     * @param message - Mensaje de la alerta.
     * @param iconId - Icono a mostrar.
     * @param textButton - Texto a mostrar en el botón.
	 */	
	public static void showAlertDialog(Context context, String title, String message, 
		   int iconId, String textButton) { 
	   
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(false);       
		if (iconId > 0) builder.setIcon(iconId); 
       
		builder.setNegativeButton(textButton, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
       
		AlertDialog alert = builder.create();
		alert.show();
   }	
}

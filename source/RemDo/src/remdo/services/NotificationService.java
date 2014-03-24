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

package remdo.services;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import remdo.sqlite.helper.DatabaseHelper;

import com.opendomo.notifier.Event;
import com.opendomo.notifier.EventsList;
import com.opendomo.notifier.EventsXmlParser;
import com.opendomo.utilities.AlertUtilities;
import com.opendomo.utilities.Login;
import com.opendomo.utilities.NetworkUtilities;
import com.opendomo.utilities.StringUtilities;
import com.remdo.app.AlertsCategoryActivity;
import com.remdo.app.ProjectApplication;
import com.remdo.app.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothClass.Device;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;


/**
 * Servicio de notificación.<p>
 * 
 * Este servicio comprobará cada x minutos, según configuración, 
 * si el modulo odEvents del servidor Opendomo ha registrado algún evento. 
 * Si así es, el servició creará una notificación por cada evento leído
 * en la barra de notificaciones.<p>
 * En cada notificación se mostrará la descripción del evento y
 * el elemento que lo ha originado.<p>
 * La lectura de los eventos del servidor OpenDomo se realizará
 * mediante una petición HTTP. Así pues, esta actividad requiere
 * conexión a Internet. 
 *  
 * @see <a href="http://es.opendomo.org/odevents">Gestión de eventos en servidor OpenDomo</a>  
 * @version  1.0, 30/05/2013
 * @version  2.0, 07/03/2014 - Gerard Torrents se añaden pequeñas modiicaciones 
 * para trabajar con RemoteDomo
 */

public class NotificationService extends Service {

	private static String mHost;
	private static int notificationId = 1;
	DatabaseHelper dm;
	
	//Almacenaremos la tarea de lectura de eventos para garantizar que
	//podemos cancelarla si así lo solicitamos
	private ReadEventsTask mREventsTask = null;	
	
    @Override
    public void onCreate() {
    	//Toast.makeText(this, "NotificationService.onCreate()", Toast.LENGTH_LONG).show();
    	
    	//Establecemos el administrador de cookies por defecto
	    ProjectApplication applicationContext = ((ProjectApplication)getApplicationContext());
	    CookieManager cookieManager = applicationContext.getCookieManager();
		CookieHandler.setDefault(cookieManager);
    	
		//Obtenemos las preferencias compartidas
		SharedPreferences preferencesLogin = getSharedPreferences("login", MODE_PRIVATE);
		//mHost = preferencesLogin.getString("host", null);
		
    	dm = new DatabaseHelper(applicationContext);
    	mHost = dm.getDevicebyDevType(1,0).url;
    }

    @Override
    public IBinder onBind(Intent intent) {
    	//Toast.makeText(this, "NotificationService.onBind()", Toast.LENGTH_LONG).show();
    	return null;
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	//Toast.makeText(this, "NotificationService.onDestroy()", Toast.LENGTH_LONG).show();
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        //Toast.makeText(this, "NotificationService.onStartCommand()", Toast.LENGTH_LONG).show();
        	               
        readEventsAndDisplayNotifications();
        //Devolviendo START_STICKY garantizamos que el servicio continua ejecutandose
        //hasta que explícitamente lo paremos
        return START_STICKY;
    }
    
	private void readEventsAndDisplayNotifications() {
		if (mREventsTask != null) return;

		//Ejecutamos una tarea en background que leerá los eventos
	    mREventsTask = new ReadEventsTask();
	    mREventsTask.execute(mHost);
	}
	
	/**
     * Tarea asíncrona de lectura de eventos del servidor OpenDomo.<p>
     * La lectura de los eventos se realizará mediante una petición HTTP GET.<p> 
     * El XML obtenido se analizará y se convertirá en una lista de obtetos Event.
     */
  	public class ReadEventsTask extends AsyncTask<String, Void, String> {
  		
  		private Login LoginTask = null;
  		@Override
  		protected String doInBackground(String... params) {
  			
   			String host = params[0].toString();
  			
			Boolean success = false;
  			String respuesta = null;
  			HttpURLConnection urlConnection = null;
  			try {
  				
  				int reintentos = 2;
  				do{
  	  				URL url = new URL(host + "/cgi-bin/od.cgi/tools/showEvents.sh?&GUI=XML");
  	  				urlConnection = (HttpURLConnection) url.openConnection();
  	  				urlConnection.setRequestMethod("GET");
  	  				urlConnection.setDoInput(true);
  	  				urlConnection.setDoOutput(true);
  					
	  				int responseCode = urlConnection.getResponseCode();
					if ((responseCode >= 200) && (responseCode<300)) {					
						//Si existe una cookie de respuesta llamada HTSESSID estamos logueados
		  				List<String> cookies = urlConnection.getHeaderFields().get("Set-Cookie");
		  				if (cookies != null) {
		  					for (String cookie : cookies) {
		  						String cookieNameValue = cookie.substring(0, cookie.indexOf(';'));
		  						String cookieName = cookieNameValue.substring(0, cookieNameValue.indexOf('='));
		  						if (cookieName.equals("HTSESSID")) {
		  							success = true;
		  							break;
		  						}
		  					}
		  				}
						else
  						{
  					    	dm = new DatabaseHelper(getApplicationContext());
  					    	remdo.sqlite.model.Device dev = dm.getDevicebyDevType(1,0);
  					  
  							//Recopilamos los parametros
  							ArrayList<String> connParams = new ArrayList<String>();
  							connParams.add(dev.url);
  							connParams.add(dev.usr);
  							connParams.add(dev.pwd);
  							//Ejecutamos una tarea en background que realizará el intento de inicio de sesion
  							LoginTask = new Login();
  							LoginTask.execute(connParams);
  							reintentos = reintentos -  1;
  							try {
  								Thread.sleep(500);
  							} catch (InterruptedException e) {
  								// TODO Auto-generated catch block
  								e.printStackTrace();
  							}
  						}
					}
  				}while(reintentos > 0 && success == false);
				
				if (success) {
					InputStream is = new BufferedInputStream(urlConnection.getInputStream());					
					respuesta = StringUtilities.convertStreamToString(is);
				}
   			} 
  			catch (MalformedURLException e) { } 
  			catch (UnsupportedEncodingException e) { } 
  			catch (IOException e) {}
  			finally {
  				if (urlConnection != null) 
  					urlConnection.disconnect(); 
  			}  			
  						
  			return respuesta;
  		}
  		  		
  		@Override
  		protected void onPreExecute() {
  			if (!NetworkUtilities.isNetworkAvailable(NotificationService.this)) {
  				//Toast.makeText(getApplicationContext(), "NotificationService. Conexión a Internet no disponible", Toast.LENGTH_LONG).show();
  				cancel(true);
  			}
  		}

  		@Override
  		protected void onPostExecute(final String notificacionesXML) {
  			mREventsTask = null;
  			
  			if (!TextUtils.isEmpty(notificacionesXML)) {
				try {
					InputStream stream = new ByteArrayInputStream(notificacionesXML.getBytes("UTF-8"));
					EventsList events = EventsXmlParser.parse(stream);
					saveNotifications(events);
				} 
				catch (XmlPullParserException e) {} 
				catch (IOException e) {}     			
          	}
  		}
  		
  		@Override
		protected void onCancelled() {
  			mREventsTask = null;
		}
  	}
  	
  	public void saveNotifications (EventsList events) {
  		dm = new DatabaseHelper(getApplicationContext());
   		Iterator<Event> iter = events.getIterator();
  		int notif= 1;
   		while (iter.hasNext()) {
  			Event event = iter.next();
  			if (!dm.existsEvent(event))
  			{
  				dm.insertEvent(event);
  			}
  			notif++;
  		}
			displayNotification(notif);
  	}
  	
  	
  	/**
	 * Muestra una notificación en la barra de notificaciones
	 * 
	 * @param event - Objeto de tipo Event.
	 * @param identifier - Identificador de la notificación.
	 */
  	public void displayNotification (Event event, int identifier) {
  		NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE); 
  		
  		Intent i = new Intent(this, AlertsCategoryActivity.class);
  		//i.putExtra("com.opendomo.notifier.notificationID", 1);
  		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
                  
        CharSequence ticker ="Nuevos eventos OpenDomo";
        CharSequence contentTitle = "OpenDomo (" + event.getTransmitter() + ")";
        CharSequence contentText = event.getMessage();
        Notification noti = new NotificationCompat.Builder(getApplicationContext())
                                 .setContentIntent(pendingIntent)
                                 .setTicker(ticker)
                                 .setContentTitle(contentTitle)
                                 .setContentText(contentText)
                                 .setSmallIcon(R.drawable.ic_notificaciones_blanco)
                                 .addAction(R.drawable.ic_notificaciones_blanco, ticker, pendingIntent)
                                 .build();
        nManager.notify(identifier, noti);         
  	}

  	
  	/**
	 * Muestra una notificación en la barra de notificaciones
	 * 
	 * @param quantity - Número de notificaciones nuevas
	 */
  	public void displayNotification (int quantity) {
  		NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE); 
  		
  		Intent i = new Intent(this, AlertsCategoryActivity.class);

  		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
                  
        CharSequence ticker = quantity +" New OpenDomo events";
        CharSequence contentTitle = "RemoteDomo";
        CharSequence contentText = quantity +" New OpenDomo events.";
        Notification noti = new NotificationCompat.Builder(getApplicationContext())
                                 .setContentIntent(pendingIntent)
                                 .setTicker(ticker)
                                 .setContentTitle(contentTitle)
                                 .setContentText(contentText)
                                 .setSmallIcon(R.drawable.ic_notificaciones_blanco)
                                 .addAction(R.drawable.ic_notificaciones_blanco, ticker, pendingIntent)
                                 .build();
        nManager.notify(1, noti);         
  	}

}

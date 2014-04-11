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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import remdo.sqlite.helper.DatabaseHelper;

import com.opendomo.utilities.NetworkUtilities;
import com.remdo.app.ProjectApplication;
import com.remdo.app.R;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Servicio de geoposicionamiento.<p>
 * 
 * Este servicio enviará cada x minutos, según configuración, las coordenadas 
 * del dispositivo (latitud y longitud) al servidor OpenDomo.<p>
 * Para averiguar la ubicación se utilizaran tanto las redes inalámbricas 
 * como los satélites GPS. Dichas características deben estar activadas
 * en el dispositivo Android.<p>
 * El envío de las coordenadas al servidor se realizará mediante una 
 * petición HTTP. Así pues, esta actividad requiere conexión a Internet.
 *  
 * @version  1.0, 30/05/2013
 * @version  2.0, 05/03/2014 - Gerard Torrents se añaden pequeñas modiicaciones 
 * para trabajar con RemoteDomo
 */

public class GeopositioningService extends Service implements LocationListener {
	 
	//Flags de estado del GPS y de la red
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    
    //Ubicacion
    private static LocationManager locationManager; 
    private Location location;
    private double latitude = 0;
    private double longitude = 0;
    private double oldLatitude = 0;
    private double oldLongitude = 0;
    DatabaseHelper dm;
 
    //Distancia minima entre actualizaciones en metros
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
 
    //Tiempo minimo entre actualizaciones en milisegundos
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;    
 	
    //almacenamos la tarea asincrona de envio de la ubicacion al servidor Opendomo
  	private SendLocationTask mSendLocationTask = null;
    
    //Servidor OpenDomo
	private static String mHost;

    @Override
    public void onCreate() {
    	//Toast.makeText(this, "GeopositioningService.onCreate()", Toast.LENGTH_LONG).show();
    	
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
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        getLocationAndSendToServer();
        //Devolviendo START_STICKY garantizamos que el servicio continua ejecutandose
        //hasta que explícitamente lo paremos
        return START_STICKY;
    }
    
    @SuppressWarnings("unchecked")
	private void getLocationAndSendToServer() {
		if (mSendLocationTask != null) return;
		
		//Obtenemos la ubicación
		getLocation();
		
		//Enviamos las coordenadas al servidor solo si hemos cambiado de ubicación
		//if (!(latitude == oldLatitude && longitude == oldLongitude)) {		
			//Recopilamos los parametros
			ArrayList<String> params = new ArrayList<String>();
			params.add(mHost);
			params.add(Double.toString(latitude));
			params.add(Double.toString(longitude));
	
			//Ejecutamos una tarea en background que enviará la ubicación
			mSendLocationTask = new SendLocationTask();
			mSendLocationTask.execute(params);
		//}
	}

    /**
     * Funcion que nos permite obtener la ubicación mediante las 
     * redes inalámbricas o los satélites GPS.
     */
    public Location getLocation() {
    	try {
    		locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
 
    		//Obtenemos el estado del GPS
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            //Obtenemos el estado de la red
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (isGPSEnabled || isNetworkEnabled) {

            	//Primero obtenemos la localizacion desde el proveedor de red
                if (isNetworkEnabled) {
                	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                        	latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                
                //Si el GPS esta habilitado y no hemos sido capaces de encontrar la ubicacion
                //mediante el proveedor de red, localizamos las coordenadas mediante el proveedor GPS
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
    }

    /**
     * Funcion para dejar de usar el GPS en la aplicación
     */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }       
    }
    
    /**
     * Tarea asíncrona de envío de la ubicación del terminal (latitud y longitud) 
     * al servidor Opendomo.<p> 
     * El envío de las coordenadas al servidor se realizará mediante 
     * una petición HTTP POST.
     */
  	public class SendLocationTask extends AsyncTask<List<String>, Void, Boolean> {
  		
  		@Override
  		protected Boolean doInBackground(List<String>... params) {
 			
  			String host = params[0].get(0).toString();
			String latitude = params[0].get(1).toString();
			String longitude = params[0].get(2).toString();
  			
			Boolean success = false;
  			HttpURLConnection urlConnection = null;
  			try {
  				//"http://192.168.1.47/cgi-bin/geopos.cgi?lon=MMM&lat=YYY&hash=ZZZ"
  				URL url = new URL( host + "/cgi-bin/geopos.cgi?lon=" + longitude + "&lat=" + latitude);
  				urlConnection = (HttpURLConnection) url.openConnection();
  				urlConnection.setRequestMethod("POST");
  				urlConnection.setDoInput(true);
  				urlConnection.setDoOutput(true);  		
  				
				String paramsRequest = "LAT=" + latitude + "&LON=" + longitude;
				
				urlConnection.setFixedLengthStreamingMode(paramsRequest.getBytes().length);
				urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				
				OutputStream out = urlConnection.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
				writer.write(paramsRequest);
				writer.flush();
				writer.close();			
				out.close();
  				  				  				
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
				}
   			} 
  			catch (MalformedURLException e) { } 
  			catch (UnsupportedEncodingException e) { } 
  			catch (IOException e) { String i ;
  			i = e.getMessage();
  			int s;} 
  			finally {
  				if (urlConnection != null) 
  					urlConnection.disconnect(); 
  			}  			
  						
  			return success;
  		}
  		  		
  		@Override
  		protected void onPreExecute() {
  			if (!NetworkUtilities.isNetworkAvailable(GeopositioningService.this)) {
  				//Toast.makeText(getApplicationContext(), "NotificationService. Conexión a Internet no disponible", Toast.LENGTH_LONG).show();
  				cancel(true);
  			}
  		}

  		@Override
  		protected void onPostExecute(final Boolean success) {
  			mSendLocationTask = null;
  			
  			//Las variables oldLatitude y oldLongitude nos ayudaran a saber si hemos
  			//cambiado de ubicación.
  			if (success) {
  				oldLatitude = latitude;
  				oldLongitude = longitude;
  			}  			
  		}
  		
  		@Override
		protected void onCancelled() {
  			mSendLocationTask = null;
		}
  	}
  	
  	//****** Service ********
    @Override
    public IBinder onBind(Intent intent) {
    	//Toast.makeText(this, "GeopositioningService.onBind()", Toast.LENGTH_LONG).show();
    	return null;
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Toast.makeText(this, getString(R.string.stopped_geo_service), Toast.LENGTH_LONG).show();
    }
  	
    //****** LocationListener ********
  	@Override
    public void onLocationChanged(Location location) {
     	//Toast.makeText(this, "GeopositioningService.onLocationChanged()", Toast.LENGTH_LONG).show();
    }
  
    @Override
    public void onProviderDisabled(String provider) {
     	//Toast.makeText(this, "GeopositioningService.onProviderDisabled()", Toast.LENGTH_LONG).show();
    }
  
    @Override
    public void onProviderEnabled(String provider) {
     	//Toast.makeText(this, "GeopositioningService.onProviderEnabled()", Toast.LENGTH_LONG).show();
    }
     
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
     	//Toast.makeText(this, "GeopositioningService.onStatusChanged()", Toast.LENGTH_LONG).show();
    }
}
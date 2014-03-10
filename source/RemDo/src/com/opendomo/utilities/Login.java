package com.opendomo.utilities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.remdo.app.R;

//Tarea asíncrona utilizada para autenticar el usuario
public class Login extends AsyncTask<ArrayList<String>, Integer, String> {
		
	//Valores por defecto
	private static final String HOST_DEFAULT = "169.254.0.25";
	private static final String USER_DEFAULT = "admin";
	private static final String PASSWORD_DEFAULT = "opendomo";
	
	//Posibles errores
	private static final int HOST_ERROR = 1;
	private static final int USER_PASSWORD_ERROR = 2;
	
	//Intervalo por defecto entre comprovaciones de notificaciones 
	//y envio de coordenadas
	private static final int INTERVAL_NOTI_DEFAULT = 15;
	private static final int INTERVAL_GEO_DEFAULT = 15;
	

	//Valor de conexión en el momento del intento de inicio de sesión
	private String mHost;
	private String mUser;
	private String mPassword;
	
	@Override
	protected String doInBackground(ArrayList<String>... params) {
		
		String host = params[0].get(0).toString();
		String user = params[0].get(1).toString();
		String password = params[0].get(2).toString();
		
		//Intento de autenticación contra un servidor de red
		//**************************************************
		String sessionId = null;
		HttpURLConnection urlConnection = null;
		try {
							
			
			//"http://192.168.1.47/cgi-bin/od.cgi?GUI=XML"
			URL url = new URL("http://172.25.1.100/cgi-bin/od.cgi?GUI=XML");
			urlConnection = (HttpURLConnection) url.openConnection();
			//urlConnection.setReadTimeout(20000);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
						
			String paramsRequest = "USER=" + URLEncoder.encode(user,"UTF-8") + 
					"&PASS=" + URLEncoder.encode(password,"UTF-8");
			
			urlConnection.setFixedLengthStreamingMode(paramsRequest.getBytes().length);
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			OutputStream out = urlConnection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.write(paramsRequest);
			writer.flush();
			writer.close();			
			out.close();
			
			//Si existe una cookie de respuesta llamada HTSESSID es que se ha logueado correctamente
			List<String> cookies = urlConnection.getHeaderFields().get("Set-Cookie");
			if (cookies != null) {
				for (String cookie : cookies) {
					String cookieNameValue = cookie.substring(0, cookie.indexOf(';'));
					String cookieName = cookieNameValue.substring(0, cookieNameValue.indexOf('='));
					if (cookieName.equals("HTSESSID")) {
						sessionId = cookieNameValue.substring(cookieNameValue.indexOf('=')+1, cookieNameValue.length());
						break;
					}
				}
			}				
			
			if (TextUtils.isEmpty(sessionId)) {
				publishProgress(USER_PASSWORD_ERROR);
			}
			
		} catch (MalformedURLException e) {
			publishProgress(HOST_ERROR);
		} catch (UnsupportedEncodingException e) {
			publishProgress(HOST_ERROR);
		} catch (IOException e) {
			publishProgress(HOST_ERROR);
		} finally {
			if (urlConnection != null) 
				urlConnection.disconnect(); 
		}
					
		return sessionId;
	}		

}

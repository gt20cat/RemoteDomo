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

package com.remdo.app;

import java.net.CookieManager;

import android.app.Application;

/**
 * Extensión de las propiedades de la aplicación.<p>
 * 
 * Forma elegante de poder compartir el administrador de coockies
 * entre todas las actividades y servicios de la aplicación, sin
 * tener que recurrir explícitamente a variables globales<p>.
 * En el AndroidManifest.xml se deberá hacer referencia a esta clase,
 * en vez de a Aplication.
 *  
 * @version  1.0, 30/05/2013
 */

public class ProjectApplication extends Application {
	
	private CookieManager cookieManager;

	public ProjectApplication() {
		super();
		cookieManager = new CookieManager();
	}
	
	public CookieManager getCookieManager() {
		return cookieManager;
	}
}
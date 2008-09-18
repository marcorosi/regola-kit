/**
 * Copyright (C) 2008 nicola 
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.regola.ws;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

public class Server {

	protected Server() throws Exception {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(RestManager.class);
		sf.setResourceProvider(RestManager.class, new SingletonResourceProvider(new RestManager()));
		sf.setAddress("http://localhost:9000/");
		 
		sf.create();
	}

	public static void main(String args[]) throws Exception {
		new Server();
		System.out.println("Server su...");

		Thread.sleep(5 * 60 * 1000);
		System.out.println("Server spento");
		System.exit(0);
	}
	
}

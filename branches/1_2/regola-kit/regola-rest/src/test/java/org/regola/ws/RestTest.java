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

import static org.regola.ws.RestClient.delete;
import static org.regola.ws.RestClient.get;
import static org.regola.ws.RestClient.post;
import static org.regola.ws.RestClient.put;
import static org.regola.xml.JAXBMarshaller.fromXml;
import static org.regola.xml.JAXBMarshaller.toXml;
import junit.framework.TestCase;

public class RestTest extends TestCase {

	// private String url = "http://localhost:8080/prova/services/prova/foo";
	private String url = "http://localhost:9000/nome-servizio/foo";
	
	@Override
	protected void setUp() throws Exception {

		new Server();
		System.out.println("Server avviato...");
		super.setUp();
	}

	public void testURL() throws Throwable {
		String url = RestClient.buildUrl("http://plitvice.unibo.it?id=1&nome=2", 1, "2");

		assertEquals("http://plitvice.unibo.it/1/2?id=1&nome=2", url);
	}

	public void testMethodsCall() throws Throwable {

		String result1 = get(url, 1, "salve!");
		assertEquals("GET di:1:salve!", result1);

		String result2 = delete(url, 1, "salve!");
		assertEquals("DELETE di:1:salve!", result2);

		Dto dto = new Dto("PROVA");
		String dtoXml = toXml("org.regola.ws", "dto", dto);

		String result3 = post(url, dtoXml, 1, "salve!");
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><dto><prova>1:salve!:PROVA</prova><x>0</x></dto>", result3);

		String result4 = put(url, dtoXml, 1, "salve!");
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><dto><prova>1:salve!:PROVA</prova><x>0</x></dto>", result4);

		dto = fromXml("org.regola.ws", result4);
		assertNotNull(dto);
		assertEquals("1:salve!:PROVA", dto.getProva());

	}
	
}

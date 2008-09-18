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

package org.regola.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import com.sun.tools.xjc.generator.bean.ObjectFactoryGenerator;

/**
 * Un piccolo wrapper attorno ai servizi di JAXB, in particolare i metodi per
 * convertire un oggetto in xml e viceversa.<br>
 * <br>
 * 
 * <b>NOTA BENE:</b> Prima di utilizzare queste classi è necessario utilizzare
 * il compilatore di JAXB per annotare le classi coinvolte e produrre un'oggetto
 * del tipo ObjectFactory (si veda {@link ObjectFactoryGenerator}).<br>
 * <br>
 * Ad esempio, partendo da uno schema xml che descrive il documento relativo ad
 * una certa classe, diciamo Dto, bisogna invocare il compilatore di JAXB come
 * segue: <code><pre>
 * xjc schema1.xsd -p it.il.tuo.package -d src/main/java  
 * </pre></code> A questo punto tutte le classi coinvolte saranno create nel
 * package it.il.tuo.package unitamente alla classe ObjectFactory.<br>
 * <br>
 * Infine se per qualche bizzarra circostanza si disponesse solo della classe e
 * non dello schema realtivo, sarà possibile usare il metodo di utilità
 * {@link #generateSchema(Class, String)} per ottenere uno schema di base.
 * 
 * @see {@link http
 *      ://www.javaworld.com/javaworld/jw-06-2006/jw-0626-jaxb.html</a>}
 * 
 * @author nicola
 * 
 */
@SuppressWarnings("unchecked")
public class JAXBMarshaller {

	/**
	 * Effettua l' unmarshall di un documento xml restituiendo un'istanza di T
	 * 
	 * @param <T>
	 *            la classe dell'oggetto da ricavare
	 * @param path
	 *            tipicamente il package di T
	 * @param xml
	 *            l'xml da cui ricavare un'oggetto
	 * @return
	 * @throws JAXBException
	 */
	static public <T> T fromXml(String path, String xml) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(path);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		JAXBElement dtoElement = (JAXBElement) unmarshaller.unmarshal(new StringReader(xml));
		return (T) dtoElement.getValue();
	}

	/**
	 * Effettua il marshall dell'istanza passata trasformandola in xml
	 * 
	 * @param <T>
	 *            il tipo di object
	 * @param path
	 *            tipicamente il package di object
	 * @param localpart
	 *            il nome del tag che rappresenta T
	 * @param object
	 *            l'oggetto da convertire in xml
	 * @return
	 * @throws JAXBException
	 */
	static public <T> String toXml(String path, String localpart, T object) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(path);

		Marshaller marshaller = jaxbContext.createMarshaller();
		JAXBElement element = new JAXBElement(new QName("", localpart), object.getClass(), null, object);

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		StringWriter sw = new StringWriter();
		marshaller.marshal(element, sw);

		return sw.toString();

	}

	/**
	 * Genera la schema usato da JAXB per il passaggio da Java ad Xml di un
	 * certa classe clazz.
	 * 
	 * @param clazz
	 *            il tipo per cui generare lo schema
	 * @param dir
	 *            la cartella dove generare lo schema
	 * @throws Throwable
	 */
	public void generateSchema(Class clazz, String dir) throws Throwable {
		final File baseDir = new File(dir);

		class MySchemaOutputResolver extends SchemaOutputResolver {
			public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
				return new StreamResult(new File(baseDir, suggestedFileName));
			}
		}

		JAXBContext context = JAXBContext.newInstance(clazz);
		context.generateSchema(new MySchemaOutputResolver());
	}


}

package org.regola.codeassistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class Utils {

	public static String getPackagePath(String packageName)
	{
		return packageName.replace('.', '/');
	}
	
	public static String lowerFirstLetter(String s)
	{
		if(s != null && s.length() > 0)
			s = s.replaceFirst("^[A-Z]", String.valueOf(Character.toLowerCase(s.charAt(0))));
		
		return s;
	}

	public static String upperFirstLetter(String s)
	{
		if(s != null && s.length() > 0)
			s = s.replaceFirst("^[a-z]", String.valueOf(Character.toUpperCase(s.charAt(0))));
		
		return s;		
	}
	
	public static boolean existsXPath(File xmlDocument, String xPathExp) {
		SAXBuilder saxBuilder = new SAXBuilder(
				"org.apache.xerces.parsers.SAXParser");

		//File xmlDocument = new File(getProjectDir() + "/" + acFilePath);
 
		if (!xmlDocument.exists())
		{
			throw new RuntimeException("Il file xml "+ xmlDocument +" non esiste.");
		}
		
		Element levelNode;
		try {
			org.jdom.Document jdomDocument = saxBuilder.build(xmlDocument);
			XPath xpath = XPath.newInstance(xPathExp);
			xpath.addNamespace("s",
					"http://www.springframework.org/schema/beans");
			xpath.addNamespace("f",
					"http://java.sun.com/dtd/web-facesconfig_1_1.dtd");
			xpath.addNamespace("webflow",
			"http://www.springframework.org/schema/webflow-config");
			xpath.addNamespace("jaxws", "http://cxf.apache.org/jaxws");
			levelNode = (Element) xpath.selectSingleNode(jdomDocument);
			return levelNode != null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static boolean existsBean(File acFilePath, String beanId) {
		return existsXPath(acFilePath, "/s:beans/s:bean[@id='" + beanId + "']");
	}
	
	public static boolean existsJaxwsEndpoint(File acFilePath, String beanId) {
		return existsXPath(acFilePath, "/s:beans/jaxws:endpoint[@id='" + beanId + "']");
	}
	
	public static boolean existsManagedBean(File acFilePath, String beanId) {
		return existsXPath(acFilePath, "/f:faces-config/f:navigation-rule/f:from-view-id[text()='" + beanId + "']");
	}
	
	public static boolean existsFlow(File acFilePath, String flowPath) {
		return existsXPath(acFilePath, "/webflow:flow-registry/webflow:flow-location[@path='" + flowPath + "']");
	}

	public static String readFileAsString(File file) {
		try {

			StringBuffer fileData = new StringBuffer(1000);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
			return fileData.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

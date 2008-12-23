package org.regola.codeassistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.regola.descriptor.DescriptorService;
import org.regola.descriptor.IClassDescriptor;
import org.regola.descriptor.ReflectionDescriptorFactory;
import org.regola.descriptor.RegolaDescriptorService;
import org.regola.descriptor.TrailsDescriptorService;
import org.regola.util.ValueReader;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * @author  nicola
 */
public class Environment {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private Configuration freemarkerConfiguration;
	private DescriptorService descriptorService;
	private String projectDir = "";
	private String outputDir = "";
	private String packageName = "";
	private String springServiceFileName = "applicationContext-service.xml";
	private String springDaoFileName = "applicationContext-dao.xml";
	private String springTestResourcesFileName = "applicationContext-dao-test.xml";
	private String facesConfigFileName = "faces-config.xml";
	private boolean simulate=false;
	private Map<String,String> simulationMap = new HashMap<String,String>();

	@SuppressWarnings( { "unchecked", "unchecked" })
	public Environment() {
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(getClass(), "/templates");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		setFreemarkerConfiguration(cfg);
		setDescriptorService(new RegolaDescriptorService());
		
		setProjectDir(".");
		
		try {
			setOutputDir(new File(".").getCanonicalPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * @return  the outputDir
	 * @uml.property  name="outputDir"
	 */
	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * @param outputDir  the outputDir to set
	 * @uml.property  name="outputDir"
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * @return  the descriptorService
	 * @uml.property  name="descriptorService"
	 */
	private DescriptorService getDescriptorService() {
		return descriptorService;
	}

	/**
	 * @param descriptorService  the descriptorService to set
	 * @uml.property  name="descriptorService"
	 */
	private void setDescriptorService(DescriptorService descriptorService) {
		this.descriptorService = descriptorService;
	}

	/**
	 * @return  the freemarkerConfiguration
	 * @uml.property  name="freemarkerConfiguration"
	 */
	public Configuration getFreemarkerConfiguration() {
		return freemarkerConfiguration;
	}

	/**
	 * @param freemarkerConfiguration  the freemarkerConfiguration to set
	 * @uml.property  name="freemarkerConfiguration"
	 */
	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
		this.freemarkerConfiguration = freemarkerConfiguration;
	}

	/**
	 * @return  the projectDir
	 * @uml.property  name="projectDir"
	 */
	public String getProjectDir() {
		return projectDir;
	}

	/**
	 * @param projectDir  the projectDir to set
	 * @uml.property  name="projectDir"
	 */
	public void setProjectDir(String projectDir) {
		this.projectDir = projectDir;
	}

	public String getJavaSrcPath() {
		return "src/main/java";
	}
	
	public String getJavaTestSrcPath() {
		return "src/test/java";
	}

	public String getWebSrcPath() {
		return "src/main/webapp";
	}
	
	public String getWebInfPath() {
		return "src/main/webapp/WEB-INF";
	}
	
	public String getFlowPath() {
		return "src/main/webapp/WEB-INF/flows";
	}
	
	public String getResourcePath() {
		return "src/main/resources";
	}
	
	public String getTestResourcePath() {
		return "src/test/resources";
	}

	public void writeFile(String filePath, String fileName, Template template, Object root) {
		writeFile(filePath, fileName, template, root, false);
	}
	
	public void writeToStandardOut(String filePath, String fileName, Template template, Object root, boolean append) 
	{
		try {
			String dirPath = getOutputDir() + "/" + filePath;
			String path = dirPath + fileName;

			log.info("Contenuto da aggiungere al file: " + path);

			template.process(root, new PrintWriter(System.out));
			

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void writeToSimulationMap(String filePath, String fileName, Template template, Object root, boolean append) 
	{
		try {
			String dirPath = getOutputDir() + "/" + filePath;
			String path = dirPath + fileName;

			StringWriter writer = new StringWriter();
			writer.append("// file " + path);

			template.process(root, writer);
			
			simulationMap.put(fileName, writer.toString());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void writeFile(String filePath, String fileName, Template template, Object root, boolean append) {

		if (isSimulate())
		{
			writeToStandardOut(filePath, fileName, template, root, append);
			writeToSimulationMap(filePath, fileName, template, root, append);
			return;
		}
		
		try {
			String dirPath = getOutputDir() + "/" + filePath;
			String path = dirPath + fileName;

			log.info("Scrittura del file " + path);

			File dir = new File(dirPath);
						
			if(!dir.exists())
				dir.mkdirs();
			
			Writer out = new FileWriter(path,append);
			template.process(root, out);
			out.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Template getTemplate(String templateName) {
		try {
			return freemarkerConfiguration.getTemplate(templateName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void writeResource(String fileName, Template template,
			Map<String, Object> root) 
	{
		writeFile(getResourcePath()+"/", fileName, template, root);
	}

	public void writeApplicationProperties(Template template, Map<String, Object> root) 
	{
		//writeFile(getResourcePath()+"/", "ApplicationResources.properties", template, root, true);
		writeFile(getResourcePath()+"/", "ApplicationResources_en.properties", template, root, true);
		writeFile(getResourcePath()+"/", "ApplicationResources_it.properties", template, root, true);
	}
	
	public void writeWebSource(String fileName, Template template, Map<String, Object> parameters) 
	{
		writeFile(getWebSrcPath()+"/", (fileName+".xhtml").toLowerCase(), template, parameters);		
	}
	
	public void writeJavaSource(String packageName, String fileName, Template template,
			Map<String, Object> root) 
	{
		writeJavaSource(packageName, fileName, template, root, null);
	}
	
	public void writeJavaTestSource(String packageName, String fileName, Template template,
			Map<String, Object> root) 
	{
		writeJavaTestSource(packageName, fileName, template, root, null);
	}
	
	
	public void writeJavaSource(String packageName, String fileName, 
			Template template, Map<String, Object> root, Object value) 
	{
		ValueReader reader = new ValueReader(value);
		root.put("reader", reader);

		writeFile(getJavaSrcPath()+"/"+Utils.getPackagePath(packageName)+"/", fileName+".java", template, root);
	}
	
	public void writeJavaTestSource(String packageName, String fileName, 
			Template template, Map<String, Object> root, Object value) 
	{
		ValueReader reader = new ValueReader(value);
		root.put("reader", reader);

		writeFile(getJavaTestSrcPath()+"/"+Utils.getPackagePath(packageName)+"/", fileName+".java", template, root);
	}
	
	public void writeFlowsResource(String flowName, String fileName, 
			Template template, Map<String, Object> root, Object value) 
	{
		ValueReader reader = new ValueReader(value);
		root.put("reader", reader);

		writeFile(getWebInfPath()+"/flows/"+ flowName  +"/", fileName, template, root);
	}

	public void writeServiceEndpoint(String xmlfile, String beanId, Template template, Map<String, Object> parameters) 
	{
		String resource = xmlfile.contains("test") ? getTestResourcePath() :  getResourcePath();
		
		File f = new File(getOutputDir()+"/"+resource+"/"+xmlfile);
		if(!f.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste",xmlfile));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the file doesn't exists -->";
				simulationMap.put(xmlfile, msg);
			}
			
			
			return;
		}
		
		if(existsJaxwsEndpoint(resource+"/"+xmlfile, beanId))
		{
			log.info(String.format("Il file %s non è stato modificato perchè il bean %s è già definito"
					,xmlfile, beanId));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the bean "+ beanId +" is already defined -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}

		
		StringWriter sw = new StringWriter();
		try {
			template.process(parameters, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		String xml = readFileAsString(resource+"/"+xmlfile);
		xml = xml.replaceFirst("</beans>", sw.toString());
		
		writeStringToFile(resource+"/"+xmlfile, xml, false);
	}

	public void writeXmlSource(String xmlfile, String beanId, Template template, Map<String, Object> parameters) 
	{
		String resource = xmlfile.contains("test") ? getTestResourcePath() :  getResourcePath();
		
		File f = new File(getOutputDir()+"/"+resource+"/"+xmlfile);
		if(!f.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste",xmlfile));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the file doesn't exists -->";
				simulationMap.put(xmlfile, msg);
			}
			
			
			return;
		}
		
		if(existsBean(resource+"/"+xmlfile, beanId))
		{
			log.info(String.format("Il file %s non è stato modificato perchè il bean %s è già definito"
					,xmlfile, beanId));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the bean "+ beanId +" is already defined -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}

		
		StringWriter sw = new StringWriter();
		try {
			template.process(parameters, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		String xml = readFileAsString(resource+"/"+xmlfile);
		xml = xml.replaceFirst("</beans>", sw.toString());
		
		writeStringToFile(resource+"/"+xmlfile, xml, false);
	}
	
	public void writeFacesConfig(String xmlfile, String beanId, Template template, Map<String, Object> parameters) 
	{
		File f = new File(getOutputDir()+"/"+getWebSrcPath()+"/WEB-INF/" + xmlfile);
		if(!f.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste",xmlfile));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the file doesn't exists -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}
		
		if(existsManagedBean(getWebSrcPath()+"/WEB-INF/" + xmlfile, beanId))
		{
			log.info(String.format("Il file %s non è stato modificato perchè il bean %s è già definito"
					,xmlfile, beanId));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the file doesn't exists -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}

		StringWriter sw = new StringWriter();
		try {
			template.process(parameters, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		String xml = readFileAsString(getWebSrcPath()+"/WEB-INF/" + xmlfile);
		xml = xml.replaceFirst("</faces-config>", sw.toString());
		
		writeStringToFile(getWebSrcPath()+"/WEB-INF/" + xmlfile, xml, false);
	}

	public void writeFlowConfig( String flowPath, Template template, Map<String, Object> parameters) 
	{
		String xmlfile = "webflow-config.xml";
		String relativePath = getWebInfPath()+ "/config/" + xmlfile;
		String absolutePath = getOutputDir()+"/" + relativePath;
		
		File f = new File(absolutePath);
		if(!f.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste",xmlfile));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the file doesn't exists -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}
		
		if(existsFlow(relativePath, flowPath))
		{
			log.info(String.format("Il file %s non è stato modificato perchè il flusso %s è già definito"
					,xmlfile, flowPath));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the flow is alreay registered  -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}

		StringWriter sw = new StringWriter();
		try {
			template.process(parameters, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		String xml = readFileAsString(relativePath);
		xml = xml.replaceFirst("</webflow:flow-registry>", sw.toString());
		
		writeStringToFile(relativePath, xml, false);
	}

	public void writeWebXmlConfig( String element, Template template, Map<String, Object> parameters) 
	{
		String xmlfile = "web.xml";
		String relativePath = getWebInfPath()+ "/" + xmlfile;
		String absolutePath = getOutputDir()+"/" + relativePath;
		
		File f = new File(absolutePath);
		if(!f.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste",xmlfile));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the file doesn't exists -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}
		
		String xml = readFileAsString(relativePath);
		if( xml.contains(element))
		{
			log.info(String.format("Il file %s non è stato modificato perchè l'elemento %s è già definita"
					,xmlfile, element));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the element is already registered  -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}

		StringWriter sw = new StringWriter();
		try {
			template.process(parameters, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		xml = xml.replaceFirst("</web-app>", sw.toString());
		
		writeStringToFile(relativePath, xml, false);
	}
	
	public void writePortletConfig( String portletName, Template template, Map<String, Object> parameters) 
	{
		String xmlfile = "portlet.xml";
		String relativePath = getWebInfPath()+ "/" + xmlfile;
		String absolutePath = getOutputDir()+"/" + relativePath;
		
		File f = new File(absolutePath);
		if(!f.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste",xmlfile));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the file doesn't exists -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}
		
		String xml = readFileAsString(relativePath);
		if( xml.contains(portletName))
		{
			log.info(String.format("Il file %s non è stato modificato perchè la portlet %s è già definita"
					,xmlfile, portletName));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the portlet is already registered  -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}

		StringWriter sw = new StringWriter();
		try {
			template.process(parameters, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		xml = xml.replaceFirst("</portlet-app>", sw.toString());
		
		writeStringToFile(relativePath, xml, false);
	}

	
	public void writeTilesFlowConfig( String tilesPath, Template template, Map<String, Object> parameters) 
	{
		String xmlfile = "spring-mvc-servlet.xml";
		String relativePath = getWebInfPath()+ "/config/" + xmlfile;
		String absolutePath = getOutputDir()+"/" + relativePath;
		
		File f = new File(absolutePath);
		if(!f.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste",xmlfile));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the file doesn't exists -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}

		String xml = readFileAsString(relativePath);
		
		if(xml.contains(tilesPath))
		{
			log.info(String.format("Il file %s non è stato modificato perchè il flusso %s è già definito"
					,xmlfile, tilesPath));
			
			if (isSimulate())
			{
				String msg = "<!-- file: " + f.getPath() + " -->\n";
				msg += "<!-- Don't write anything beacuse the flow is alreay registered  -->";
				simulationMap.put(xmlfile, msg);
			}
			
			return;
		}

		StringWriter sw = new StringWriter();
		try {
			template.process(parameters, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		xml = xml.replaceFirst("<!-- NEW TILES DEFS HERE-->", sw.toString());
		
		writeStringToFile(relativePath, xml, false);
	}

	
	
	public void writeStringToFile(String filePath, String content,
			boolean append) {
		
		if (isSimulate())
		{
			log.info("Contenuto da aggiungere al file " + getOutputDir()+"/"+ filePath);
			System.out.println(content);
			
			writeToSimulationMap(filePath, content, append);
			return;
		}
		
		
		try {
			log.info("Scrittura del file " + getOutputDir()+"/"+ filePath);
			
			FileWriter out = new FileWriter(new File(getOutputDir() + "/"
					+ filePath), append);
			out.write(content);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	protected void writeToSimulationMap(String filePath, String content,
			boolean append) {
		
		try {
			filePath = getOutputDir() + "/" + filePath;
			
			StringWriter writer = new StringWriter();
			writer.append("<!-- file: " + filePath + " -->");

			writer.append(content);
			
			String name = new File(filePath).getName();
			
			simulationMap.put(name, writer.toString());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public boolean existsXPath(String acFilePath, String xPathExp) {
		SAXBuilder saxBuilder = new SAXBuilder(
				"org.apache.xerces.parsers.SAXParser");

		File xmlDocument = new File(getProjectDir() + "/" + acFilePath);
 
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

	public boolean existsBean(String acFilePath, String beanId) {
		return existsXPath(acFilePath, "/s:beans/s:bean[@id='" + beanId + "']");
	}
	
	public boolean existsJaxwsEndpoint(String acFilePath, String beanId) {
		return existsXPath(acFilePath, "/s:beans/jaxws:endpoint[@id='" + beanId + "']");
	}
	
	public boolean existsManagedBean(String acFilePath, String beanId) {
		return existsXPath(acFilePath, "/f:faces-config/f:navigation-rule/f:from-view-id[text()='" + beanId + "']");
	}
	
	public boolean existsFlow(String acFilePath, String flowPath) {
		return existsXPath(acFilePath, "/webflow:flow-registry/webflow:flow-location[@path='" + flowPath + "']");
	}

	public String readFileAsString(String filePath) {
		try {

			StringBuffer fileData = new StringBuffer(1000);
			BufferedReader reader = new BufferedReader(new FileReader(
					getProjectDir() + "/" + filePath));
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

	/**
	 * @return  the packageName
	 * @uml.property  name="packageName"
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName  the packageName to set
	 * @uml.property  name="packageName"
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return  the springDaoFileName
	 * @uml.property  name="springDaoFileName"
	 */
	public String getSpringDaoFileName() {
		return springDaoFileName;
	}
	
	public String getSpringTestResourcesFileName() {
		return springTestResourcesFileName;
	}
	
	
	

	/**
	 * @param springDaoFileName  the springDaoFileName to set
	 * @uml.property  name="springDaoFileName"
	 */
	public void setSpringDaoFileName(String springDaoFileName) {
		this.springDaoFileName = springDaoFileName;
	}

	/**
	 * @return  the springServiceFileName
	 * @uml.property  name="springServiceFileName"
	 */
	public String getSpringServiceFileName() {
		return springServiceFileName;
	}

	/**
	 * @param springServiceFileName  the springServiceFileName to set
	 * @uml.property  name="springServiceFileName"
	 */
	public void setSpringServiceFileName(String springServiceFileName) {
		this.springServiceFileName = springServiceFileName;
	}

	public boolean isSimulate() {
		return simulate;
	}

	public void setSimulate(boolean simulate) {
		this.simulate = simulate;
		if (simulate) simulationMap.clear();
	}

	public String getFacesConfigFileName() {
		
		return facesConfigFileName;
	}

	public IClassDescriptor getClassDescriptor(Class<?> type) {
		return getDescriptorService().getClassDescriptor(type);
	}

	public void setSimulationMap(Map<String,String> simulationMap) {
		this.simulationMap = simulationMap;
	}

	public Map<String,String> getSimulationMap() {
		return simulationMap;
	}

}

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
import org.regola.codeassistence.writers.ChainOfWriters;
import org.regola.codeassistence.writers.FSWriter;
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
	
	private String springDaoFileName = "applicationContext-dao.xml";
	private String springTestResourcesFileName = "applicationContext-dao-test.xml";
	
	
	public ChainOfWriters writers = new ChainOfWriters();

	@SuppressWarnings( "unchecked" )
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

	private String getFlexSrcPath() {
		return "src";
	}
	
	public String getJavaTestSrcPath() {
		return "src/test/java";
	}

	public String getWebRootPath() {
		return "src/main/webapp";
	}
	
	public String getWebInfPath() {
		return "src/main/webapp/WEB-INF";
	}
	
	public String getRegolaConfigPath() {
		return "src/main/webapp/WEB-INF/config";
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

	public File instanceFile(String relativeToProjectPath, String fileName)
	{
		String dirPath = getOutputDir() + "/" + relativeToProjectPath;
		
		if (!dirPath.endsWith("/"))
			dirPath += "/";
		
		String path = dirPath + fileName;

		return new File(path);
	}
	
	public void writeToFile(File file, Template template, Object variables, boolean append) {

		//log.info("Scrittura del file " + file.getPath());
		writers.makeDirectory(file);
		writers.writeToFile(file, template, variables, append);
			
	}

	public Template getTemplate(String templateName) {
		try {
			return freemarkerConfiguration.getTemplate(templateName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addResource(String fileName, Template template,
			Map<String, Object> variables) 
	{
		writeToFile(instanceFile(getResourcePath(), fileName), template, variables,false);
	}

	public void addApplicationProperties(Template template, Map<String, Object> variables) 
	{
		//writeFile(getResourcePath()+"/", "ApplicationResources.properties", template, variables, true);
		writeToFile(instanceFile(getResourcePath(), "ApplicationResources_en.properties"), template, variables, true);
		writeToFile(instanceFile(getResourcePath(), "ApplicationResources_it.properties"), template, variables, true);
	}
	
	public void addWebDocument(String fileName, Template template, Map<String, Object> parameters) 
	{
		writeToFile(instanceFile(getWebRootPath(), (fileName+".xhtml").toLowerCase()), template, parameters, false);		
	}
	
	public void addJavaSource(String packageName, String fileName, Template template,
			Map<String, Object> variables) 
	{
		addJavaSource(packageName, fileName, template, variables, null);
	}
	
	public void addJavaTestSource(String packageName, String fileName, Template template,
			Map<String, Object> variables) 
	{
		addJavaTestSource(packageName, fileName, template, variables, null);
	}
	
	
	public void addJavaSource(String packageName, String fileName, 
			Template template, Map<String, Object> variables, Object value) 
	{
		ValueReader reader = new ValueReader(value);
		variables.put("reader", reader);

		writeToFile(instanceFile(getJavaSrcPath()+"/"+Utils.getPackagePath(packageName), fileName+".java"), template, variables,false);
	}
	
	public void addFlexSource(String packageName, String fileName, Template template,
			Map<String, Object> variables) 
	{
		addFlexSource(packageName, fileName, template, variables, null);
	}
	
	public void addFlexSource(String packageName, String fileName, 
			Template template, Map<String, Object> variables, Object value) 
	{
		ValueReader reader = new ValueReader(value);
		variables.put("reader", reader);

		writeToFile(instanceFile(getFlexSrcPath()+"/"+Utils.getPackagePath(packageName), fileName+".as3"), template, variables,false);
	}
	

	public void addJavaTestSource(String packageName, String fileName, 
			Template template, Map<String, Object> variables, Object value) 
	{
		ValueReader reader = new ValueReader(value);
		variables.put("reader", reader);

		writeToFile(instanceFile(getJavaTestSrcPath()+"/"+Utils.getPackagePath(packageName), fileName+".java"), template, variables,false);
	}
	
	public void addFlowsResource(String flowName, String fileName, 
			Template template, Map<String, Object> variables, Object value) 
	{
		ValueReader reader = new ValueReader(value);
		variables.put("reader", reader);

		writeToFile(instanceFile( getWebInfPath()+"/flows/"+ flowName  , fileName), template, variables,false);
	}

	
	public void addWebXmlConfig( String element, Template template, Map<String, Object> variables) 
	{
		addXmlTags(ConfigFileType.WebXml, element, template, variables);
	}
	
	public void addPortlet( String portletName, Template template, Map<String, Object> variables) 
	{
		addXmlTags(ConfigFileType.Portlet, portletName, template, variables);		
	}

	
	private enum ConfigFileType { WebXml, Portlet, TilesFlow, 
		Flow, Faces, ServiceBean, ServiceBeanTest, WS }

	
	protected void addXmlTags( ConfigFileType configType, String tag, Template template, Map<String, Object> variables) 
	{
		File file = new File("");
		
		switch (configType) {
		case WebXml:
			file = instanceFile(getWebInfPath(), "web.xml");
			break;
			
		case Portlet:
			file = instanceFile(getWebInfPath(), "portlet.xml");
			break;
		
		case TilesFlow:
			file = instanceFile(getRegolaConfigPath(), "spring-mvc-servlet.xml");
			break;

		case Flow:
			file = instanceFile(getRegolaConfigPath(), "webflow-config.xml");
			break;

		case Faces:
			file = instanceFile(getWebInfPath(), "faces-config.xml");
			break;

		case ServiceBean:
			file = instanceFile(getResourcePath(), "applicationContext-service.xml");
			break;
		
		case ServiceBeanTest:
			file = instanceFile(getTestResourcePath(), "applicationContext-service.xml");
			break;	
			
		case WS:
			file = instanceFile(getResourcePath(), "applicationContext-service.xml");
			break;
			
		default:
			break;
		}
		
		
		
		if(!file.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste ",file.getName()));
			writers.logNothingToDo(file, String.format("Salto la modifica di %s perchè non esiste",file.getName()));
			return;
		}
		
		String fileContent = Utils.readFileAsString(file);
		
		StringWriter sw = new StringWriter();
		try {
			template.process(variables, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		
		String exists = "";
		switch (configType) {
		case WebXml:
			exists = fileContent.contains(tag) ? String.format("Il file %s non è stato modificato perchè l'elemento %s è già definita"
					,file.getName(), tag) : "";
			fileContent = fileContent.replaceFirst("</web-app>", sw.toString());
			break;

		case Portlet:
			exists = fileContent.contains(tag) ? String.format("Il file %s non è stato modificato perchè la portlet %s è già definita"
					,file.getName(), tag) : "";
			fileContent = fileContent.replaceFirst("</portlet-app>", sw.toString());
			break;
		
		case TilesFlow:
			exists = fileContent.contains(tag) ? String.format("Il file %s non è stato modificato perchè il flusso %s è già definito"
					,file.getName(), tag) : "";
			fileContent = fileContent.replaceFirst("<!-- NEW TILES DEFS HERE-->", sw.toString());
			break;
		
		case Flow:
			exists = Utils.existsFlow(file, tag) ? String.format("Il file %s non è stato modificato perchè il flusso %s è già definito"
					,file.getName(), tag) : "";
			fileContent = fileContent.replaceFirst("</webflow:flow-registry>", sw.toString());
			break;
					
		case Faces:
			exists = Utils.existsManagedBean(file, tag) ? String.format("Il file %s non è stato modificato perchè il bean %s è già definito"
					,file.getName(), tag) : "";
			fileContent = fileContent.replaceFirst("</faces-config>", sw.toString());
			break;
		
		case ServiceBean:
			exists = Utils.existsBean(file, tag) ? String.format("Il file %s non è stato modificato perchè il bean %s è già definito"	
					,file.getName(), tag) : "";
			fileContent = fileContent.replaceFirst("</beans>", sw.toString());
			break;	
		
		case WS:
			exists = Utils.existsJaxwsEndpoint(file, tag) ? String.format("Il file %s non è stato modificato perchè il bean %s è già definito"
					,file.getName(), tag) : "";
			fileContent = fileContent.replaceFirst("</beans>", sw.toString());
			break;	
				
			
		default:
			break;
		}
				
		if(exists.length()>0)
		{
			log.info(exists);
			writers.logNothingToDo(file, exists);
			return;
		}

		writers.writeToFile( file , fileContent, false);

	}
	
	public void addWebServiceBean( String beanId, Template template, Map<String, Object> variables) 
	{
		addXmlTags(ConfigFileType.WS, beanId, template, variables);
	}

	
	public void addSpringBeansToApplicationContext( String beanId, Template template, Map<String, Object> variables) 
	{
		addXmlTags(ConfigFileType.ServiceBean, beanId, template, variables);
	}

	
	public void addFacesBeans( String beanId, Template template, Map<String, Object> variables) 
	{
		addXmlTags(ConfigFileType.Faces, beanId, template, variables);
	}
	
	public void addFlowDefinition( String flowPath, Template template, Map<String, Object> variables) 
	{
		addXmlTags(ConfigFileType.Flow, flowPath, template, variables);
	}
	
	
	public void addTilesFlowDefinition( String tilesPath, Template template, Map<String, Object> variables) 
	{
		addXmlTags(ConfigFileType.TilesFlow, tilesPath, template, variables);
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

	



	

	public IClassDescriptor getClassDescriptor(Class<?> type) {
		return getDescriptorService().getClassDescriptor(type);
	}



	public void setFlexOutputDir(String flexOutputDir) {
		this.flexOutputDir = flexOutputDir;
	}

	public String getFlexOutputDir() {
		return flexOutputDir;
	}



	private String flexOutputDir;

}

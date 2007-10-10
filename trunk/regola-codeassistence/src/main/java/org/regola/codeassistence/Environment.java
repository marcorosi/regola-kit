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
import org.regola.descriptor.ReflectionDescriptorFactory;
import org.regola.descriptor.TrailsDescriptorService;
import org.regola.util.ValueReader;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * @author  nicola
 */
public class Environment {
	private Configuration freemarkerConfiguration;

	private DescriptorService descriptorService;

	private String projectDir = "";

	private String outputDir = "";

	private String packageName = "";

	private String springServiceFileName = "applicationContext-service.xml";
	private String springDaoFileName = "applicationContext-dao.xml";
	
	private boolean simulate=false;
	
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Una classe di callback che consente di navigare un ramo del file system.
	 * In particolare cerca tutti i file .class contenuti direttamente od
	 * indirettamente nella cartella di nome "model".
	 */
	public class ClassDirectoryWalker extends DirectoryWalker {
		public ClassDirectoryWalker(IOFileFilter filter) {
			super(null, filter, -1);

		}

		@SuppressWarnings("unchecked")
		public void go(File startDirectory, Collection results)
				throws IOException {

			walk(startDirectory, results);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected void handleFile(File file, int depth, Collection results)
				throws IOException {

			if (file.getPath().matches(".*/model/.*"))
				results.add(file.getPath());

		}

	} 
	
	@SuppressWarnings("unchecked")
	static Class[] wrappedTypes = { Character.class, Byte.class, Short.class,  Integer.class, Long.class, Float.class, 
		Double.class, Boolean.class, String.class};

	/**
	 * Legge tutti il ramo del file system che inizia da classDirPath
	 * e costruisce una lista con tutte le classi di modello. Ovvero che 
	 * sono contenute direttamente od indirettamente dentro un package 
	 * che contiene la parola "model" 
	 * 
	 * 
	 * @param classDirPath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Class> findModelClassesFromDir(String classDirPath) {

		List<Class> list = new ArrayList<Class>();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null) {
			loader = FullStack.class.getClassLoader();
		}

		List<String> fileNames = new ArrayList<String>();

		ClassDirectoryWalker walker = new ClassDirectoryWalker(
				new SuffixFileFilter(".class"));

		// String classDirPath = "./target/classes/";
		try {
			walker.go(new File(classDirPath), fileNames);
		} catch (IOException e1) {
			log.error("Error processing dir " + classDirPath, e1);
		}

		log.info("Search for model classes inside directory: " + classDirPath);
		for (String name : fileNames) {
			name = name.replaceAll(classDirPath, "");
			name = name.substring(0, name.length() - 6);
			name = name.replace('/', '.');
			Class clazz = null;
			try {
				clazz = loader.loadClass(name);
			} catch (Exception e) {

				log.error("Could not load class :" + name, e);
			}

			if (clazz != null) {
				list.add(clazz);
				log.info("added class " + clazz.getCanonicalName());
			}

		}
		
		for (Class wrappedType : wrappedTypes)
		{
			list.add(wrappedType);
		}

		return list;
	}
	
	/**
	 * Verifica l'estenza di un file, o directory sul file system.
	 * @param filePath
	 * @return true   - se il file esiste
	 *         false  - se non esiste
	 */
	public boolean existsFile(String filePath)
	{
		return new File(filePath).exists();
	}

	@SuppressWarnings( { "unchecked", "unchecked" })
	public Environment() {
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(getClass(), "/templates");
		cfg.setObjectWrapper(new DefaultObjectWrapper());

		setFreemarkerConfiguration(cfg);

		TrailsDescriptorService descriptorService = new TrailsDescriptorService();

		// descriptorService.setTypes(Arrays.asList(Anagrafica.class,
		// Verbale.class, VerbaleId.class));

		descriptorService
				.setTypes(findModelClassesFromDir("./target/classes/"));
		
		

		descriptorService
				.setDescriptorFactory(new ReflectionDescriptorFactory());
		try {
			descriptorService.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		setDescriptorService(descriptorService);
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
	public DescriptorService getDescriptorService() {
		return descriptorService;
	}

	/**
	 * @param descriptorService  the descriptorService to set
	 * @uml.property  name="descriptorService"
	 */
	public void setDescriptorService(DescriptorService descriptorService) {
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

	public String getWebSrcPath() {
		return "src/main/webapp";
	}

	public String getResourcePath() {
		return "src/main/resources";
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
	
	public void writeFile(String filePath, String fileName, Template template, Object root, boolean append) {

		if (isSimulate())
		{
			writeToStandardOut(filePath, fileName, template, root, append);
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
		writeFile(getResourcePath()+"/", "ApplicationResources.properties", template, root, true);
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
	
	public void writeJavaSource(String packageName, String fileName, 
			Template template, Map<String, Object> root, Object value) 
	{
		ValueReader reader = new ValueReader(value);
		root.put("reader", reader);

		writeFile(getJavaSrcPath()+"/"+Utils.getPackagePath(packageName)+"/", fileName+".java", template, root);
	}


	public void writeXmlSource(String xmlfile, String beanId, Template template, Map<String, Object> parameters) 
	{
		File f = new File(getOutputDir()+"/"+getResourcePath()+"/"+xmlfile);
		if(!f.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste",xmlfile));
			return;
		}
		
		if(existsBean(getResourcePath()+"/"+xmlfile, beanId))
		{
			log.info(String.format("Il file %s non è stato modificato perchè il bean %s è già definito"
					,xmlfile, beanId));
			return;
		}

		StringWriter sw = new StringWriter();
		try {
			template.process(parameters, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		String xml = readFileAsString(getResourcePath()+"/"+xmlfile);
		xml = xml.replaceFirst("</beans>", sw.toString());
		
		writeStringToFile(getResourcePath()+"/"+xmlfile, xml, false);
	}
	
	public void writeFacesConfig(String xmlfile, String beanId, Template template, Map<String, Object> parameters) 
	{
		File f = new File(getOutputDir()+"/"+getResourcePath()+"/"+xmlfile);
		if(!f.exists())
		{
			log.info(String.format("Salto la modifica di %s perchè non esiste",xmlfile));
			return;
		}
		
		if(existsManagedBean(getResourcePath()+"/"+xmlfile, beanId))
		{
			log.info(String.format("Il file %s non è stato modificato perchè il bean %s è già definito"
					,xmlfile, beanId));
			return;
		}

		StringWriter sw = new StringWriter();
		try {
			template.process(parameters, sw);
		} catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
		
		String xml = readFileAsString(getResourcePath()+"/"+xmlfile);
		xml = xml.replaceFirst("</faces-config>", sw.toString());
		
		writeStringToFile(getResourcePath()+"/"+xmlfile, xml, false);
	}

	
	public void writeStringToFile(String filePath, String content,
			boolean append) {
		
		if (isSimulate())
		{
			log.info("Contenuto da aggiungere al file " + getOutputDir()+"/"+ filePath);
			System.out.println(content);
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

	public boolean existsXPath(String acFilePath, String xPathExp) {
		SAXBuilder saxBuilder = new SAXBuilder(
				"org.apache.xerces.parsers.SAXParser");

		File xmlDocument = new File(getProjectDir() + "/" + acFilePath);

		Element levelNode;
		try {
			org.jdom.Document jdomDocument = saxBuilder.build(xmlDocument);
			XPath xpath = XPath.newInstance(xPathExp);
			xpath.addNamespace("s",
					"http://www.springframework.org/schema/beans");
			levelNode = (Element) xpath.selectSingleNode(jdomDocument);
			return levelNode != null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public boolean existsBean(String acFilePath, String beanId) {
		return existsXPath(acFilePath, "/s:beans/s:bean[@id='" + beanId + "']");
	}
	
	public boolean existsManagedBean(String acFilePath, String beanId) {
		return existsXPath(acFilePath, "/faces-config/managed-bean/managed-bean-name[text()='" + beanId + "']");
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
	}

}

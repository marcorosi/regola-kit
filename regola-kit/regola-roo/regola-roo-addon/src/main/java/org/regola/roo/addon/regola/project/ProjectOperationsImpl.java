package org.regola.roo.addon.regola.project;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.DependencyScope;
import org.springframework.roo.project.DependencyType;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Property;
import org.springframework.roo.project.Repository;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Qui ci si preoccupa di aggiungere le dipendenze
 * ed i file necessari per un progetto regola-kit. 
 * @author nicola
 *
 */
@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class ProjectOperationsImpl implements ProjectOperation {

	 private static final Logger LOGGER = HandlerUtils.getLogger(ProjectOperationsImpl.class);
	
	@Reference private ProjectOperations projectOperations;
    @Reference private TypeLocationService typeLocationService;
    @Reference private TypeManagementService typeManagementService;
    @Reference private PathResolver pathResolver;
    @Reference private FileManager fileManager;
    
    static final String PERSISTENCE_XML = "META-INF/persistence.xml";
    static final String APPLICATION_CONTEXT_XML = "applicationContext.xml";
    
    /* (non-Javadoc)
	 * @see org.regola.roo.addon.regola.project.ProjectOperation#isCommandAvailable()
	 */
    public boolean isCommandAvailable() {
        // Check if a project has been created
        return projectOperations.isFocusedProjectAvailable();
    }


	/* (non-Javadoc)
	 * @see org.regola.roo.addon.regola.project.ProjectOperation#setup()
	 */
	public void setup() {
	     
		projectOperations.addProperty(projectOperations.getFocusedModuleName(), new Property("regola.version", "1.3-SNAPSHOT"));
		
		// Install code repository needed to get the annotation 
		projectOperations.addRepository("", new Repository("nexus", "Maven Proxy interno ad UniBo", "http://deposito:8082/nexus/content/groups/public"));
        
        List<Dependency> dependencies = new ArrayList<Dependency>();
        
        // Install the dependency on the add-on jar (
        //dependencies.add(new Dependency("org.regola.roo.addon.regola.pattern", "org.regola.roo.addon.regola.pattern", "0.1.0.BUILD-SNAPSHOT", DependencyType.JAR, DependencyScope.PROVIDED));
        
        // Install dependencies defined in external XML file
        for (Element dependencyElement : XmlUtils.findElements("/configuration/batch/dependencies/dependency", XmlUtils.getConfiguration(getClass()))) {
            dependencies.add(new Dependency(dependencyElement));
        }

        // Add all new dependencies to pom.xml
        projectOperations.addDependencies("", dependencies);
        
        Dependency dependency = new Dependency("com.oracle", 
        		"ojdbc14", 
        		"10.2.0.2", 
        		DependencyType.JAR, DependencyScope.COMPILE);
        
        projectOperations.removeDependency(projectOperations.getFocusedModuleName(), dependency);
        
        //update properties path
        updateApplicationCtxXml();
        
        // Fix hbm2ddlto avoid table drops at startup
        updatePersistenceXml();
        
        // Write an application context to use with test witch overrride db config
        updateTestApplicationContext();
        
        // Remove spring/META-INF/jndi.properties file 
        removeJndiProperties();
    }
	
    private String getPersistencePathOfFocussedModule() {
        return pathResolver.getFocusedIdentifier(Path.SRC_MAIN_RESOURCES,
                PERSISTENCE_XML);
    }
    
    private String getApplicationCtxPathOfFocussedModule() {
        return pathResolver.getFocusedIdentifier(Path.SPRING_CONFIG_ROOT,
                "applicationContext.xml");
    }
    
    private void updatePersistenceXml() {
	   
    	final String persistencePath = getPersistencePathOfFocussedModule();
    	final InputStream inputStream;
        
    	if (!fileManager.exists(persistencePath)) {
        	LOGGER.warning("persistence.xml doesn't exist");
        	return;
        }
        
        // There's an existing persistence config file; read it
        inputStream = fileManager.getInputStream(persistencePath);
        
        final Document persistence = XmlUtils.readXml(inputStream);
        final Element root = persistence.getDocumentElement();
        final Element persistenceElement = XmlUtils.findFirstElement(
                "/persistence", root);
        Validate.notNull(persistenceElement, "No persistence element found");
        
        Element ddlProperty = XmlUtils.findFirstElement("persistence-unit/properties/property[@name = 'hibernate.hbm2ddl.auto']",persistenceElement);
        
        
        if (ddlProperty == null || "validate".equals(ddlProperty.getAttribute("value"))) {
        	return;
        }
                
        ddlProperty.setAttribute("value", "none");
        
        fileManager.createOrUpdateTextFileIfRequired(persistencePath,
                XmlUtils.nodeToString(persistence), false);

    }
    
    private void updateApplicationCtxXml() {
 	   
    	final String appCtxPath = getApplicationCtxPathOfFocussedModule();
    	final InputStream inputStream;
        
    	if (!fileManager.exists(appCtxPath)) {
        	LOGGER.warning("applicationContext.xml doesn't exist");
        	return;
        }
        
        // There's an existing persistence config file; read it
        inputStream = fileManager.getInputStream(appCtxPath);
        
        final Document appCtx = XmlUtils.readXml(inputStream);
        final Element root = appCtx.getDocumentElement();
        final Element beansElement =XmlUtils.findFirstElement(
                "/beans", root);
        Validate.notNull(beansElement, "No beans element found");
        
        final String LOCATION = "classpath*:META-INF/spring/*.properties, classpath*:*-${regola.env}.properties";
        
        Element propPlaceholderElement = XmlUtils.findFirstElement("/beans/property-placeholder",beansElement);
        
        
        if (propPlaceholderElement == null || LOCATION.equals(propPlaceholderElement.getAttribute("location"))) {
        	return;
        }
                
        propPlaceholderElement.setAttribute("location", LOCATION);
        
        fileManager.createOrUpdateTextFileIfRequired(appCtxPath,
                XmlUtils.nodeToString(appCtx), false);

    }
    
    private void updateTestApplicationContext()
    {
    	  final String contextPath = projectOperations.getPathResolver()
                  .getFocusedIdentifier(Path.SRC_TEST_RESOURCES, "spring-test/" +
                          APPLICATION_CONTEXT_XML);
          
    	  if (fileManager.exists(contextPath)) {
          	LOGGER.warning("applicationContext.xml di test already exist!");
          	return;
          }
    	  
          // Use the addon's template file
    	  final InputStream inputStream = FileUtils.getInputStream(getClass(),
                  "applicationContext-template.xml");
 
    	  final Document appCtx = XmlUtils.readXml(inputStream);
    	  //final Element root = appCtx.getDocumentElement();
          
    	  fileManager.createOrUpdateTextFileIfRequired(contextPath,
                  XmlUtils.nodeToString(appCtx), false);
  
    }
    
    private void removeJndiProperties()
    {
        final String jndiPropertiesPath = projectOperations.getPathResolver()
                .getFocusedIdentifier(Path.SPRING_CONFIG_ROOT,
                        "jndi.properties");
        
        if (fileManager.exists(jndiPropertiesPath)) {
        	fileManager.delete(jndiPropertiesPath);
        }
    }
        
    
	
}

package org.regola.roo.addon.regola.dbre;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.dbre.model.Schema;
import org.springframework.roo.file.monitor.event.FileDetails;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.process.manager.MutableFile;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class DbreOperationsImpl  implements DbreOperations {

//    private static final Logger LOGGER = HandlerUtils
//            .getLogger(DbreOperationsImpl.class);
    
    @Reference private FileManager fileManager;
    @Reference private PathResolver pathResolver;
    @Reference private ProjectOperations projectOperations;
    @Reference private org.springframework.roo.addon.dbre.DbreOperations dbreOperations;

    static final String APPLICATION_CONTEXT_XML = "applicationContext.xml";

  
    public boolean isDbreInstallationPossible() {
        return dbreOperations.isDbreInstallationPossible();
    }

    private void writeProperties(final String path, final boolean exists,
            final Properties props) {
        OutputStream outputStream = null;
        try {
            final MutableFile mutableFile = exists ? fileManager
                    .updateFile(path) : fileManager.createFile(path);
            outputStream = mutableFile == null ? new FileOutputStream(path)
                    : mutableFile.getOutputStream();
            props.store(outputStream, "Updated at " + new Date());
        }
        catch (final IOException e) {
            throw new IllegalStateException(e);
        }
        finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
    
    public void reverseEngineerDatabase(final Set<Schema> schemas,
            final JavaPackage destinationPackage,
            final boolean testAutomatically, final boolean view,
            final Set<String> includeTables, final Set<String> excludeTables,
            final boolean includeNonPortableAttributes,
            final boolean activeRecord) {
    	
    	 final String databasePath =  pathResolver.getFocusedIdentifier(
                 Path.SPRING_CONFIG_ROOT, "database.properties");
         final boolean databaseExists = fileManager.exists(databasePath);
    	 
         Properties properties = getConnectionPropertiesFromTestConfiguration();
         writeProperties(databasePath, databaseExists, properties);
         
         updateApplicationCtx(true);
         
    	 dbreOperations.reverseEngineerDatabase(schemas, destinationPackage, testAutomatically, view, includeTables, excludeTables, includeNonPortableAttributes, activeRecord);
             
         fileManager.delete(databasePath);
         updateApplicationCtx(false);
         
    }


    
    private Properties getConnectionPropertiesFromTestConfiguration() {
		final String persistenceXmlPath = projectOperations.getPathResolver()
				.getFocusedIdentifier(Path.SRC_TEST_RESOURCES,
						"spring-test/" + APPLICATION_CONTEXT_XML);

		if (!fileManager.exists(persistenceXmlPath)) {
			throw new IllegalStateException("Failed to find "
					+ persistenceXmlPath);
		}

		final FileDetails fileDetails = fileManager
				.readFile(persistenceXmlPath);
		Document document = null;
		try {
			final InputStream is = new BufferedInputStream(new FileInputStream(
					fileDetails.getFile()));
			final DocumentBuilder builder = XmlUtils.getDocumentBuilder();
			builder.setErrorHandler(null);
			document = builder.parse(is);
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}

		final List<Element> propertyElements = XmlUtils.findElements(
				"/beans/bean[@id='dataSource']/property",
				document.getDocumentElement());
		Validate.notEmpty(propertyElements,
				"Failed to find property elements in " + persistenceXmlPath);
		final Properties properties = new Properties();

		for (final Element propertyElement : propertyElements) {
			final String key = propertyElement.getAttribute("name");
			final String value = propertyElement.getAttribute("value");
			if ("driverClassName".equals(key)) {
				properties.put("database.driverClassName", value);
			}
			if ("url".equals(key)) {
				properties.put("database.url", value);
			}
			if ("username".equals(key)) {
				properties.put("database.username", value);
			}
			if ("password".equals(key)) {
				properties.put("database.password", value);
			}

			if (properties.size() == 4) {
				// All required properties have been found so ignore rest of
				// elements
				break;
			}
		}
		return properties;
	}


    private void updateApplicationCtx(boolean hack) {
    	 
    	final String actualName =  hack ?  "dataSource" : "dataSource-FAKE";
    	final String newName    =  !hack ?  "dataSource" : "dataSource-FAKE";
    	
    	final String contextPath = projectOperations.getPathResolver()
                 .getFocusedIdentifier(Path.SPRING_CONFIG_ROOT,
                         "applicationContext.xml");
         final Document appCtx = XmlUtils.readXml(fileManager
                 .getInputStream(contextPath));
         final Element root = appCtx.getDocumentElement();
         final Element dataSourceJndi = XmlUtils.findFirstElement(
                 "/beans/jndi-lookup[@id = '"+ actualName  +"']", root);
         
         if (dataSourceJndi != null)
         {
        	 //String jndi = dataSourceJndi.getAttribute("id");
        	 //LOGGER.warning(jndi);
        	 dataSourceJndi.setAttribute("id", newName);
        	 
         }
         
        fileManager.createOrUpdateTextFileIfRequired(contextPath,
                    XmlUtils.nodeToString(appCtx), false);
        
    }

	
}

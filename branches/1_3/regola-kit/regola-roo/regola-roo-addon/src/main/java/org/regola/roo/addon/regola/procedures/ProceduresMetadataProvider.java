package org.regola.roo.addon.regola.procedures;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.addon.dbre.jdbc.ConnectionProvider;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.file.monitor.event.FileDetails;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Provides {@link PatternMetadata}. This type is called by Roo to retrieve the metadata for this add-on.
 * Use this type to reference external types and services needed by the metadata type. Register metadata triggers and
 * dependencies here. Also define the unique add-on ITD identifier.
 * 
 * @since 1.1
 */
@Component
@Service
public final class ProceduresMetadataProvider extends AbstractItdMetadataProvider {

	 
	 @Reference private ProjectOperations projectOperations;
	 @Reference private ConnectionProvider connectionProvider;

    /**
     * The activate method for this OSGi component, this will be called by the OSGi container upon bundle activation 
     * (result of the 'addon install' command) 
     * 
     * @param context the component context can be used to get access to the OSGi container (ie find out if certain bundles are active)
     */
    protected void activate(ComponentContext context) {
        metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        addMetadataTrigger(new JavaType(RooRegolaProcedures.class.getName()));
    }
    
    /**
     * The deactivate method for this OSGi component, this will be called by the OSGi container upon bundle deactivation 
     * (result of the 'addon uninstall' command) 
     * 
     * @param context the component context can be used to get access to the OSGi container (ie find out if certain bundles are active)
     */
    protected void deactivate(ComponentContext context) {
        metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        removeMetadataTrigger(new JavaType(RooRegolaProcedures.class.getName()));    
    }
    
    
    /**
     * Return an instance of the Metadata offered by this add-on
     */
    protected ItdTypeDetailsProvidingMetadataItem getMetadata(String metadataIdentificationString, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, String itdFilename) {
        
        final Properties connectionProperties = getConnectionPropertiesFromTestConfiguration();
        Connection connection = connectionProvider.getConnection(connectionProperties,false);
    	
        final RegolaProceduresAnnotationValues annotationValues = new RegolaProceduresAnnotationValues(
                governorPhysicalTypeMetadata);
                       
        
        return new ProceduresMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata, connection, annotationValues);
    }
    
    /**
     * Define the unique ITD file name extension, here the resulting file name will be **_ROO__ActiveRecord.aj
     */
    public String getItdUniquenessFilenameSuffix() {
        return "Regola_Procedures";
    }

    protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
        JavaType javaType = ProceduresMetadata.getJavaType(metadataIdentificationString);
        LogicalPath path = ProceduresMetadata.getPath(metadataIdentificationString);
        return PhysicalTypeIdentifier.createIdentifier(javaType, path);
    }
    
    protected String createLocalIdentifier(JavaType javaType, LogicalPath path) {
        return ProceduresMetadata.createIdentifier(javaType, path);
    }

    public String getProvidesType() {
        return ProceduresMetadata.getMetadataIdentiferType();
    }
    
    private Properties getConnectionPropertiesFromTestConfiguration() {
        final String acTestPath = projectOperations.getPathResolver()
                .getFocusedIdentifier(Path.SRC_TEST_RESOURCES,
                        "spring-test/applicationContext.xml");
        if (!fileManager.exists(acTestPath)) {
            throw new IllegalStateException("Failed to find "
                    + acTestPath);
        }

        final FileDetails fileDetails = fileManager.readFile(acTestPath);
        Document document = null;
        try {
            final InputStream is = new BufferedInputStream(new FileInputStream(fileDetails.getFile()));
            final DocumentBuilder builder = XmlUtils.getDocumentBuilder();
            builder.setErrorHandler(null);
            document = builder.parse(is);
        }
        catch (final Exception e) {
            throw new IllegalStateException(e);
        }

        final List<Element> propertyElements = XmlUtils.findElements(
                "/beans/bean[@id='dataSource']/property",
                document.getDocumentElement());
        Validate.notEmpty(propertyElements,"Failed to find property elements in " + acTestPath);
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

}

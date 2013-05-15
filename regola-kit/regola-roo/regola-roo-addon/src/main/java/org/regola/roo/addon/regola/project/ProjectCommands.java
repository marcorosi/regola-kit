package org.regola.roo.addon.regola.project;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.jpa.JdbcDatabase;
import org.springframework.roo.addon.jpa.JpaOperations;
import org.springframework.roo.addon.jpa.OrmProvider;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.GAV;
import org.springframework.roo.project.MavenOperations;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.packaging.JarPackaging;
import org.springframework.roo.project.packaging.PackagingProvider;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;
import org.springframework.roo.support.logging.HandlerUtils;

/**
 * Questa classe implementa il comando 
 * regola project setup
 * 
 * @author nicola
 *
 */

@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class ProjectCommands implements CommandMarker {
	 
    private static final String PROJECT_COMMAND = "regola project";
    private static Logger LOGGER = HandlerUtils.getLogger(ProjectCommands.class);
    
    @Reference private JpaOperations jpaOperations;
    @Reference private ProjectOperations projectOperations;
	@Reference private ProjectOperation operations;
    @Reference private MavenOperations mavenOperations;
    @Reference private FileManager fileManager;
    //@Reference private RegolaPackaging regolaPackaging;
	
    @CliAvailabilityIndicator("regola project setup")
    public boolean isCommandAvailable() {
    	//return operations.isCommandAvailable();
    	return false;
    }
	
	@CliCommand(value = "regola project setup", help = "Setup a Roo application for use with Regola-Kit")
    public void setup() {
		//operations.setup(); 
    }
	
    @CliAvailabilityIndicator(PROJECT_COMMAND)
    public boolean isCreateProjectAvailable() {
        return mavenOperations.isCreateProjectAvailable();
    }
	
	@CliCommand(value = PROJECT_COMMAND, help = "Creates a new Maven project")
    public void createProject(
            @CliOption(key = { "", "topLevelPackage" }, mandatory = true, optionContext = "update", help = "The uppermost package name (this becomes the <groupId> in Maven and also the '~' value when using Roo's shell)") final JavaPackage topLevelPackage,
            @CliOption(key = "projectName", help = "The name of the project (last segment of package name used as default)") final String projectName,
            @CliOption(key = "jndiDataSource", mandatory = false, help = "The JNDI datasource to use", specifiedDefaultValue ="java:comp/env/jdbc/DEVELOPMENT")   String jndi,
            @CliOption(key = "java", help = "Forces a particular major version of Java to be used (will be auto-detected if unspecified; specify 5 or 6 or 7 only)") final Integer majorJavaVersion,
            @CliOption(key = "parent", help = "The Maven coordinates of the parent POM, in the form \"groupId:artifactId:version\"") final GAV parentPom,
            @CliOption(key = "packaging", help = "The Maven packaging of this project", unspecifiedDefaultValue = "regola-war") final PackagingProvider packaging) {
		
         mavenOperations.createProject(topLevelPackage, projectName,
                majorJavaVersion, parentPom, packaging);
        
        //mavenOperations.createProject(topLevelPackage, projectName,
        //        majorJavaVersion, parentPom, regolaPackaging);
               
        
        fileManager.commit();
        
        if (null==jndi) jndi = "java:comp/env/jdbc/DEVELOPMENT";
        
        jpaOperations.configureJpa(OrmProvider.HIBERNATE, JdbcDatabase.ORACLE, jndi,
        		null, null, null, null, null,
        		null, null,
        		projectOperations.getFocusedModuleName());

        fileManager.commit();
        
        
        operations.setup(projectName); 
    }
	
    @CliCommand(value = "xxx", help = "Install or updates a JPA persistence provider in your project")
    public void installJpa(
            @CliOption(key = "provider", mandatory = true, help = "The persistence provider to support") final OrmProvider ormProvider,
            @CliOption(key = "database", mandatory = true, help = "The database to support") final JdbcDatabase jdbcDatabase,
            @CliOption(key = "applicationId", mandatory = false, unspecifiedDefaultValue = "the project's name", help = "The Google App Engine application identifier to use") final String applicationId,
            @CliOption(key = "jndiDataSource", mandatory = false, help = "The JNDI datasource to use") final String jndi,
            @CliOption(key = "hostName", mandatory = false, help = "The host name to use") final String hostName,
            @CliOption(key = "databaseName", mandatory = false, help = "The database name to use") final String databaseName,
            @CliOption(key = "userName", mandatory = false, help = "The username to use") final String userName,
            @CliOption(key = "password", mandatory = false, help = "The password to use") final String password,
            @CliOption(key = "transactionManager", mandatory = false, help = "The transaction manager name") final String transactionManager,
            @CliOption(key = "persistenceUnit", mandatory = false, help = "The persistence unit name to be used in the persistence.xml file") final String persistenceUnit) {

    	

    }
}

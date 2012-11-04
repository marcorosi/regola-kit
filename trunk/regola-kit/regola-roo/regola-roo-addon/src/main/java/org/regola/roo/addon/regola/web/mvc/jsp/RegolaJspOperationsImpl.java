package org.regola.roo.addon.regola.web.mvc.jsp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.propfiles.PropFileOperations;
import org.springframework.roo.classpath.operations.AbstractOperations;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.process.manager.MutableFile;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.FeatureNames;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.osgi.OSGiUtils;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
@Service
public class RegolaJspOperationsImpl extends AbstractOperations implements
		RegolaJspOperations {
	

	@Reference
	private ProjectOperations projectOperations;
	// @Reference
	// protected FileManager fileManager;
	@Reference
	private PathResolver pathResolver;
	@Reference
	private MetadataService metadataService;
	
	@Reference private PropFileOperations propFileOperations;

	public Document getDocumentTemplate(final String templateName) {
		return XmlUtils.readXml(FileUtils.getInputStream(getClass(),
				templateName));
	}

	private boolean isProjectAvailable() {
		return projectOperations.isFocusedProjectAvailable();
	}

	public void installRegolaMvc() {

		final LogicalPath webappPath = Path.SRC_MAIN_WEBAPP
				.getModulePathId(projectOperations.getFocusedModuleName());

		final String classesPath = pathResolver.getFocusedIdentifier(
				Path.SRC_MAIN_WEBAPP, "/WEB-INF/classes/");

		// add dependency
		addDependencies();

		// add autodeploy.sh
		installAutodeploy();
		
		// add context.xml file
		installContextFile();

		// create theme in src/main/resource e cancella WEB-INF/classes (ahimè)
		writeTextFile("standard.properties",
				"styleSheet=resources/styles/sol.css");
		
		if (fileManager.exists(classesPath)) {
			fileManager.delete(classesPath);
		}
		
		
		//add properties
		addProperties();
				 
		// copia stili, immagini, viste, tags, ecc.
		copyDirectoryContents("starc/*.*", 	pathResolver.getIdentifier(webappPath, "starc"), false);
		copyDirectoryContents("styles/*.*", 	pathResolver.getIdentifier(webappPath, "styles"), false);
		copyDirectoryContents("tags/*.*", 	pathResolver.getIdentifier(webappPath, "WEB-INF/tags"), true);
		copyDirectoryContents("layouts/*.*", 	pathResolver.getIdentifier(webappPath, "WEB-INF/layouts"), true);
		copyDirectoryContents("views/*.*", 	pathResolver.getIdentifier(webappPath, "WEB-INF/views"), true);

	}
	
	public void addProperties() 
	{	
		  // Use the addon's template file
  	  final InputStream inputStream = FileUtils.getInputStream(getClass(),
                "messages.properties");
		
  	  Properties props = new Properties();
  	  
      try {
    	  props.load(inputStream);
      }
      catch (final IOException ioe) {
          throw new IllegalStateException(ioe);
      }

      final Map<String, String> result = new HashMap<String, String>();
      
      for (final Object key : props.keySet()) {
          result.put(key.toString(), props.getProperty(key.toString()));
      }
      
  	  propFileOperations.addProperties(projectOperations
                 .getPathResolver().getFocusedPath(Path.ROOT),
                 "src/main/webapp/WEB-INF/i18n/messages.properties", Collections.unmodifiableMap(result), true,
                 false);
		 
		 
	}

	private void writeTextFile(final String fullPathFromResource,
			final String message) {
		Validate.notBlank(fullPathFromResource,
				"Text file name to write is required");
		Validate.notBlank(message, "Message required");
		final String path = pathResolver.getFocusedIdentifier(
				Path.SRC_MAIN_RESOURCES, fullPathFromResource);
		final MutableFile mutableFile = fileManager.exists(path) ? fileManager
				.updateFile(path) : fileManager.createFile(path);
		OutputStream outputStream = null;
		try {
			outputStream = mutableFile.getOutputStream();
			IOUtils.write(message, outputStream);
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}
	
	  private void installAutodeploy() {
	       
	        String destinationFile = "";

	        destinationFile = pathResolver.getFocusedIdentifier(Path.ROOT,
	                   "autodeploy.sh");
	       	        
	        if (!fileManager.exists(destinationFile)) {
	            final InputStream templateInputStream = FileUtils.getInputStream(
	                    getClass(), "autodeploy-template.sh");
	            OutputStream outputStream = null;
	            try {
	                // Read template and insert the user's package
	                String input = IOUtils.toString(templateInputStream);
//	                input = input.replace("__PROJECT_NAME__",
//	                        projectName.toLowerCase());

	                // Output the file for the user
	                final MutableFile mutableFile = fileManager
	                        .createFile(destinationFile);
	                outputStream = mutableFile.getOutputStream();
	                IOUtils.write(input, outputStream);
	            }
	            catch (final IOException ioe) {
	                throw new IllegalStateException("Unable to create '"
	                        + destinationFile + "'", ioe);
	            }
	            finally {
	                IOUtils.closeQuietly(templateInputStream);
	                IOUtils.closeQuietly(outputStream);
	            }
	        }
	    }


	public void installContextFile() {
		final String contextPath = projectOperations.getPathResolver()
				.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP,
						"META-INF/context.xml");

		if (fileManager.exists(contextPath)) {
			// LOGGER.warning("META-INF/context.xml di test already exist!");
			return;
		}

		// Use the addon's template file
		final InputStream inputStream = FileUtils.getInputStream(getClass(),
				"context-template.xml");

		final Document appCtx = XmlUtils.readXml(inputStream);
		// final Element root = appCtx.getDocumentElement();

		fileManager.createOrUpdateTextFileIfRequired(contextPath,
				XmlUtils.nodeToString(appCtx), false);

	}

	public boolean isControllerAvailable() {
		return fileManager.exists(pathResolver.getFocusedIdentifier(
				Path.SRC_MAIN_WEBAPP, "WEB-INF/views"))
				&& !projectOperations
						.isFeatureInstalledInFocusedModule(FeatureNames.JSF);
	}

	
	public void addDependencies() {
		List<Dependency> dependencies = new ArrayList<Dependency>();

		// Install the dependency on the add-on jar (
		// dependencies.add(new
		// Dependency("org.regola.roo.addon.regola.pattern",
		// "org.regola.roo.addon.regola.pattern", "0.1.0.BUILD-SNAPSHOT",
		// DependencyType.JAR, DependencyScope.PROVIDED));

		// Install dependencies defined in external XML file
		for (Element dependencyElement : XmlUtils.findElements(
				"/configuration/batch/dependencies/dependency",
				XmlUtils.getConfiguration(getClass()))) {
			dependencies.add(new Dependency(dependencyElement));
		}

		// Add all new dependencies to pom.xml
		projectOperations.addDependencies("", dependencies);

	}

	public void copyDirectoryContents(final String sourceAntPath,
			String targetDirectory, final boolean replace) {
		Validate.notBlank(sourceAntPath, "Source path required");
		Validate.notBlank(targetDirectory, "Target directory required");

		if (!targetDirectory.endsWith("/")) {
			targetDirectory += "/";
		}

		if (!fileManager.exists(targetDirectory)) {
			fileManager.createDirectory(targetDirectory);
		}

		final String path = FileUtils.getPath(getClass(), sourceAntPath);
		final Iterable<URL> urls = OSGiUtils.findEntriesByPattern(
				context.getBundleContext(), path);
		Validate.notNull(urls,
				"Could not search bundles for resources for Ant Path '" + path
						+ "'");
		for (final URL url : urls) {
			final String fileName = url.getPath().substring(
					url.getPath().lastIndexOf("/") + 1);

			
			if (fileManager.exists(targetDirectory + fileName) && replace) {
				fileManager.delete(targetDirectory + fileName);
			}
			
			if (!fileManager.exists(targetDirectory + fileName)) {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				try {
					inputStream = url.openStream();
					outputStream = fileManager.createFile(
							targetDirectory + fileName).getOutputStream();
					IOUtils.copy(inputStream, outputStream);
				} catch (final Exception e) {
					throw new IllegalStateException(
							"Encountered an error during copying of resources for the add-on.",
							e);
				} finally {
					IOUtils.closeQuietly(inputStream);
					IOUtils.closeQuietly(outputStream);
				}
			}

		}
	}

}

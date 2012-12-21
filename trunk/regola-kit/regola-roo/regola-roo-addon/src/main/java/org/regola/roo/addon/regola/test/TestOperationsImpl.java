package org.regola.roo.addon.regola.test;

import static org.springframework.roo.model.SpringJavaType.MOCK_STATIC_ENTITY_METHODS;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.test.IntegrationTestOperationsImpl;
import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.customdata.CustomDataKeys;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.ImportMetadataBuilder;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.ArrayAttributeValue;
import org.springframework.roo.classpath.details.annotations.ClassAttributeValue;
import org.springframework.roo.classpath.details.annotations.StringAttributeValue;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.classpath.scanner.MemberDetails;
import org.springframework.roo.classpath.scanner.MemberDetailsScanner;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.DependencyScope;
import org.springframework.roo.project.DependencyType;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;




/**
 * Creazione di test compatibili con l'ambiente di Regola Kit
 * 
 * @author nicola
 *
 */

@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class TestOperationsImpl implements TestOperations {

	@Reference private TypeLocationService typeLocationService;
	@Reference private TypeManagementService typeManagementService;
	@Reference private MetadataService metadataService;
	@Reference private ProjectOperations projectOperations;
	@Reference private MemberDetailsScanner memberDetailsScanner;
    @Reference private PathResolver pathResolver;
    @Reference private FileManager fileManager;

	
    public static final JavaType CONTEXT_CONFIGURATION = new JavaType(
            "org.springframework.test.context.ContextConfiguration");
    private static final JavaType SPRING_JUNIT = new JavaType(
            "org.springframework.test.context.junit4.SpringJUnit4ClassRunner");
    private static final JavaType RUN_WITH = new JavaType(
            "org.junit.runner.RunWith");
    private static final JavaType TEST = new JavaType("org.junit.Test");
    
    private static final JavaType CONFIGURABLE = new JavaType("org.springframework.beans.factory.annotation.Configurable");

	
	 private ClassOrInterfaceTypeDetails getEntity(final JavaType entity) {
	        final ClassOrInterfaceTypeDetails cid = typeLocationService
	                .getTypeDetails(entity);
	        Validate.notNull(cid, "Java source code details unavailable for type "
	                + cid);
	        return cid;
	    }
	 
	 public MethodMetadataBuilder getMockStaticMethodTest(String declaredByMetadataId, JavaType entity) {
		   
	        final List<AnnotationMetadataBuilder> methodAnnotations = new ArrayList<AnnotationMetadataBuilder>();
	        methodAnnotations.add(new AnnotationMetadataBuilder(TEST));

	        // Get the entity so we can hopefully make a demo method that will be
	        // usable
	        final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();

	        final ClassOrInterfaceTypeDetails cid = typeLocationService
	                .getTypeDetails(entity);
	        
	        if (cid != null) {
	            final MemberDetails memberDetails = memberDetailsScanner
	                    .getMemberDetails(
	                            IntegrationTestOperationsImpl.class.getName(), cid);
	            final List<MethodMetadata> countMethods = memberDetails
	                    .getMethodsWithTag(CustomDataKeys.COUNT_ALL_METHOD);
	            if (countMethods.size() == 1) {
	                final String countMethod = entity.getSimpleTypeName() + "."
	                        + countMethods.get(0).getMethodName().getSymbolName()
	                        + "()";
	                bodyBuilder.appendFormalLine("int expectedCount = 13;");
	                bodyBuilder.appendFormalLine(countMethod + ";");
	                bodyBuilder
	                        .appendFormalLine("org.regola.test.mock.RegolaStaticMethodsMock.expectReturn(expectedCount);");
	                bodyBuilder
	                        .appendFormalLine("org.regola.test.mock.RegolaStaticMethodsMock.playback();");
	                bodyBuilder
	                        .appendFormalLine("Assert.assertEquals(expectedCount, "
	                                + countMethod + ");");
	           
	            }
	        }

	        
	        MethodMetadataBuilder methodMetadataBuilder = new MethodMetadataBuilder(
	                declaredByMetadataId, Modifier.PUBLIC, new JavaSymbolName(
	                        "testStaticMethodMock"), JavaType.VOID_PRIMITIVE, bodyBuilder);
	        
	        methodMetadataBuilder.setAnnotations(methodAnnotations);
	        
	        return methodMetadataBuilder;

	 }
	 
	 private MethodMetadataBuilder getMockMethodTest(String declaredByMetadataId, JavaType entity) {
		 final List<AnnotationMetadataBuilder> methodAnnotations = new ArrayList<AnnotationMetadataBuilder>();
	        methodAnnotations.add(new AnnotationMetadataBuilder(TEST));

	        // Get the entity so we can hopefully make a demo method that will be
	        // usable
	        final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();

	        final ClassOrInterfaceTypeDetails cid = typeLocationService
	                .getTypeDetails(entity);
	        if (cid != null) {
	            final MemberDetails memberDetails = memberDetailsScanner
	                    .getMemberDetails(
	                            IntegrationTestOperationsImpl.class.getName(), cid);
	      
	           
	                 
	                
	                final List<MethodMetadata> persistMethods = memberDetails
		                    .getMethodsWithTag(CustomDataKeys.PERSIST_METHOD);
		            if (persistMethods.size() == 1) {
	                
		            	String persist = persistMethods.get(0).getMethodName().getSymbolName();
		            	bodyBuilder.newLine();
		            	bodyBuilder.appendFormalLine("//test di metodi d'istanza mocckizzati");
		            	bodyBuilder.appendFormalLine(entity.getSimpleTypeName() + " mock = EasyMock.createMock("+entity.getSimpleTypeName() + ".class);");
		                bodyBuilder.appendFormalLine("mock."+ persist +"();");
		                bodyBuilder.appendFormalLine("EasyMock.replay(mock);");
		                bodyBuilder.appendFormalLine(" ");
		                bodyBuilder.appendFormalLine("mock."+ persist+"();");
		                bodyBuilder.appendFormalLine("EasyMock.verify(mock);");
		                
		            }
	           
	        }

	        MethodMetadataBuilder methodMetadataBuilder = new MethodMetadataBuilder(
	                declaredByMetadataId, Modifier.PUBLIC, new JavaSymbolName(
	                        "testMethodMock"), JavaType.VOID_PRIMITIVE, bodyBuilder);
	        
	        methodMetadataBuilder.setAnnotations(methodAnnotations);
	        
	        return methodMetadataBuilder;

	 }
	 
	 public void newRegolaTest(final JavaType entity) {
	        Validate.notNull(entity,
	                "Entity to produce a regola test for is required");

	        final JavaType name = new JavaType(entity + "Test");
	        final String declaredByMetadataId = PhysicalTypeIdentifier
	                .createIdentifier(name, Path.SRC_TEST_JAVA
	                        .getModulePathId(projectOperations
	                                .getFocusedModuleName()));

	        if (metadataService.get(declaredByMetadataId) != null) {
	            // The file already exists
	            return;
	        }

	        // annotazioni
	        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
	        final List<AnnotationAttributeValue<?>> config = new ArrayList<AnnotationAttributeValue<?>>();
	        config.add(new ClassAttributeValue(new JavaSymbolName("value"), SPRING_JUNIT));
	        annotations.add(new AnnotationMetadataBuilder(RUN_WITH, config));
	        annotations.add(new AnnotationMetadataBuilder(new JavaType("org.regola.test.mock.RegolaMockStaticEntityMethods") ));
	        annotations.add(new AnnotationMetadataBuilder(CONFIGURABLE));
	        
	        
	        final AnnotationMetadataBuilder ctxAnnotation = new AnnotationMetadataBuilder(
                    CONTEXT_CONFIGURATION);
                       
            final List<StringAttributeValue> locations = new ArrayList<StringAttributeValue>();
            locations.add(new StringAttributeValue(new JavaSymbolName("locations"), "classpath:/META-INF/spring/applicationContext*.xml"));
            locations.add(new StringAttributeValue(new JavaSymbolName("locations"), "classpath:/spring-test/applicationContext*.xml"));
            
            
            ArrayAttributeValue<StringAttributeValue> arrayAttributeValue = new ArrayAttributeValue<StringAttributeValue>(new JavaSymbolName("locations"), locations);
            ctxAnnotation.addAttribute(arrayAttributeValue);
            annotations.add(ctxAnnotation);
	        
	        
	        
            //metodi
	        final List<MethodMetadataBuilder> methods = new ArrayList<MethodMetadataBuilder>();
	        methods.add(getMockStaticMethodTest(declaredByMetadataId, entity));
	        methods.add(getMockMethodTest(declaredByMetadataId, entity));
	        
	        final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(
	                declaredByMetadataId, Modifier.PUBLIC, name,
	                PhysicalTypeCategory.CLASS);
	        cidBuilder.setAnnotations(annotations);
	        cidBuilder.setDeclaredMethods(methods);
	        
	        //imports
	        ImportMetadataBuilder easyMockImport = new ImportMetadataBuilder(
	        		declaredByMetadataId, 
	        		Modifier.PUBLIC, 
	        		new JavaPackage("org.easymock.EasyMock"), 
	        		new JavaType("org.easymock.EasyMock"), 
	        		false, 
	        		false);
	        
	        ImportMetadataBuilder junitAssertImport = new ImportMetadataBuilder(
	        		declaredByMetadataId, 
	        		Modifier.PUBLIC, 
	        		new JavaPackage("org.junit.Assert"), 
	        		new JavaType("org.junit.Assert"), 
	        		false, 
	        		false);

	        //queste importazioni statiche non sembrano funzionare :-(
	        ImportMetadataBuilder playbackImport = new ImportMetadataBuilder(
	        		declaredByMetadataId, 
	        		/*Modifier.PUBLIC |*/ Modifier.STATIC, 
	        		new JavaPackage("org.regola.test.mock.RegolaStaticMethodsMock"), 
	        		new JavaType("org.regola.test.mock.RegolaStaticMethodsMock.playback"), 
	        		true, 
	        		false);

	        //ImportMetadataBuilder.getImport(declaredByMetadataId, new JavaType(""));

	        
	        cidBuilder.addImports(Arrays.asList(easyMockImport.build(), junitAssertImport.build()));
	        
	        typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
	        
	        updateAspectJBuildPlugin();
	        
	    }

	 
	    
	 
	 protected void updateAspectJBuildPlugin() {
			
		 
	        Dependency dependency = new Dependency("org.regola", 
	        		"regola-test", 
	        		"1.3-SNAPSHOT", 
	        		DependencyType.JAR, DependencyScope.COMPILE);
	        
	        projectOperations.addDependency(projectOperations.getFocusedModuleName(), dependency);
	    
		    final String pom = pathResolver
		                .getFocusedIdentifier(Path.ROOT, "pom.xml");
	        final Document document = XmlUtils.readXml(fileManager
	                .getInputStream(pom));
	        final Collection<String> changes = new ArrayList<String>();


	        final Element root = document.getDocumentElement();
	        final Element aspectLibrariesElement = XmlUtils
	                .findFirstElement(
	                        "/project/build/plugins/plugin[artifactId = 'aspectj-maven-plugin']/configuration/aspectLibraries",
	                        root);
	        Validate.notNull(aspectLibrariesElement,
	                "aspectLibraries element of the aspectj-maven-plugin required");
	       
	        Element xxx = XmlUtils.findFirstElement("aspectLibrary[artifactId='regola-test']", aspectLibrariesElement);
	        
	        //se c'è già non faccio nulla
	        if (xxx!=null)
	        	return;
	        
	    
	        Element aspectLibraryElement = document.createElement("aspectLibrary");

	        final Element groupIdElement = document.createElement("groupId");
	        groupIdElement.setTextContent("org.regola");
	        aspectLibraryElement.appendChild(groupIdElement);
	        
	        final Element artifactIdElement = document.createElement("artifactId");
	        artifactIdElement.setTextContent("regola-test");
	        aspectLibraryElement.appendChild(artifactIdElement);
	        
	        aspectLibrariesElement.appendChild(aspectLibraryElement);
	        
	        changes.add("added regola-test aspectLibrary to aspectj-maven-plugin");
	         
	        if (!changes.isEmpty()) {
	            final String changesMessage = StringUtils.join(changes, "; ");
	            fileManager.createOrUpdateTextFileIfRequired(pom,
	                    XmlUtils.nodeToString(document), changesMessage, false);
	        }
		}


     
	
	public void newIntegrationTest(JavaType entity) {

		Validate.notNull(entity, "Entity to produce an integration test for is required");
		
	    // Verify the requested entity actually exists as a class and is not
        // abstract
        final ClassOrInterfaceTypeDetails cid = getEntity(entity);
        Validate.isTrue(!Modifier.isAbstract(cid.getModifier()), "Type " + entity.getFullyQualifiedTypeName() + " is abstract");

        //final LogicalPath path = PhysicalTypeIdentifier.getPath(cid.getDeclaredByMetadataId());
        
        final JavaType javaTestType = new JavaType(entity + "IntegrationTest");

		// Use Roo's Assert type for null checks
        Validate.notNull(javaTestType, "Java type required");

        // Obtain ClassOrInterfaceTypeDetails for this java type
        ClassOrInterfaceTypeDetails existing = typeLocationService.getTypeDetails(javaTestType);

        // Test if the annotation already exists on the target type
        if (existing != null && MemberFindingUtils.getAnnotationOfType(existing.getAnnotations(), CONTEXT_CONFIGURATION) == null) {
        
        	ClassOrInterfaceTypeDetailsBuilder classOrInterfaceTypeDetailsBuilder = new ClassOrInterfaceTypeDetailsBuilder(existing);
            
            // Create Annotation metadata
            final AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(
                    CONTEXT_CONFIGURATION);
                        
            final List<StringAttributeValue> locations = new ArrayList<StringAttributeValue>();
            locations.add(new StringAttributeValue(new JavaSymbolName("locations"), "classpath:/META-INF/spring/applicationContext*.xml"));
            locations.add(new StringAttributeValue(new JavaSymbolName("locations"), "classpath:/spring-test/applicationContext*.xml"));
            
            
            ArrayAttributeValue<StringAttributeValue> arrayAttributeValue = new ArrayAttributeValue<StringAttributeValue>(new JavaSymbolName("locations"), locations);
            annotationBuilder.addAttribute(arrayAttributeValue);
            
            
            // Add annotation to target type
            classOrInterfaceTypeDetailsBuilder.addAnnotation(annotationBuilder.build());
            
            // Save changes to disk
            typeManagementService.createOrUpdateTypeOnDisk(classOrInterfaceTypeDetailsBuilder.build());
        }

		
	}
	 
	
	

	
}

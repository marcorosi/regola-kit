package org.regola.roo.addon.regola.procedures;

import static org.springframework.roo.model.JavaType.OBJECT;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.propfiles.PropFileOperations;
import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.model.JdkJavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;

@Component
@Service
public class ProceduresOperationsImpl implements ProceduresOperations {

	    @Reference private ProjectOperations projectOperations;
	    @Reference private TypeLocationService typeLocationService;
	    @Reference private TypeManagementService typeManagementService;
//	    @Reference private MetadataService metadataService;
//	    @Reference FileManager fileManager;
	    @Reference PathResolver pathResolver;
//	    @Reference PropFileOperations propFileOperations;
	    
	    
	    public boolean isCommandAvailable() {
	        // Check if a project has been created
	        return projectOperations.isFocusedProjectAvailable();
	    }
	    
	    public void newProcedureDaoClass(final JavaType name, final JavaType superclass,
	            final List<AnnotationMetadataBuilder> annotations) {
	        Validate.notNull(name, "Procedures Dao name required");

	        Validate.isTrue(
	                !JdkJavaType.isPartOfJavaLang(name.getSimpleTypeName()),
	                "Entity name '%s' must not be part of java.lang",
	                name.getSimpleTypeName());

	        int modifier = Modifier.PUBLIC;
	        

	        final String declaredByMetadataId = PhysicalTypeIdentifier
	                .createIdentifier(name,
	                        pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
	        final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(
	                declaredByMetadataId, modifier, name,
	                PhysicalTypeCategory.CLASS);

	        if (!superclass.equals(OBJECT)) {
	            final ClassOrInterfaceTypeDetails superclassClassOrInterfaceTypeDetails = typeLocationService
	                    .getTypeDetails(superclass);
	            if (superclassClassOrInterfaceTypeDetails != null) {
	                cidBuilder
	                        .setSuperclass(new ClassOrInterfaceTypeDetailsBuilder(
	                                superclassClassOrInterfaceTypeDetails));
	            }
	        }

	        cidBuilder.setExtendsTypes(Arrays.asList(superclass));
	        cidBuilder.setAnnotations(annotations);

	        typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
	    }

		
	
}

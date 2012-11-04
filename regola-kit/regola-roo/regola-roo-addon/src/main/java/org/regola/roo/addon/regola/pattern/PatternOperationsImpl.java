package org.regola.roo.addon.regola.pattern;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.BeanInfoUtils;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.ConstructorMetadataBuilder;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.ProjectOperations;

/**
 * Implementation of operations this add-on offers.
 *
 * @since 1.1
 */
@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class PatternOperationsImpl implements PatternOperations {
    
    @Reference private ProjectOperations projectOperations;
    @Reference private TypeLocationService typeLocationService;
    @Reference private TypeManagementService typeManagementService;
    @Reference private MetadataService metadataService;

    /** {@inheritDoc} */
    public boolean isCommandAvailable() {
        // Check if a project has been created
        return projectOperations.isFocusedProjectAvailable();
    }
    
    private ClassOrInterfaceTypeDetails getEntity(final JavaType entity) {
        final ClassOrInterfaceTypeDetails cid = typeLocationService
                .getTypeDetails(entity);
        Validate.notNull(cid, "Java source code details unavailable for type "
                + cid);
        return cid;
    }
    

	public void newModelPattern(final JavaType entity)
    {
        Validate.notNull(entity,
                "Entity to produce an integration test for is required");

        // Verify the requested entity actually exists as a class and is not
        // abstract
        final ClassOrInterfaceTypeDetails cid = getEntity(entity);
        Validate.isTrue(!Modifier.isAbstract(cid.getModifier()), "Type "
                + entity.getFullyQualifiedTypeName() + " is abstract");

        final LogicalPath path = PhysicalTypeIdentifier.getPath(cid
                .getDeclaredByMetadataId());
        
             
        final JavaType name = new JavaType(entity + "Pattern");

        final String declaredByMetadataId = PhysicalTypeIdentifier
                .createIdentifier(name, Path.SRC_MAIN_JAVA.getModulePathId(path.getModule()));

        if (metadataService.get(declaredByMetadataId) != null) {
            // The file already exists
            return;
        }
        
        //Aggiungo l'annotazione RooJavaBean
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        final List<AnnotationAttributeValue<?>> config = new ArrayList<AnnotationAttributeValue<?>>();
        annotations.add(new AnnotationMetadataBuilder(new JavaType("org.springframework.roo.addon.javabean.RooJavaBean"),
                config));
        
        final List<FieldMetadataBuilder> fields = new ArrayList<FieldMetadataBuilder>();
        final List<MethodMetadataBuilder> methods = new ArrayList<MethodMetadataBuilder>();
        
        
        fields.add(getSerialVersionField(declaredByMetadataId));
        
        for (final FieldMetadata field : cid.getDeclaredFields()) {
        	Validate.notNull(field, "Field required");

            // Compute the mutator method name
            final JavaSymbolName methodName = BeanInfoUtils.getAccessorMethodName(field);
            
            //if (cid.getDeclaredField(field.getFieldName()) == null)
            
            //if ( getDeclaredMethod(cid, methodName) == null ) {

            	//methods.add(getAccessor(declaredByMetadataId, field, methodName));
            	
            	AnnotationMetadataBuilder equalsAnno = 
            			new AnnotationMetadataBuilder(new JavaType("org.regola.filter.annotation.Equals"));
            
            	equalsAnno.addStringAttribute("value", field.getFieldName().toString());
            	
            	fields.add(new FieldMetadataBuilder(declaredByMetadataId, Modifier.PUBLIC,
            			Arrays.asList(equalsAnno) ,field.getFieldName(),
            			field.getFieldType() ));
        	//}
                   
        }

        final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(
                declaredByMetadataId, Modifier.PUBLIC, name,
                PhysicalTypeCategory.CLASS);
        cidBuilder.setAnnotations(annotations);
        cidBuilder.setDeclaredMethods(methods);
        cidBuilder.setDeclaredFields(fields);
        cidBuilder.setDeclaredConstructors( Arrays.asList(getNoArgConstructor(declaredByMetadataId)));
        
		cidBuilder.setExtendsTypes(Arrays.asList(new JavaType("org.regola.model.ModelPattern")) );

        typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
     
    }
	
	
	 private FieldMetadataBuilder getSerialVersionField(String declaredByMetadataId) {
	     
	        int modifier = Modifier.STATIC | Modifier.PRIVATE | Modifier.FINAL;
	        
	        // Using the FieldMetadataBuilder to create the field definition. 
	        return  new FieldMetadataBuilder(declaredByMetadataId, // Metadata ID provided by supertype
	            modifier, // Using package protection rather than private
	            //new ArrayList<AnnotationMetadataBuilder>(), // No annotations for this field
	            new JavaSymbolName("serialVersionUID"), // Field name
	            JavaType.LONG_PRIMITIVE, "1L"); // Field type
	}

	private MethodMetadataBuilder getAccessor(String declaredByMetadataId, FieldMetadata field, JavaSymbolName methodName ) {
		
		final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        bodyBuilder.appendFormalLine("return this."
                + field.getFieldName().getSymbolName() + ";");

        final List<AnnotationMetadataBuilder> methodAnnotations = new ArrayList<AnnotationMetadataBuilder>();
        AnnotationMetadataBuilder equalsAnno = new AnnotationMetadataBuilder(new JavaType("org.regola.filter.annotation.Equals"));
        equalsAnno.addStringAttribute("value", field.getFieldName().toString());
        methodAnnotations.add(equalsAnno);
        
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(declaredByMetadataId, Modifier.PUBLIC,
                methodName, field.getFieldType(), bodyBuilder);
        methodBuilder.setAnnotations(methodAnnotations);
        
        return methodBuilder;
	}
    
    private ConstructorMetadataBuilder getNoArgConstructor(String declaredByMetadataId) {
  
    	// Create the constructor
        final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        bodyBuilder.appendFormalLine("super(pagingEnabled);");

        final ConstructorMetadataBuilder constructorBuilder = new ConstructorMetadataBuilder(
        		declaredByMetadataId);
        constructorBuilder.setBodyBuilder(bodyBuilder);
        constructorBuilder.setModifier(Modifier.PUBLIC);
        
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.BOOLEAN_PRIMITIVE));
        constructorBuilder.setParameterTypes(parameterTypes);
        
        constructorBuilder.setParameterNames(Arrays.asList(new JavaSymbolName("pagingEnabled")));
        return constructorBuilder;
    }

	 
	
    public static MethodMetadata getDeclaredMethod(
            final ClassOrInterfaceTypeDetails memberHoldingTypeDetails,
            final JavaSymbolName methodName) {
        if (memberHoldingTypeDetails == null) {
            return null;
        }
        List<JavaType> parameters = new ArrayList<JavaType>();
        
        for (final MethodMetadata method : memberHoldingTypeDetails
                .getDeclaredMethods()) {
            if (method.getMethodName().equals(methodName)) {
                final List<JavaType> parameterTypes = AnnotatedJavaType
                        .convertFromAnnotatedJavaTypes(method
                                .getParameterTypes());
                if (parameterTypes.equals(parameters)) {
                    return method;
                }
            }
        }
        return null;
    }
    
    
    /** {@inheritDoc} */
    public void annotateAll() {
        // Use the TypeLocationService to scan project for all types with a specific annotation
        for (JavaType type: typeLocationService.findTypesWithAnnotation(new JavaType("org.springframework.roo.addon.javabean.RooJavaBean"))) {
            //annotateType(type);
        }
    }
    
  
}
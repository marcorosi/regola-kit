package org.regola.roo.addon.regola.procedures;



import static org.springframework.roo.model.JdkJavaType.MAP;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.felix.scr.annotations.Reference;
import org.springframework.roo.addon.dbre.jdbc.ConnectionProvider;
import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.file.monitor.event.FileDetails;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.DataType;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProceduresMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
	  // Constants
    private static final String PROVIDES_TYPE_STRING = ProceduresMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
    
    private static final Logger LOGGER = HandlerUtils
			.getLogger( ProceduresMetadata.class);
    

    public static final String getMetadataIdentiferType() {
        return PROVIDES_TYPE;
    }
    
    public static final String createIdentifier(JavaType javaType, LogicalPath path) {
        return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
    }

    public static final JavaType getJavaType(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static final LogicalPath getPath(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static boolean isValid(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }


    
    public ProceduresMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, Connection connection, RegolaProceduresAnnotationValues annotationValues) {
        super(identifier, aspectName, governorPhysicalTypeMetadata);
        Validate.isTrue(isValid(identifier), "Metadata identification string '" + identifier + "' does not appear to be a valid");
     
        if (!annotationValues.isValid()) {
        	return;
        }
        
        // Adding fields
        builder.addField(getJdbcTemplate());
        
        String[] procedures = annotationValues.getProcedureName().split(",");
        String[] functions  = annotationValues.getFunctionName().split(",");
        
        // Adding methods
        builder.addMethod(getSetDataSource(annotationValues, procedures, functions));
        
        try {

        	for (String procedure: procedures) 
	        {
	        	if (StringUtils.isEmpty(procedure)) continue;
	        	
        		List<Parameter> parametri = Collections.emptyList();
	
	        	parametri = extractProcedureParams(connection, annotationValues.getCatalog() , annotationValues.getSchema(), 	procedure);
	       		builder.addField(getSimpleJdbcCall(procedure  + "Proc"));
	       		builder.addMethod(getProcedureInvocation(procedure, parametri));
	        }
        	
        	for (String function: functions) {
        		
        		if (StringUtils.isEmpty(function)) continue;
        		
        		List<Parameter> parametri = Collections.emptyList();
        		
        		//parametri = extractFunctionParams(connection, annotationValues.getCatalog() , annotationValues.getSchema(), 	function);
        		parametri = extractProcedureParams(connection, annotationValues.getCatalog() , annotationValues.getSchema(), 	function);
        		
        		builder.addField(getSimpleJdbcCall(function  + "Func"));
        		//builder.addMethod(getProcedureInvocation(function, parametri));
        		builder.addMethod(getFunctionInvocation(function, parametri));
        	}
        
        	//connection.close();
        } catch (SQLException e1) {
        	e1.printStackTrace();
        }
        
        // Adding imports
        builder.getImportRegistrationResolver().addImport(new JavaType("java.util.Map"));
        builder.getImportRegistrationResolver().addImport(new JavaType("org.springframework.dao.DataAccessException"));
        builder.getImportRegistrationResolver().addImport(new JavaType("org.springframework.jdbc.core.namedparam.MapSqlParameterSource"));
        builder.getImportRegistrationResolver().addImport(new JavaType("org.springframework.jdbc.core.namedparam.SqlParameterSource"));
        builder.getImportRegistrationResolver().addImport(new JavaType("java.sql.SQLException"));
        
        // Create a representation of the desired output ITD
        itdTypeDetails = builder.build();
    }
    
    private MethodMetadata getSetDataSource(RegolaProceduresAnnotationValues annotationValues, String[] procedures, String[] functions) {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName("setDataSource");
                
        // Define method parameter types (none in this case)
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(new  JavaType("javax.sql.DataSource")));
        
        // Define method parameter names (none in this case)
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
        parameterNames.add(new JavaSymbolName("dataSource"));
        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        
        final AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(
                new JavaType("org.springframework.beans.factory.annotation.Autowired"));
        annotations.add(annotationBuilder);
       
        
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        bodyBuilder.appendFormalLine("jdbcTemplate = new JdbcTemplate(dataSource);");
        bodyBuilder.appendFormalLine("jdbcTemplate.setResultsMapCaseInsensitive(true);");
        
        for (String procedura: procedures) {
        	
        	if (StringUtils.isEmpty(procedura)) continue;
        	
        	bodyBuilder.newLine();
        	bodyBuilder.appendFormalLine( procedura +"Proc = new SimpleJdbcCall(jdbcTemplate)");
        	bodyBuilder.appendFormalLine(".withSchemaName(\"" + annotationValues.getSchema() + "\")");
        	bodyBuilder.appendFormalLine(".withCatalogName(\"" + annotationValues.getCatalog()+ "\")");
        	bodyBuilder.appendFormalLine(".withProcedureName(\"" + procedura + "\");");
        	
        }
        
        for (String function: functions) {
        	
        	if (StringUtils.isEmpty(function)) continue;
        	
        	bodyBuilder.newLine();
        	bodyBuilder.appendFormalLine( function +"Func = new SimpleJdbcCall(jdbcTemplate)");
        	bodyBuilder.appendFormalLine(".withSchemaName(\"" + annotationValues.getSchema() + "\")");
        	bodyBuilder.appendFormalLine(".withCatalogName(\"" + annotationValues.getCatalog()+ "\")");
        	bodyBuilder.appendFormalLine(".withFunctionName(\"" + function + "\");");
        	
        }
        
        // Create the return type
        final JavaType returnType  = JavaType.VOID_PRIMITIVE;
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC  , methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }


    private MethodMetadata getProcedureInvocation(String proc, List<Parameter> params ) {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName(proc);
                
        // Define method parameter types (none in this case)
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        // Define method parameter names (none in this case)
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();

        for (Parameter param: params) {

        	if (param.getParameterType() != DatabaseMetaData.procedureColumnOut ) {
        		parameterTypes.add(AnnotatedJavaType.convertFromJavaType(param.getJavaType() ));
        		parameterNames.add(new JavaSymbolName(param.getParameterCamelName()));
        	} 
        		
        }
        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        
        
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();

        bodyBuilder.appendFormalLine("SqlParameterSource in = new MapSqlParameterSource()");
        for (Parameter param: params) {
        	
          	if (param.getParameterType() != DatabaseMetaData.procedureColumnOut ) {
            bodyBuilder.appendFormalLine(String.format(".addValue(\"%s\", %s) ", 
    				param.getParameterName(), 
    				param.getParameterCamelName()));
          	}
          		
        }
        
        bodyBuilder.appendFormalLine(";");
        
        bodyBuilder.appendFormalLine("Map<String, Object> out;");
        bodyBuilder.appendFormalLine("try {");
        bodyBuilder.appendFormalLine("out = " +  proc +"Proc.execute(in);");
        bodyBuilder.appendFormalLine("} catch (DataAccessException e) {");
        bodyBuilder.appendFormalLine("	if (!e.contains(SQLException.class) || !(e.getMessage().contains(\"ORA-04068\"))) throw e;");
        bodyBuilder.appendFormalLine("out = " +  proc +"Proc.execute(in);");
        bodyBuilder.appendFormalLine("}");
        
        for (Parameter param: params) {
        	
          	if (param.getParameterType() == DatabaseMetaData.procedureColumnOut )
          		bodyBuilder.appendFormalLine(String.format("// OUT PARAM: %s %s", 
    				param.getParameterName(), param.getTypeName()));
          		
        }
        
        
        bodyBuilder.appendFormalLine("return out;");
        //bodyBuilder.appendFormalLine("");
        
        // Create the return type
        //final JavaType returnType  = JavaType.VOID_PRIMITIVE;
        
        final JavaType returnType = new JavaType(
        		MAP.getFullyQualifiedTypeName(), 0, DataType.TYPE, null,
                Arrays.asList(JavaType.STRING, JavaType.OBJECT));
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC  , methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }


    private MethodMetadata getFunctionInvocation(String functionName, List<Parameter> params ) {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName(functionName);
                
        // Define method parameter types (none in this case)
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        // Define method parameter names (none in this case)
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();

        Parameter returnValue = null;

        
        
        for (Parameter param: params) {

        	if (param.getParameterType() == DatabaseMetaData.functionColumnIn ) {
        		
        		parameterTypes.add(AnnotatedJavaType.convertFromJavaType(param.getJavaType() ));
        		parameterNames.add(new JavaSymbolName(param.getParameterCamelName()));
        	}
        	else if (param.getParameterType() == DatabaseMetaData.functionColumnResult) {
        		returnValue = param;
        	}
        		
        }
        
        // Create the return type
        JavaType returnType  = JavaType.VOID_PRIMITIVE;

        if ( returnValue != null )
        	returnType = returnValue.getJavaType();

        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null ) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        
        
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();

        bodyBuilder.appendFormalLine("SqlParameterSource in = new MapSqlParameterSource()");
        for (Parameter param: params) {
        	
          	if (param.getParameterType() == DatabaseMetaData.procedureColumnIn ||
          			param.getParameterType() == DatabaseMetaData.procedureColumnInOut ) {
            bodyBuilder.appendFormalLine(String.format(".addValue(\"%s\", %s) ", 
    				param.getParameterName(), 
    				param.getParameterCamelName()));
          	}
          		
        }
        
        bodyBuilder.appendFormalLine(";");
        
        bodyBuilder.appendFormalLine("try {");
        bodyBuilder.appendFormalLine("return " +  functionName +"Func.executeFunction("+ returnValue.getJavaType().getSimpleTypeName()  + ".class, in);");
        bodyBuilder.appendFormalLine("} catch (DataAccessException e) {");
        bodyBuilder.appendFormalLine("	if (!e.contains(SQLException.class) || !(e.getMessage().contains(\"ORA-04068\"))) throw e;");
        bodyBuilder.appendFormalLine("return " +  functionName +"Func.executeFunction("+ returnValue.getJavaType().getSimpleTypeName()  + ".class, in);");
        bodyBuilder.appendFormalLine("}");
        
        for (Parameter param: params) {
          	if (param.getParameterType() == DatabaseMetaData.functionColumnOut )
          		bodyBuilder.appendFormalLine(String.format("// OUT PARAM: %s %s", 
    				param.getParameterName(), param.getTypeName()));
        }
        
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC  , methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }

    private FieldMetadata getSimpleJdbcCall(String fieldName) {
    	if ( governorTypeDetails.getDeclaredField(new JavaSymbolName(fieldName)) != null  )
			return null;
	
    	// Note private fields are private to the ITD, not the target type, this is undesirable if a dependent method is pushed in to the target type
        int modifier =  Modifier.PRIVATE;
        
        // Using the FieldMetadataBuilder to create the field definition. 
        final FieldMetadataBuilder fieldBuilder = new FieldMetadataBuilder(getId(), // Metadata ID provided by supertype
            modifier, // Using package protection rather than private
            //new ArrayList<AnnotationMetadataBuilder>(), // No annotations for this field
            new JavaSymbolName(fieldName ), // Field name
            new  JavaType("org.springframework.jdbc.core.simple.SimpleJdbcCall"), null); // Field type
        
        return fieldBuilder.build(); // Build and return a FieldMetadata instance
    }
    
    private FieldMetadata getJdbcTemplate() {
    	if ( governorTypeDetails.getDeclaredField(new JavaSymbolName("jdbcTemplate")) != null  )
    			return null;
    	
    	// Note private fields are private to the ITD, not the target type, this is undesirable if a dependent method is pushed in to the target type
        int modifier =  Modifier.PRIVATE;
        
        // Using the FieldMetadataBuilder to create the field definition. 
        final FieldMetadataBuilder fieldBuilder = new FieldMetadataBuilder(getId(), // Metadata ID provided by supertype
            modifier, // Using package protection rather than private
            //new ArrayList<AnnotationMetadataBuilder>(), // No annotations for this field
            new JavaSymbolName("jdbcTemplate"), // Field name
            new  JavaType("org.springframework.jdbc.core.JdbcTemplate"), null); // Field type
        
        return fieldBuilder.build(); // Build and return a FieldMetadata instance
    }
    
        
    private MethodMetadata methodExists(JavaSymbolName methodName, List<AnnotatedJavaType> paramTypes) {
        // We have no access to method parameter information, so we scan by name alone and treat any match as authoritative
        // We do not scan the superclass, as the caller is expected to know we'll only scan the current class
        for (MethodMetadata method : governorTypeDetails.getDeclaredMethods()) {
        	//LOGGER.warning(method.getMethodName().toString());
        	
            //diciamo solo che AnnotatedJavaType non ha un'equals...
        	boolean paramsCheck = method.getParameterTypes().toString().equals(paramTypes.toString());        	
        	
            //if (method.getMethodName().equals(methodName) && method.getParameterTypes().equals(paramTypes)) {
            if (method.getMethodName().equals(methodName) && paramsCheck) {
                // Found a method of the expected name; we won't check method parameters though
                return method;
            }
        }
        return null;
    }
    
    // Typically, no changes are required beyond this point
    
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("identifier", getId());
        builder.append("valid", valid);
        builder.append("aspectName", aspectName);
        builder.append("destinationType", destination);
        builder.append("governor", governorPhysicalTypeMetadata.getId());
        builder.append("itdTypeDetails", itdTypeDetails);
        return builder.toString();
    }
    
    public List<Parameter> extractProcedureParams(Connection con, String catalogName, String schemaName, String procedureName) 
    		throws SQLException {
    	
    	ResultSet procs = null;
    	List<Parameter> callParameterMetaData = new ArrayList<Parameter>();
    	List<String> found = new ArrayList<String>();
   			
    	DatabaseMetaData metaData = con.getMetaData();
		
    	if (StringUtils.isEmpty(procedureName))
    		return callParameterMetaData;
			
    	LOGGER.warning("Elenco delle procedure presenti su db:");
    	
		procs = metaData.getProcedures(catalogName.toUpperCase(), schemaName.toUpperCase(), null);
		while (procs.next()) {
			
			LOGGER.warning(procs.getString("PROCEDURE_CAT") + "." + procs.getString("PROCEDURE_SCHEM") +
					"." + procs.getString("PROCEDURE_NAME"));
		}
		
		procs = metaData.getProcedureColumns(
				catalogName.toUpperCase(), schemaName.toUpperCase(), procedureName.toUpperCase(), null);
		
		
		LOGGER.warning("\nRicerco parametri per " + procedureName.toUpperCase());
		while (procs.next()) {
			String columnName = procs.getString("COLUMN_NAME");
			int columnType = procs.getInt("COLUMN_TYPE");
			if (columnName == null && (
					columnType == DatabaseMetaData.procedureColumnIn  ||
					columnType == DatabaseMetaData.procedureColumnInOut ||
					columnType == DatabaseMetaData.procedureColumnOut)) {
				
				LOGGER.warning("Skipping metadata for: "
						+ columnName +
						" " + columnType +
						" " + procs.getInt("DATA_TYPE") +
						" " + procs.getString("TYPE_NAME") +
						" " + procs.getBoolean("NULLABLE") +
						" (probably a member of a collection)");
					
					
			}
			else {
				
				if (columnType == DatabaseMetaData.functionColumnResult &&
						columnName == null) {
					columnName = "returnValue";
				}
				
				Parameter meta = new Parameter(columnName, columnType,
						procs.getInt("DATA_TYPE"), procs.getString("TYPE_NAME"), procs.getBoolean("NULLABLE")
				);
				
				callParameterMetaData .add(meta);
				LOGGER.warning("Retrieved metadata: " + meta.getParameterName() + " " +
					meta.getParameterType() + " " + meta.getSqlType() +
					" " + meta.getTypeName() + " " + meta.isNullable());
					
			}
		}
		
		return callParameterMetaData;
    }
    
    public List<Parameter> extractFunctionParams(Connection con, String catalogName, String schemaName, String functionName) 
    		throws SQLException {
    	
    	ResultSet procs = null;
    	List<Parameter> callParameterMetaData = new ArrayList<Parameter>();
    	List<String> found = new ArrayList<String>();
   			
    	DatabaseMetaData metaData = con.getMetaData();
		
    	if (StringUtils.isEmpty(functionName))
    		return callParameterMetaData;
			
    	LOGGER.warning("Elenco delle funzioni presenti su db:");
    	
    	procs = metaData.getFunctions(catalogName.toUpperCase(), schemaName.toUpperCase(), functionName.toUpperCase());
		while (procs.next()) {
			
			LOGGER.warning(procs.getString("PROCEDURE_CAT") + "." + procs.getString("PROCEDURE_SCHEM") +
					"." + procs.getString("PROCEDURE_NAME"));
		}
		
    	try {
		procs = metaData.getFunctionColumns(
				catalogName.toUpperCase(), schemaName.toUpperCase(), functionName.toUpperCase(), null);
    	} catch (Throwable t) {
    		t.printStackTrace();
    	}
    	
    	LOGGER.warning("\nRicerco parametri per " + functionName.toUpperCase());
		while (procs.next()) {
			String columnName = procs.getString("COLUMN_NAME");
			int columnType = procs.getInt("COLUMN_TYPE");
			if (columnName == null && (
					columnType == DatabaseMetaData.functionColumnResult  ||
					columnType == DatabaseMetaData.functionColumnIn  ||
					columnType == DatabaseMetaData.functionColumnInOut ||
					columnType == DatabaseMetaData.functionColumnOut)) {
				
				LOGGER.warning("Skipping metadata for: "
						+ columnName +
						" " + columnType +
						" " + procs.getInt("DATA_TYPE") +
						" " + procs.getString("TYPE_NAME") +
						" " + procs.getBoolean("NULLABLE") +
						" (probably a member of a collection)");
					
					
			}
			else {
				Parameter meta = new Parameter(columnName, columnType,
						procs.getInt("DATA_TYPE"), procs.getString("TYPE_NAME"), procs.getBoolean("NULLABLE")
				);
				
				callParameterMetaData .add(meta);
				LOGGER.warning("Retrieved metadata: " + meta.getParameterName() + " " +
					meta.getParameterType() + " " + meta.getSqlType() +
					" " + meta.getTypeName() + " " + meta.isNullable());
					
			}
		}
		
		return callParameterMetaData;
    }
    
}

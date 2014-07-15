package org.regola.roo.addon.regola.procedures;

import org.apache.commons.lang3.StringUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AbstractAnnotationValues;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.model.RooJavaType;

public class RegolaProceduresAnnotationValues extends AbstractAnnotationValues {
	
    @AutoPopulate private String catalog;
    @AutoPopulate private String schema;
    @AutoPopulate private String procedureName = "";
    @AutoPopulate private String functionName = "";
    
    /**
     * Constructor
     * 
     * @param governorPhysicalTypeMetadata
     */
    public RegolaProceduresAnnotationValues(
            final PhysicalTypeMetadata governorPhysicalTypeMetadata) {
        super(governorPhysicalTypeMetadata, new JavaType("org.regola.roo.addon.regola.procedures.RooRegolaProcedures"));
        AutoPopulationUtils.populate(this, annotationMetadata);
    }

	public String getCatalog() {
		return catalog;
	}

	public String getSchema() {
		return schema;
	}

	public String getProcedureName() {
		return procedureName ;
	}

	public String getFunctionName() {
		return functionName;
	}
	
	
	public boolean isValid() {
		if ( StringUtils.isEmpty(catalog)) {
			return false;
		}
		
		if ( StringUtils.isEmpty(schema)) {
			return false;
		}
		
		if ( StringUtils.isEmpty(procedureName) && StringUtils.isEmpty(functionName)) {
			return false;
		}
		
		
		return true;
	}



}

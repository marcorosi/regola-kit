package org.regola.roo.addon.regola.procedures;

import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.JavaType;

public interface ProceduresOperations {
	
	public void newProcedureDaoClass(final JavaType name, final JavaType superclass,
            final List<AnnotationMetadataBuilder> annotations);

	boolean isCommandAvailable();

}

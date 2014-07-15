package org.regola.roo.addon.regola.procedures;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.regola.roo.addon.regola.test.TestOperations;
import org.springframework.roo.classpath.details.BeanInfoUtils;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;
import org.springframework.roo.support.logging.HandlerUtils;

@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class ProceduresCommands  implements CommandMarker {

	private static final Logger LOGGER = HandlerUtils
			.getLogger( ProceduresCommands.class);
	
	@Reference private ProceduresOperations proceduresOperations;
	@Reference private TestOperations regolaTestOperations;

	
	public static final JavaType ROO_REGOLA_PROCEDURES = new JavaType(
			"org.regola.roo.addon.regola.procedures.RooRegolaProcedures");
	

	public static final JavaType SPRING_REPOSITORY = new JavaType(
			"org.springframework.stereotype.Repository");
	
	final AnnotationMetadataBuilder SPRING_REPOSITORY_BUILDER = new AnnotationMetadataBuilder(
			SPRING_REPOSITORY);
	
	
	@CliAvailabilityIndicator("regola dao procedures" )
    public boolean isPersistentClassAvailable() {
		return proceduresOperations.isCommandAvailable();
    }
	
	@CliCommand(value = "regola dao procedures", help = "Creates a new PL/SQL Procedures DAO  in SRC_MAIN_JAVA")
	public void newProcedureClassDao(
			@CliOption(key = "class", optionContext = "update,project", mandatory = true, help = "Name of the Dao to create") final JavaType name,
			@CliOption(key = "extends", mandatory = false, unspecifiedDefaultValue = "java.lang.Object", help = "The superclass (defaults to java.lang.Object)") final JavaType superclass,
			@CliOption(key = "testAutomatically", mandatory = false, specifiedDefaultValue = "true", unspecifiedDefaultValue = "false", help = "Create automatic integration tests for this entity") final boolean testAutomatically,
			@CliOption(key = "procedureNames", mandatory = true, help = "The name of procedures to adapt, commas separated") final String procedureNames,
			@CliOption(key = "schema", mandatory = true, help = "The schema of the procedures") final String schema,
			@CliOption(key = "catalog", mandatory = true, help = "The catalag (package name) of procedures") final String catalog) {
		
		// Reject attempts to name the entity "Test", due to possible clashes
		// with data on demand (see ROO-50)
		// We will allow this to happen, though if the user insists on it via
		// --permitReservedWords (see ROO-666)
		if (!BeanInfoUtils.isEntityReasonablyNamed(name)) {
			if (testAutomatically) {
				throw new IllegalArgumentException(
						"Procedure Dao name cannot contain 'Test' or 'TestCase' as you are requesting tests; remove --testAutomatically or rename the proposed entity");
			}
		}

		// Create entity's annotations
		final List<AnnotationMetadataBuilder> annotationBuilder = new ArrayList<AnnotationMetadataBuilder>();
		annotationBuilder.add(SPRING_REPOSITORY_BUILDER);
		annotationBuilder.add(getEntityAnnotationBuilder(procedureNames,schema, catalog));

		// Produce the entity itself
		proceduresOperations.newProcedureDaoClass(name,  superclass, annotationBuilder);

		if (testAutomatically) {
			regolaTestOperations.newRegolaTest(name);
			
		}
	}

	private AnnotationMetadataBuilder getEntityAnnotationBuilder(
			final String procedureName, final String schema,
			final String catalog) {

		final AnnotationMetadataBuilder entityAnnotationBuilder = new AnnotationMetadataBuilder(
				ROO_REGOLA_PROCEDURES);

		if (catalog != null) {
			entityAnnotationBuilder.addStringAttribute("catalog", catalog);
		}

		if (schema != null) {
			entityAnnotationBuilder.addStringAttribute("schema", schema);
		}
		
		if (procedureName != null) {
			entityAnnotationBuilder.addStringAttribute("procedureName",
					procedureName);
		}

		return entityAnnotationBuilder;
	}

	
}

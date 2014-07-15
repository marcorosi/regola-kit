package org.regola.roo.addon.regola.test;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.test.IntegrationTestOperations;
import org.springframework.roo.classpath.details.BeanInfoUtils;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.model.ReservedWords;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

/**
 * Supporto per i test in salsa Regola Kit
 * 
 * @author nicola
 * 
 */
@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class TestCommands implements CommandMarker {

	@Reference private TestOperations regolaTestOperations;
	@Reference private IntegrationTestOperations integrationTestOperations;

	@CliAvailabilityIndicator("regola test")
	public boolean isCommandAvailable() {
		return integrationTestOperations.isIntegrationTestInstallationPossible();
	}

	@CliCommand(value = "regola test", help = "Creates a new integration test for the specified entity")
	public void newIntegrationTest(
			@CliOption(key = {"","entity"}, mandatory = false, unspecifiedDefaultValue = "*", optionContext = "update,project", help = "The name of the entity to create an integration test for") final JavaType entity,
			@CliOption(key = "permitReservedWords", mandatory = false, unspecifiedDefaultValue = "false", specifiedDefaultValue = "true", help = "Indicates whether reserved words are ignored by Roo") final boolean permitReservedWords,
			@CliOption(key = "transactional", mandatory = false, unspecifiedDefaultValue = "true", specifiedDefaultValue = "true", help = "Indicates whether the created test cases should be run withing a Spring transaction") final boolean transactional) {

		if (!permitReservedWords) {
			ReservedWords.verifyReservedWordsNotPresent(entity);
		}

		Validate.isTrue(
				BeanInfoUtils.isEntityReasonablyNamed(entity),
				"Cannot create an integration test for an entity named 'Test' or 'TestCase' under any circumstances");

		//integrationTestOperations.newIntegrationTest(entity, transactional);
		regolaTestOperations.newRegolaTest(entity);
	}

}

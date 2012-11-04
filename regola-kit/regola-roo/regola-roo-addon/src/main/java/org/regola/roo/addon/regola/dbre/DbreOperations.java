package org.regola.roo.addon.regola.dbre;

import java.util.Set;

import org.springframework.roo.addon.dbre.model.Schema;
import org.springframework.roo.model.JavaPackage;

public interface DbreOperations {

	public abstract boolean isDbreInstallationPossible();

	public abstract void reverseEngineerDatabase(Set<Schema> schemas,
			JavaPackage destinationPackage, boolean testAutomatically,
			boolean view, Set<String> includeTables, Set<String> excludeTables,
			boolean includeNonPortableAttributes, boolean activeRecord);

}
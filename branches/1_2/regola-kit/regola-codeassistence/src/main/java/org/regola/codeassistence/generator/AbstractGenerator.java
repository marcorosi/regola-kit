package org.regola.codeassistence.generator;

import java.util.Map;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;

public class AbstractGenerator implements Generator {

	protected String name;
	
	@Override
	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		throw new UnsupportedOperationException();
		
	}
 
	@Override
	public void generate(Environment env, VariablesBuilder pb) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public String getDisplayName() {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public String getName() {
		return name;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractGenerator other = (AbstractGenerator) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public Map<String, String> simulate(Environment env, VariablesBuilder pb) {
		env.writers.setSimulate(true);
		this.generate(env, pb);
		env.writers.setSimulate(false);
		
		return env.writers.getSimulationMap();
	}

	@Override
	public String getDescription() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		
		return getDisplayName();
	}
	
	

}

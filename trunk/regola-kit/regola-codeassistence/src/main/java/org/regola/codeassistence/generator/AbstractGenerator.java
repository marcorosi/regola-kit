package org.regola.codeassistence.generator;

import java.util.Map;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;

public class AbstractGenerator implements Generator {

	@Override
	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		throw new UnsupportedOperationException();
		
	}
 
	@Override
	public void generate(Environment env, ParameterBuilder pb) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public String getDisplayName() {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public Map<String, String> simulate(Environment env, ParameterBuilder pb) {
		env.setSimulate(true);
		this.generate(env, pb);
		env.setSimulate(false);
		
		return env.getSimulationMap();
	}

	@Override
	public String getDescription() {
		throw new UnsupportedOperationException();
	}

}

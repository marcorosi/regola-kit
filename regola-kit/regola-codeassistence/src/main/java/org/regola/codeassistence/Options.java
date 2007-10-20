package org.regola.codeassistence;

import org.regola.codeassistence.generator.ApplicationPropertiesGenerator;
import org.regola.codeassistence.generator.CustomDaoGenerator;
import org.regola.codeassistence.generator.CustomManagerGenerator;
import org.regola.codeassistence.generator.FilterGenerator;
import org.regola.codeassistence.generator.FormManagedBeanGenerator;
import org.regola.codeassistence.generator.FormPageGenerator;
import org.regola.codeassistence.generator.Generator;
import org.regola.codeassistence.generator.ListManagedBeanGenerator;
import org.regola.codeassistence.generator.ListPageGenerator;
import org.regola.codeassistence.generator.ServiceManagerGenerator;
import org.regola.util.Ognl;
import java.util.ArrayList;
import java.util.List;



/**
 * Raccoglie le opzioni impostate da riga di comando quando si lancia l'assistente alla generazione del codice.
 * @author  nicola
 */
public class Options {

	/**
	 * @uml.property  name="allGenerators"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	static Generator[] allGenerators = { new ApplicationPropertiesGenerator(),
			new CustomDaoGenerator(), new FilterGenerator(),
			new FormPageGenerator(), new ListManagedBeanGenerator(),
			new FormManagedBeanGenerator(), new CustomManagerGenerator(),
			new ListPageGenerator(), new ServiceManagerGenerator() };
	
	String modelClass;
	
	public static Generator[] getAllGenerators() {
		return allGenerators;
	}

	/**
	 * @return  the modelClass
	 * @uml.property  name="modelClass"
	 */
	public String getModelClass() {
		return modelClass;
	}

	/**
	 * @param modelClass  the modelClass to set
	 * @uml.property  name="modelClass"
	 */
	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public Options(String className) {

		modelClass = className;
		
	}
	
	@SuppressWarnings("unchecked")
	public Generator[] getGeneratorListByNames(String[] names)
	{
		List<Generator> list = new ArrayList<Generator>();
		
		for (String name :names)
		{
			List<Generator> gens = (List<Generator>) Ognl
			.getValue("#root.{^ #this.name == '" +name + "'}", allGenerators);
			
			if (gens.size()==1 && gens.get(0)!=null) list.add(gens.get(0));
			
		}
		
		Generator[] gn = {};
		return list.toArray(gn);
		
	}

}

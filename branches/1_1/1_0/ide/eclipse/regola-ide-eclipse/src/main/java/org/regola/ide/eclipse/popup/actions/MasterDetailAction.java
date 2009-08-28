package org.regola.ide.eclipse.popup.actions;

import static org.eclipse.jface.window.Window.OK;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Shell;
import org.regola.codeassistence.generator.ApplicationPropertiesGenerator;
import org.regola.codeassistence.generator.CustomDaoGenerator;
import org.regola.codeassistence.generator.CustomManagerGenerator;
import org.regola.codeassistence.generator.FilterGenerator;
import org.regola.codeassistence.generator.FormManagedBeanGenerator;
import org.regola.codeassistence.generator.FormPageGenerator;
import org.regola.codeassistence.generator.Generator;
import org.regola.codeassistence.generator.ListManagedBeanGenerator;
import org.regola.codeassistence.generator.ListPageGenerator;
import org.regola.ide.eclipse.dialogs.CodeGeneratorDialog;

public class MasterDetailAction extends CodeGenerationAbstract {

	public MasterDetailAction() {
		super();
	}

	public void run(IAction action) {
		if (modelTarget == null)
			return;

		Generator[] generators = { 
			new CustomDaoGenerator(), new FilterGenerator(),
			new CustomManagerGenerator(),
			new ApplicationPropertiesGenerator(),
			new ListManagedBeanGenerator(),
			new ListPageGenerator(), 
			new FormManagedBeanGenerator(),
			new FormPageGenerator()
			};
		
		try {
			run(Arrays.asList(generators));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				

		modelTarget = null;
		
	}


}

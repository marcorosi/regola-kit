package org.regola.ide.eclipse.popup.actions;

import static org.eclipse.jface.window.Window.OK;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.regola.ide.eclipse.dialogs.CodeGeneratorDialog;

public class CodeGeneratorAction extends CodeGenerationAbstract {

	/**
	 * Constructor for Action1.
	 */
	public CodeGeneratorAction() {
		super();
	}

	
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		if (modelTarget == null)
			return;

		Shell shell = new Shell();
		CodeGeneratorDialog dlg = new CodeGeneratorDialog(shell);

		dlg
				.setModelClass(modelTarget.findPrimaryType()
						.getFullyQualifiedName());

		dlg.create();
		dlg.setBlockOnOpen(true);

		if (OK == dlg.open()) {

			if (dlg.getSelected().size()>0)
			{
				try {
					run(dlg.getSelected());
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		modelTarget = null;
		/*
		 * MessageDialog.openInformation(shell,"Regola kit Eclipse IDEPlug-in",
		 * "Regola-kit was executed.");
		 */

	}

	
}

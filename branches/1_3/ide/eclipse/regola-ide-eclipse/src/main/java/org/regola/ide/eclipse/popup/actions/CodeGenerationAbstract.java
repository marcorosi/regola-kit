package org.regola.ide.eclipse.popup.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.regola.codeassistence.generator.Generator;

public abstract class CodeGenerationAbstract implements IObjectActionDelegate {
	
	ICompilationUnit modelTarget = null;
	
	public void run(List<Generator> generators) throws CoreException {

		IJavaProject javaProject = modelTarget.getJavaProject();

		String generatorLine = "";
		for (Generator generator : generators) {
			if (generatorLine.length() > 0)
				generatorLine += ",";
			generatorLine += generator.getName();
		}

		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager
				.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		ILaunchConfigurationWorkingCopy wc = type.newInstance(null,
				"Regola-kit code generator");
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
				javaProject.getProject().getName());
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
				"org.regola.codeassistence.FullStack");
		wc.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "-c "
						+ modelTarget.findPrimaryType().getFullyQualifiedName()
						+ " -g " + generatorLine);
		
		ILaunchConfiguration config = wc.doSave();
		config.launch(ILaunchManager.RUN_MODE, null);

		
	}

	public void runCodeAssistence(IJavaProject javaProject)
			throws CoreException {

		IVMInstall vmInstall = JavaRuntime.getVMInstall(javaProject);
		if (vmInstall == null)
			vmInstall = JavaRuntime.getDefaultVMInstall();

		IVMRunner vmRunner = vmInstall.getVMRunner(ILaunchManager.RUN_MODE);

		String[] classPath = null;
		classPath = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
		if (classPath != null) {
			VMRunnerConfiguration vmConfig = new VMRunnerConfiguration(
					"it.kion.regola.codeassistence.FullStackX", classPath);

			// String args = "-c MiaClasse -g dao";
			// vmConfig.setProgramArguments(args.split(" "));

			ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);

			// DebugUIPlugin.getDefault().getProcessConsoleManager().launchAdded(
			// launch);
			vmRunner.run(vmConfig, launch, null);
		}

	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

	public abstract void run(IAction action) ;

	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection) {

		if (selection instanceof IStructuredSelection) {
			Iterator iter = ((IStructuredSelection) selection).iterator();

			while (iter.hasNext()) {
				Object obj = iter.next();

				if (obj instanceof ICompilationUnit) {
					modelTarget = (ICompilationUnit) obj;
					// IJavaProject project = src.getJavaProject();

				}

			}
		}

	}


}

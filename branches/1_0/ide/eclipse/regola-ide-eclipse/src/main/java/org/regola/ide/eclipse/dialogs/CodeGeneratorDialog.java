package org.regola.ide.eclipse.dialogs;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.regola.codeassistence.Options;
import org.regola.codeassistence.generator.Generator;

public class CodeGeneratorDialog extends Dialog {

	private StyledText selezionaIlGeneratoreStyledText;
	class Sorter extends ViewerSorter {
		public int compare(Viewer viewer, Object e1, Object e2) {
			Generator item1 = (Generator) e1;
			Generator item2 = (Generator) e2;
			return item1.getName().compareTo(item2.getName());
		}
	}
	private CheckboxTableViewer checkboxTableViewer;
	private Table table;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public CodeGeneratorDialog(Shell parentShell) {
		super(parentShell);
	}
	
	List<Generator> generators = Arrays.asList( Options.getAllGenerators());
	
	public List<Generator> getGenerators() {
		return generators;
	}

	public void setGenerators(List<Generator> generators) {
		this.generators = generators;
	}
	
	String modelClass = "";
	
	

	public void init()
	{
		selezionaIlGeneratoreStyledText.setText(
				String.format(selezionaIlGeneratoreStyledText.getText(),getModelClass()));
	}
	
	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		selezionaIlGeneratoreStyledText = new StyledText(container, SWT.BORDER);
		selezionaIlGeneratoreStyledText.setWordWrap(true);
		selezionaIlGeneratoreStyledText.setEditable(false);
		final GridData gd_selezionaIlGeneratoreStyledText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_selezionaIlGeneratoreStyledText.heightHint = 32;
		selezionaIlGeneratoreStyledText.setLayoutData(gd_selezionaIlGeneratoreStyledText);
		selezionaIlGeneratoreStyledText.setText("Classe di modello: %s");

		checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.MULTI | SWT.BORDER);
		checkboxTableViewer.setSorter(new Sorter());
		checkboxTableViewer.setColumnViewerEditor(null);
		table = checkboxTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(100);
		newColumnTableColumn_1.setText("Nome");

		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(100);
		newColumnTableColumn.setText("Descrizione");
		//
		
		
		
		init();
		return container;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		initDataBindings();
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(306, 324);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Regola-kit Code Generator");
	}
	protected DataBindingContext initDataBindings() {
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		//
		ObservableListContentProvider checkboxTableViewerContentProviderList = new ObservableListContentProvider();
		checkboxTableViewer.setContentProvider(checkboxTableViewerContentProviderList);
		//
		IObservableMap[] checkboxTableViewerLabelProviderMaps = BeansObservables.observeMaps(checkboxTableViewerContentProviderList.getKnownElements(), Generator.class, new String[]{"name", "displayName"});
		checkboxTableViewer.setLabelProvider(new ObservableMapLabelProvider(checkboxTableViewerLabelProviderMaps));
		//
		WritableList generatorsWritableList = new WritableList(generators, Generator.class);
		checkboxTableViewer.setInput(generatorsWritableList);
		//
		return bindingContext;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public CheckboxTableViewer getCheckboxTableViewer() {
		return checkboxTableViewer;
	}

	public void setCheckboxTableViewer(CheckboxTableViewer checkboxTableViewer) {
		this.checkboxTableViewer = checkboxTableViewer;
	}
	
	List<Generator> selected = new ArrayList<Generator>();
	
	protected void okPressed() {
		
		selected.clear();  
		
		for (Object g: getCheckboxTableViewer().getCheckedElements())
		{
			Generator generator= (Generator) g;
			selected.add(generator);
			
			System.out.println(generator.getName());
		}
		
		super.okPressed();
	}

	public List<Generator> getSelected() {
		return selected;
	}

	public void setSelected(List<Generator> selected) {
		this.selected = selected;
	}

}

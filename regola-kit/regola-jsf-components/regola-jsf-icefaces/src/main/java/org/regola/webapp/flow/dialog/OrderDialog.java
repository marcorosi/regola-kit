package org.regola.webapp.flow.dialog;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.regola.model.ModelPattern;
import org.regola.model.ModelProperty;
import org.regola.model.Order;
import org.regola.util.Clonator;

public class OrderDialog extends Dialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ModelPattern pattern;
	private ModelPattern originalPattern;
	private List<ModelProperty> residualColumns = new ArrayList<ModelProperty>();
	
	public void onRemoveColumn(ActionEvent e)
	{
		String name = (String) e.getComponent().getAttributes().get("name");
		int index = pattern.getSortedProperties().indexOf(new ModelProperty(name));

		ModelProperty p = pattern.getSortedProperties().remove(index);
		residualColumns.add(Clonator.clone(p));
	}

	public void onFlipOrderDirection(ActionEvent e)
	{
		String name = (String) e.getComponent().getAttributes().get("name");
		int index = pattern.getSortedProperties().indexOf(new ModelProperty(name));

		pattern.getSortedProperties().get(index).flipOrderDirection();

	}

	public void onAddColumn(ActionEvent e)
	{
		String name = (String) e.getComponent().getAttributes().get("name");
		int index = getResidualColumns().indexOf(new ModelProperty(name));
		ModelProperty property = getResidualColumns().get(index);
		if (!pattern.getSortedProperties().contains(property))
		{
			property.setOrder(Order.asc);
			pattern.getSortedProperties().add(Clonator.clone(property));
			residualColumns.remove(index);
		}
	}
	
	public void setPattern(ModelPattern pattern) {
		this.pattern = pattern;
		setOriginalPattern(Clonator.clone(pattern));
		residualColumns.clear();
		for(ModelProperty p : pattern.getAllProperties())
		{
			if(!pattern.getSortedProperties().contains(p))
				residualColumns.add(Clonator.clone(p));
		}
	}
	
	public ModelPattern getPattern() {
		return pattern;
	}
	public void setOriginalPattern(ModelPattern originalPattern) {
		this.originalPattern = originalPattern;
	}
	public ModelPattern getOriginalPattern() {
		return originalPattern;
	}
	public void setResidualColumns(List<ModelProperty> residualColumns) {
		this.residualColumns = residualColumns;
	}
	public List<ModelProperty> getResidualColumns() {
		return residualColumns;
	}

}

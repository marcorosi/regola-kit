package org.regola.webapp.flow.dialog;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.regola.model.ModelPattern;
import org.regola.model.ModelProperty;
import org.regola.model.Order;
import org.regola.util.Clonator;

public class ColumnsDialog extends Dialog {

	private static final long serialVersionUID = -8042640017947052011L;

	private ModelPattern pattern;
	private ModelPattern originalPattern;
	private List<ModelProperty> residualColumns = new ArrayList<ModelProperty>();

	public void onRemoveColumn(ActionEvent e)
	{
		String name = (String) e.getComponent().getAttributes().get("name");
		int index = pattern.getVisibleProperties().indexOf(new ModelProperty(name));

		ModelProperty p = pattern.getVisibleProperties().remove(index);
		residualColumns.add(Clonator.clone(p));
	}

	public void onAddColumn(ActionEvent e)
	{
		String name = (String) e.getComponent().getAttributes().get("name");
		int index = getResidualColumns().indexOf(new ModelProperty(name));
		ModelProperty property = getResidualColumns().get(index);
		if (!pattern.getVisibleProperties().contains(property))
		{
			property.setOrder(Order.asc);
			pattern.getVisibleProperties().add(Clonator.clone(property));
			residualColumns.remove(index);
		}
	}
	
	
	public void setPattern(ModelPattern pattern) {
		this.pattern = pattern;
		setOriginalPattern(Clonator.clone(pattern));
		residualColumns.clear();
		for(ModelProperty p : pattern.getAllProperties())
		{
			if(!pattern.getVisibleProperties().contains(p))
				residualColumns.add(Clonator.clone(p));
		}
	}
	

	public ModelPattern getPattern() {
		return pattern;
	}

	public void setResidualColumns(List<ModelProperty> residualColumns) {
		this.residualColumns = residualColumns;
	}

	public List<ModelProperty> getResidualColumns() {
		return residualColumns;
	}

	public void setOriginalPattern(ModelPattern originalPattern) {
		this.originalPattern = originalPattern;
	}

	public ModelPattern getOriginalPattern() {
		return originalPattern;
	}
	
}

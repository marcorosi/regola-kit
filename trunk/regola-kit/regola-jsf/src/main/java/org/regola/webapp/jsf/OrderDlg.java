package org.regola.webapp.jsf;

import org.regola.util.Clonator;
import org.regola.model.ModelPattern;
import org.regola.model.ModelProperty;
import org.regola.model.Order;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

public class OrderDlg extends Dialog
{
	ModelPattern filter;
	ModelPattern originalFilter;
	List<ModelProperty> residualColumns = new ArrayList<ModelProperty>();
	
	public ModelPattern getFilter()
	{
		return filter;
	}

	public ModelPattern getOriginalFilter()
	{
		return originalFilter;
	}

	public void setOriginalFilter(ModelPattern originalFilter)
	{
		this.originalFilter = originalFilter;
	}

	public void setFilter(ModelPattern filter)
	{
		this.filter = filter;
		setOriginalFilter(Clonator.clone(filter));
		residualColumns.clear();
		for(ModelProperty p : filter.getAllProperties())
		{
			if(!filter.getSortedProperties().contains(p))
				residualColumns.add(Clonator.clone(p));
		}
	}

	public void onRemoveColumn(ActionEvent e)
	{
		String name = (String) e.getComponent().getAttributes().get("name");
		int index = filter.getSortedProperties().indexOf(new ModelProperty(name));

		ModelProperty p = filter.getSortedProperties().remove(index);
		residualColumns.add(Clonator.clone(p));
	}

	public void onFlipOrderDirection(ActionEvent e)
	{
		String name = (String) e.getComponent().getAttributes().get("name");
		int index = filter.getSortedProperties().indexOf(new ModelProperty(name));

		filter.getSortedProperties().get(index).flipOrderDirection();

	}

	public void onAddColumn(ActionEvent e)
	{
		String name = (String) e.getComponent().getAttributes().get("name");
		int index = getResidualColumns().indexOf(new ModelProperty(name));
		ModelProperty property = getResidualColumns().get(index);
		if (!filter.getSortedProperties().contains(property))
		{
			property.setOrder(Order.asc);
			filter.getSortedProperties().add(Clonator.clone(property));
			residualColumns.remove(index);
		}
	}

	public List<ModelProperty> getResidualColumns()
	{
		return residualColumns;
	}

	public void setResidualColumns(List<ModelProperty> residualColumns)
	{
		this.residualColumns = residualColumns;
	}

}

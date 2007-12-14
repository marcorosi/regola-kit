package org.regola.webapp.action.lookup;

import org.regola.service.GenericManager;
import org.regola.model.ModelPattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class TableLookup<M,ID extends Serializable, F extends ModelPattern> extends BaseLookupStrategy<M, ID, F> {

	List<SelectItem> list = new ArrayList<SelectItem>();
	

	public List<SelectItem> getList() {
		return list;
	}

	public void setList(List<SelectItem> list) {
		this.list = list;
	}
	
	public List<M> getSelection() {
		return new ArrayList<M>();
	}
	
	public void init(M model, F filter, GenericManager<M, ID> manager) {
		super.init(model, filter, manager);
		List<M> allModels = manager.getAll();
		
		for (M m: allModels)
		{
			getFilter().init(m);
			list.add(new SelectItem(m, getSelectLabel(m)));
		}
		
	}

	

}

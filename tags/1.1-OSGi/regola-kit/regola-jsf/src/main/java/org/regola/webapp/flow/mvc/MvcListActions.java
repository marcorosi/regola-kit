package org.regola.webapp.flow.mvc;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.DataModel;

import org.regola.dao.UniversalDao;
import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.webapp.flow.ListActions;

public class MvcListActions implements Serializable{
	
	@SuppressWarnings("unchecked")
	public static List refresh(final GenericManager serviceManager, ModelPattern modelPattern) {
		
		modelPattern.setTotalItems(serviceManager.countFind(modelPattern));
		return ListActions.fetch(serviceManager, modelPattern);

	}
	
	@SuppressWarnings("unchecked")
	public static List refresh(final UniversalDao dao, Serializable model, ModelPattern modelPattern) {
		
		modelPattern.setTotalItems(dao.count(model.getClass(),modelPattern));
		return ListActions.fetch(dao,model, modelPattern);

	}


}

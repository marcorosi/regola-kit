package org.regola.webapp.action.plug;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.regola.util.DynamicProxy;
import org.regola.webapp.action.ListPage;

@SuppressWarnings("unchecked")
public class ListPagePlugAnnotationProxy extends DynamicProxy implements ListPagePlug {

	public ListPagePlugAnnotationProxy(Object target) {
		super(target);
		
	}

	public void setListPage(ListPage listPage) {
		Class[] argsType = { ListPage.class};
		invoke(Resource.class, argsType, listPage );
	}

	public void init() {
		Class[] argsType = { };
		invoke(PostConstruct.class, argsType, (Object[]) null);
	}

}

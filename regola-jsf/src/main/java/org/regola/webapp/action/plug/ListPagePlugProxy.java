package org.regola.webapp.action.plug;

import org.regola.util.DynamicProxy;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.ListPage;

@SuppressWarnings("unchecked")
public class ListPagePlugProxy extends DynamicProxy implements ListPagePlug {

	public ListPagePlugProxy(Object target) {
		super(target);
		
	}

	public void setListPage(ListPage listPage) {
		Class[] argsType = { ListPage.class};
		invoke("setListPage", argsType, listPage );

	}

	public void init() {
		Class[] argsType = { };
		invoke("init", argsType, (Object[]) null);


	}

}

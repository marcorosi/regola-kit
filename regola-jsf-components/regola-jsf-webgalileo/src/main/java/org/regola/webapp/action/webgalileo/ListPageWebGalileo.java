package org.regola.webapp.action.webgalileo;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.model.ModelPattern;
import org.regola.util.ELFunction;
import org.regola.webapp.action.ListPage;
import org.regola.webapp.action.component.ListPageComponent;
import org.regola.webapp.jsf.Dialog.DialogCallback;



public class ListPageWebGalileo<T, ID extends Serializable, F extends ModelPattern>
        extends BasePageWebGalileo implements ListPageComponent<T, ID, F> {

    ListPage<T, ID, F> listPage;

    public ListPage<T, ID, F> getListPage() {
        return listPage;
    }

    public void setListPage(ListPage<T, ID, F> listPage) {
        this.listPage = listPage;
    }

    public void init() {

    }


    public void paginatorListener(ActionEvent event) {
    }

    public void setPage(ListPage<T, ID, F> page) {
        listPage = page;

    }

 
}

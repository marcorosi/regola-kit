package org.regola.webapp.action.icefaces;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.model.ModelPattern;
import org.regola.util.ELFunction;
import org.regola.webapp.action.ListPage;
import org.regola.webapp.action.component.ListPageComponent;
import org.regola.webapp.jsf.Dialog.DialogCallback;

import com.icesoft.faces.component.datapaginator.PaginatorActionEvent;
import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;

public class ListPageIceFaces<T, ID extends Serializable, F extends ModelPattern>
        extends BasePageIceFaces implements ListPageComponent<T, ID, F> {

    ListPage<T, ID, F> listPage;

    public ListPage<T, ID, F> getListPage() {
        return listPage;
    }

    public void setListPage(ListPage<T, ID, F> listPage) {
        this.listPage = listPage;
    }

    public void init() {
        state = PersistentFacesState.getInstance();

    }

    /**
     * Chiamato a seguito della selezione di una riga
     */
    public void onRowSelection(RowSelectorEvent event) {
        log.info("call back for " + event.getRow() + " " + event.isSelected());
        log.info("elementi selezionati " + listPage.getSelectedId().size());

        listPage.onRowSelection();

    }

    public void paginatorListener(ActionEvent event) {
        PaginatorActionEvent e = (PaginatorActionEvent) event;

        if ("next".equals(e.getScrollerfacet())) {
            listPage.getFilter().nextPage();
        }

        if ("previous".equals(e.getScrollerfacet())) {
            listPage.getFilter().previousPage();
        }

        if ("last".equals(e.getScrollerfacet())) {
            listPage.getFilter().gotoLastPage();
        }

        if ("first".equals(e.getScrollerfacet())) {
            listPage.getFilter().setCurrentPage(0);
        }

        if (e.getPageIndex() > 0) {
            listPage.getFilter().gotoPage(e.getPageIndex() - 1);
        }

        //filter.setCurrentPage(e.getPageIndex());

        event.toString();
    }

    public void setPage(ListPage<T, ID, F> page) {
        listPage = page;

    }
}

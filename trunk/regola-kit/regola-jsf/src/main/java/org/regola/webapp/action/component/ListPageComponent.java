/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.regola.webapp.action.component;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.regola.model.ModelPattern;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.ListPage;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 *
 * @author nicola
 */
public interface ListPageComponent<T, ID extends Serializable, F extends ModelPattern> extends BasePageComponent {

	public void onRowSelection(RowSelectorEvent event);
	public void paginatorListener(ActionEvent event);
	public void setPage(ListPage<T,ID,F> page);
	
}

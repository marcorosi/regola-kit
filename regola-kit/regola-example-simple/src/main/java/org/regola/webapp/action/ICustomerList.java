/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.regola.webapp.action;

import org.regola.model.Customer;
import org.regola.model.pattern.CustomerPattern;
import org.regola.webapp.action.plug.ListPagePlug;

/**
 *
 * @author nicola
 */
public interface ICustomerList extends ListPagePlug<Customer, Integer, CustomerPattern>
{
    ListPage<Customer, Integer, CustomerPattern> getListPage();
}

/*
 * Invoice.java
 *
 * Created on 13-ott-2007, 12.23.32
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.regola.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.regola.model.Invoice.InvoiceId;

/**
 *
 * @author romaz
 */
public class Invoice implements Serializable {

    public static class InvoiceId implements Serializable {
        
        public Integer id;
        
        public InvoiceId() {
        }
        
        public InvoiceId(String id) {
            this.id = Integer.valueOf(id);
        }

        public InvoiceId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InvoiceId other = (InvoiceId) obj;
            if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
            return hash;
        }
        
        public String toString() {
            return "" + this.id;
        }
    }

    private Integer id;
    private Customer customer;
    private BigDecimal total;
    private List<Item> items = new ArrayList<Item>();

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    protected void setItems(List<Item> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    protected void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * This method calculates the invoice total and updates it.
     * It should be used after any update to the items collection.
     */
    public void updateTotal() {
        BigDecimal sum = new BigDecimal(0);
        for (Item item : getItems()) {
            sum =   sum.add(item.getCost().
                    multiply(new BigDecimal(item.getQuantity())));
        }
        setTotal(sum);
    }
}
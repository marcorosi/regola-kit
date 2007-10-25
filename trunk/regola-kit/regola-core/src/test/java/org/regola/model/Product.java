/*
 * Product.java
 * 
 * Created on 13-ott-2007, 12.19.48
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.regola.model;

import java.math.BigDecimal;

/**
 *
 * @author romaz
 */
public class Product {

    private Integer id;
    private String name;
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    
}

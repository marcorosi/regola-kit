/*
 * Order.java
 * 
 * Created on 2-ott-2007, 11.25.46
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.regola.model;

/**
 * 
 * @author nicola
 */
public enum Order {
	asc, desc, none;

	public Order[] getLabels() {
		return Order.values();
	}

	public String getLabel() {
		return this.toString();
	}
}

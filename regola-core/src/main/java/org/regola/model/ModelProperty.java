package org.regola.model;

import java.io.Serializable;

/**
 * 
 * @author nicola
 */
public class ModelProperty implements Cloneable, Serializable {

	private static final long serialVersionUID = 9202027753507813003L;

	String name = "";
	Order order = Order.none;
	boolean hidden = false;
	String label = "";

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ModelProperty() {

	}

	public ModelProperty(String name) {
		if (name == null)
			throw new RuntimeException(
					"PropertyFilter name non puo essere nullo");
		this.name = name;
	}

	public ModelProperty(String name, String prefix) {
		this(name);
		this.label = prefix + name;
	}

	public ModelProperty(String name, String prefix, Order order) {
		this(name, prefix);
		this.order = order;
	}

	public ModelProperty(String name, String prefix, Order order, boolean hidden) {
		this(name, prefix, order);
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Order getOrder() {
		return order;
	}

	public String getOrderStr() {
		return order.toString();
	}

	public void setOrderStr(String order) {
		this.order = Order.valueOf(order);
	}

	public boolean isOrderAscending() {
		return Order.asc.ordinal() == getOrder().ordinal();
		// return Order.asc.equals(getOrder());
	}

	public boolean isOrderDescending() {
		return Order.desc.ordinal() == getOrder().ordinal();
		// return Order.desc.equals(getOrder());
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;

		if (!(object instanceof ModelProperty)) {
			return false;
		}

		ModelProperty altro = (ModelProperty) object;

		return getName().equals(altro.getName());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getName() != null ? getName().hashCode() : 0;
	}

	@Override
	public ModelProperty clone() {
		try {
			return (ModelProperty) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void flipOrderDirection() {
		if (Order.asc.equals(getOrder()))
			setOrder(Order.desc);
		else
			setOrder(Order.asc);
	}

}

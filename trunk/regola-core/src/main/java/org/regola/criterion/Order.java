package org.regola.criterion;

public class Order {
	
	/**
	 * Nome della propriet&agrave; usata per l'ordinamento
	 */
	private final String propertyName;

	/**
	 * Indica ordine ascendente o discendente
	 */
	private final boolean ascending;

	protected Order(String propertyName) {
		this(propertyName, true);
	}

	protected Order(String propertyName, boolean ascending) {
		this.propertyName = propertyName;
		this.ascending = ascending;
	}

	public static Order asc(String propertyName) {
		return new Order(propertyName, true);
	}

	public static Order desc(String propertyName) {
		return new Order(propertyName, false);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isAscending() {
		return ascending;
	}
}

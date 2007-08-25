package org.regola;

/**
 * Criterio di filtro
 * <p>
 * Rappresenta il tipo di criterio (operatore), la propriet&agrave; interessata
 * e il valore di filtraggio.
 * 
 */
public class Criterion {

	private Operator operator;
	private String property;
	private Object value;

	public Criterion(Operator operator, String property, Object value) {
		if (operator == null) {
			throw new RuntimeException(
					"Indicare l'operatore nella creazione di un nuovo criterio");
		}
		this.operator = operator;
		if (property == null) {
			throw new RuntimeException(
					"Indicare la proprietà nella creazione di un nuovo criterio");
		}
		this.property = property;
		this.value = value;
	}

	/**
	 * Tipo di criterio
	 */
	public static enum Operator {
		EQ("="), NE("<>"), GT(">"), LT("<"), LE("<="), GE(">="), LIKE("LIKE"), ILIKE(
				"ILIKE"), IN("IN");

		private String token;

		Operator(String token) {
			this.token = token;
		}

		public String toString() {
			return token;
		}
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValues(Object value) {
		this.value = value;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getProperty())
			.append(" ").append(getOperator()).append(" ")
			.append("'").append(getValue()).append("'");
		return sb.toString();
	}	
}

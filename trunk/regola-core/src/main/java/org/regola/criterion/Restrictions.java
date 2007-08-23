package org.regola.criterion;

import org.regola.Criterion;
import org.regola.Criterion.Operator;

public class Restrictions {

	public static Criterion eq(String property, Object value) {
		return new Criterion(Operator.EQ, property, value);
	}

	public static Criterion ne(String property, Object value) {
		return new Criterion(Operator.NE, property, value);
	}

	public static Criterion gt(String property, Object value) {
		return new Criterion(Operator.GT, property, value);
	}

	public static Criterion lt(String property, Object value) {
		return new Criterion(Operator.LT, property, value);
	}

	public static Criterion like(String property, Object value) {
		return new Criterion(Operator.LIKE, property, value);
	}

	public static Criterion ilike(String property, Object value) {
		return new Criterion(Operator.ILIKE, property, value);
	}

	public static Criterion in(String property, Object value) {
		return new Criterion(Operator.IN, property, value);
	}

}

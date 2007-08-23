package org.regola.criterion;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.regola.Criterion;
import org.regola.Criterion.Operator;

public class RestrictionsTest {

	@Test
	public void eq() {
		Criterion criterion = Restrictions.eq("property", null);
		assertSame(Operator.EQ, criterion.getOperator());
	}

	@Test
	public void ne() {
		Criterion criterion = Restrictions.ne("property", null);
		assertSame(Operator.NE, criterion.getOperator());
	}

	@Test
	public void gt() {
		Criterion criterion = Restrictions.gt("property", null);
		assertSame(Operator.GT, criterion.getOperator());
	}

	@Test
	public void lt() {
		Criterion criterion = Restrictions.lt("property", null);
		assertSame(Operator.LT, criterion.getOperator());
	}

	@Test
	public void like() {
		Criterion criterion = Restrictions.like("property", null);
		assertSame(Operator.LIKE, criterion.getOperator());
	}

	@Test
	public void ilike() {
		Criterion criterion = Restrictions.ilike("property", null);
		assertSame(Operator.ILIKE, criterion.getOperator());
	}

	@Test
	public void in() {
		Criterion criterion = Restrictions.in("property", null);
		assertSame(Operator.IN, criterion.getOperator());
	}
}

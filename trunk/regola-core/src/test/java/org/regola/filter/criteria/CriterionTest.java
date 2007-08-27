package org.regola.filter.criteria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.regola.filter.criteria.Criterion.Operator;

public class CriterionTest {

	@Test(expected = RuntimeException.class)
	public void constructorWithNullOperator() {
		new Criterion(null, "property", (Object) null);
	}

	@Test(expected = RuntimeException.class)
	public void constructorWithNullProperty() {
		new Criterion(Operator.EQ, null, (Object) null);
	}

	@Test(expected = RuntimeException.class)
	public void constructorWithEmptyProperty() {
		new Criterion(Operator.EQ, "", (Object) null);
	}

	@Test
	public void constructorWithNullValue() {
		String propertyPath = "property";
		Criterion criterion = new Criterion(Operator.EQ, propertyPath,
				(Object) null);

		assertSame(Operator.EQ, criterion.getOperator());
		assertEquals(propertyPath, criterion.getProperty());
		assertNull(criterion.getValue());
	}

	@Test(expected = RuntimeException.class)
	public void constructorWithNulls() {
		new Criterion(null, null, (Object) null);
	}

}

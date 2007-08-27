package org.regola.filter.criteria;

import org.junit.Test;
import org.regola.filter.criteria.Criterion;
import org.regola.filter.criteria.Criterion.Operator;

public class CriterionTest {

	@Test(expected = RuntimeException.class)
	public void constructorWithNulls() {
		new Criterion(Operator.EQ, null, (Object) null);
		new Criterion(null, "", (Object) null);
		new Criterion(null, null, (Object) null);
	}

}

package org.regola;

import org.junit.Test;
import org.regola.Criterion.Operator;

public class CriterionTest {

	@Test(expected = RuntimeException.class)
	public void constructorWithNulls() {
		new Criterion(Operator.EQ, null, (Object) null);
		new Criterion(null, "", (Object) null);
		new Criterion(null, null, (Object) null);
	}

}

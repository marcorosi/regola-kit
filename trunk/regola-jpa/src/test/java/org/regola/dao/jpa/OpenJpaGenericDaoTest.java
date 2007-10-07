package org.regola.dao.jpa;

public class OpenJpaGenericDaoTest extends AbstractJpaGenericDaoTest {

	@Override
	public String getConfigPath() {
		return "applicationContext-openjpa.xml";
	}

}

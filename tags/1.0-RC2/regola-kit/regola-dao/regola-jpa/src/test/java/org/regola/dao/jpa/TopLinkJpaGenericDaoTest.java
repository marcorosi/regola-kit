package org.regola.dao.jpa;

public class TopLinkJpaGenericDaoTest extends AbstractJpaGenericDaoTest {

	@Override
	public String getConfigPath() {
		return "applicationContext-toplink.xml";
	}

}

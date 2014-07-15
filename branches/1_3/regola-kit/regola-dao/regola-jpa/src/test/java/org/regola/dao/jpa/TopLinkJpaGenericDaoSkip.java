package org.regola.dao.jpa;

public class TopLinkJpaGenericDaoSkip extends AbstractJpaGenericDaoTest {

	@Override
	public String getConfigPath() {
		return "applicationContext-toplink.xml";
	}

}

package org.regola.dao.jpa;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;

@Ignore
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext("classpath:**/applicationContext-toplink.xml")
public class TopLinkJpaGenericDaoTest extends AbstractJpaGenericDaoTest {

}

package org.regola.dao.jpa;

import org.junit.runner.RunWith;
import org.regola.dao.BaseGenericDaoTest;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;

@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext("classpath:**/applicationContext-hibernate.xml")
public class HibernateJpaGenericDaoTest extends BaseGenericDaoTest {

}

package ${package}.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ${package}.dao.LanguageDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=
{"file:META-INF/spring/module-context.xml",
"classpath:module-test-context.xml"})
@Transactional
public class LanguageDaoHibernateTest {
	
	@Autowired
	private LanguageDao languageDao;
	
	@Test
	public void testCounterDefined() throws Exception {
		assertNotNull(this.languageDao);
		assertEquals(15, languageDao.readAll().size());
	}
	
}


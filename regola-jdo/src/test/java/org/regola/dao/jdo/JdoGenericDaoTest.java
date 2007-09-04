package org.regola.dao.jdo;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.regola.dao.BaseGenericDaoTest;
import org.unitils.UnitilsJUnit4TestClassRunner;
import javax.sql.DataSource;
import javax.sql.*;
import java.sql.*;
import org.unitils.database.annotations.*;

import static org.junit.Assert.*;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class JdoGenericDaoTest extends BaseGenericDaoTest {

	@TestDataSource
	private DataSource theDataSource;

	@Test
	public void testDataSourceNotJDOImpl() throws SQLException {
		try {
			theDataSource.getConnection().getMetaData();
			assertTrue(true);
		} catch (javax.jdo.JDOUserException e) {
			fail("Shouldn't throw JDOUserException");
		}
	}

}

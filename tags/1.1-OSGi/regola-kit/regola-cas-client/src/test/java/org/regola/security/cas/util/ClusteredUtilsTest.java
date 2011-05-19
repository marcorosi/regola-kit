package org.regola.security.cas.util;

import junit.framework.TestCase;

public class ClusteredUtilsTest extends TestCase {

	public void testEncodeDecode()
	{
		String encpass = ClusteredUtils.encodePassword("pass", "id");
		assertEquals("pass",ClusteredUtils.decodePassword(encpass));
		assertEquals("id",ClusteredUtils.decodeSessionId(encpass));
	}

	public void testInvalidString()
	{
		try {
			ClusteredUtils.decodePassword("??");
			fail();
		} catch (Exception e) {
		}

		try {
			ClusteredUtils.decodeSessionId("??");
			fail();
		} catch (Exception e) {
		}
	}
		
}

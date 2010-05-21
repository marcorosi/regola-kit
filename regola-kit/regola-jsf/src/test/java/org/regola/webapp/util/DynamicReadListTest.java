package org.regola.webapp.util;

import java.util.Date;

import org.regola.model.ModelProperty;

import junit.framework.TestCase;

public class DynamicReadListTest extends TestCase {

	public void testData()
	{
		Dto dto = new Dto();
		dto.setData(new Date(1274452028620L));
		
		ModelProperty property = new ModelProperty("data");
		assertEquals("21/05/2010", DynamicReadList.getCellValue(property, dto));
	}
	
	class Dto
	{
		private Date data;

		public void setData(Date data) {
			this.data = data;
		}

		public Date getData() {
			return data;
		}
	}
}

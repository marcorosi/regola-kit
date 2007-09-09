package org.regola.filter.builder;


import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.regola.filter.FilterBuilder;
import org.regola.filter.builder.DefaultFilterBuilder;

/**
 * @author  nicola
 */
public class DefaultFilterBuilderTest {

	FilterBuilder fb = new DefaultFilterBuilder();
	MockCriteria criteria;
	MockModelFilter modelFilter;
	
	@Before
	public void init()
	{
		criteria = new MockCriteria();
		modelFilter = new MockModelFilter();		
	}
	
	@Test
	public void testEqualsFilter()
	{
		modelFilter.setName("clint");
		modelFilter.setSurname("eastwood");
		fb.createFilter(criteria, modelFilter);
		
		//System.out.println(criteria.printCriterion());
		//TODO usare assertRefEquals di Unitils?
		assertTrue(criteria.containsCriterion("modelName = 'clint'"));
		assertTrue(criteria.containsCriterion("surname = 'eastwood'"));
	}

	@Test
	public void testLikeFilter()
	{
		modelFilter.setNickname("the good");
		fb.createFilter(criteria, modelFilter);
		
		assertEquals("nickname ILIKE 'the good'",criteria.printCriterion());
	}

	@Test
	public void testGreaterThanFilter()
	{
		modelFilter.setAge(30);
		fb.createFilter(criteria, modelFilter);
		
		assertEquals("age > '30'",criteria.printCriterion());
	}

	@Test
	public void testLessThanFilter()
	{
		modelFilter.setWeight(70);
		fb.createFilter(criteria, modelFilter);
		
		assertEquals("weight < '70'",criteria.printCriterion());
	}

	@Test
	public void testNotEqualsFilter()
	{
		modelFilter.setHairColor("brown");
		fb.createFilter(criteria, modelFilter);
		
		assertEquals("hairColor <> 'brown'",criteria.printCriterion());
	}

	@Test
	public void testInFilter()
	{
		modelFilter.setHobbies(Arrays.asList(new String[] {"cooking","jogging"}));
		fb.createFilter(criteria, modelFilter);
		
		assertEquals("hobbies IN '[cooking, jogging]'",criteria.printCriterion());
	}
}

package org.regola.filter.impl;


import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.impl.DefaultModelPatternParser;

/**
 * @author  nicola
 */
public class DefaultFilterBuilderTest {

	ModelPatternParser fb = new DefaultModelPatternParser();
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
		fb.createQuery(criteria, modelFilter);
		
		//System.out.println(criteria.printCriterion());
		//TODO usare assertRefEquals di Unitils?
		assertTrue(criteria.containsCriterion("modelName = 'clint'"));
		assertTrue(criteria.containsCriterion("surname = 'eastwood'"));
	}

	@Test
	public void testLikeFilter()
	{
		modelFilter.setNickname("the good");
		fb.createQuery(criteria, modelFilter);
		
		assertEquals("nickname ILIKE 'the good'",criteria.printCriterion());
	}

	@Test
	public void testGreaterThanFilter()
	{
		modelFilter.setAge(30);
		fb.createQuery(criteria, modelFilter);
		
		assertEquals("age > '30'",criteria.printCriterion());
	}

	@Test
	public void testLessThanFilter()
	{
		modelFilter.setWeight(70);
		fb.createQuery(criteria, modelFilter);
		
		assertEquals("weight < '70'",criteria.printCriterion());
	}

	@Test
	public void testNotEqualsFilter()
	{
		modelFilter.setHairColor("brown");
		fb.createQuery(criteria, modelFilter);
		
		assertEquals("hairColor <> 'brown'",criteria.printCriterion());
	}

	@Test
	public void testInFilter()
	{
		modelFilter.setHobbies(Arrays.asList(new String[] {"cooking","jogging"}));
		fb.createQuery(criteria, modelFilter);
		
		assertEquals("hobbies IN '[cooking, jogging]'",criteria.printCriterion());
	}
}

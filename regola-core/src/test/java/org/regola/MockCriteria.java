package org.regola;

import java.util.ArrayList;
import java.util.List;

import org.regola.criterion.Order;

class MockCriteria implements Criteria
{
	List<Criterion> criterias = new ArrayList<Criterion>();
	
	public Criteria add(Criterion criterion) {
		criterias.add(criterion);
		return this;
	}

	public boolean contains(Criterion criterion) {
		for(Criterion c : criterias)
		{
			if(c.equals(criterion))
				return true;
		}
		return false;
	}

	public String printCriterion()
	{
		StringBuilder sb = new StringBuilder();
		for(Criterion c : criterias)
		{
			if(sb.length() > 0)
				sb.append(",");
			
			sb.append(c.toString());
		}
		return sb.toString();
	}

	public boolean containsCriterion(String criterion) {
		for(Criterion c : criterias)
		{
			if(c.toString().equals(criterion))
				return true;
		}
		return false;
	}

	public Criteria addOrder(Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	public Criteria setFirstResult(int firstResult) {
		// TODO Auto-generated method stub
		return null;
	}

	public Criteria setMaxResults(int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	public Criteria setProjection(Projection projection) {
		// TODO Auto-generated method stub
		return null;
	}
}

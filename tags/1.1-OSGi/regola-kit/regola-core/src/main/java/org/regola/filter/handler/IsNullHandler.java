package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.IsNull;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;

/*
 * L'annotazione IsNull si può apporre solo a proprietà di tipo Boolean.
 * Se il valore è null non viene aggiunto il filtraggio.
 * Se il valore è true si applica un filtro "is null".
 * Se il valore è false si applica un filtro "is not null".
 */
public class IsNullHandler extends AbstractAnnotationHandler {
	
	public IsNullHandler() {
		super(IsNull.class);
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((IsNull) annotation).value();
	}

	@Override
	protected void handleFilter(Annotation annotation, String propertyPath,
			Object filterValue, Criteria criteria) {
		Boolean b = (Boolean)filterValue;
		if ( b )
			criteria.add(Restrictions.isNull(propertyPath));
		else
			criteria.add(Restrictions.isNotNull(propertyPath));
	}
	
	/*
	 * Ridefinito per controllare che l'annotazione sia stata effettivamente
	 * apposta ad una proprietà Boolean.
	 */
	@Override
	protected void checkValue(Object filterValue) 
	{
		if(! (filterValue instanceof Boolean) )
			throw new RuntimeException ("L'annotazione @IsNull è applicabile soltanto a proprietà di tipo Boolean.");
	}
	
}

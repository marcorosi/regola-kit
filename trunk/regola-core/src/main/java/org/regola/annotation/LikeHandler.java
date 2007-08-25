package org.regola.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.regola.Criteria;
import org.regola.ModelFilter;
import org.regola.criterion.Restrictions;
import org.springframework.util.StringUtils;

public class LikeHandler extends FilterAnnotationHandler {

	@Override
	public void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter) {
		Like like = (Like) annotation;
		Object value = getFilterValue(property, filter);

		if(!isSet(value))
			return;
		
		if (!(value instanceof String)) {
			throw new IllegalArgumentException(
					"L'operatore like è applicabile soltanto a valori di tipo String");
		}
		
		String propertyPath = StringUtils.hasLength(like.value()) ? like.value() : property.getName();  
		
		if (StringUtils.hasLength((String) value)) {
			if (like.caseSensitive()) {
				criteria.add(Restrictions.like(propertyPath, value));
			} else {
				criteria.add(Restrictions.ilike(propertyPath, value));
			}
		}
	}
}

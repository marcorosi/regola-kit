package org.regola.filter.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.regola.filter.ModelFilter;
import org.regola.filter.builder.FilterAnnotationHandler;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.springframework.util.StringUtils;

public class LikeHandler extends FilterAnnotationHandler {

	@Override
	public void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter) {
		Like like = (Like) annotation;
		Object value = getFilterValue(property, filter);

		if (!isSet(value))
			return;

		if (!(value instanceof String)) {
			throw new IllegalArgumentException(
					"L'annotazione @Like è applicabile soltanto a valori di tipo String");
		}

		String propertyPath = StringUtils.hasLength(like.value()) ? like
				.value() : property.getName();

		if (StringUtils.hasLength((String) value)) {
			if (like.caseSensitive()) {
				criteria.add(Restrictions.like(propertyPath, (String) value));
			} else {
				criteria.add(Restrictions.ilike(propertyPath, (String) value));
			}
		}
	}
}
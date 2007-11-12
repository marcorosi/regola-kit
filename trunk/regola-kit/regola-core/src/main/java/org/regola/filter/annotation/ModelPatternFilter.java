package org.regola.filter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tutte le annotazioni che rappresentano criteri utilizzabili in
 * {@link org.regola.model.ModelPattern} devono essere marcate con questa
 * annotazione per poter essere elaborate. <br>
 * Ad esempio per essere tradotte in un oggetto query da un
 * {@link org.regola.filter.ModelPatternParser} attraverso un'implementazione
 * concreta di {@link org.regola.filter.criteria.impl.AbstractQueryBuilder}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ModelPatternFilter {

}

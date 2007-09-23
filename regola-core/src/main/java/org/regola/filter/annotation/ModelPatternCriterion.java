package org.regola.filter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.impl.AbstractQueryBuilder;
import org.regola.model.ModelPattern;

/**
 * Tutte le annotazioni che rappresentano criteri utilizzabili in
 * {@link ModelPattern} devono essere marcate con questa annotazione per poter
 * essere elaborate. <br>
 * Ad esempio per essere tradotte in un oggetto query da un
 * {@link ModelPatternParser} attraverso un'implementazione concreta di
 * {@link AbstractQueryBuilder}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ModelPatternCriterion {

}

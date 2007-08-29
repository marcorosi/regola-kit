package org.regola.filter.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tutte le annotazioni utilizzabili nelle classi che
 * estendono ModelFilter devono essere a loro volta
 * annotate con FilterHandler in modo da specificare
 * l'handler da utilizzare per la loro gestione.
 * 
 * Deprecata: sostituita da...
 * 
 * @author nicola
 *
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface FilterHandler {

	Class<? extends FilterAnnotationHandler> value();

}

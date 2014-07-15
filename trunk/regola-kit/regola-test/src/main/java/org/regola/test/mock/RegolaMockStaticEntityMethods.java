package org.regola.test.mock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * Annotation to indicate a test class for whose @Test methods
 * static methods on Entity classes should be mocked.
 *
 * @author Rod Johnson
 * @see AbstractMethodMockingControl
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegolaMockStaticEntityMethods {

}

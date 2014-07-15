package org.regola.test.mock;


import org.junit.Test;

/**
 * Annotation-based aspect to use in test build to enable mocking static methods
 * on JPA-annotated <code>@Entity</code> classes, as used by Roo for finders.
 *
 * <p>Mocking will occur in the call stack of any method in a class (typically a test class) 
 * that is annotated with the @MockStaticEntityMethods annotation. 
 *
 * <p>Also provides static methods to simplify the programming model for
 * entering playback mode and setting expected return values.
 *
 * <p>Usage:
 * <ol> 
 * <li>Annotate a test class with @MockStaticEntityMethods.
 * <li>In each test method, AnnotationDrivenStaticEntityMockingControl will begin in recording mode.
 * Invoke static methods on Entity classes, with each recording-mode invocation
 * being followed by an invocation to the static expectReturn() or expectThrow()
 * method on AnnotationDrivenStaticEntityMockingControl.
 * <li>Invoke the static AnnotationDrivenStaticEntityMockingControl() method.
 * <li>Call the code you wish to test that uses the static methods. Verification will
 * occur automatically.
 * </ol>
 * 
 * @author Rod Johnson
 * @author Ramnivas Laddad
 * @see MockStaticEntityMethods
 */
public aspect RegolaStaticMethodsMock extends AbstractMethodMockingControl {

	/**
	 * Stop recording mock calls and enter playback state
	 */
	public static void playback() {
		RegolaStaticMethodsMock.aspectOf().playbackInternal();
	}
	
	public static void expectReturn(Object retVal) {
		RegolaStaticMethodsMock.aspectOf().expectReturnInternal(retVal);
	}

	public static void expectThrow(Throwable throwable) {
		RegolaStaticMethodsMock.aspectOf().expectThrowInternal(throwable);
	}
	
	public static void dontCheckArguments() {
		RegolaStaticMethodsMock.aspectOf().dontCheckArgumentsInternal();
	}

	// Only matches directly annotated @Test methods, to allow methods in
	// @MockStatics classes to invoke each other without resetting the mocking environment
	protected pointcut mockStaticsTestMethod() : execution(@Test * (@RegolaMockStaticEntityMethods *).*(..));

	protected pointcut methodToMock() : execution(public static * (@javax.persistence.Entity *).*(..)) ;

	pointcut never();

}



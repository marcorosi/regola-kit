package org.regola.finder;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

public class FinderIntroductionInterceptor implements IntroductionInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {

		FinderExecutor<?> finder = (FinderExecutor<?>) methodInvocation
				.getThis();

		if (isFinderMethod(methodInvocation.getMethod())) {
			return finder.executeFinder(methodInvocation.getMethod().getName(),
					methodInvocation.getArguments());
		}

		return methodInvocation.proceed();
	}

	@SuppressWarnings("unchecked")
	public boolean implementsInterface(Class clazz) {
		return clazz.isInterface() && hasFinderMethods(clazz);
	}

	protected boolean hasFinderMethods(Class<?> clazz) {
		for (Method method : clazz.getMethods()) {
			if (isFinderMethod(method)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isFinderMethod(Method method) {
		String methodName = method.getName();
		if (!methodName.equals("find") && methodName.startsWith("find")) {
			return true;
		}
		return false;
	}
}

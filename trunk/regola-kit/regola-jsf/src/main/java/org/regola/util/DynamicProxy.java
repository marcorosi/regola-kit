package org.regola.util;

import java.lang.reflect.Method;


public class DynamicProxy {
	
	Object target;
	
	public DynamicProxy(Object target)
	{
		if (target==null) throw new RuntimeException("Passed null target for Dynamic Proxy.");
		
		this.target=target;
	}
	
	@SuppressWarnings("unchecked")
	protected Object invoke(String methodName, Class[] argsType ,Object... args)
	{
		
		try {
			Method method = target.getClass().getDeclaredMethod(methodName, argsType);
			return method.invoke(target, args);
		 
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	protected Object invoke(Class methodAnnotation, Class[] argsType ,Object... args)
	{
		
		try {
			
			for (Method method : target.getClass().getMethods())
			{
				if (null!=method.getAnnotation(methodAnnotation))
				{
					return invoke(method.getName(), method.getParameterTypes(), args);
				}
			}
			
			throw new RuntimeException(
					String.format("Could not find a method with annotation %s in type.",methodAnnotation, target.getClass() ));
		
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		
	}

}

package org.regola.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import static org.regola.util.AnnotationUtils.findMethodsByAnnotation;


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
			return invoke(method, args);
		 
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	protected Object invoke(Method method, Object... args)
	{
		
		try {
			return method.invoke(target, args);
		 
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	protected Object invoke(Class methodAnnotation, Class[] argsType ,Object... args)
	{
		
		try {
			
			Method[] methods  = findMethodsByAnnotation(target.getClass(), methodAnnotation);
			
			if (methods.length==0)
			{
				throw new RuntimeException(
					String.format("Could not find a method with annotation %s in type %s.",methodAnnotation, target.getClass() ));
			}
                        
                        for (Method method: methods)
                        {
                            if ( Arrays.equals(argsType, method.getParameterTypes()) )
                            {
                                return invoke(method,args);
                            }
                        }
			
			throw new RuntimeException(
					String.format("Found %d methods annotatated with %s in type %s but no one with required parameters.",methods.length, methodAnnotation, target.getClass() ));
			
			
		
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		
	}

}

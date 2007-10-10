package org.regola.codeassistence;

public class Utils {

	public static String getPackagePath(String packageName)
	{
		return packageName.replace('.', '/');
	}
	
	public static String lowerFirstLetter(String s)
	{
		if(s != null && s.length() > 0)
			s = s.replace(s.charAt(0), Character.toLowerCase(s.charAt(0)));
		
		return s;		
	}

	public static String upperFirstLetter(String s)
	{
		if(s != null && s.length() > 0)
			s = s.replace(s.charAt(0), Character.toUpperCase(s.charAt(0)));
		
		return s;		
	}
}

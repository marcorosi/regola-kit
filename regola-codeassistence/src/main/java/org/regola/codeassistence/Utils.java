package org.regola.codeassistence;

public class Utils {

	public static String getPackagePath(String packageName)
	{
		return packageName.replace('.', '/');
	}
	
	public static String lowerFirstLetter(String s)
	{
		if(s != null && s.length() > 0)
			s = s.replaceFirst("^[A-Z]", String.valueOf(Character.toLowerCase(s.charAt(0))));
		
		return s;
	}

	public static String upperFirstLetter(String s)
	{
		if(s != null && s.length() > 0)
			s = s.replaceFirst("^[a-z]", String.valueOf(Character.toUpperCase(s.charAt(0))));
		
		return s;		
	}
}

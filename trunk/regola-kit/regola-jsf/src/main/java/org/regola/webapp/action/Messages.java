package org.regola.webapp.action;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public class Messages extends BasePage implements Map<String,String>
{
		public void clear()
		{
			// TODO Auto-generated method stub
			
		}

		public boolean containsKey(Object arg0)
		{
			return true;
		}

		public boolean containsValue(Object arg0)
		{
			return true;
		}

		public Set<java.util.Map.Entry<String, String>> entrySet()
		{
			return null;
		}

		public String get(Object key)
		{
			String message;

      try {
          message = getBundle().getString(key.toString());
          
      } catch (java.util.MissingResourceException mre) {
          log.warn("Missing key for '" + key + "'");
          return "???" + key + "???";
      }

      return message;
			
		}

		public boolean isEmpty()
		{
			return false;
		}

		public Set<String> keySet()
		{
			return null;
		}

		public String put(String arg0, String arg1)
		{
			return null;
		}

		public void putAll(Map<? extends String, ? extends String> arg0)
		{
			
		}

		public String remove(Object arg0)
		{
			return null;
		}

		public int size()
		{
			return 0;
		}

		public Collection<String> values()
		{
			
			return null;
		}

}

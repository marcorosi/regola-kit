package org.regola.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5
{
	public static String calcolaMd5(String messaggio)
	{
		MessageDigest md;
		
		try
		{
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
		
		md.reset();

		md.update(messaggio.getBytes());
		byte[] impronta = md.digest();
		
		return new String(impronta);
	}
}

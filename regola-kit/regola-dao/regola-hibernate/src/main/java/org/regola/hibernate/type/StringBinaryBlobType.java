package org.regola.hibernate.type;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class StringBinaryBlobType implements UserType {

	public int[] sqlTypes() {		
		return new int[] { Types.BLOB }; 
	}
	
	public Class returnedClass() {		
		return String.class;
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		return (x == y) 
	      || (x != null 
	        && y != null 
	        && x.equals(y)); 
	}
	
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException {
			
		Blob blob = rs.getBlob(names[0]);
		//byte[] blob = rs.getBytes(names[0]);
		
		if(blob == null)
			return null;
			
	    try {
			return new String(blob.getBytes(1, (int) blob.length()),"UTF-8");
	    	//return new String(blob,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} 

	}

	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		
		if(value == null){
			st.setNull(index, Types.BLOB);
			return;
		}
		
		if(value instanceof String){
			//Blob blob = Hibernate.createBlob(((String) value).getBytes());
			try {
				st.setBytes(index, ((String) value).getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			return;
		}
		
		
		
		throw new HibernateException("nullSafeSet: wrong type -- "+value.getClass());
		
	}
	
	public Object deepCopy(Object value) throws HibernateException {
		
		 if (value == null) return null; 

		    byte[] bytes;
			try {
				bytes = ((String) value).getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			} 
		    byte[] result = new byte[bytes.length]; 
		    System.arraycopy(bytes, 0, result, 0, bytes.length); 

		    return new String(result); 

		
	}

	public boolean isMutable() {		
		return true;
	}

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
    	if(value == null) return null;
    	
        return (Serializable) value;
    }

	public int hashCode(Object x) throws HibernateException {		
		if(x == null) throw new HibernateException("x parameter is null");
		
		return x.hashCode();
	}

	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		 return original;
	}
	
}

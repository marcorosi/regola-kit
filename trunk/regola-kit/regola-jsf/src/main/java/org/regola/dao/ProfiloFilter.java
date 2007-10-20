package org.regola.dao;

import org.regola.model.ModelPattern;
import org.regola.filter.annotation.Equals;


public class ProfiloFilter extends ModelPattern
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1140187794220970352L;
	public String logonName;
	public String nome;

	@Equals( value="id.logonName")
	public String getLogonName()
	{
		return logonName;
	}

	public void setLogonName(String logonName)
	{
		this.logonName = logonName;
	}

	@Equals(value="id.nome")
	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

}

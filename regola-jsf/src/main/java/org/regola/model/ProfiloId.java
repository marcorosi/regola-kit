package org.regola.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Embeddable
public class ProfiloId implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6851579179388290201L;
	public String logonName;
	public String nome;
	public String etichetta;

	public String getEtichetta()
	{
		return etichetta;
	}

	public void setEtichetta(String etichetta)
	{
		this.etichetta = etichetta;
	}

	@Column(name="LOGONNAME")
	public String getLogonName()
	{
		return logonName;
	}

	public void setLogonName(String logonName)
	{
		this.logonName = logonName;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public ProfiloId(String logonName, String nome, String etichetta)
	{
		super();
		this.logonName = logonName;
		this.nome = nome;
		this.etichetta = etichetta;
	}
	
	public ProfiloId()
	{
		
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("nome", this.nome).append("etichetta", this.etichetta)
				.append("logonName", this.logonName).toString();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof ProfiloId))
		{
			return false;
		}
		ProfiloId rhs = (ProfiloId) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(this.nome, rhs.nome)
				.append(this.etichetta, rhs.etichetta).append(this.logonName, rhs.logonName).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(-1357621297, 1330286217).appendSuper(super.hashCode()).append(
				this.nome).append(this.etichetta).append(this.logonName).toHashCode();
	}

}

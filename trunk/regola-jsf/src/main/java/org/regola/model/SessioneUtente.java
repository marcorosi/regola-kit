package org.regola.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="DI_SESSIONI_ATTIVE"
    , uniqueConstraints = {  }
)
public class SessioneUtente implements Serializable
{
	private static final long serialVersionUID = 424665029406538152L;

	private String id;
	private String logonName;
	private Timestamp data;
	private String xmlContestoPlitvice;

	public SessioneUtente()
	{
	}
	
	public SessioneUtente(String token, String username)
	{
		this.id = token;
		this.logonName = username;
	}

	public SessioneUtente(String token)
	{
		this.id = token;
	}

	@Transient
	public Timestamp getData()
	{
		return data;
	}
	
  @Id
	public String getId()
	{
		return id;
	}
  
	public String getLogonName()
	{
		return logonName;
	}

	@Column(name="CONTESTO")
	public String getXmlContestoPlitvice()
	{
		return xmlContestoPlitvice;
	}

	public void setXmlContestoPlitvice(String xmlContestoPlitvice)
	{
		this.xmlContestoPlitvice = xmlContestoPlitvice;
	}
	
	/**
	 * da usare per confrontare 2 oggetti SessioneUtente relativi alla stessa sessione 
	 */
	public boolean eCambiata(SessioneUtente daConfrontare)
	{
		if(daConfrontare == null)
			return true;
		
		if(!this.getId().equals(daConfrontare.getId()))
			throw new IllegalArgumentException("Impossibile confrontare due sessioni con ID differente");
		
		if(getXmlContestoPlitvice() == null)
			return daConfrontare.getXmlContestoPlitvice() != null;
		else
			return !getXmlContestoPlitvice().equals(daConfrontare.getXmlContestoPlitvice());
	}

	public void setData(Timestamp data)
	{
		this.data = data;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setLogonName(String logonName)
	{
		this.logonName = logonName;
	}
}

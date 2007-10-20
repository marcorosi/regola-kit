package org.regola.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DI_CONTESTO",schema="DIDATTICA")
public class Contesto implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Integer idcontesto;

	private String descrizione;

	private Integer ereditaCicloMaster;

	public Contesto()
	{
		super();
	}
	
	@Column(name="EREDITACICLO")
	public Integer getEreditaCicloMaster() {
		return ereditaCicloMaster;
	}

	public void setEreditaCicloMaster(Integer ereditaCicloMaster) {
		this.ereditaCicloMaster = ereditaCicloMaster;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getIdcontesto() {
		return idcontesto;
	}

	public void setIdcontesto(Integer idcontesto) {
		this.idcontesto = idcontesto;
	}

	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}

	public String getDescrizione()
	{
		return this.descrizione;
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}

		if (!(o instanceof Contesto))
		{
			return false;
		}

		Contesto pk = (Contesto) o;
		return (getIdcontesto().equals(pk.getIdcontesto()));
	}

	public int hashCode()
	{
		int result = 17;
		result = (37 * result) + idcontesto.hashCode();
		return result;
	}
}

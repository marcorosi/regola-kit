package org.regola.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="DI_PROFILI",schema="DIDATTICA")
@org.hibernate.annotations.Entity( dynamicUpdate = true)
        
public class Profilo
{
	public ProfiloId id;
	public String classe;
	public String contenuto;
	public Integer selezionato;
	public ModelPattern filter;
	
	@Transient
	public ModelPattern getFilter()
	{
		return filter;
	}

	public void setFilter(ModelPattern filter)
	{
		this.filter = filter;
	}

	public String getClasse()
	{
		return classe;
	}

	public void setClasse(String classe)
	{
		this.classe = classe;
	}

	@Lob
	public String getContenuto()
	{
		return contenuto;
	}

	public void setContenuto(String contenuto)
	{
		this.contenuto = contenuto;
	}

  @EmbeddedId
	public ProfiloId getId()
	{
		return id;
	}

	public void setId(ProfiloId id)
	{
		this.id = id;
	}

	public Integer getSelezionato()
	{
		return selezionato;
	}

	public void setSelezionato(Integer selezionato)
	{
		this.selezionato = selezionato;
	}

}

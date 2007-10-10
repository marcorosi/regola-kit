package org.regola.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="DI_RELAZIONECONTESTO",schema="DIDATTICA")
public class RelazioneContesto implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Integer idRelazioneContesto;
	
	private Integer idContesto;

	private String CodCorso;

	private Character corsoVerbali;

	public RelazioneContesto()
	{
		super();
	}

	@Id 
	@Column(name="IDRELAZIONECONTESTO") 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "IDRELAZIONECONTESTO_SEQ")
	@SequenceGenerator(name="IDRELAZIONECONTESTO_SEQ", sequenceName = "DI_SEQ_RELAZIONECONTESTO")
	public Integer getIdRelazioneContesto() {
		return idRelazioneContesto;
	}

	public void setIdRelazioneContesto(Integer idRelazioneContesto) {
		this.idRelazioneContesto = idRelazioneContesto;
	}
	
	@Column(name="IDCONTESTO")
	public Integer getIdContesto() {
		return idContesto;
	}

	public void setIdContesto(Integer idContesto) {
		this.idContesto = idContesto;
	}
	
	@Column(name="COD_CORSO")
	public String getCodCorso() {
		return CodCorso;
	}

	public void setCodCorso(String codCorso) {
		CodCorso = codCorso;
	}
	
	@Column(name="CORSO_VERBALI")
	public Character getCorsoVerbali() {
		return corsoVerbali;
	}

	public void setCorsoVerbali(Character corsoVerbali) {
		this.corsoVerbali = corsoVerbali;
	}	

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}

		if (!(o instanceof RelazioneContesto))
		{
			return false;
		}

		RelazioneContesto pk = (RelazioneContesto) o;
		return (getIdRelazioneContesto().equals(pk.getIdRelazioneContesto()));
	}

	public int hashCode()
	{
		int result = 17;
		result = (37 * result) + idRelazioneContesto.hashCode();
		return result;
	}

}

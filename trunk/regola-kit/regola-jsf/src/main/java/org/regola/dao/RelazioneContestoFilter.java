package org.regola.dao;

import org.regola.model.ModelPattern;
import org.regola.filter.annotation.Equals;


public class RelazioneContestoFilter extends ModelPattern
{
	
	private static final long serialVersionUID = 6374165709824974423L;
	
	private Integer idContesto;

	@Equals(value="idContesto")
	public Integer getIdContesto() {
		return idContesto;
	}

	public void setIdContesto(Integer idContesto) {
		this.idContesto = idContesto;
	}
	
}

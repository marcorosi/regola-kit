package org.regola.dao;

import java.math.BigDecimal;

import org.regola.model.ModelPattern;
import org.regola.filter.annotation.GreaterThan;
import org.regola.filter.annotation.LessThan;

public class UtenteContestoProfiloFilter extends ModelPattern
{
	
	private static final long serialVersionUID = 6374165709824974423L;

	public String logonname;
	
	private BigDecimal idContesto;
	
	@GreaterThan( value="id.logonname")
	public String getCodCorso() {
		return logonname;
	}

	public void setLogonname(String logonname) {
		this.logonname = logonname;
	}
	
	@LessThan(value="id.idcontesto")
	public BigDecimal getIdContesto() {
		return idContesto;
	}

	public void setIdContesto(BigDecimal idContesto) {
		this.idContesto = idContesto;
	}

}

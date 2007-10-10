package org.regola.dao;

import org.regola.model.ModelPattern;
import org.regola.filter.annotation.NotEquals;


public class RuoliFilter extends ModelPattern
{

	private static final long serialVersionUID = -2747357319829469634L;
	
	public Integer ruolo;

	@NotEquals(value="ruolo.ruolo")
	public Integer getRuolo()
	{
		return ruolo;
	}

	public void setRuolo(Integer ruolo)
	{
		this.ruolo = ruolo;
	}

}

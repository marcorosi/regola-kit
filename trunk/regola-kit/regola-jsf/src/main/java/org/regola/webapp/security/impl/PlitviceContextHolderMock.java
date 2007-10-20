package org.regola.webapp.security.impl;

import org.regola.model.ContestoPlitvice;
import org.regola.webapp.security.PlitviceContextHolder;

public class PlitviceContextHolderMock implements PlitviceContextHolder
{
	
	ContestoPlitvice contesto;

	public ContestoPlitvice getContesto()
	{
		return contesto;
	}

	public void setContesto(ContestoPlitvice contesto)
	{
		this.contesto = contesto;
	}

}

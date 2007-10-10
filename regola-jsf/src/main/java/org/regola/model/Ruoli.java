package org.regola.model;

public enum Ruoli
{
	TuttiIContesti(10)
	,GestioneAttivita(11)
	,GestionePersonale(12)
	,GestionePiani(15)
	,GestioneRegolamenti(13)
	,GestioneChiusuraRegolamenti(16)
	,GestioneInvalidaChiusuraRegolamenti(14)
	,GestioneDataGiunta(17)
	,SbloccaPD(19)
	,SbloccaCoperture(20)
	,VedeAttivita(21)
	,VedeRegolamenti(22)
	,VedePiani(23)
	,GestioneManifesti(24)
	;
	
	private final int codice;
	
	Ruoli(int c) 
	{
		this.codice = c;
	}
	
	public int getCodice() 
	{
		return codice;
	}
}

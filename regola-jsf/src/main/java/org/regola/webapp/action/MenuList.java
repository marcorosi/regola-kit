package org.regola.webapp.action;

import org.regola.model.Ruoli;
import org.regola.service.AutorizzatoreManager;

import java.util.HashMap;
import java.util.Map;

public class MenuList 
{
	private AutorizzatoreManager autorizzatore;
	
	private Map<String,Boolean> autorizzazioni;
	
	public MenuList()
	{
	}
	
	//TODO: farlo fare al costruttore quando i managed bean saranno definiti in spring 
	private void initAutorizzazioni()
	{
		autorizzazioni = new HashMap<String, Boolean>();
		autorizzazioni.put("regolamenti"
				, autorizzatore.vede(Ruoli.GestioneRegolamenti) || autorizzatore.vede(Ruoli.VedeRegolamenti));
		autorizzazioni.put("formative"
				, autorizzatore.vede(Ruoli.GestioneAttivita) || autorizzatore.vede(Ruoli.VedeAttivita));
		autorizzazioni.put("coperture"
				, autorizzatore.vede(Ruoli.GestionePersonale));
		autorizzazioni.put("verbali"
				, autorizzatore.vede(Ruoli.GestioneAttivita));
		autorizzazioni.put("piani"
				, autorizzatore.vede(Ruoli.GestionePiani) || autorizzatore.vede(Ruoli.VedePiani));
		autorizzazioni.put("manifesti"
				, autorizzatore.vede(Ruoli.GestioneManifesti));
	}
	
	public void setAutorizzatore(AutorizzatoreManager autorizzatore)
	{
		this.autorizzatore = autorizzatore;
	}

	public Map<String, Boolean> getAutorizzazioni()
	{
		if(autorizzazioni == null)
			initAutorizzazioni();
		
		return autorizzazioni;
	}

}

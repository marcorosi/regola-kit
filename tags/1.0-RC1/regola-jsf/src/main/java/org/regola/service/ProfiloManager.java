package org.regola.service;

import org.regola.model.Profilo;
import org.regola.model.ModelPattern;

import java.util.List;



public interface ProfiloManager
{
	
	public List<Profilo> listaProfili(String area);
	public ModelPattern getFilter(String etichetta, String area);
	public Profilo saveFilter(String etichetta, String area, ModelPattern oggetto);
	//public void remove(String etichetta, String area);
	public String remove(String etichetta, String area);
	public void seleziona(String etichetta,String area);
	public String listaProfiliAsString(List<Profilo> /*IN*/ profili, List<String> /*OUT*/ profiliStr);
	
}
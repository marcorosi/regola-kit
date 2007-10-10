package org.regola.dao;

import org.regola.model.Contesto;
import org.regola.model.SessioneUtente;
import org.regola.model.UtenteContestoProfilo;
import org.cesia.pd.model.J_Ruolo;

import java.util.List;

public interface AutenticatoreDao extends PlitviceDao
{
	SessioneUtente getSessioneUtente(String token);
	
	//TODO: spostare in dao generico
	int getAnnoCorrente();

	List<J_Ruolo> findRuoliUtente(String logonName);

	List<UtenteContestoProfilo> findContestiUtente(String logonName);
	
	public List<Contesto> getContestiUtente(String principal);
}

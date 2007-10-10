package org.regola.service;

import org.regola.model.Contesto;
import org.regola.model.ContestoPlitvice;
import org.regola.model.Ruoli;

import java.util.List;

public interface AutorizzatoreManager
{
	/**
	 * popola il contesto passato con le autorizzazioni dell'utente
	 * 
	 * @throws EccezioneDiAutorizzazione se l'utente non ha ruoli
	 */
	void popolaAutorizzazioni(ContestoPlitvice contesto) throws EccezioneDiAutorizzazione;

	/**
	 * @return true se l'utente corrente possiede il ruolo passato
	 */
	boolean vede(Ruoli ruolo);
	
	public List<Contesto> getContestiUtente(String principal);
	
}

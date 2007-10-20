package org.regola.service.impl;

import java.util.List;

import org.cesia.pd.model.J_Ruolo;
import org.regola.dao.AutenticatoreDao;
import org.regola.model.Contesto;
import org.regola.model.ContestoPlitvice;
import org.regola.model.Ruoli;
import org.regola.model.UtenteContestoProfilo;
import org.regola.service.AutorizzatoreManager;
import org.regola.service.EccezioneDiAutorizzazione;
import org.regola.webapp.security.PlitviceContextHolder;

public class AutorizzatoreManagerImpl implements AutorizzatoreManager
{
	private AutenticatoreDao autenticatoreDao;

	public void setAutenticatoreDao(AutenticatoreDao autenticatoreDao)
	{
		this.autenticatoreDao = autenticatoreDao;
	}
	
	private PlitviceContextHolder plitviceContextHolder;
	
	

	public void setPlitviceContextHolder(PlitviceContextHolder plitviceContextHolder)
	{
		this.plitviceContextHolder = plitviceContextHolder;
	}

	public void popolaAutorizzazioni(ContestoPlitvice contestoUtente) throws EccezioneDiAutorizzazione
	{
		popolaRuoli(contestoUtente);
		popolaCorsi(contestoUtente);
	}

	private void popolaCorsi(ContestoPlitvice contestoUtente)
	{
		//TODO: implementare 
		// e scommentare assertTrue(contesto.getCorsiDiStudio().size()>0); 
		// in testAutenticaConPassword()
	}

	private void popolaRuoli(ContestoPlitvice contestoUtente) throws EccezioneDiAutorizzazione
	{
		// Verifico i ruoli utente
		List<J_Ruolo> ruoli = autenticatoreDao.findRuoliUtente(contestoUtente.getPrincipal());

		if (ruoli.size() == 0)
		{
			throw new EccezioneDiAutorizzazione("L'utente non ha ruoli");
		} else
		{
			contestoUtente.setRuoli(ruoli);
			if (vede(contestoUtente,Ruoli.TuttiIContesti))
			{
				contestoUtente.setUtenteGlobale(true);
				contestoUtente.setIdContesto(new Integer(1));
			} else
			{
				//carica contesto
				List<UtenteContestoProfilo> contesti = autenticatoreDao.findContestiUtente(contestoUtente.getPrincipal());
				contestoUtente.setIdContesto(contesti.get(0).getId().getIdcontesto().intValue());
			}			
		}
	}
	
	private boolean vede(ContestoPlitvice autenticazione, Ruoli ruolo)
	{
		return cercaRuolo(autenticazione, ruolo);
	}

	private boolean cercaRuolo(ContestoPlitvice autenticazione, Ruoli r)
	{
		for(J_Ruolo ruolo : autenticazione.getRuoli())
		{
			if (ruolo.getRuolo().intValue() == r.getCodice())
			{
				return true;
			}			
		}

		return false;
	}

	public boolean vede(Ruoli ruolo)
	{
		ContestoPlitvice c = plitviceContextHolder.getContesto();
		return vede(c, ruolo);
	}

	public List<Contesto> getContestiUtente(String principal) {
		return autenticatoreDao.getContestiUtente(principal);
	}
}


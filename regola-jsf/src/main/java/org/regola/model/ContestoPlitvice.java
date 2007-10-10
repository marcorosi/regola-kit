package org.regola.model;

import org.cesia.pd.model.J_Ruolo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contesto utente per Plitvice.
 * Contiene informazioni su chi è l'utente autenticato e sulle sue autorizzazioni
 *
 * NOTA: 
 * Questa classe viene salvata in XML con castor.
 * Tutti i campi che hanno come tipo un'interfaccia (ad es. Collection)
 * devono essere definiti in castor-mapping.xml per garantire il funzionamento
 * dell'unmarshalling
 */
public class ContestoPlitvice implements Serializable
{
	private static final String SEPARATORE = "###";

	private static final long serialVersionUID = 1L;

	private Collection<J_Ruolo> ruoli = new ArrayList<J_Ruolo>();

	private Integer anno;

	private Integer idContesto = new Integer(0);

	private String credenziali = "";

	private String principal = "";

	private List<String> codCorsiDiStudio = new ArrayList<String>();

	private boolean utenteGlobale = false;

	//private Integer annoModificabile;

	private boolean ereditaCicloMaster = false;
	
	private String paginaIniziale = "";
	
	private String cookieSSO = "";
	
	private boolean cambiato = false;

	//private RuoliCache ruoliCache;

	public String getCookieSSO()
	{
		return cookieSSO;
	}

	public void setCookieSSO(String cookieSSO)
	{
		setCambiato();
		this.cookieSSO = cookieSSO;
	}

	public String getPaginaIniziale()
	{
		return paginaIniziale;
	}

	public void setPaginaIniziale(String paginaIniziale)
	{
		setCambiato();
		this.paginaIniziale = paginaIniziale;
	}

	public ContestoPlitvice()
	{
	}

	public void setAnno(Integer anno)
	{
		setCambiato();
		this.anno = anno;
	}

	public Integer getAnno()
	{
		return anno;
	}

	public void setCorsiDiStudio(List<String> corsiDiStudio)
	{
		setCambiato();
		this.codCorsiDiStudio = corsiDiStudio;
	}

	public List<String> getCorsiDiStudio()
	{
		return codCorsiDiStudio;
	}

	public void setCredenziali(String credenziali)
	{
		setCambiato();
		this.credenziali = credenziali;
	}

	public String getCredenziali()
	{
		return credenziali;
	}

	public void setIdContesto(Integer idContesto)
	{
		setCambiato();
		this.idContesto = idContesto;
	}

	public Integer getIdContesto()
	{	
		/*
		 * si dà il menu dei contesti anche all'utente globale
		 * che può quindi cambiare da un contesto all'altro.
		 * 
		if (isUtenteGlobale())
		{
			return new Integer(1);
		} else
		{
		*/
			return idContesto;
		/* } */
	}

	public void setPrincipal(String principal)
	{
		setCambiato();
		this.principal = principal;
	}

	public String getPrincipal()
	{
		return principal;
	}

	@SuppressWarnings("unchecked")
	public void setRuoli(Collection ruoli)
	{
		setCambiato();
		this.ruoli = ruoli;
	}

	public Collection<J_Ruolo> getRuoli()
	{
		return ruoli;
	}

	public void setUtenteGlobale(boolean utenteGlobale)
	{
		setCambiato();
		this.utenteGlobale = utenteGlobale;
		setEreditaCicloMaster(utenteGlobale);
	}

	public boolean isUtenteGlobale()
	{
		return utenteGlobale;
	}

//	public RuoliCache getRuoliCache()
//	{
//		return ruoliCache;
//	}
//
//	public void setRuoliCache(RuoliCache ruoliCache)
//	{
//		setCambiato();
//		this.ruoliCache = ruoliCache;
//	}
//	
//	public Integer getAnnoModificabile()
//	{
//		return annoModificabile;
//	}
//
//	public void setAnnoModificabile(Integer annoModificabile)
//	{
//		this.annoModificabile = annoModificabile;
//	}

	public void setEreditaCicloMaster(boolean b)
	{
		setCambiato();
		this.ereditaCicloMaster = b;
	}

	/**
	 * @return true se il ciclo del master di una ar deve essere ereditato anche
	 *         dagli altri componenti
	 * @author marco
	 */
	public boolean isEreditaCicloMaster()
	{
		return this.ereditaCicloMaster;
	}

	private Integer COD_CONTESTO_SSIS=81;
	
	/**
	 * @return true se l'utente può inserire cfu con decimali
	 *        
	 * @author marco
	 */
	public boolean puoInserireCfuDecimali()
	{
		return COD_CONTESTO_SSIS.equals(getIdContesto()) || isUtenteGlobale();
	}
	
	/**
	 * @return true se l'utente vede il corso passato
	 * @author marco
	 */
	public boolean vedeCorso(Corso corso)
	{
		return this.codCorsiDiStudio.contains(corso.getCodCorso());
	}
	
	public String getPrincipalCodificato()
	{
		return principal+SEPARATORE+cookieSSO;
	}
	
	public static String decodificaCookie(String p)
	{
		if(p == null)
			throw new IllegalArgumentException("impossibile decodificare un principal nullo");
		
		if(p.indexOf(SEPARATORE) < 0)
			throw new IllegalArgumentException("impossibile decodificare il principal:" +p);
		
		return p.substring(p.indexOf(SEPARATORE)+SEPARATORE.length(),p.length());
	}
	
	public static String decodificaUsername(String p)
	{
		if(p == null)
			throw new IllegalArgumentException("impossibile decodificare un principal nullo");
		
		if(p.indexOf(SEPARATORE) < 0)
			throw new IllegalArgumentException("impossibile decodificare il principal:" +p);
		
		return p.substring(0,p.indexOf(SEPARATORE));
	}

	public boolean isCambiato()
	{
		return cambiato;
	}

	public boolean resetCambiato()
	{
		return cambiato = false;
	}

	private void setCambiato()
	{
		cambiato = true;
	}
}

package org.regola.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.regola.dao.GenericDao;
import org.regola.dao.ProfiloFilter;
import org.regola.model.ModelPattern;
import org.regola.model.Profilo;
import org.regola.model.ProfiloId;
import org.regola.service.ProfiloManager;
import org.regola.webapp.security.PlitviceContextHolder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ProfiloManagerImpl implements ProfiloManager
{
	public static final Object semaforo = ProfiloManagerImpl.class;

	PlitviceContextHolder plitviceContextHolder;
	GenericDao<Profilo, ProfiloId> profiloDao;

	public ModelPattern defaultFactory(String area)
	{
		try {
			return (ModelPattern) Class.forName(area + "Filter").newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Could not create default filter for " + area);
		} 
	}

	public String listaProfiliAsString(List<Profilo> /*IN*/ profili, List<String> /*OUT*/ profiliStr)
	{
		profiliStr.clear();
		String current = "";

		for (Profilo profilo : profili)
		{
			profiliStr.add(profilo.getId().getEtichetta());

			if (profilo.getSelezionato() == 1)
				current = profilo.getId().getEtichetta();
		}

		if (current == "")
			throw new RuntimeException("Manca il profilo selezionato");

		Collections.sort(profiliStr);

		return current;
	}
	
	/*
	public void remove(String etichetta, String  nome)
	{
		Profilo profilo = preparaSettaggio(etichetta, nome, null);
		profiloDao.remove(profilo.getId());

		List<Profilo> profili = listaProfiliInternal(nome);

		if (profili.size() == 0)
			return;

		boolean senzaSelezione = (Boolean) Ognl.getValue("#root.{^ selezionato==1}.size==0", profili);

		if (senzaSelezione)
			profili.get(0).setSelezionato(1);

	}
	*/
	
	/*
	 * Ritorna l'etichetta del profilo che diventa selezionato dopo la cancellazione 
	 */
	public String remove(String etichetta, String  nome)
	{
		Profilo profilo = preparaSettaggio(etichetta, nome, null);
		profiloDao.remove(profilo.getId());

		List<Profilo> profili = listaProfiliInternal(nome);

		if (profili.size() == 0)
			return null;
		
		for (Profilo p : profili) {
			if(p.getSelezionato() == 1)
				return p.getId().getEtichetta();
		}
		
		profili.get(0).setSelezionato(1);
		return profili.get(0).getId().getEtichetta();

	}	

	protected List<Profilo> listaProfiliInternal(String nome)
	{
		ProfiloFilter filter = new ProfiloFilter();

		filter.setNome(nome.toString());
		filter.setLogonName(getPlitviceContextHolder().getContesto().getPrincipal());

		return profiloDao.find(filter);
	}

	public void seleziona(String etichetta, String nome)
	{
		ProfiloFilter filter = new ProfiloFilter();

		filter.setNome(nome.toString());
		filter.setLogonName(getPlitviceContextHolder().getContesto().getPrincipal());

		List<Profilo> profili = profiloDao.find(filter);

		boolean trovato = false;
		for (Profilo profilo : profili)
		{
			if (profilo.getId().getEtichetta().equals(etichetta))
			{
				trovato = true;
				profilo.setSelezionato(1);
			} else
			{
				profilo.setSelezionato(0);
			}

		}

		if (!trovato)
			throw new RuntimeException("Etichetta non esistente" + etichetta);

	}

	public List<Profilo> listaProfili(String nome)
	{
		List<Profilo> profili = listaProfiliInternal(nome);

		if (profili.size() > 0)
			return profili;

		ModelPattern ModelPattern = defaultFactory(nome);
		Profilo profilo = saveFilter("Default", nome, ModelPattern);

		return Arrays.asList(profilo);

	}

	public ModelPattern getFilter(Profilo profilo)
	{
		profilo = profiloDao.get(profilo.getId());

		XStream xstream = new XStream(new DomDriver());
		profilo.setFilter((ModelPattern) xstream.fromXML(profilo.contenuto));

		return profilo.getFilter();

	}

	public ModelPattern getFilter(String etichetta, String nome)
	{
		Profilo profilo = preparaSettaggio(etichetta, nome, null);
		return getFilter(profilo);

	}

	protected Profilo preparaSettaggio(String etichetta, String nome, ModelPattern filter)
	{
		/*
		 * controllo che il profilo sia o meno un profilo nuovo
		 */
		Profilo p = profiloDao.get(new ProfiloId(getPlitviceContextHolder().getContesto().getPrincipal(),
				nome, etichetta));
		if(p != null)
		{
			//p.selezionato = 1;
			p.setFilter(filter);
			return p;	
		}
		//****
		
		Profilo settaggio = new Profilo();
		ProfiloId id = new ProfiloId(getPlitviceContextHolder().getContesto().getPrincipal(), nome
				.toString(), etichetta.toString());

		settaggio.setId(id);
		settaggio.selezionato = 1;
		settaggio.setFilter(filter);

		return settaggio;
	}

	public Profilo saveFilter(Profilo profilo)
	{
		List<Profilo> profili = listaProfiliInternal(profilo.getId().getNome());

		for (Profilo profiliEsistenti : profili)
		{
			profiliEsistenti.setSelezionato(0);
		}

		XStream xstream = new XStream(new DomDriver());

		profilo.contenuto = xstream.toXML(profilo.getFilter());
		profilo.classe = profilo.getFilter().getClass().toString();
		profilo.selezionato = 1; //fix all'azzeramento sopra....

		profiloDao.save(profilo);

		return profilo;

	}

	public Profilo saveFilter(String etichetta, String nome, ModelPattern oggetto)
	{	
		Profilo profilo = preparaSettaggio(etichetta, nome, oggetto);
		return saveFilter(profilo);
	}

	public PlitviceContextHolder getPlitviceContextHolder()
	{
		return plitviceContextHolder;
	}

	public void setPlitviceContextHolder(PlitviceContextHolder plitviceContextHolder)
	{
		this.plitviceContextHolder = plitviceContextHolder;
	}

	public GenericDao<Profilo, ProfiloId> getProfiloDao()
	{
		return profiloDao;
	}

	public void setProfiloDao(GenericDao<Profilo, ProfiloId> profiloDao)
	{
		this.profiloDao = profiloDao;
	}
}

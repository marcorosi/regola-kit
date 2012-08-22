package org.regola.codeassistence.generator;

import java.util.Map;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;

/**
 * Un generatore si occupa di produrre uno o più artefatti
 * oppure di modificare il contenuto di uno o più file esistenti
 * (ad esempio file di configurazione come gli applicationContext.xml).
 * 
 * Gli assistenti alla scrittura di codice utilizzano diverse classi
 * che implementano Generator in successione.
 * 
 * @author nicola
 *
 */
public interface Generator
{
	/**
	 * Un'etichetta per distinguere il descrittore
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Un'etichetta per mostrare a video il descrittore
	 * @return
	 */
	public String getDisplayName();
	
	/**
	 * Una breve descrizione dell'intervento realizzato
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Crea o modifica dei file realizzando lo scopo per cui il 
	 * generatore è stato pensato. Ad esempio crea una classe oppure
	 * modifica in file di configurazione.
	 * @param env
	 * @param pb
	 */
	public void generate(Environment env, VariablesBuilder pb);
	
	/**
	 * Restituisce true se i file che questo genratore dovrebbe creare
	 * esistono già oppure se le modifiche che dovrebbe realizzare su un file
	 * esistente sono già presenti.
	 * 
	 * Dopo che il metodo generate è stato invocato questo metodo deve restituire
	 * true.
	 * 
	 * @param env
	 * @param pb
	 * @return
	 */
	public boolean existsArtifact(Environment env, VariablesBuilder pb);
	
	/**
	 * Effettua una simulazione restituendo una mappa con in chiave i 
	 * file che il generatore produrrebbe o modificherebbe e come valore
	 * gli interventi che realizzerebbe.
	 * 
	 * @param env
	 * @param pb
	 * @return
	 */
	public Map<String,String> simulate(Environment env, VariablesBuilder pb);
	
	

}
package org.regola.webapp.action;

import org.regola.model.Profilo;
import org.regola.service.ProfiloManager;
import org.regola.model.ModelPattern;
import org.regola.webapp.jsf.ConfirmDlg;
import org.regola.webapp.jsf.InputTextDlg;
import org.regola.webapp.jsf.Dialog.DialogCallback;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class ProfiloForm extends BasePage
{
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected ProfiloManager profiloManager;
	
	protected String nomeCompleto;
	
	//protected String areaListBean;
	
	protected String selezionato;
	
	protected InputTextDlg inputTextDlg; // per inserimento etichetta (salva con nome)
	
	
	
	//riferimento al bean che gestisce la lista della pagina
	protected ListPage listPageBean;
	
	
	
	public ProfiloForm()
	{
		
	}
	
	public ProfiloForm(String nomeCompleto)
	{
		this();
		this.nomeCompleto = nomeCompleto;
	}	
	
	
	
	public ProfiloManager getProfiloManager() 
	{
		return profiloManager;
	}

	public void setProfiloManager(ProfiloManager profiloManager) 
	{
		this.profiloManager = profiloManager;
	}

	public String getSelezionato() 
	{
		return selezionato;
	}

	public void setSelezionato(String selezionato) 
	{
		this.selezionato = selezionato;
		profiloManager.seleziona(selezionato, nomeCompleto);
	}
	
	public List<SelectItem> getListaProfili()
	{

		List<Profilo> profili = profiloManager.listaProfili(nomeCompleto);
		
		List<String> profiliStr = new ArrayList<String>();
		if(selezionato == null)
		{
			selezionato = profiloManager.listaProfiliAsString(profili, profiliStr);
		
			setCurrentFilter(profiloManager.getFilter(selezionato, nomeCompleto));
		}else{
			selezionato = profiloManager.listaProfiliAsString(profili, profiliStr);
		}
		
		//costruzione array di SelectItem
		ArrayList<SelectItem> profiliSelIt = new ArrayList<SelectItem>();		
		for (String profiloStr : profiliStr) {
			profiliSelIt.add(new SelectItem( profiloStr , profiloStr ));
		}
		
		return profiliSelIt; 
	}
	
	public String salva()
	{
		profiloManager.saveFilter(selezionato, nomeCompleto, getCurrentFilter());
		return "save";
	}
	
	public String salvaConNome(String etichetta)
	{	
		profiloManager.saveFilter(etichetta, nomeCompleto, getCurrentFilter());
		setSelezionato(etichetta);
		return "save";
	}
	
	public String cancella()
	{
		selezionato = profiloManager.remove(selezionato, nomeCompleto);
		
		//la remove riseleziona uno degli altri profili
		/*
		List<Profilo> profili = profiloManager.listaProfili(nomeCompleto);		
		List<String> profiliStr = new ArrayList<String>();
		selezionato = profiloManager.listaProfiliAsString(profili, profiliStr);
		*/
		
		if( selezionato != null )
			setCurrentFilter(profiloManager.getFilter(selezionato, nomeCompleto));
		
		return "remove";
	}
	
	protected ModelPattern getCurrentFilter()
	{
		/*
		 * versione senza riferimento esplicito al bean
		 * 
		ListPage listPage = (ListPage)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(areaListBean);
		return listPage.getFilter();
		*/
		return listPageBean.getFilter();
	}
	
	protected void setCurrentFilter(ModelPattern filter)
	{
		/*
		 * versione senza riferimento esplicito al bean
		 * 		
		ListPage listPage = (ListPage)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(areaListBean);
		listPage.setFilter(filter);
		listPage.refresh();
		*/
		listPageBean.setFilter(filter);
		listPageBean.refresh();
	}
	
	public boolean isSelezionatoDefault()
	{
		return "Default".equals(selezionato);
	}
	
	public InputTextDlg getInputTextDlg() {
		return inputTextDlg;
	}

	public void setInputTextDlg(InputTextDlg inputTextDlg) {
		this.inputTextDlg = inputTextDlg;
	}
	
	public ConfirmDlg getConfirmDlg() {
		return confirmDlg;
	}

	public void setConfirmDlg(ConfirmDlg confirmDlg) {
		this.confirmDlg = confirmDlg;
	}
	
	
	public ListPage getListPageBean() {
		return listPageBean;
	}

	public void setListPageBean(ListPage listPageBean) {
		this.listPageBean = listPageBean;
	}
	
	
	/* 
	 * =======================================================================================================
	 * Metodi di ascolto degli eventi 
	 * =======================================================================================================
	 */
	public void onSavingEvent()
	{
		salva();
	}
	
	public void onSavingWithNameEvent()
	{

		getInputTextDlg().setLabel("Nome profilo:");
		getInputTextDlg().show("Nuovo Profilo - salva con nome", "Inserire il nome del profilo:", new DialogCallback()
		{

			public void onConfirm()
			{
				log.info("Confermato salvataggio con nome:" + getInputTextDlg().getValue());
				
				salvaConNome(getInputTextDlg().getValue());
			}

			public void onCancel()
			{
			}
		});		
		
	}
	
	public void onRemovingEvent()
	{

		getConfirmDlg().show("Attenzione", "Sei sicuro di voler cancellare il profilo?", new DialogCallback()
		{

			public void onConfirm()
			{
				log.info("Confermata cancellazione profilo:" + selezionato);
				
				cancella();
			}

			public void onCancel()
			{
			}
		});				
		
	}
	
	public void onCambioProfilo(ValueChangeEvent event)
	{
		setSelezionato((String)event.getNewValue());
		
		setCurrentFilter(profiloManager.getFilter(selezionato, nomeCompleto));
		
		
	}

}

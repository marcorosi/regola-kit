package org.regola.webapp.action.icefaces;

import javax.faces.event.ValueChangeEvent;

import org.regola.webapp.action.ProfiloForm;

import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;


public class ProfiloFormIceFaces extends ProfiloForm {
	
	protected PersistentFacesState state;
	
	public ProfiloFormIceFaces()
	{
		state = PersistentFacesState.getInstance();
	}
	
	public ProfiloFormIceFaces(String nomeCompleto)
	{
		this();
		
	}	
	
	public PersistentFacesState getState() {
		return state;
	}

	public void setState(PersistentFacesState state) {
		this.state = state;
	}
	
	@Override
	public void onCambioProfilo(ValueChangeEvent event)
	{
		super.onCambioProfilo(event);
		
		try {
			getState().render();
		} catch (RenderingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


}

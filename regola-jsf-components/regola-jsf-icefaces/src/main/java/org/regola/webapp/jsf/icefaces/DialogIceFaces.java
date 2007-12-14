package org.regola.webapp.jsf.icefaces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.component.panelpositioned.PanelPositionedEvent;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Fade;
import com.icesoft.faces.context.effects.Highlight;


public class DialogIceFaces {
	private Effect statusFadeEffect;
	private Effect statusEffect;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public String updateStatus()
	{
		if (statusEffect == null)
		{
			statusEffect = new Highlight("#AADDFF");
		}
		if (statusFadeEffect == null)
		{
			statusFadeEffect = new Fade(1.0f, 0.1f);
		}
		statusEffect.setFired(false);
		statusFadeEffect.setFired(false);
		return null;
	}

	public Effect getStatusFadeEffect()
	{
		return statusFadeEffect;
	}

	public void setStatusFadeEffect(Effect statusFadeEffect)
	{
		this.statusFadeEffect = statusFadeEffect;
	}

	public Effect getStatusEffect()
	{
		return statusEffect;
	}

	public void setStatusEffect(Effect statusEffect)
	{
		this.statusEffect = statusEffect;
	}
	
	/**
	 * default listener for ice:panelPositioned, icefaces 1.5.3 needs this to work 
	 *  
	 * @param evt
	 */
  public void onOrderChange(PanelPositionedEvent evt) 
  {
    if (evt.getOldIndex() >= 0 && log.isDebugEnabled())
    {
    	log.debug(String.format("spostato da %d a %d",evt.getOldIndex(),evt.getIndex()));
    }
  }



}

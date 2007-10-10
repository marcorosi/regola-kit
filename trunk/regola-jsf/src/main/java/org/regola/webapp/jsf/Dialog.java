package org.regola.webapp.jsf;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.component.panelpositioned.PanelPositionedEvent;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Fade;
import com.icesoft.faces.context.effects.Highlight;

public abstract class Dialog
{
	private enum Mode 
	{
		draggable, modal;
	}
	
	// icons used for draggable panel
	private String closePopupImage = "./images/popupPanel/popupclose.gif";
	protected final Log log = LogFactory.getLog(getClass());
	// show or hide each popup panel
	//private boolean showDraggablePanel = false;
	//private boolean showModalPanel = false;
	private Mode mode;
	private Effect statusFadeEffect;
	private Effect statusEffect;
	private String message = "...";
	private String title = "Attenzione";
	private boolean visible = false;

	public interface DialogCallback
	{
		void onConfirm();
		void onCancel();
	}
	
	private void init(String title, String message, DialogCallback callback)
	{
		setTitle(title);
		setMessage(message);
		setCallback(callback);
		setVisible(true);		
	}
	
	/**
	 * opens a modal dialog
	 *  
	 * @param title
	 * @param message
	 * @param callback
	 */
	public void showModal(String title, String message, DialogCallback callback)
	{
		mode = Mode.modal;
		init(title, message, callback);
	}

	/**
	 * opens a draggable dialog
	 *  
	 * @param title
	 * @param message
	 * @param callback
	 */
	public void show(String title, String message, DialogCallback callback)
	{
		mode = Mode.draggable;
		init(title, message, callback);
	}

	private DialogCallback callback;

	public DialogCallback getCallback()
	{
		return callback;
	}

	public void setCallback(DialogCallback callback)
	{
		this.callback = callback;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public boolean isDraggable()
	{
		return Mode.draggable.equals(mode);
		//return showDraggablePanel;
	}

	/*
	public void setShowDraggablePanel(boolean showDraggablePanel)
	{ 
		this.showDraggablePanel = showDraggablePanel;
	}
	*/

	public boolean isModal()
	{
		return Mode.modal.equals(mode);
		//return showModalPanel;
	}

	/*
	public void setShowModalPanel(boolean showModalPanel)
	{
		this.showModalPanel = showModalPanel;
	}
	*/

	/*
	public void closeDraggablePopup(ActionEvent e)
	{
		showDraggablePanel = false;

		if (e.getComponent().getId().matches(".*OK.*") && callback != null)
			callback.onConfirm();
	}

	public void closeModalPopup(ActionEvent e)
	{
		showModalPanel = false;

		if (e.getComponent().getId().matches(".*OK.*") && callback != null)
			callback.onConfirm();
		else if (e.getComponent().getId().matches(".*Cancel.*") && callback != null)
			callback.onCancel();


	}
	*/

	public void closePopup(ActionEvent e)
	{
		setVisible(false);
		
		if (e.getComponent().getId().matches(".*OK.*") && callback != null)
			callback.onConfirm();
		else if (e.getComponent().getId().matches(".*Cancel.*") && callback != null)
			callback.onCancel();
	}
	
	public void setClosePopupImage(String closePopupImage)
	{
		this.closePopupImage = closePopupImage;
	}

	public String getClosePopupImage()
	{
		return this.closePopupImage;
	}

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

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public boolean isVisible()
	{
		return this.visible;
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

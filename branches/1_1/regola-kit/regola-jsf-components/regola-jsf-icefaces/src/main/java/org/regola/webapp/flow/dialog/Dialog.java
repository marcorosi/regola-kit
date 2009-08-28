package org.regola.webapp.flow.dialog;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Dialog implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	private enum Mode 
	{
		draggable, modal;
	}
	
	
	// icons used for draggable panel
	private String closePopupImage = "./images/popupPanel/popupclose.gif";
	protected final transient Log log = LogFactory.getLog(getClass());
	// show or hide each popup panel
	//private boolean showDraggablePanel = false;
	//private boolean showModalPanel = false;
	private Mode mode;
	private String message = "...";
	private String title = "Attenzione";
	private boolean visible = false;

	
	private void init(String title, String message)
	{
		setTitle(title);
		setMessage(message);
		
		setVisible(true);		
	}
	
	/**
	 * opens a modal dialog
	 *  
	 * @param title
	 * @param message
	 * @param callback
	 */
	public void showModal(String title, String message)
	{
		mode = Mode.modal;
		init(title, message);
	}

	/**
	 * opens a draggable dialog
	 *  
	 * @param title
	 * @param message
	 * @param callback
	 */
	public void show(String title, String message)
	{
		mode = Mode.draggable;
		init(title, message);
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



	public boolean isModal()
	{
		return Mode.modal.equals(mode);
		//return showModalPanel;
	}

	
	public void closePopup(ActionEvent e)
	{
		setVisible(false);
		
//		if (e.getComponent().getId().matches(".*OK.*") && callback != null)
//			callback.onConfirm();
//		else if (e.getComponent().getId().matches(".*Cancel.*") && callback != null)
//			callback.onCancel();
	}
	
	public void close()
	{
		setVisible(false);
	}
	
	public void setClosePopupImage(String closePopupImage)
	{
		this.closePopupImage = closePopupImage;
	}

	public String getClosePopupImage()
	{
		return this.closePopupImage;
	}


	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public boolean isVisible()
	{
		return this.visible;
	}

}

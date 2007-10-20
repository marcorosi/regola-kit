package org.regola.webapp.jsf;


/**
 * Backing bean for an input text popup
 * 
 */
public class InputTextDlg extends Dialog
{

	private String label = "";
	
	private String value = "";
	
	
	public InputTextDlg()
	{
		log.info("Costruttore InputTextDlg....");
	}
	
	public String getLabel() 
	{
		return label;
	}

	public void setLabel(String label) 
	{
		this.label = label;
	}

	public String getValue() 
	{
		return value;
	}

	public void setValue(String value) 
	{
		this.value = value;
	}
	
}

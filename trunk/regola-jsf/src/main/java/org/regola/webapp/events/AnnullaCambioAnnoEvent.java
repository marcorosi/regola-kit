package org.regola.webapp.events;

import org.regola.events.Event;
import org.regola.events.UserEvent;

public class AnnullaCambioAnnoEvent extends UserEvent implements Event {

	private Integer annoDaRipristinare;
	
	public Integer getAnnoDaRipristinare() {
		return annoDaRipristinare;
	}

	public AnnullaCambioAnnoEvent(String userId, Integer annoDaRipristinare) 
	{
		super(userId);
		this.annoDaRipristinare=annoDaRipristinare;
	}
}

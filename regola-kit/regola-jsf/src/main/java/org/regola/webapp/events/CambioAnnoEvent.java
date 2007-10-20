package org.regola.webapp.events;

import org.regola.events.Event;
import org.regola.events.UserEvent;

public class CambioAnnoEvent extends UserEvent implements Event {

	private Integer nuovoAnno;
	private Integer vecchioAnno;
	
	public Integer getNuovoAnno() {
		return nuovoAnno;
	}

	public Integer getVecchioAnno() {
		return vecchioAnno;
	}

	public CambioAnnoEvent(String userId, Integer nuovoAnno, Integer vecchioAnno) 
	{
		super(userId);
		this.nuovoAnno=nuovoAnno;
		this.vecchioAnno=vecchioAnno;
	}
}

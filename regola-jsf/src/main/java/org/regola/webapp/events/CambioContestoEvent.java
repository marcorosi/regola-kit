package org.regola.webapp.events;

import org.regola.events.Event;
import org.regola.events.UserEvent;

public class CambioContestoEvent extends UserEvent implements Event {

	private Integer nuovoContesto;
	private Integer vecchioContesto;
	
	public Integer getNuovoContesto() {
		return nuovoContesto;
	}

	public Integer getVecchioContesto() {
		return vecchioContesto;
	}

	public CambioContestoEvent(String userId, Integer nuovoContesto, Integer vecchioContesto) {
		super(userId);
		this.nuovoContesto=nuovoContesto;
		this.vecchioContesto=vecchioContesto;
	}

}

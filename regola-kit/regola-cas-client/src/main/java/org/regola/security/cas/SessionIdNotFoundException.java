package org.regola.security.cas;

public class SessionIdNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6680117363882997640L;

	public SessionIdNotFoundException() {
		super("Impossibile ricavare il session id dalla richiesta. Il funzionamento in cluster non è possibile.");
	}

}

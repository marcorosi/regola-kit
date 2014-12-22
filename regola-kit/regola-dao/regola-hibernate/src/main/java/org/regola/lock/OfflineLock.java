package org.regola.lock;

import java.io.Serializable;
import java.util.Date;

public class OfflineLock implements Serializable {
	private static final long serialVersionUID = 1L;

	private String target;
	private String owner;
	private Date since;

	public OfflineLock() {
	}

	public OfflineLock(String target, String owner) {
		setTarget(target);
		setOwner(owner);
		setSince(new Date());
	}

	public Date getSince() {
		return since == null ? null : new Date(since.getTime());
	}

	public void setSince(Date since) {
		this.since = since == null ? null : new Date(since.getTime());
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}

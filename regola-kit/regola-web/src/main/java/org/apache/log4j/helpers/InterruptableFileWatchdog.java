package org.apache.log4j.helpers;


import java.io.Closeable;
import java.io.IOException;
import java.util.Date;

public abstract class InterruptableFileWatchdog extends FileWatchdog implements
		Closeable {

	private String contextName;

	public InterruptableFileWatchdog(String filename, String contextName) {
		super(filename);
		this.contextName = contextName;
	}

	@Override
	protected void checkAndConfigure() {
		super.checkAndConfigure();
		setName();
	}
	
	//@Override
	public void close() throws IOException {
		this.interrupted = true;
		setName();
	}

	private void setName() {
		setName(String.format("Log4jFileWatchdog[%s]-%s-%s[%4$tF_%4$tT]",
				contextName, getId(), interrupted ? "interrupted" : "running",
				new Date(lastModif)));
	}
}

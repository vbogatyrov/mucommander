package com.mucommander.ui.main.tabs;

import com.mucommander.commons.file.AbstractFile;
import com.mucommander.core.LocalLocationHistory;

public class ConfFileTableTab implements FileTableTab {

	private boolean lock;
	private AbstractFile location;
	
	public ConfFileTableTab(AbstractFile location) {
		this(false, location);
	}
	
	public ConfFileTableTab(boolean lock, AbstractFile location) {
		this.lock = lock;
		this.location = location;
	}

	public boolean isLocked() {
		return lock;
	}

	public AbstractFile getLocation() {
		return location;
	}
	
	public void setLocation(AbstractFile location) {
		throw new UnsupportedOperationException("cannot change location of configuration tab");
	}

	public void setLocked(boolean locked) {
		throw new UnsupportedOperationException("cannot lock configuration tab");
	}

	public LocalLocationHistory getLocationHistory() {
		return null;
	}
}

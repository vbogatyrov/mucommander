/*
 * This file is part of muCommander, http://www.mucommander.com
 * Copyright (C) 2002-2012 Maxence Bernard
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mucommander.ui.main.tabs;

import com.mucommander.commons.file.AbstractFile;
import com.mucommander.ui.tabs.TabFactory;

/**
 * Factory for creating {@link com.mucommander.ui.main.tabs.FileTableTab} presenting the given location
 * 
 * @author Arik Hadas
 */
public class FileTableTabFactory implements TabFactory<FileTableTab, AbstractFile> {

	@Override
	public FileTableTab createTab(AbstractFile location) {
		if (location == null)
			throw new RuntimeException("Invalid location");

		return new DefaultFileTableTab(location);
	}

	class DefaultFileTableTab implements FileTableTab {
		
		/** The location presented in this tab */
		private AbstractFile location;

		// 	 private boolean isLocked;

		/**
		 * Private constructor
		 * 
		 * @param location - the location that would be presented in the tab
		 */
		private DefaultFileTableTab(AbstractFile location) {
			setLocation(location);
		}
		
		@Override
		public void setLocation(AbstractFile location) {
			this.location = location;
		}

		@Override
		public AbstractFile getLocation() {
			return location;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FileTableTab)
				return location.getAbsolutePath().equals(((FileTableTab) obj).getLocation().getAbsolutePath());
			return false;
		}

		@Override
		public int hashCode() {
			return location.hashCode();
		}
	}
}
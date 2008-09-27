/*
 * This file is part of muCommander, http://www.mucommander.com
 * Copyright (C) 2002-2008 Maxence Bernard
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

package com.mucommander.ui.quicklist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.mucommander.file.AbstractFile;
import com.mucommander.file.FileFactory;
import com.mucommander.ui.icon.CustomFileIconProvider;
import com.mucommander.ui.icon.FileIcons;
import com.mucommander.ui.icon.IconManager;
import com.mucommander.ui.icon.SpinningDial;
import com.mucommander.ui.quicklist.item.DataList;

/**
 * FileTablePopupWithIcons is a FileTablePopupWithDataList in which the data list 
 * 	contains icons.
 * 
 * @author Arik Hadas
 */

public abstract class QuickListWithIcons extends QuickListWithDataList {
	// This HashMap's keys are items and its objects are the corresponding icon.
	private HashMap itemToIconCacheMap = new HashMap();
	// This SpinningDial will appear until the icon fetching of an item is over.
	private static final SpinningDial waitingIcon = new SpinningDial();
	// If the icon fetching fails for some item, the following icon will appear for it. 
	private static final Icon notAvailableIcon = IconManager.getIcon(IconManager.FILE_ICON_SET, CustomFileIconProvider.NOT_ACCESSIBLE_FILE);
	// Saves the number of waiting-icons (SpinningDials) appearing in the list.
	private int numOfWaitingIconInList;
	
	public QuickListWithIcons(String header, String emptyPopupHeader) {
		super(header, emptyPopupHeader);
		numOfWaitingIconInList = 0;
		addPopupMenuListener(new PopupMenuListener() {

			public void popupMenuCanceled(PopupMenuEvent e) {}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// Clear icon-caching before opening popup-list in order to let the icons be fetched again.
				itemToIconCacheMap.clear();
			}			
		});
	}
	
	/**
	 * Called when waitingIcon is added to the list.
	 */
	private synchronized void waitingIconAddedToList() {
		// If there was no other waitingIcon in the list before current addition - start the spinning dial.
		if (numOfWaitingIconInList++ == 0)
			waitingIcon.setAnimated(true);
	}
	
	/**
	 * Called when waitingIcon is removed from the list.
	 */
	private synchronized void waitingIconRemovedFromList() {
		// If after current remove operation, there will be no waitingIcon in the list - stop the spinning dial.
		if (--numOfWaitingIconInList == 0)
			waitingIcon.setAnimated(false);
	}
	
	protected DataList getList() { return new GenericPopupDataListWithIcons(); }
	
	/**
	 * This function gets an item from the data list and return its icon.
	 *  
	 * @param value - an item from the data list.
	 * @return icon.
	 */
	protected abstract Icon itemToIcon(String value);
	
	/**
	 * This function gets a path, resolves the file it points to, and return the file's icon.
	 * 
	 * @param filepath - path.
	 * @return icon of the file in the given path. null is returned if a file in the given path
	 *  does not exist or is not accessible. 
	 */
	protected Icon getIconOfFile(String filepath) {
		AbstractFile file = FileFactory.getFile(filepath);
		return (file != null && file.exists()) ?
			IconManager.getImageIcon(FileIcons.getFileIcon(file)) : null; 
	}
	
	private Icon getImageIconOfItem(final String item) {
		boolean found;
		synchronized(itemToIconCacheMap) {
			if (!(found = itemToIconCacheMap.containsKey(item))) {
				itemToIconCacheMap.put(item, waitingIcon);
				waitingIconAddedToList();
			}
		}
		
		if (!found)
			new Thread() {
				public void run() {
					Icon icon = itemToIcon(item);
					synchronized(itemToIconCacheMap) {
						// If the item does not exist or is not accessible, show notAvailableIcon for it.
						itemToIconCacheMap.put(item, icon != null ? icon : notAvailableIcon);
					}
					waitingIconRemovedFromList();
					repaint();
				}
			}.start();
		
		Icon result;
		synchronized(itemToIconCacheMap) {
			result = (Icon) itemToIconCacheMap.get(item);
		}
		return result;
	}
	
	private class GenericPopupDataListWithIcons extends DataList {		
		public GenericPopupDataListWithIcons() {
			super();
			setCellRenderer(new CellWithIconRenderer());
		}

		private class CellWithIconRenderer extends DefaultListCellRenderer {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				// Let superclass deal with most of it...
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				// Add its icon
				String item = (String) (getModel().getElementAt(index));				
				Icon icon = getImageIconOfItem(item);
				setIcon(resizeIcon(icon));

				return this;
			}
			
			private Icon resizeIcon(Icon icon) {
				if (icon instanceof ImageIcon) {
					Image image = ((ImageIcon) icon).getImage();
					final Dimension dimension = this.getPreferredSize();
					final double height = dimension.getHeight();
					final double width = (height / icon.getIconHeight()) * icon.getIconWidth();
					image = image.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
					return new ImageIcon(image);
				}

				return icon;
			}
		}
	}
}
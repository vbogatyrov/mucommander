/**
 * This file is part of muCommander, http://www.mucommander.com
 * Copyright (C) 2002-2016 Maxence Bernard
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mucommander.commons.file.protocol.hadoop;

import javax.swing.JFrame;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.mucommander.commons.file.AuthenticationType;
import com.mucommander.commons.file.DefaultSchemeHandler;
import com.mucommander.commons.file.DefaultSchemeParser;
import com.mucommander.commons.file.SchemeHandler;
import com.mucommander.commons.file.osgi.FileProtocolService;
import com.mucommander.commons.file.protocol.ProtocolProvider;
import com.mucommander.protocol.ui.ProtocolPanelProvider;
import com.mucommander.protocol.ui.ServerPanel;
import com.mucommander.protocol.ui.ServerPanelListener;

/**
 * @author Arik Hadas
 */
public class Activator implements BundleActivator {

	private ServiceRegistration<FileProtocolService> serviceRegistration;
	private ServiceRegistration<ProtocolPanelProvider> uiServiceRegistration;

	@Override
	public void start(BundleContext context) throws Exception {
		FileProtocolService service = new FileProtocolService() {
			@Override
			public String getSchema() {
				return "hdfs";
			}

			@Override
			public ProtocolProvider getProtocolProvider() {
				return new HDFSProtocolProvider();
			}

			@Override
			public SchemeHandler getSchemeHandler() {
				return new DefaultSchemeHandler(new DefaultSchemeParser(true), 8020, "/", AuthenticationType.AUTHENTICATION_OPTIONAL, null);
			}
		};
		ProtocolPanelProvider panelProvider = new ProtocolPanelProvider() {
			@Override
			public String getSchema() {
				return "hdfs";
			}

			@Override
			public ServerPanel get(ServerPanelListener listener, JFrame mainFrame) {
				return new HDFSPanel(listener, mainFrame);
			}
		};
		serviceRegistration = context.registerService(FileProtocolService.class, service, null);
		uiServiceRegistration = context.registerService(ProtocolPanelProvider.class, panelProvider, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		serviceRegistration.unregister();
		uiServiceRegistration.unregister();
	}

}

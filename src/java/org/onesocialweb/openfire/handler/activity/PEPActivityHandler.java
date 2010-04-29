/*
 *  Copyright 2010 Vodafone Group Services Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *    
 */

package org.onesocialweb.openfire.handler.activity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Element;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.onesocialweb.openfire.handler.pep.PEPCommandHandler;
import org.onesocialweb.openfire.handler.pep.PEPNodeHandler;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

public class PEPActivityHandler extends PEPNodeHandler {

	public static String NODE = "urn:xmpp:microblog:0";
	
	private XMPPServer server;
	
	private Map<String, PEPCommandHandler> handlers = new ConcurrentHashMap<String, PEPCommandHandler>();

	public PEPActivityHandler() {
		super("Handler for PEP microbloging PEP node");
	}
		
	@Override
	public String getNode() {
		return NODE;
	}

	@Override
	public void initialize(XMPPServer server) {
		super.initialize(server);
		this.server = server;
		addHandler(new ActivityPublishHandler());
		addHandler(new ActivityQueryHandler());
		addHandler(new ActivityDeleteHandler());
		addHandler(new ActivitySubscribeHandler());
		addHandler(new ActivityUnsubscribeHandler());
		addHandler(new ActivitySubscribersHandler());
		addHandler(new ActivitySubscriptionsHandler());
	}

	@SuppressWarnings("unchecked")
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		// We search for a handler based on the element name
		// and process the packet with the handler if found.
        final Element childElement = packet.getChildElement();
        final List<Element> pubsubElements = childElement.elements();	

        if (pubsubElements != null && pubsubElements.size() > 0) {
        	Element actionElement = pubsubElements.get(0);
        	PEPCommandHandler handler = getHandler(actionElement.getName());
			if (handler != null) {
				return handler.handleIQ(packet);
			}
		}

		// No valid hanlder found. Return a feature not implemented
		// error.
		IQ result = IQ.createResultIQ(packet);
		result.setChildElement(packet.getChildElement().createCopy());
		result.setError(PacketError.Condition.feature_not_implemented);
		return result;
	}

	private void addHandler(PEPCommandHandler handler) {
		handler.initialize(server);
		handlers.put(handler.getCommand(), handler);
	}

	private PEPCommandHandler getHandler(String name) {
		return handlers.get(name);
	}

}

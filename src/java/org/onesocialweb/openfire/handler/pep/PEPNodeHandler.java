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
package org.onesocialweb.openfire.handler.pep;

import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.container.BasicModule;
import org.xmpp.packet.IQ;

public abstract class PEPNodeHandler extends BasicModule {
	
    /**
     * Create a basic module with the given name.
     *
     * @param moduleName The name for the module or null to use the default
     */
    public PEPNodeHandler(String moduleName) {
        super(moduleName);
    }
	
    public abstract IQ handleIQ(IQ packet) throws UnauthorizedException;
    
	public abstract String getNode();
	
}

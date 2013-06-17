//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2003 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 2003 Jun 11: Added a "catch" for RRD update errors. Bug #748.
// 2003 Jan 31: Added the ability to imbed RRA information in poller packages.
// 2003 Jan 31: Cleaned up some unused imports.
// 2003 Jan 29: Added response times to certain monitors.
// 2002 Nov 13: Added ICMP response time measurements.
// 2002 Nov 12: Added response time graphs to webUI.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
// Tab Size = 8
//

package org.infosec.ismp.poller.monitor;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import org.infosec.ismp.model.poller.MonitoredService;
import org.infosec.ismp.model.poller.NetworkInterface;
import org.infosec.ismp.model.poller.NetworkInterfaceNotSupportedException;
import org.infosec.ismp.model.poller.PollStatus;
import org.infosec.ismp.model.poller.monitors.IPv4Monitor;
import org.infosec.ismp.ping.PingConstants;
import org.infosec.ismp.ping.Pinger;
import org.infosec.ismp.util.ParameterMap;
import org.infosec.ismp.util.ThreadCategory;

/**
 * <P>
 * This class is designed to be used by the service poller framework to test the
 * availability of the ICMP service on remote interfaces. The class implements
 * the ServiceMonitor interface that allows it to be used along with other
 * plug-ins by the service poller framework.
 * </P>
 * 
 * @author <A HREF="mailto:tarus@opennms.org">Tarus Balog</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS</A>
 * 
 */

// this is marked not distributable because it relies on a shared library

final public class IcmpMonitor extends IPv4Monitor {
    /**
     * Constructs a new monitor.
     */
    public IcmpMonitor() throws IOException {
    }

    /**
     * <P>
     * Poll the specified address for ICMP service availability.
     * </P>
     * 
     * <P>
     * The ICMP service monitor relies on Discovery for the actual generation of
     * IMCP 'ping' requests. A JSDT session with two channels (send/recv) is
     * utilized for passing poll requests and receiving poll replies from
     * discovery. All exchanges are SOAP/XML compliant.
     * </P>
     * @param parameters
     *            The package parameters (timeout, retry, etc...) to be used for
     *            this poll.
     * @param iface
     *            The network interface to test the service on.
     * @return The availability of the interface and if a transition event
     *         should be suppressed.
     * 
     */
    public PollStatus poll(MonitoredService svc, Map<String, Object> parameters) {
        NetworkInterface iface = svc.getNetInterface();

        // Get interface address from NetworkInterface
        //
        if (iface.getType() != NetworkInterface.TYPE_IPV4)
            throw new NetworkInterfaceNotSupportedException("Unsupported interface type, only TYPE_IPV4 currently supported");

        ThreadCategory log = ThreadCategory.getInstance(this.getClass());
        Long rtt = null;
        InetAddress host = (InetAddress) iface.getAddress();

        try {
            
            // get parameters
            //
            int retries = ParameterMap.getKeyedInteger(parameters, "retry", PingConstants.DEFAULT_RETRIES);
            long timeout = ParameterMap.getKeyedLong(parameters, "timeout", PingConstants.DEFAULT_TIMEOUT);
            
            rtt = Pinger.ping(host, timeout, retries);
        } catch (Exception e) {
            log.debug("failed to ping " + host, e);
        }
        
        if (rtt != null) {
            return PollStatus.available(rtt.doubleValue());
        } else {
            // TODO add a reason code for unavailability
            return PollStatus.unavailable();
        }

    }

}

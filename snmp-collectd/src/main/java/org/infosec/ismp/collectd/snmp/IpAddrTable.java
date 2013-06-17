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
// 2003 Sep 29: Modifications to allow for OpenNMS to handle duplicate IP Addresses.
// 2003 Jan 31: Cleaned up some unused imports.
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

package org.infosec.ismp.collectd.snmp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.infosec.ismp.snmp.SnmpInstId;
import org.infosec.ismp.snmp.SnmpObjId;
import org.infosec.ismp.util.ThreadCategory;

/**
 * <P>
 * IpAddrTable uses a SnmpSession to collect the ipAddrTable entries It
 * implements the SnmpHandler to receive notifications when a reply is
 * received/error occurs in the SnmpSession used to send requests /recieve
 * replies.
 * </P>
 * 
 * @author <A HREF="mailto:brozow@opennms.org">Matt Brozowski</A>
 * @author <A HREF="mailto:jamesz@opennms.org">James Zuo </A>
 * @author <A HREF="mailto:sowmya@opennms.org">Sowmya </A>
 * @author <A HREF="mailto:weave@oculan.com">Weave </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * 
 * @see <A HREF="http://www.ietf.org/rfc/rfc1213.txt">RFC1213 </A>
 */
public class IpAddrTable extends SnmpTable<IpAddrTableEntry> {

    /**
     * <P>
     * Constructs an IpAddrTable object that is used to collect the address
     * elements from the remote agent. Once all the elements are collected, or
     * there is an error in the collection the signaler object is <EM>notified
     * </EM> to inform other threads.
     * </P>
     * @param address TODO
     * @see IpAddrTableEntry
     */
    public IpAddrTable(InetAddress address) {
        super(address, "ipAddrTable", IpAddrTableEntry.ms_elemList);
    }

    protected IpAddrTableEntry createTableEntry(SnmpObjId base, SnmpInstId inst, Object val) {
        return new IpAddrTableEntry();
    }

     public InetAddress[] getIfAddressAndMask(int ifIndex) {
        if (getEntries() == null)
            return null;
        
        for(IpAddrTableEntry entry : getEntries()) {

            Integer ndx = entry.getIpAdEntIfIndex();
            if (ndx != null && ndx.intValue() == ifIndex) {
                // found it
                // extract the address
                //
                InetAddress[] pair = new InetAddress[2];
                pair[0] = entry.getIpAdEntAddr();
                pair[1] = entry.getIpAdEntNetMask();
                return pair;
            }
        }
        return null;
    }

    public int getIfIndex(InetAddress address) {
        if (getEntries() == null) {
            return -1;
        }
        if (log().isDebugEnabled())
            log().debug("getIfIndex: num ipAddrTable entries: " + getEntries().size());

        for(IpAddrTableEntry entry : getEntries()) {

            InetAddress ifAddr = entry.getIpAdEntAddr();
            if (ifAddr != null && ifAddr.equals(address)) {
                // found it
                // extract the ifIndex
                //
                Integer ndx = entry.getIpAdEntIfIndex();
                log().debug("getIfIndex: got a match for address " + address.getHostAddress() + " index: " + ndx);
                if (ndx != null)
                    return ndx.intValue();
            }
        }
        log().debug("getIfIndex: no matching ipAddrTable entry for " + address.getHostAddress());
        return -1;
    }

    protected final ThreadCategory log() {
        return ThreadCategory.getInstance(IpAddrTable.class);
    }

    /**
     * Returns all Internet addresses at the corresponding index. If the address
     * cannot be resolved then a null reference is returned.
     * 
     * @param ifIndex
     *            The index to search for.
     * 
     * @return list of InetAddress objects representing each of the interfaces
     *         IP addresses.
     */
    
    public List<InetAddress> getIpAddresses(int index) {
        if (index == -1 || getEntries() == null) {
            return null;
        }
        
        List<InetAddress> addresses = new ArrayList<InetAddress>();
        
        for(IpAddrTableEntry entry : getEntries()) {

            Integer ndx = entry.getIpAdEntIfIndex();
            if (ndx != null && ndx.intValue() == index) {
                
                InetAddress ifAddr = entry.getIpAdEntAddr();
                if (ifAddr != null) {
                    addresses.add(ifAddr);
                }
            }
        }
        return addresses;
    }

    /**
     * Returns all Internet addresses in the ipAddrEntry list. If the address
     * cannot be resolved then a null reference is returned.
     * 
     * @param ipAddrEntries
     *            List of IpAddrTableEntry objects to search
     * 
     * @return list of InetAddress objects representing each of the interfaces
     *         IP addresses.
     */
    public List<InetAddress> getIpAddresses() {
        if (getEntries() == null) {
            return null;
        }
        
        List <InetAddress>addresses = new ArrayList<InetAddress>();
        
        for(IpAddrTableEntry entry : getEntries()) {

            Integer ndx = entry.getIpAdEntIfIndex();
            if (ndx != null) {
        
                InetAddress ifAddr = entry.getIpAdEntAddr();
                if (ifAddr != null) {
                    addresses.add(ifAddr);
                }
        
            }
        }
        return addresses;
    }
}

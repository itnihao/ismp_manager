//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2009 The OpenNMS Group, Inc. All rights
// reserved.
// OpenNMS(R) is a derivative work, containing both original code, included
// code and modified
// code that was published under the GNU General Public License. Copyrights
// for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 2003 Jan 31: Cleaned up some unused imports.
// 2009 Mar 23: Add support for discarding messages. - jeffg@opennms.org
//
// Original code base Copyright (C) 1999-2001 Oculan Corp. All rights
// reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//       
// For more information contact:
// OpenNMS Licensing <license@opennms.org>
// http://www.opennms.org/
// http://www.opennms.com/
//

package org.infosec.ismp.syslogd;

import java.io.IOException;
import java.net.DatagramSocket;

import org.infosec.ismp.model.event.Event;
import org.infosec.ismp.model.event.EventReceipt;
import org.infosec.ismp.util.ThreadCategory;

/**
 * This class implements the User Datagram Protocol (UDP) event receiver. When
 * the an agent sends an event via UDP/IP the receiver will process the event
 * and then add the UUIDs to the internal list. If the event is successfully
 * processed then an event-receipt is returned to the caller.
 *
 */
public final class SyslogHandler {
	/**
	 * The default User Datagram Port for the receipt and transmission of
	 * events.
	 */

	/**
	 * The UDP receiver thread.
	 */
	private SyslogReceiver m_receiver;

	/**
	 * The user datagram packet processor
	 */
	private SyslogProcessor m_processor;

	/**
	 * The Fiber's status.
	 */
	private volatile int m_status;

	/**
	 * The UDP socket for receipt and transmission of packets from agents.
	 */
	private DatagramSocket m_dgSock;

	/**
	 * The UDP socket port binding.
	 */
	private int m_dgPort;

	/**
	 * The log prefix
	 */
	private String m_logPrefix;

	private final UeiList m_UeiList;

	/**
	 * Set the Trapd configuration
	 */
	private static SyslogdConfig m_syslogdConfig;

	static QueueManager queueManager = new QueueManager();

	public SyslogHandler() {
		m_dgSock = null;
		m_dgPort = m_syslogdConfig.getSyslogPort();

		m_UeiList = m_syslogdConfig.getUeiList();

		m_status = START_PENDING;

		m_dgSock = null;
		m_receiver = null;
		m_processor = null;
		m_logPrefix = null;
	}

	public static void setSyslogConfig(SyslogdConfig syslogdConfig) {
		m_syslogdConfig = syslogdConfig;
	}

	public synchronized void start() {
		if (m_status != START_PENDING)
			throw new RuntimeException("The Fiber is in an incorrect state");

		m_status = STARTING;

		try {
			m_dgSock = new DatagramSocket(m_dgPort);

			m_receiver = new SyslogReceiver(m_dgSock, m_UeiList);
			m_processor = new SyslogProcessor(m_UeiList);

			if (m_logPrefix != null) {
				m_receiver.setLogPrefix(m_logPrefix);
				m_processor.setLogPrefix(m_logPrefix);
			}
		} catch (IOException e) {
			throw new java.lang.reflect.UndeclaredThrowableException(e);
		}

		Thread rThread = new Thread(m_receiver, "Syslog Event Receiver["
				+ m_dgPort + "]");
		Thread pThread = new Thread(m_processor, "Syslog Event Processor["
				+ m_dgPort + "]");

		try {
			rThread.start();
			pThread.start();

		} catch (RuntimeException e) {
			rThread.interrupt();
			pThread.interrupt();

			m_status = STOPPED;
			throw e;
		}

		m_status = RUNNING;
	}

	public synchronized void stop() {
		if (m_status == STOPPED)
			return;
		if (m_status == START_PENDING) {
			m_status = STOPPED;
			return;
		}

		m_status = STOP_PENDING;

		try {
			m_receiver.stop();
			m_processor.stop();

		} catch (InterruptedException e) {
			ThreadCategory log = ThreadCategory.getInstance(this.getClass());
			log.warn(
					"The thread was interrupted while attempting to join sub-threads",
					e);
		}

		m_dgSock.close();

		m_status = STOPPED;
	}

	public String getName() {
		return "SyslogdHandler[" + m_dgPort + "]";
	}

	public int getStatus() {
		return m_status;
	}

	public void init() {
	}

	public void destroy() {
	}

	public void setPort(Integer port) {
		if (m_status == STARTING || m_status == RUNNING
				|| m_status == STOP_PENDING)
			throw new IllegalStateException("The process is already running");

		m_dgPort = port;
	}

	public Integer getPort() {
		return m_dgPort;
	}

	/**
	 * Adds a new event handler to receiver. When new events are received the
	 * decoded event is passed to the handler.
	 *
	 * @param handler
	 *            A reference to an event handler
	 */
	/*
	 * public void addEventHandler(Syslogd handler) { synchronized
	 * (m_handlers) { if (!m_handlers.contains(handler))
	 * m_handlers.add(handler); } }
	 */

	/**
	 * Removes an event handler from the list of handler called when an event
	 * is received. The handler is removed based upon the method
	 * <code>equals()</code> inherieted from the <code>Object</code>
	 * class.
	 * <p/>
	 * A reference to the event handler.
	 */
	/*
	 * public void removeEventHandler(Syslogd handler) { synchronized
	 * (m_handlers) { m_handlers.remove(handler); }
	 */
	public void setLogPrefix(String prefix) {
		m_logPrefix = prefix;
	}

	// YUCK!

	/**
	 * The string names that correspond to the states of the fiber.
	 */
	public static final String STATUS_NAMES[] = { "START_PENDING", // 0
			"STARTING", // 1
			"RUNNING", // 2
			"STOP_PENDING", // 3
			"STOPPED", // 4
			"PAUSE_PENDING", // 5
			"PAUSED", // 6
			"RESUME_PENDING" // 7
	};

	/**
	 * This is the initial <code>Fiber</code> state. When the
	 * <code>Fiber</code> begins it startup process it will transition to
	 * the <code>STARTING</code> state. A <code>Fiber</code> in a start
	 * pending state has not begun any of the initilization process.
	 */
	public static final int START_PENDING = 0;

	/**
	 * This state is used to define when a <code>Fiber</code> has begun the
	 * initilization process. Once the initilization process is completed the
	 * <code>Fiber</code> will transition to a <code>RUNNING</code>
	 * status.
	 */
	public static final int STARTING = 1;

	/**
	 * This state is used to define the normal runtime condition of a
	 * <code>Fiber</code>. When a <code>Fiber</code> is in this state
	 * then it is processing normally.
	 */
	public static final int RUNNING = 2;

	/**
	 * This state is used to denote when the <code>Fiber</code> is
	 * terminating processing. This state is always followed by the state
	 * <code>ST0PPED</code>.
	 */
	public static final int STOP_PENDING = 3;

	/**
	 * This state represents the final resting state of a <code>Fiber</code>.
	 * Depending on the implementation it may be possible to resurect the
	 * <code>Fiber</code> from this state.
	 */
	public static final int STOPPED = 4;

	public interface EventHandler {
		public boolean processEvent(Event event);

		public void receiptSent(EventReceipt receipt);
	}

	// private static SyslogdConfig m_syslogdConfig;

}
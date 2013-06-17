package org.infosec.ismp.model.poller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class PollStatus {

	private Date m_timestamp = new Date();

	/**
	 * Status of the pollable object.
	 */
	private int m_statusCode;

	private String m_reason;

	private Map<String, Number> m_properties = new LinkedHashMap<String, Number>();

	/**
	 * <P>
	 * The constant that defines a service that is up but is most likely
	 * suffering due to excessive load or latency issues and because of that has
	 * not responded within the configured timeout period.
	 * </P>
	 */
	public static final int SERVICE_UNRESPONSIVE = 3;

	/**
	 * <P>
	 * The constant that defines a service that is not working normally and
	 * should be scheduled using the downtime models.
	 * </P>
	 */
	public static final int SERVICE_UNAVAILABLE = 2;

	/**
	 * <P>
	 * The constant that defines a service as being in a normal state. If this
	 * is returned by the poll() method then the framework will re-schedule the
	 * service for its next poll using the standard uptime interval
	 * </P>
	 */
	public static final int SERVICE_AVAILABLE = 1;

	/**
	 * The constant the defines a status is unknown. Used mostly internally
	 */
	public static final int SERVICE_UNKNOWN = 0;

	private static final String[] s_statusNames = { "Unknown", "Up", "Down",
			"Unresponsive" };

	private static int decodeStatusName(String statusName) {

		for (int statusCode = 0; statusCode < s_statusNames.length; statusCode++) {
			if (s_statusNames[statusCode].equalsIgnoreCase(statusName)) {
				return statusCode;
			}
		}
		return SERVICE_UNKNOWN;
	}

	public static PollStatus decode(String statusName) {
		return decode(statusName, null, null);
	}

	public static PollStatus decode(String statusName, String reason) {
		return decode(statusName, reason, null);
	}

	public static PollStatus decode(String statusName, Double responseTime) {
		return decode(statusName, null, responseTime);
	}

	public static PollStatus decode(String statusName, String reason,
			Double responseTime) {
		return new PollStatus(decodeStatusName(statusName), reason,
				responseTime);
	}

	public static PollStatus get(int status, String reason) {
		return get(status, reason, null);
	}

	public static PollStatus get(int status, Double responseTime) {
		return get(status, null, responseTime);
	}

	public static PollStatus get(int status, String reason, Double responseTime) {
		return new PollStatus(status, reason, responseTime);
	}

	private PollStatus() {
		this(SERVICE_UNKNOWN, null, null);
	}

	private PollStatus(int statusCode, String reason, Double responseTime) {
		m_statusCode = statusCode;
		m_reason = reason;
		setResponseTime(responseTime);
	}

	public static PollStatus up() {
		return up(null);
	}

	public static PollStatus up(Double responseTime) {
		return available(responseTime);
	}

	public static PollStatus available() {
		return available(null);
	}

	public static PollStatus available(Double responseTime) {
		return new PollStatus(SERVICE_AVAILABLE, null, responseTime);
	}

	public static PollStatus unknown() {
		return unknown(null);
	}

	public static PollStatus unknown(String reason) {
		return new PollStatus(SERVICE_UNKNOWN, reason, null);
	}

	public static PollStatus unresponsive() {
		return unresponsive(null);
	}

	public static PollStatus unresponsive(String reason) {
		return new PollStatus(SERVICE_UNRESPONSIVE, reason, null);
	}

	public static PollStatus down() {
		return down(null);
	}

	public static PollStatus unavailable() {
		return unavailable(null);
	}

	public static PollStatus down(String reason) {
		return unavailable(reason);
	}

	public static PollStatus unavailable(String reason) {
		return new PollStatus(SERVICE_UNAVAILABLE, reason, null);
	}

	public boolean equals(Object o) {
		if (o instanceof PollStatus) {
			return m_statusCode == ((PollStatus) o).m_statusCode;
		}
		return false;
	}

	public int hashCode() {
		return m_statusCode;
	}

	public boolean isUp() {
		return !isDown();
	}

	public boolean isAvailable() {
		return this.m_statusCode == SERVICE_AVAILABLE;
	}

	public boolean isUnresponsive() {
		return this.m_statusCode == SERVICE_UNRESPONSIVE;
	}

	public boolean isUnavailable() {
		return this.m_statusCode == SERVICE_UNAVAILABLE;
	}

	public boolean isDown() {
		return this.m_statusCode == SERVICE_UNAVAILABLE;
	}

	public boolean isUnknown() {
		return this.m_statusCode == SERVICE_UNKNOWN;
	}

	public String toString() {
		return getStatusName();
	}

	public Date getTimestamp() {
		return m_timestamp;
	}

	public void setTimestamp(Date timestamp) {
		m_timestamp = timestamp;
	}

	public String getReason() {
		return m_reason;
	}

	public void setReason(String reason) {
		m_reason = reason;
	}

	public Double getResponseTime() {
		Number val = getProperty("response-time");
		return (val == null ? null : val.doubleValue());

	}

	/* stores the individual item for compatibility with database schema, as well as the new property map */
	public void setResponseTime(Double responseTime) {
		if (responseTime == null) {
			m_properties.remove("response-time");
		} else {
			m_properties.put("response-time", responseTime);
		}
	}

	public Map<String, Number> getProperties() {
		if (m_properties == null) {
			m_properties = new LinkedHashMap<String, Number>();
		}
		return m_properties;
	}

	public void setProperties(Map<String, Number> p) {
		m_properties = p;
	}

	public Number getProperty(String key) {
		if (m_properties != null) {
			return m_properties.get(key);
		} else {
			return null;
		}
	}

	public void setProperty(String key, Number value) {
		Map<String, Number> m = getProperties();
		m.put(key, value);
		setProperties(m);
	}

	public int getStatusCode() {
		return m_statusCode;
	}

	private void setStatusCode(int statusCode) {
		m_statusCode = statusCode;
	}

	public String getStatusName() {
		return s_statusNames[m_statusCode];
	}

}

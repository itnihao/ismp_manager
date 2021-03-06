package org.infosec.ismp.agent.winsensor.strategy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Rocky
 * @version create time：Oct 20, 2010 3:52:13 PM
 */

@MappedSuperclass
public class BaseStrategy implements Serializable {

	private static final long serialVersionUID = -6416691219718689166L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="xxGen")
	@TableGenerator(name="xxGen",allocationSize=1)
	@Column(name="id")
	private long id;

	/*
	 * Strategy create time.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;
	
	/*
	 * Strategy is to be issued. 1: successful, 0: unsuccessful.
	 */
	@Column(name="issued")
	private int issued = 0;
	
	/*
	 * Strategy issued time. 
	 * or covered time when it was covered by new strategy.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="issue_time")
	private Date issueTime;
	
	/*
	 * Sensor client ip.
	 */
	@Column(name="ip", length=100)
	private String ip;
	
	/*
	 * Sensor client id.
	 */
	@Column(name="sensor_id", length=100)
	private String sensorId;

	/*
	 * Is roughly covered by new strategy.
	 */
	@Column(name="covered")
	private int covered = 0;
	
	/*
	 * Strategy identifies generated by the web client.
	 * It can be traced back the origin.
	 */
	@Column(name="remote_id")
	private long remoteId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getIssued() {
		return issued;
	}

	public void setIssued(int issued) {
		this.issued = issued;
	}

	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public int getCovered() {
		return covered;
	}

	public void setCovered(int covered) {
		this.covered = covered;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}
}

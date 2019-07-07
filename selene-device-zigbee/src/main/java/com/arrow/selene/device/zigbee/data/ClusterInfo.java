package com.arrow.selene.device.zigbee.data;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClusterInfo implements Serializable {
	private static final long serialVersionUID = 4577646323572846022L;

	private int clusterId;
	private String name;
	private Map<Integer, AttributeInfo> attributes = new ConcurrentHashMap<>();
	private Map<Integer, CommandInfo> receivedCommands = new ConcurrentHashMap<>();
	private Map<Integer, CommandInfo> generatedCommands = new ConcurrentHashMap<>();

	public ClusterInfo() {
	}

	public ClusterInfo(int clusterId, String name) {
		this.clusterId = clusterId;
		if (name == null || name.isEmpty()) {
			name = "Unknown cluster";
		}
		this.name = name;
	}

	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, AttributeInfo> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<Integer, AttributeInfo> attributes) {
		this.attributes = attributes;
	}

	public Map<Integer, CommandInfo> getReceivedCommands() {
		return receivedCommands;
	}

	public void setReceivedCommands(Map<Integer, CommandInfo> receivedCommands) {
		this.receivedCommands = receivedCommands;
	}

	public Map<Integer, CommandInfo> getGeneratedCommands() {
		return generatedCommands;
	}

	public void setGeneratedCommands(Map<Integer, CommandInfo> generatedCommands) {
		this.generatedCommands = generatedCommands;
	}

	@Override
	public String toString() {
		return String.format("0x%04x (%s)", clusterId, name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ClusterInfo cluster = (ClusterInfo) o;

		return clusterId == cluster.clusterId;
	}

	@Override
	public int hashCode() {
		return clusterId;
	}
}

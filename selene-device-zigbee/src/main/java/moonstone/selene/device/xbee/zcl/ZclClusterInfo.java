package moonstone.selene.device.xbee.zcl;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class ZclClusterInfo implements Serializable {
	private static final long serialVersionUID = 2443011777933692961L;

	private Integer id;
	private String name;
	private Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> attributes;
	private Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> receivedCommands;
	private Map<Integer, String> generatedCommands;

	public ZclClusterInfo withId(Integer id) {
		setId(id);
		return this;
	}

	public ZclClusterInfo withName(String name) {
		setName(name);
		return this;
	}

	public ZclClusterInfo withAttributes(Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>>
			attributes) {
		setAttributes(attributes);
		return this;
	}

	public ZclClusterInfo withCommandsReceived(
			Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> receivedCommands) {
		setReceivedCommands(receivedCommands);
		return this;
	}

	public ZclClusterInfo withCommandsGenerated(Map<Integer, String> generatedCommands) {
		setGeneratedCommands(generatedCommands);
		return this;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>>  getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> attributes) {
		this.attributes = attributes;
	}

	public Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> getReceivedCommands() {
		return receivedCommands;
	}

	public void setReceivedCommands(
			Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> receivedCommands) {
		this.receivedCommands = receivedCommands;
	}

	public Map<Integer, String> getGeneratedCommands() {
		return generatedCommands;
	}

	public void setGeneratedCommands(Map<Integer, String> generatedCommands) {
		this.generatedCommands = generatedCommands;
	}
}

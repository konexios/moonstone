package moonstone.selene.device.zigbee.data;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import moonstone.acs.JsonUtils;
import moonstone.selene.device.xbee.zcl.ZclClusters;

public class ZigBeeInfoHolder implements Serializable {
	private static final long serialVersionUID = 969169090673770969L;

	private Map<Integer, EndpointInfo> endpoints = new ConcurrentHashMap<>();

	public Map<Integer, EndpointInfo> getEndpoints() {
		return endpoints;
	}

	public EndpointInfo checkCreateEndpoint(int endpointId) {
		EndpointInfo endpoint = endpoints.get(endpointId);
		if (endpoint == null) {
			endpoint = new EndpointInfo();
			endpoint.setId(endpointId);
			endpoints.put(endpointId, endpoint);
		}
		return endpoint;
	}

	public ClusterInfo checkCreateEndpointCluster(int endpointId, int clusterId) {
		EndpointInfo endpoint = endpoints.get(endpointId);
		if (endpoint == null) {
			endpoint = new EndpointInfo();
			endpoint.setId(endpointId);
			endpoints.put(endpointId, endpoint);
		}
		ClusterInfo clusterInfo = endpoint.getClusters().get(clusterId);
		if (clusterInfo == null) {
			clusterInfo = new ClusterInfo(clusterId, ZclClusters.getName(clusterId));
			endpoint.getClusters().put(clusterId, clusterInfo);
		}
		return clusterInfo;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}

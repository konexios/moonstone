package com.arrow.selene.device.zigbee.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.arrow.acs.JsonUtils;

public class BindingsHolder implements Serializable {
	private static final long serialVersionUID = 4732709539768621098L;

	private Set<BindingInfo> bindings = new HashSet<>();

	public Set<BindingInfo> getBindings() {
		return bindings;
	}

	public void setBindings(Set<BindingInfo> bindings) {
		this.bindings = bindings;
	}

	public void addBinding(BindingInfo bindingInfo) {
		bindings.add(bindingInfo);
	}

	public void removeBinding(BindingInfo bindingInfo) {
		bindings.remove(bindingInfo);
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}

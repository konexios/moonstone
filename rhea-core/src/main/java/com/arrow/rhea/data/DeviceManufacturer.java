package com.arrow.rhea.data;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.rhea.RheaCoreConstants;

@Document(collection = "device_manufacturer")
public class DeviceManufacturer extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -5694557640364456648L;

	private boolean editable = CoreConstant.DEFAULT_EDITABLE;

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	protected String getProductPri() {
		return RheaCoreConstants.RHEA_PRI;
	}

	@Override
	protected String getTypePri() {
		return RheaCoreConstants.RheaPri.DEVICE_MANUFACTURER;
	}
}

package com.arrow.pegasus.data.location;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;

@Document(collection = "last_location")
public class LastLocation extends LocationAbstract {
	private static final long serialVersionUID = -2602419028521980794L;

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.LAST_LOCATION;
	}
}

package com.arrow.pegasus.data.profile;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;

@Document(collection = "region")
@CompoundIndexes({ @CompoundIndex(name = "name", unique = true, background = true, def = "{'name': 1}") })
public class Region extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -5768128469129807172L;

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.REGION;
	}
}

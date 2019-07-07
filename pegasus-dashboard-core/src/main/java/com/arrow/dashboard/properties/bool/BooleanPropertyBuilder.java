package com.arrow.dashboard.properties.bool;

import com.arrow.dashboard.property.impl.SimplePropertyBuilder;

/**
 * Property builder definition for {@link BooleanProperty}
 * 
 * @author dantonov
 *
 */
public class BooleanPropertyBuilder extends SimplePropertyBuilder<BooleanProperty, Boolean, BooleanPropertyView> {

	public BooleanPropertyBuilder() {
		super(BooleanProperty.class);
	}

}

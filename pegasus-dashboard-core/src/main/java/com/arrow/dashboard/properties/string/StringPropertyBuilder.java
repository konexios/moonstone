package com.arrow.dashboard.properties.string;

import com.arrow.dashboard.property.impl.SimplePropertyBuilder;

/**
 * Property builder definition for {@link StringProperty}
 * 
 * @author dantonov
 *
 */
public class StringPropertyBuilder extends SimplePropertyBuilder<StringProperty, String, StringPropertyView> {

	public StringPropertyBuilder() {
		super(StringProperty.class);
	}
}
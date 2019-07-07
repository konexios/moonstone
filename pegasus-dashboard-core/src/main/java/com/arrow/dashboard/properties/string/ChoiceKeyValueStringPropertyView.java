package com.arrow.dashboard.properties.string;

import java.util.List;

import com.arrow.dashboard.widget.annotation.property.PropertyView;

@PropertyView("choice-Key-Value-String")
public final class ChoiceKeyValueStringPropertyView implements StringPropertyView {

	private List<StringKeyValue> possibleValues;

	public ChoiceKeyValueStringPropertyView(List<StringKeyValue> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public List<StringKeyValue> getPossibleValues() {
		return possibleValues;
	}
}

package com.arrow.dashboard.properties.integer;

import java.util.List;

import com.arrow.dashboard.widget.annotation.property.PropertyView;

@PropertyView("choice-Key-Value-Integer")
public final class ChoiceKeyValueIntegerPropertyView implements IntegerPropertyView {

	private List<IntegerKeyValue> possibleValues;

	public ChoiceKeyValueIntegerPropertyView(List<IntegerKeyValue> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public List<IntegerKeyValue> getPossibleValues() {
		return possibleValues;
	}
}

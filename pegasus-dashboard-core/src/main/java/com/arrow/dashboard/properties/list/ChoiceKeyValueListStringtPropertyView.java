package com.arrow.dashboard.properties.list;

import java.util.List;

import com.arrow.dashboard.properties.string.StringKeyValue;
import com.arrow.dashboard.widget.annotation.property.PropertyView;

@PropertyView("choice-Key-Value-List-String")
public class ChoiceKeyValueListStringtPropertyView implements StringListPropertyView{

	private List<StringKeyValue> possibleValues;
	
	public ChoiceKeyValueListStringtPropertyView(List<StringKeyValue> possibleValues) {
		super();
		this.possibleValues = possibleValues;
	}

	public List<StringKeyValue> getPossibleValues() {
		return possibleValues;
	}
}

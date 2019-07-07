package com.arrow.dashboard.properties.string;

import java.util.List;

import com.arrow.dashboard.widget.annotation.property.PropertyView;

/**
 * View for {@link StringProperty} that allows user to select a value from list
 * of possible values. Like 'select' element
 * 
 * @author dantonov
 *
 */
@PropertyView("choice-String")
public final class ChoiceStringPropertyView implements StringPropertyView {

	private List<String> possibleValues;

	public ChoiceStringPropertyView(List<String> possibleValues) {
		super();
		this.possibleValues = possibleValues;
	}

	public List<String> getPossibleValues() {
		return possibleValues;
	}

}

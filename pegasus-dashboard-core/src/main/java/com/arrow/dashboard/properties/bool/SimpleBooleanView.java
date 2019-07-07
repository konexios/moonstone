package com.arrow.dashboard.properties.bool;

import com.arrow.dashboard.widget.annotation.property.PropertyView;

/**
 * View for {@link BooleanProperty} that represents a boolean value as check box
 * 
 * @author dantonov
 *
 */
@PropertyView("simple-Boolean")
public final class SimpleBooleanView implements BooleanPropertyView {

	private final static String DEFAULT_TRUE_LABEL = "True";
	private final static String DEFAULT_FALSE_LABEL = "False";

	private String trueLabel = DEFAULT_TRUE_LABEL;
	private String falseLabel = DEFAULT_FALSE_LABEL;

	public String getTrueLabel() {
		return trueLabel;
	}

	public void setTrueLabel(String trueLabel) {
		this.trueLabel = trueLabel;
	}

	public String getFalseLabel() {
		return falseLabel;
	}

	public void setFalseLabel(String falseLabel) {
		this.falseLabel = falseLabel;
	}

	public SimpleBooleanView withTrueLabel(String trueLabel) {
		setTrueLabel(trueLabel);

		return this;
	}

	public SimpleBooleanView withFalseLabel(String falseLabel) {
		setFalseLabel(falseLabel);

		return this;
	}
}

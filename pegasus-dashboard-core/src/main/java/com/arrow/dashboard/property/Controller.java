package com.arrow.dashboard.property;

/**
 * Represents a controller for {@link Property} model<br>
 * Responsible for persistence of model object
 * 
 * @author dantonov
 *
 * @param <Model>
 *            type of property model
 */
public interface Controller<Model> {
	Object persist(Model model);

	Model restore(Object persistence);
}

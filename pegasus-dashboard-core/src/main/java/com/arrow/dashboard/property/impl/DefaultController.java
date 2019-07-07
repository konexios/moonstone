package com.arrow.dashboard.property.impl;

import com.arrow.dashboard.property.Controller;

/**
 * Default {@link Controller} persists model object as is
 * <p>
 * This is suitable for MongoDB database. So controller write and read model
 * value just as an object on model's type, without additional transformation
 * 
 * @author dantonov
 *
 * @param <M>
 */
public class DefaultController<M> implements Controller<M> {

	@Override
	public Object persist(M model) {
		return model;
	}

	@Override
	public M restore(Object persistence) {
		return (M) persistence;
	}

}

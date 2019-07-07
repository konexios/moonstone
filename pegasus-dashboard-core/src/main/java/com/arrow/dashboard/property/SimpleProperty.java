package com.arrow.dashboard.property;

import com.arrow.dashboard.property.impl.DefaultController;

/**
 * Interface to extend properties with default controller
 * {@link DefaultController}<br>
 * Simple property value is persisted 'as is'
 * 
 * @author dantonov
 *
 * @param <M>
 * @param <V>
 */
public interface SimpleProperty<M, V> extends Property<M, V, DefaultController<M>> {

}

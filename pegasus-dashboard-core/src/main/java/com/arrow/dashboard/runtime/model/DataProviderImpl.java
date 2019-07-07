package com.arrow.dashboard.runtime.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * Represents a data provider implementation, annotated by {@link DataProvider}
 * 
 * @author dantonov
 *
 */
public class DataProviderImpl extends AbstractExceptionable implements Serializable {
	private static final long serialVersionUID = -7195565830339092788L;

	private Class<?> dataProviderImplClass;
	private List<Class<?>> implementedDataProviders;

	public Class<?> getDataProviderImplClass() {
		return dataProviderImplClass;
	}

	public List<Class<?>> getImplementedDataProviders() {
		return implementedDataProviders;
	}

	public DataProviderImpl(BeanDefinition beanDefinition) {

		String method = "DataProviderImpl";

		String beanClassName = beanDefinition.getBeanClassName();
		logDebug(method, "DataProviderImpl start loading data provider implementation from " + beanClassName);

		try {
			try {
				dataProviderImplClass = Class.forName(beanClassName);
			} catch (ClassNotFoundException e) {
				exception = new WidgetDefinitionException("Class not found", e);
			}

			com.arrow.dashboard.widget.annotation.data.DataProviderImpl dataProviderAnnotation = dataProviderImplClass
			        .getAnnotation(com.arrow.dashboard.widget.annotation.data.DataProviderImpl.class);
			implementedDataProviders = Arrays.asList(dataProviderAnnotation.dataProviders());
			logDebug(method,
			        "DataProviderImpl loaded data provider implementation from " + beanClassName
			                + ". Implemented data providers: "
			                + implementedDataProviders.stream().map(Class::getName).collect(Collectors.toList()));
		} catch (Throwable t) {
			exception = new WidgetDefinitionException(
			        "Unexpected exception when parsing data provider " + beanClassName + " :" + t.getMessage(), t);
		}
	}

}

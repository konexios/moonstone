package com.arrow.dashboard.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.arrow.dashboard.runtime.model.DataProviderImpl;
import com.arrow.dashboard.widget.annotation.data.DataProvider;

/**
 * Service to search all data provider implementations<br>
 * Please refer to {@link DataProvider} and {@link DataProviderImpl}
 * 
 * @author dantonov
 *
 */
public class DataProviderScanner {

	private List<DataProviderImpl> dataProviders = new ArrayList<>();

	/**
	 * Method to scan classpath for data provider implementations
	 */
	public void scan() {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(com.arrow.dashboard.widget.annotation.data.DataProviderImpl.class));
		dataProviders = scanner.findCandidateComponents("com.arrow.widget").stream().map(DataProviderImpl::new)
		        .collect(Collectors.toList());
		// TODO: analyze implementations. check there are no errors, check if
		// all method is fine
	}

	/**
	 * Method to get implementation class for data provider, annotated by
	 * {@link DataProvider}
	 * 
	 * @param widgetDataProvider
	 * @return
	 */
	public Class<?> getImplementation(Class<?> widgetDataProvider) {
		return dataProviders.stream().filter(dp -> dp.getImplementedDataProviders().contains(widgetDataProvider))
		        .findFirst().map(DataProviderImpl::getDataProviderImplClass).orElse(null);
	}

	/**
	 * Method to get data provider implementation in the system
	 * 
	 * @return
	 */
	public List<DataProviderImpl> getDataProviders() {
		return new ArrayList<>(dataProviders);
	}
}

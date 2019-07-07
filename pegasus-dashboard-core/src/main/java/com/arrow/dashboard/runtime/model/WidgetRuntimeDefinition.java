package com.arrow.dashboard.runtime.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.dashboard.widget.WidgetAbstract;
import com.arrow.dashboard.widget.annotation.OnCorrection;
import com.arrow.dashboard.widget.annotation.OnCreate;
import com.arrow.dashboard.widget.annotation.OnDestroy;
import com.arrow.dashboard.widget.annotation.OnError;
import com.arrow.dashboard.widget.annotation.OnLoading;
import com.arrow.dashboard.widget.annotation.OnReady;
import com.arrow.dashboard.widget.annotation.OnRunning;
import com.arrow.dashboard.widget.annotation.OnStopped;
import com.arrow.dashboard.widget.annotation.Widget;
import com.arrow.dashboard.widget.annotation.messaging.TopicProvider;
import com.arrow.dashboard.widget.annotation.size.Large;
import com.arrow.dashboard.widget.annotation.size.Medium;
import com.arrow.dashboard.widget.annotation.size.Small;
import com.arrow.dashboard.widget.annotation.size.XtraLarge;

public class WidgetRuntimeDefinition extends AbstractExceptionable {

	private String id;
	private Class<?> widgetClass;
	private String widgetName;
	private String widgetDescription;
	private String directive;
	private WidgetSizesRuntimeInstance sizes;

	private Method onCreateMethod;
	private Method onLoadingMethod;
	private Method onReadyMethod;
	private Method onRunningMethod;
	private Method onStoppedMethod;
	private Method onErrorMethod;
	private Method onCorrectionMethod;
	private Method onDestroyMethod;

	private List<MessageEndpoint> messageEndpoints = new ArrayList<>();
	private List<TopicProviderField> fieldTopicProviders = new ArrayList<>();
	private List<DataProvider> dataProviders = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Class<?> getWidgetClass() {
		return widgetClass;
	}

	public String getWidgetClassName() {
		return widgetClass.getName();
	}

	public String getWidgetName() {
		return widgetName;
	}

	public String getWidgetDescription() {
		return widgetDescription;
	}

	public String getDirective() {
		return directive;
	}

	public WidgetSizesRuntimeInstance getSizes() {
		return sizes;
	}

	public Method getOnCreateMethod() {
		return onCreateMethod;
	}

	public Method getOnLoadingMethod() {
		return onLoadingMethod;
	}

	public Method getOnReadyMethod() {
		return onReadyMethod;
	}

	public Method getOnRunningMethod() {
		return onRunningMethod;
	}

	public Method getOnStoppedMethod() {
		return onStoppedMethod;
	}

	public Method getOnErrorMethod() {
		return onErrorMethod;
	}

	public Method getOnCorrectionMethod() {
		return onCorrectionMethod;
	}

	public Method getOnDestroyMethod() {
		return onDestroyMethod;
	}

	public List<MessageEndpoint> getMessageEndpoints() {
		return messageEndpoints;
	}

	public List<TopicProviderField> getFieldTopicProviders() {
		return fieldTopicProviders;
	}

	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public WidgetRuntimeDefinition(BeanDefinition beanDefinition) {

		String method = "WidgetTypeRuntimeInstance";

		String beanClassName = beanDefinition.getBeanClassName();
		logDebug(method, "WidgetTypeRuntimeInstance start loading widget from " + beanClassName);

		try {
			parseWidgetBeanDefinition(beanDefinition);
		} catch (WidgetDefinitionException e) {
			// expected exceptions
			logError(method, "WidgetTypeRuntimeInstance failed to load widget " + beanClassName + " with error: "
			        + e.getMessage());
			logError(method, "exception", e);
			exception = e;
		} catch (Throwable t) {
			// not expected exception!
			logError(method, "WidgetTypeRuntimeInstance failed to load widget " + beanClassName
			        + " with unexpected exception: " + t.getMessage());
			logError(method, "exception", t);
			exception = new WidgetDefinitionException("Unexpected exception during loading widget: " + t.getMessage(),
			        t);
		}

	}

	private void parseWidgetBeanDefinition(BeanDefinition beanDefinition) throws WidgetDefinitionException {

		try {
			widgetClass = Class.forName(beanDefinition.getBeanClassName());
		} catch (ClassNotFoundException e) {
			throw new AcsLogicalException("Class not found", e);
		}

		if (!WidgetAbstract.class.isAssignableFrom(widgetClass))
			throw new AcsLogicalException(
			        "Widget " + widgetClass.getName() + " must extend " + WidgetAbstract.class.getName());

		try {
			Widget widgetAnnotation = widgetClass.getAnnotation(Widget.class);
			Assert.notNull(widgetAnnotation, "Widget has no required @Widget annotation");

			widgetName = widgetAnnotation.name();
			widgetDescription = widgetAnnotation.description();
			directive = widgetAnnotation.directive();

			// widget sizes
			populateWidgetSizes();

			// these methods are required of any widget to manage widget
			// state machine
			onCreateMethod = findAnnotationMethodRecursively(widgetClass, OnCreate.class);
			Assert.isTrue(onCreateMethod != null,
			        "widget must have a method annotated with @" + OnCreate.class.getSimpleName());
			onLoadingMethod = findAnnotationMethodRecursively(widgetClass, OnLoading.class);
			Assert.isTrue(onLoadingMethod != null,
			        "widget must have a method annotated with @" + OnLoading.class.getSimpleName());
			onReadyMethod = findAnnotationMethodRecursively(widgetClass, OnReady.class);
			Assert.isTrue(onReadyMethod != null,
			        "widget must have a method annotated with @" + OnReady.class.getSimpleName());
			onRunningMethod = findAnnotationMethodRecursively(widgetClass, OnRunning.class);
			Assert.isTrue(onRunningMethod != null,
			        "widget must have a method annotated with @" + OnRunning.class.getSimpleName());
			onStoppedMethod = findAnnotationMethodRecursively(widgetClass, OnStopped.class);
			Assert.isTrue(onStoppedMethod != null,
			        "widget must have a method annotated with @" + OnStopped.class.getSimpleName());
			onErrorMethod = findAnnotationMethodRecursively(widgetClass, OnError.class);
			Assert.isTrue(onErrorMethod != null,
			        "widget must have a method annotated with @" + OnError.class.getSimpleName());
			onCorrectionMethod = findAnnotationMethodRecursively(widgetClass, OnCorrection.class);
			Assert.isTrue(onCorrectionMethod != null,
			        "widget must have a method annotated with @" + OnCorrection.class.getSimpleName());
			onDestroyMethod = findAnnotationMethodRecursively(widgetClass, OnDestroy.class);
			Assert.isTrue(onDestroyMethod != null,
			        "widget must have a method annotated with @" + OnDestroy.class.getSimpleName());

			// message endpoints
			findMessageEndpointsRecursively(widgetClass, messageEndpoints);
			Assert.isTrue(messageEndpoints.stream().filter(me -> me.getException() != null).collect(Collectors.toList())
			        .isEmpty(), "Message endpoints has exceptions");

			// topic providers
			try {
				findTopicProvidersRecursively(widgetClass, fieldTopicProviders);
			} catch (Throwable t) {
				throw new AcsLogicalException(
				        "Failed to load field topic providers with unexpected exception: " + t.getMessage(), t);
			}

			// data providers
			try {
				findDataProvidersRecursively(widgetClass, dataProviders);
			} catch (Throwable t) {
				throw new AcsLogicalException(
				        "Failed to load field data providers with unexpected exception: " + t.getMessage(), t);
			}
		} catch (IllegalArgumentException e) {
			// assertion failed
			throw new AcsLogicalException("Widget does not meet requirements: " + e.getMessage());
		}
	}

	// recursively look for message endpoints
	private void findMessageEndpointsRecursively(Class<?> instanceClass, List<MessageEndpoint> list) {
		Assert.notNull(instanceClass, "instanceClass is null");
		Assert.notNull(list, "list is null");

		if (WidgetAbstract.class.isAssignableFrom(instanceClass)) {
			list.addAll(Arrays.asList(instanceClass.getDeclaredMethods()).stream()
			        .filter(c -> c
			                .isAnnotationPresent(com.arrow.dashboard.widget.annotation.messaging.MessageEndpoint.class))
			        .map(MessageEndpoint::new).collect(Collectors.toList()));

			findMessageEndpointsRecursively(instanceClass.getSuperclass(), list);
		}
	}

	// recursively look for annotation method
	private Method findAnnotationMethodRecursively(Class<?> instanceClass,
	        Class<? extends Annotation> annotationClass) {
		Assert.notNull(instanceClass, "instanceClass is null");
		Assert.notNull(annotationClass, "annotationClass is null");

		Method method = null;
		if (WidgetAbstract.class.isAssignableFrom(instanceClass)) {
			method = getAnnotationMethod(instanceClass, annotationClass);
			if (method == null)
				method = findAnnotationMethodRecursively(instanceClass.getSuperclass(), annotationClass);
		}

		return method;
	}

	// recursively look for topic providers
	private void findTopicProvidersRecursively(Class<?> instanceClass, List<TopicProviderField> list) {
		Assert.notNull(instanceClass, "instanceClass is null");
		Assert.notNull(list, "list is null");

		if (WidgetAbstract.class.isAssignableFrom(instanceClass)) {
			list.addAll(Arrays.asList(instanceClass.getDeclaredFields()).stream()
			        .filter(f -> f.isAnnotationPresent(TopicProvider.class)).map(TopicProviderField::new)
			        .collect(Collectors.toList()));

			findTopicProvidersRecursively(instanceClass.getSuperclass(), list);
		}
	}

	// recursively look for data providers
	private void findDataProvidersRecursively(Class<?> instanceClass, List<DataProvider> list) {
		Assert.notNull(instanceClass, "instanceClass is null");
		Assert.notNull(list, "list is null");

		if (WidgetAbstract.class.isAssignableFrom(instanceClass)) {
			list.addAll(
			        Arrays.asList(instanceClass.getDeclaredFields()).stream()
			                .filter(c -> c
			                        .isAnnotationPresent(com.arrow.dashboard.widget.annotation.data.DataProvider.class))
			                .map(DataProvider::new).collect(Collectors.toList()));

			findDataProvidersRecursively(instanceClass.getSuperclass(), list);
		}
	}

	private Method getAnnotationMethod(Class<?> instanceClass, Class<? extends Annotation> annotationClass) {
		Assert.notNull(instanceClass, "instanceClass is null");
		Assert.notNull(annotationClass, "annotationClass is null");

		Optional<Method> optionalMethod = Arrays.asList(instanceClass.getDeclaredMethods()).stream()
		        .filter(c -> c.isAnnotationPresent(annotationClass)).findFirst();
		if (optionalMethod.isPresent())
			return optionalMethod.get();
		else
			return null;
	}

	private void populateWidgetSizes() {
		String method = "populateWidgetSizes";
		logInfo(method, "...");
		logDebug(method, "sizes for widget: %s", widgetName);

		WidgetSizesRuntimeInstance widgetSizesRuntimeInstance = new WidgetSizesRuntimeInstance();

		Small small = widgetClass.getAnnotation(Small.class);
		if (small != null)
			widgetSizesRuntimeInstance.setSmall(new WidgetDimensionsRuntimeInstance(small.width(), small.height()));

		Medium medium = widgetClass.getAnnotation(Medium.class);
		if (medium != null)
			widgetSizesRuntimeInstance.setMedium(new WidgetDimensionsRuntimeInstance(medium.width(), medium.height()));

		Large large = widgetClass.getAnnotation(Large.class);
		if (large != null)
			widgetSizesRuntimeInstance.setLarge(new WidgetDimensionsRuntimeInstance(large.width(), large.height()));

		XtraLarge xtraLarge = widgetClass.getAnnotation(XtraLarge.class);
		if (xtraLarge != null)
			widgetSizesRuntimeInstance
			        .setXtraLarge(new WidgetDimensionsRuntimeInstance(xtraLarge.width(), xtraLarge.height()));

		this.sizes = widgetSizesRuntimeInstance;
	}
}
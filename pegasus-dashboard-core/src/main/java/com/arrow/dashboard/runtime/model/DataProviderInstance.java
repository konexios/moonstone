package com.arrow.dashboard.runtime.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.arrow.dashboard.DashboardEntityAbstract;
import com.arrow.dashboard.exception.DataProviderException;

/**
 * Represents an instance of data provider implementation for data provider
 * filed in widget
 * 
 * @author dantonov
 *
 */
public class DataProviderInstance extends AbstractExceptionable implements Serializable {
	private static final long serialVersionUID = 1767613974502703051L;

	private Class<?> dataProviderClass;
	private Object dataProviderImplInstance;
	private Object widgetDataProviderProxy;

	public Class<?> getDataProviderClass() {
		return dataProviderClass;
	}

	public DataProviderInstance(UserRuntimeInstance userRuntime, DataProvider dataProvider,
	        Object widgetRuntimeInstance, AutowireCapableBeanFactory beanFactory, ApplicationContext appContext,
	        List<DataProviderImpl> dataProvidersImplementations) {
		try {

			Field dataProviderField = dataProvider.getField();
			dataProviderClass = dataProviderField.getType();

			Optional<DataProviderImpl> dataProviderImpl = dataProvidersImplementations.stream()
			        .filter(dpi -> dpi.getImplementedDataProviders().contains(dataProviderClass)).findFirst();

			if (!dataProviderImpl.isPresent()) {
				throw new WidgetDefinitionException("DataProviderInstance did not find implementation for "
				        + dataProviderClass.getName() + " data provider");
			}

			DataProviderImpl dataProviderImplementation = dataProviderImpl.get();

			Class<?> dataProviderImplClass = dataProviderImplementation.getDataProviderImplClass();
			com.arrow.dashboard.widget.annotation.data.DataProviderImpl dataProviderImplAnnotation = dataProviderImplClass
			        .getAnnotation(com.arrow.dashboard.widget.annotation.data.DataProviderImpl.class);

			try {
				switch (dataProviderImplAnnotation.retention()) {
				case AUTOWIRED:
					dataProviderImplInstance = appContext.getBean(dataProviderImplClass);
					break;
				case CREATE:
					dataProviderImplInstance = dataProviderImplClass.newInstance();
					beanFactory.autowireBean(dataProviderImplInstance);
					break;
				case INSTANCE:
					dataProviderImplInstance = dataProviderImplClass.getMethod("getInstance").invoke(null);
					break;
				default:
					break;
				}
			} catch (Throwable t) {
				throw new WidgetDefinitionException("DataProviderInstance failed to instantiate data provider of "
				        + dataProviderImplClass.getName(), t);
			}

			widgetDataProviderProxy = Proxy.newProxyInstance(dataProviderClass.getClassLoader(),
			        new Class[] { dataProviderClass },
			        new DataProviderProxy(userRuntime, dataProviderImplInstance, dataProviderClass.getName()));

			try {
				dataProviderField.set(widgetRuntimeInstance, widgetDataProviderProxy);
			} catch (Exception e) {
				throw new WidgetDefinitionException("DataProviderInstance failed to set data provider of "
				        + dataProviderImplClass.getName() + " to widget instance field", e);
			}
		} catch (WidgetDefinitionException e) {
			exception = e;
		} catch (Throwable t) {
			exception = new WidgetDefinitionException(
			        "DataProviderInstance failed to initialize with unexpected exception " + t.getMessage(), t);
		}
	}

	/**
	 * Data provider proxy is the instance of data provider assigned to widget
	 * runtime instance<br>
	 * Calls sequence:<br>
	 * 1. widget calls its data provider, defined as interface and annotated as
	 * {@link com.arrow.dashboard.widget.annotation.data.DataProvider}<br>
	 * 2. actually, data provider is this proxy, so invocation handler calls
	 * invoke method in the proxy<br>
	 * 3. proxy routes call to real data provider implementation (refer to
	 * {@link com.arrow.widget.annotation.data.DataProviderImpl), adding user as
	 * a first parameter<br>
	 * 
	 * @author dantonov
	 *
	 */
	private static class DataProviderProxy extends DashboardEntityAbstract implements InvocationHandler {

		private UserWrapper userWrapper;
		private Object dataProviderImplInstance;
		private String widgetDataProviderClassName;

		public DataProviderProxy(UserRuntimeInstance user, Object dataProviderImplInstance,
		        String widgetDataProviderClassName) {
			super();
			this.userWrapper = new UserWrapper(user);
			this.dataProviderImplInstance = dataProviderImplInstance;
			this.widgetDataProviderClassName = widgetDataProviderClassName;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			String methodName = "invoke";

			logDebug(methodName, "Widget data provider called. data provider:" + widgetDataProviderClassName
			        + ", user: " + userWrapper.getLogin());

			try {
				// invoke the reference method from data provider implementation
				Method realMethod = Arrays.asList(dataProviderImplInstance.getClass().getDeclaredMethods()).stream()
				        .filter(m -> m.getName().equals(method.getName())).findFirst().orElse(null);

				Object[] userParameter = new Object[] { userWrapper };

				if (args == null) {
					return realMethod.invoke(dataProviderImplInstance, userParameter);
				} else {
					return realMethod.invoke(dataProviderImplInstance, concat(userParameter, args));
				}

			} catch (Throwable t) {
				logDebug(methodName, "Widget data provider call failed. data provider:" + widgetDataProviderClassName
				        + ", user: " + userWrapper.getLogin() + ", exception: " + t.getMessage(), t);
				logError(methodName, "Widget data provider call failed. data provider:" + widgetDataProviderClassName
				        + ", user: " + userWrapper.getLogin() + ", exception: " + t.getMessage());
				throw new DataProviderException("Widget data provider call failed", t);
			}
		}

		private static <T> T[] concat(T[] first, T[] second) {
			T[] result = Arrays.copyOf(first, first.length + second.length);
			System.arraycopy(second, 0, result, first.length, second.length);
			return result;
		}

	}
}

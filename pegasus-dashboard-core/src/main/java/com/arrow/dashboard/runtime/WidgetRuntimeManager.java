package com.arrow.dashboard.runtime;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.arrow.dashboard.runtime.model.DataProviderInstance;
import com.arrow.dashboard.runtime.model.UserRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetRuntimeConfigurationPatch;
import com.arrow.dashboard.runtime.model.WidgetRuntimeDefinition;
import com.arrow.dashboard.runtime.model.WidgetRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetStateChangeResponse;
import com.arrow.dashboard.widget.configuration.ConfigurationManager;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.dashboard.service.WidgetTypeService;

import moonstone.acs.AcsLogicalException;

/**
 * Service to manage widgets lifecycle
 * 
 * @author dantonov
 *
 */
public class WidgetRuntimeManager extends RuntimeManagerAbstract<WidgetRuntimeInstance> {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DataProviderScanner dataProviderScanner;
    @Autowired
    private WidgetRuntimeDefinitionManager widgetScanner;
    @Autowired
    private RouterRuntimeManager routerRuntimeManager;
    @Autowired
    private WidgetTypeService widgetTypeService;

    public WidgetRuntimeInstance registerWidget(String parentRuntimeId, Widget widget,
            UserRuntimeInstance userRuntime) {
        return registerWidget(parentRuntimeId, widget, null, userRuntime);
    }

    public WidgetRuntimeInstance registerWidget(String parentRuntimeId, Widget widget,
            WidgetRuntimeConfigurationPatch configurationPatch, UserRuntimeInstance userRuntime) {
        Assert.hasText(parentRuntimeId, "parentRuntimeId is empty");
        Assert.notNull(widget, "widget is null");
        Assert.notNull(userRuntime, "userRuntime is null");

        WidgetRuntimeInstance widgetRuntimeInstance = initializeWidgetRuntimeInstance(parentRuntimeId, widget,
                userRuntime);
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        // register running instances
        registerRuntimeInstance(widgetRuntimeInstance.getWidgetRuntimeId(), widgetRuntimeInstance);

        // register new route
        routerRuntimeManager.registerRouter(widgetRuntimeInstance,
                initializeConfigurationManager(widgetRuntimeInstance, configurationPatch));

        return widgetRuntimeInstance;
    }

    public WidgetRuntimeInstance unregisterWidget(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        // unregister route
        routerRuntimeManager.unregisterRouter(widgetRuntimeId);

        // unregister runtime instance
        return unregisterRuntimeInstance(widgetRuntimeId);
    }

    public WidgetRuntimeInstance updateWidgetRuntimeInstance(String widgetRuntimeId,
            WidgetRuntimeInstance widgetRuntimeInstance) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        return updateRegisteredRuntimeInstance(widgetRuntimeId, widgetRuntimeInstance);
    }

    public WidgetStateChangeResponse notifyOnCreate(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "notifyOnCreate";
        logInfo(method, "...");
        logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

        // TODO a bit unsafe instance could be null!!!
        return notifyOnCreate(getRuntimeInstance(widgetRuntimeId));
    }

    public WidgetStateChangeResponse notifyOnCreate(WidgetRuntimeInstance widgetRuntimeInstance) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        String method = "notifyOnCreate";
        logInfo(method, "...");

        return notifyWidget(widgetRuntimeInstance,
                widgetRuntimeInstance.getWidgetRuntimeDefinition().getOnCreateMethod());
    }

    public WidgetStateChangeResponse notifyOnLoading(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "notifyOnLoading";
        logInfo(method, "...");
        logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

        return notifyOnLoading(getRuntimeInstance(widgetRuntimeId));
    }

    public WidgetStateChangeResponse notifyOnLoading(WidgetRuntimeInstance widgetRuntimeInstance) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        String method = "notifyOnLoading";
        logInfo(method, "...");

        return notifyWidget(widgetRuntimeInstance,
                widgetRuntimeInstance.getWidgetRuntimeDefinition().getOnLoadingMethod());
    }

    public WidgetStateChangeResponse notifyOnReady(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "notifyOnReady";
        logInfo(method, "...");
        logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

        return notifyOnReady(getRuntimeInstance(widgetRuntimeId));
    }

    public WidgetStateChangeResponse notifyOnReady(WidgetRuntimeInstance widgetRuntimeInstance) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        String method = "notifyOnReady";
        logInfo(method, "...");

        return notifyWidget(widgetRuntimeInstance,
                widgetRuntimeInstance.getWidgetRuntimeDefinition().getOnReadyMethod());
    }

    public WidgetStateChangeResponse notifyOnRunning(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "notifyOnRunning";
        logInfo(method, "...");
        logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

        return notifyOnRunning(getRuntimeInstance(widgetRuntimeId));
    }

    public WidgetStateChangeResponse notifyOnRunning(WidgetRuntimeInstance widgetRuntimeInstance) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        String method = "notifyOnRunning";
        logInfo(method, "...");

        return notifyWidget(widgetRuntimeInstance,
                widgetRuntimeInstance.getWidgetRuntimeDefinition().getOnRunningMethod());
    }

    public WidgetStateChangeResponse notifyOnStopped(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "notifyOnStopped";
        logInfo(method, "...");
        logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

        return notifyOnStopped(getRuntimeInstance(widgetRuntimeId));
    }

    public WidgetStateChangeResponse notifyOnStopped(WidgetRuntimeInstance widgetRuntimeInstance) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        String method = "notifyOnStopped";
        logInfo(method, "...");

        return notifyWidget(widgetRuntimeInstance,
                widgetRuntimeInstance.getWidgetRuntimeDefinition().getOnStoppedMethod());
    }

    public WidgetStateChangeResponse notifyOnError(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "notifyOnError";
        logInfo(method, "...");
        logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

        return notifyOnError(getRuntimeInstance(widgetRuntimeId));
    }

    public WidgetStateChangeResponse notifyOnError(WidgetRuntimeInstance widgetRuntimeInstance) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        String method = "notifyOnError";
        logInfo(method, "...");

        return notifyWidget(widgetRuntimeInstance,
                widgetRuntimeInstance.getWidgetRuntimeDefinition().getOnErrorMethod());
    }

    public WidgetStateChangeResponse notifyOnCorrection(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "notifyOnCorrection";
        logInfo(method, "...");
        logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

        return notifyOnCorrection(getRuntimeInstance(widgetRuntimeId));
    }

    public WidgetStateChangeResponse notifyOnCorrection(WidgetRuntimeInstance widgetRuntimeInstance) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        String method = "notifyOnCorrection";
        logInfo(method, "...");

        return notifyWidget(widgetRuntimeInstance,
                widgetRuntimeInstance.getWidgetRuntimeDefinition().getOnCorrectionMethod());
    }

    public WidgetStateChangeResponse notifyOnDestroyed(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "notifyOnDestroyed";
        logInfo(method, "...");
        logDebug(method, "widgetRuntimeId: %s", widgetRuntimeId);

        return notifyOnDestroyed(getRuntimeInstance(widgetRuntimeId));
    }

    public WidgetStateChangeResponse notifyOnDestroyed(WidgetRuntimeInstance widgetRuntimeInstance) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        String method = "notifyOnDestroyed";
        logInfo(method, "...");

        return notifyWidget(widgetRuntimeInstance,
                widgetRuntimeInstance.getWidgetRuntimeDefinition().getOnDestroyMethod());
    }

    private WidgetStateChangeResponse notifyWidget(WidgetRuntimeInstance widgetRuntimeInstance, Method methodToInvoke) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");
        Assert.notNull(methodToInvoke, "methodToInvoke is null");

        return notifyWidget(widgetRuntimeInstance.getInstance(), widgetRuntimeInstance.getWidgetRuntimeDefinition(),
                methodToInvoke);
    }

    private WidgetStateChangeResponse notifyWidget(Object instance, WidgetRuntimeDefinition widgetRuntimeDefinition,
            Method methodToInvoke) {
        Assert.notNull(instance, "instance is null");
        Assert.notNull(widgetRuntimeDefinition, "widgetRuntimeDefinition is null");
        Assert.notNull(methodToInvoke, "methodToInvoke is null");

        String method = "notifyWidget";
        logInfo(method, "...");
        logDebug(method, "notifying method: %s, widget: %s", methodToInvoke.getName(),
                widgetRuntimeDefinition.getWidgetName());

        WidgetStateChangeResponse response = new WidgetStateChangeResponse()
                .withWidgetId(widgetRuntimeDefinition.getId());

        try {
            methodToInvoke.invoke(instance);
            response.withResult(true);
        } catch (Throwable t) {
            t.printStackTrace();
            logError(method, "Failed to call method! method: %s, message: %s", methodToInvoke.getName(),
                    t.getMessage());
            response.withResult(false).withMessage("Failed to call method! method: " + methodToInvoke.getName())
                    .withError(t);
        }

        return response;
    }

    private WidgetRuntimeInstance initializeWidgetRuntimeInstance(String parentRuntimeId, Widget widget,
            UserRuntimeInstance userRuntime) {
        Assert.notNull(widget, "widget is null");
        Assert.notNull(userRuntime, "userRuntime is null");

        String method = "initializeWidgetRuntimeInstance";
        logInfo(method, "...");

        WidgetType widgetType = widgetTypeService.getWidgetTypeRepository().findById(widget.getWidgetTypeId())
                .orElse(null);
        Assert.notNull(widgetType, "WidgetType not found! widgetTypeId=" + widget.getWidgetTypeId());

        WidgetRuntimeDefinition widgetRuntimeDefinition = widgetScanner
                .getWidgetRuntimeDefinitionById(widgetType.getId());
        Assert.notNull(widgetRuntimeDefinition,
                "WidgetRuntimeDefinition not found! widgetTypeId=" + widgetType.getId());

        WidgetRuntimeInstance widgetRuntimeInstance = null;
        try {
            Object widgetClassInstance = widgetRuntimeDefinition.getWidgetClass().newInstance();
            beanFactory.autowireBean(widgetClassInstance);

            // data provider instances
            List<DataProviderInstance> dataProviderInstances = widgetRuntimeDefinition.getDataProviders().stream()
                    .map(dp -> new DataProviderInstance(userRuntime, dp, widgetClassInstance, beanFactory,
                            applicationContext, dataProviderScanner.getDataProviders()))
                    .collect(Collectors.toList());

            // create runtime widget id
            String runtimeWidgetId = createRuntimeInstanceId();

            // @formatter:off
			widgetRuntimeInstance = new WidgetRuntimeInstance()
					.withWidgetRuntimeId(runtimeWidgetId)
					.withWidgetId(widget.getId())
					.withParentRuntimeId(parentRuntimeId)
					.withParentId(widget.getParentId())
			        .withName(widget.getName())
			        .withDescription(widget.getDescription())
			        .withLayout(widget.getLayoutValue())
			        .withWidgetClassName(widgetRuntimeDefinition.getWidgetClassName())
			        .withInstance(widgetClassInstance)
			        .withWidgetRuntimeDefinition(widgetRuntimeDefinition)
			        .withDataProviders(dataProviderInstances)
			        .withUserRuntime(userRuntime);
			// @formatter:on
        } catch (BeansException e) {
            logError(method, e);
            throw new AcsLogicalException("Unable to create widgetRuntimeInstance!", e);
        } catch (InstantiationException e) {
            logError(method, e);
            throw new AcsLogicalException("Unable to create widgetRuntimeInstance!", e);
        } catch (IllegalAccessException e) {
            logError(method, e);
            throw new AcsLogicalException("Unable to create widgetRuntimeInstance!", e);
        }

        return widgetRuntimeInstance;
    }

    private ConfigurationManager initializeConfigurationManager(WidgetRuntimeInstance widgetRuntimeInstance,
            WidgetRuntimeConfigurationPatch configurationPatch) {
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        String method = "initializeConfigurationManager";
        logInfo(method, "...");

        ConfigurationManager configurationManager = null;
        try {
            configurationManager = new ConfigurationManager();
            beanFactory.autowireBean(configurationManager);
            configurationManager.init(widgetRuntimeInstance, configurationPatch);
        } catch (Throwable t) {
            logError(method, "Failed to initialize configuration manager for widget! widgetId: %s, error: %s",
                    widgetRuntimeInstance.getWidgetRuntimeId(), t);
            configurationManager = null;
        }

        return configurationManager;
    }

    public WidgetRuntimeInstance getWidgetRuntimeInstance(String widgetRuntimeId) {
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        if (!runtimeInstanceExists(widgetRuntimeId))
            return null;

        return getRuntimeInstance(widgetRuntimeId);
    }
}
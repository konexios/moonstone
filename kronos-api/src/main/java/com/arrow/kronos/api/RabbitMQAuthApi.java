package com.arrow.kronos.api;

import java.time.Instant;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.Gateway;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.Subscription;

import moonstone.acs.AcsLogicalException;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/kronos/rabbitmq-auth")
public class RabbitMQAuthApi extends BaseApiAbstract {
    private static final String MQTT_SUBSCRIPTION_FORMAT = "mqtt-subscription-%s";
    private final static String ALLOW = "allow";
    private final static String DENY = "deny";

    // resource types
    private final static String RESOURCE_TYPE_TOPIC = "topic";
    private final static String RESOURCE_TYPE_QUEUE = "queue";
    private final static String RESOURCE_TYPE_EXCHANGE = "exchange";

    private final static String DEFAULT_MQTT_TOPIC = "amq.topic";

    @ApiIgnore
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(@RequestParam("username") String username, @RequestParam("password") String password) {
        String method = "user";
        logInfo(method, "username: %s", username);
        try {
            validate(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password), "username & password are empty");

            Gateway gateway = getKronosCache().findGatewayByHid(username);
            if (gateway != null) {
                // validate gateway
                validate(gateway.isEnabled(), String.format("gateway is disabled, hid: %s", gateway.getHid()));

                // validate application
                Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
                validate(application != null && application.isEnabled(),
                        String.format("gateway's application is not found or disabled, hid: %s", gateway.getHid()));

                // validate subscription
                Subscription subscription = getCoreCacheService().findSubscriptionById(application.getSubscriptionId());
                validate(
                        subscription != null && subscription.isEnabled()
                                && subscription.getEndDate().isAfter(Instant.now()),
                        String.format("gateway's subscription is not found, disabled, or expired, hid: %s",
                                gateway.getHid()));

                // validate Company
                Company company = getCoreCacheService().findCompanyById(application.getCompanyId());
                validate(company != null && company.getStatus() == CompanyStatus.Active,
                        String.format("gateway's company not found or not active, hid: %s", gateway.getHid()));

                // validate accessKey
                AccessKey accessKey = getCoreCacheService()
                        .findAccessKeyByHashedApiKey(getCryptoService().getCrypto().internalHash(password));
                validate(accessKey != null,
                        String.format("gateway login failed, invalid apiKey, hid: %s", gateway.getHid()));

                validate(gateway.getApplicationId().equals(accessKey.getApplicationId()),
                        String.format("gateway login failed, application mismatched, hid: %s", gateway.getHid()));

                validate(accessKey.canWrite(application) || accessKey.canWrite(gateway),
                        String.format("gateway login failed, key has no write access, gatewayHid: %s, accessKeyHid:",
                                gateway.getHid(), accessKey.getHid()));

                logDebug(method, "---> gateway login success, hid: %s", gateway.getHid());
                return ALLOW;
            } else {
                Application application = getCoreCacheService().findApplicationByHid(username);
                validate(application != null, String.format("invalid hid: %s", username));

                // validate application
                validate(application.isEnabled(),
                        String.format("application is disabled, hid: %s", application.getHid()));

                // validate subscription
                Subscription subscription = getCoreCacheService().findSubscriptionById(application.getSubscriptionId());
                validate(
                        subscription != null && subscription.isEnabled()
                                && subscription.getEndDate().isAfter(Instant.now()),
                        String.format("application's subscription is not found, disabled, or expired, hid: %s",
                                application.getHid()));

                // validate Company
                Company company = getCoreCacheService().findCompanyById(application.getCompanyId());
                validate(company != null && company.getStatus() == CompanyStatus.Active,
                        String.format("application's company not found or not active, hid: %s", application.getHid()));

                // validate accessKey
                AccessKey accessKey = getCoreCacheService()
                        .findAccessKeyByHashedApiKey(getCryptoService().getCrypto().internalHash(password));
                validate(accessKey != null,
                        String.format("application login failed, invalid apiKey, name: %s", application.getName()));

                validate(application.getId().equals(accessKey.getApplicationId()),
                        String.format("application login failed, application mismatched, id1: %s, id2: %s",
                                application.getId(), accessKey.getApplicationId()));

                validate(accessKey.canWrite(application),
                        String.format(
                                "gateway login failed, key has no write access, applicationHid: %s, accessKeyHid:",
                                application.getHid(), accessKey.getHid()));

                logDebug(method, "---> application login success, name: %s", application.getName());
                return ALLOW;
            }
        } catch (AcsLogicalException e) {
            return DENY;
        } catch (Exception e) {
            logError(method, "error processing request", e);
            return DENY;
        }
    }

    @ApiIgnore
    @RequestMapping(value = "/vhost", method = RequestMethod.GET)
    public String vhost(@RequestParam("username") String username, @RequestParam("vhost") String vhost) {
        // TODO no validation at this time
        return ALLOW;
    }

    @ApiIgnore
    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    public String resource(@RequestParam("username") String username, @RequestParam("vhost") String vhost,
            @RequestParam("resource") String resource, @RequestParam("name") String name,
            @RequestParam("permission") String permission) {
        String method = "resource";
        logInfo(method, "resource: %s, username: %s, name: %s, permission: %s", resource, username, name, permission);
        try {
            validateResource(username, resource, name, permission, null);
            logDebug(method, "---> resource GRANTED for username: %s", username);
            return ALLOW;
        } catch (AcsLogicalException e) {
            return DENY;
        } catch (Exception e) {
            logError(method, "error processing request", e);
            return DENY;
        }
    }

    @ApiIgnore
    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    public String topic(@RequestParam("username") String username, @RequestParam("vhost") String vhost,
            @RequestParam("resource") String resource, @RequestParam("name") String name,
            @RequestParam("permission") String permission, @RequestParam("routing_key") String routingKey) {
        String method = "topic";
        logInfo(method, "resource: %s, username: %s, name: %s, permission: %s, routingKey: %s", resource, username,
                name, permission, routingKey);
        try {
            validateResource(username, resource, name, permission, routingKey);
            logDebug(method, "---> topic GRANTED for username: %s", username);
            return ALLOW;
        } catch (AcsLogicalException e) {
            return DENY;
        } catch (Exception e) {
            logError(method, "error processing request", e);
            return DENY;
        }
    }

    private void validateResource(String username, String resource, String name, String permission, String routingKey)
            throws ExecutionException {
        String method = "validateResource";
        if (resource.equals(RESOURCE_TYPE_TOPIC)) {
            // backward compatible
            if (routingKey == null) {
                validate(name.endsWith(username),
                        String.format("topic not allowed: username: %s, name: %s", username, name));
            } else {
                validate(routingKey.endsWith(username),
                        String.format("topic not allowed: username: %s, name: %s", username, name));
            }
        } else if (resource.equals(RESOURCE_TYPE_QUEUE)) {
            String queueName = String.format(MQTT_SUBSCRIPTION_FORMAT, username);
            if (!name.startsWith(queueName)) {
                logWarn(method, String.format("queue name mismatched: username: %s, name: %s", username, name));
            }
        } else if (resource.equals(RESOURCE_TYPE_EXCHANGE)) {
            validate(name.equals(DEFAULT_MQTT_TOPIC),
                    String.format("exchange not allowed: username: %s, name: %s", username, name));
        } else {
            validate(false, String.format("resource type not allowed: username: %s, resource: %s", username, resource));
        }
        // TODO permission is not validated at this time
    }

    private void validate(boolean check, String message) {
        String method = "validate";
        if (!check) {
            logInfo(method, String.format("*** ACCESS DENIED: %s", message));
            throw new AcsLogicalException(message);
        }
    }
}
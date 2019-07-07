package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.AccessKeyModel;
import com.arrow.acs.client.model.ApplicationModel;
import com.arrow.acs.client.model.CreateSubscriptionModel;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.acs.client.model.SubscriptionModel;
import com.arrow.acs.client.model.UpdateSubscriptionModel;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Subscription;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController(value = "pegasusSubscriptionApi")
@RequestMapping("/api/v1/pegasus/subscriptions")
public class SubscriptionApi extends BaseApiAbstract {

    @RequestMapping(path = "/{hid}", method = RequestMethod.GET)
    public SubscriptionModel findByHid(@PathVariable String hid) {
        validateCanReadSubscriptionWithHid(hid);
        Subscription subscription = getCoreCacheService().findSubscriptionByHid(hid);
        return toSubscriptionModel(subscription);
    }

    @RequestMapping(path = "/{hid}/applications", method = RequestMethod.GET)
    public ListResultModel<ApplicationModel> findApplicationsByHid(@PathVariable String hid, 
            @RequestParam(name = "includeDisabled", required = false, defaultValue = "false") boolean includeDisabled) {

        validateCanReadSubscriptionWithHid(hid);
        Subscription subscription = getCoreCacheService().findSubscriptionByHid(hid);
        List<Application> applications = includeDisabled 
                ? getApplicationService().getApplicationRepository().findBySubscriptionId(subscription.getId()) 
                : getApplicationService().getApplicationRepository().findBySubscriptionIdAndEnabled(subscription.getId(), true);
        List<ApplicationModel> data = new ArrayList<>();
        applications.forEach(application -> {
            data.add(toApplicationModel(application));
        });
        return new ListResultModel<ApplicationModel>().withSize(data.size()).withData(data);
    }

    @RequestMapping(path = "/{hid}/access-keys", method = RequestMethod.GET)
    public ListResultModel<AccessKeyModel> findAccessKeysByHid(@PathVariable String hid) {
        validateCanReadSubscriptionWithHid(hid);
        Subscription subscription = getCoreCacheService().findSubscriptionByHid(hid);
        List<AccessKeyModel> data = new ArrayList<>();
        getAccessKeyService().getAccessKeyRepository().findByPri(subscription.getPri()).forEach(key -> {
            data.add(toAccessKeyModel(key));
        });
        return new ListResultModel<AccessKeyModel>().withSize(data.size()).withData(data);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ListResultModel<SubscriptionModel> findBy(
            @RequestParam(name = "companyHid", required = false) String companyHid, 
            @RequestParam(name = "includeDisabled", required = false, defaultValue = "false") boolean includeDisabled) {

        AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
        Company company = null;
        if (companyHid != null) {
            company = getCoreCacheService().findCompanyByHid(companyHid);
        } else {
            company = accessKey.getRefCompany();
        }
        Assert.notNull(company, "company not found");
        List<Subscription> subscriptions = includeDisabled 
                ? getSubscriptionService().getSubscriptionRepository().findByCompanyId(company.getId()) 
                : getSubscriptionService().getSubscriptionRepository().findByCompanyIdAndEnabled(company.getId(), true);
        List<SubscriptionModel> data = new ArrayList<>();
        for (Subscription subscription: subscriptions) {
            if (hasPrivilege(accessKey::canRead, company) || accessKey.canRead(subscription)) {
                data.add(toSubscriptionModel(subscription));
            }
        }
        return new ListResultModel<SubscriptionModel>().withSize(data.size()).withData(data);
    }

    @ApiOperation(value = "create subscription")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public HidModel createSubscription(
            @ApiParam(value = "create subscription model", required = true) @RequestBody(required = false) CreateSubscriptionModel body) {

        CreateSubscriptionModel model = JsonUtils.fromJson(getApiPayload(), CreateSubscriptionModel.class);
        Assert.notNull(model, "model is null");

        AccessKey accessKey = validateCanCreateSubscription(model.getCompanyHid());
        Subscription subscription = getSubscriptionService().create(fromCreateSubscriptionModel(model),
                accessKey.getId());
        return new HidModel().withHid(subscription.getHid()).withMessage("OK");
    }

    @ApiOperation(value = "update subscription")
    @RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
    public HidModel updateSubscription(
            @ApiParam(value = "subscription hid", required = true) @PathVariable(value = "hid", required = true) String hid,
            @ApiParam(value = "update subscription model", required = true) @RequestBody(required = false) UpdateSubscriptionModel body) {

        UpdateSubscriptionModel model = JsonUtils.fromJson(getApiPayload(), UpdateSubscriptionModel.class);
        Assert.notNull(model, "model is null");

        AccessKey accessKey = validateCanWriteSubscriptionWithHid(hid);

        Subscription subscription = getCoreCacheService().findSubscriptionByHid(hid);
        Assert.notNull(subscription, "subscription is not found");

        subscription = getSubscriptionService().update(populateSubscription(subscription, model), accessKey.getId());
        return new HidModel().withHid(subscription.getHid()).withMessage("OK");
    }

    AccessKey validateCanReadSubscriptionWithHid(String hid) {
        String method = "validateCanReadSubscriptionWithHid";
        Subscription subscription = getCoreCacheService().findSubscriptionByHid(hid);
        Assert.notNull(subscription, "invalid subscription hid");
        AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
        if (!StringUtils.equals(accessKey.getCompanyId(), subscription.getCompanyId())) {
            logWarn(method, "companyId mismatched!");
        }
        Company company = getCoreCacheService().findCompanyById(subscription.getCompanyId());
        Assert.notNull(company, "company not found");
        if (!hasPrivilege(accessKey::canRead, company) && !accessKey.canRead(subscription)) {
            throw new NotAuthorizedException();
        }
        return accessKey;
    }

    AccessKey validateCanWriteSubscriptionWithHid(String hid) {
        String method = "validateCanWriteSubscriptionWithHid";
        Subscription subscription = getCoreCacheService().findSubscriptionByHid(hid);
        Assert.notNull(subscription, "invalid subscription hid");
        AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
        if (!StringUtils.equals(accessKey.getCompanyId(), subscription.getCompanyId())) {
            logWarn(method, "companyId mismatched!");
        }
        Company company = getCoreCacheService().findCompanyById(subscription.getCompanyId());
        Assert.notNull(company, "company is not found");
        if (!hasPrivilege(accessKey::canWrite, company) && !accessKey.canWrite(subscription)) {
            throw new NotAuthorizedException();
        }
        return accessKey;
    }

    AccessKey validateCanCreateSubscription(String companyHid) {
        String method = "validateCanCreateSubscription";
        Assert.hasText(companyHid, "companyHid is empty");
        AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
        Company company = getCoreCacheService().findCompanyByHid(companyHid);
        Assert.notNull(company, "company is not found");
        if (!StringUtils.equals(accessKey.getCompanyId(), company.getId())) {
            logWarn(method, "companyId mismatched!");
        }
        if (!hasPrivilege(accessKey::canWrite, company)) {
            throw new NotAuthorizedException();
        }
        return accessKey;
    }
}

package com.arrow.pegasus.dashboard.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.dashboard.data.PageProperty;
import com.arrow.pegasus.dashboard.data.PropertyValue;
import com.arrow.pegasus.dashboard.repo.PagePropertyRepository;
import com.arrow.pegasus.dashboard.repo.PropertyValueSearchParams;

import moonstone.acs.AcsLogicalException;

@Service
public class PagePropertyService extends DashboardServiceAbstract {

    @Autowired
    private PagePropertyRepository pagePropertyRepository;
    @Autowired
    private PropertyValueService propertyValueService;

    public PagePropertyService() {
        super();
    }

    public PagePropertyRepository getPagePropertyRepository() {
        return pagePropertyRepository;
    }

    public PageProperty create(PageProperty pageProperty, String who) {
        Assert.notNull(pageProperty, "pageProperty is null");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logDebug(method, "...");

        if (StringUtils.isEmpty(pageProperty.getConfigurationPageId())) {
            logDebug(method, "configurationPageId is empty");
            throw new AcsLogicalException("configurationPageId is empty");
        }

        if (StringUtils.isEmpty(pageProperty.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        if (StringUtils.isEmpty(pageProperty.getLabel())) {
            logDebug(method, "label is empty");
            throw new AcsLogicalException("label is empty");
        }

        pageProperty = pagePropertyRepository.doInsert(pageProperty, who);

        logDebug(method, "pageProperty: %s has been created for configurationPage: %s", pageProperty.getId(),
                pageProperty.getConfigurationPageId());

        return pageProperty;
    }

    public PageProperty update(PageProperty pageProperty, String who) {
        Assert.notNull(pageProperty, "PageProperty is null");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logDebug(method, "...");

        if (StringUtils.isEmpty(pageProperty.getConfigurationPageId())) {
            logDebug(method, "configurationPageId is empty");
            throw new AcsLogicalException("configurationPageId is empty");
        }

        if (StringUtils.isEmpty(pageProperty.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        if (StringUtils.isEmpty(pageProperty.getLabel())) {
            logDebug(method, "label is empty");
            throw new AcsLogicalException("label is empty");
        }

        pageProperty = pagePropertyRepository.doSave(pageProperty, who);

        logDebug(method, "pageProperty: %s has been updated for configurationPage: %s", pageProperty.getId(),
                pageProperty.getConfigurationPageId());

        return pageProperty;
    }

    public void delete(String pagePropertyId) {
        Assert.hasText(pagePropertyId, "pagePropertyId is empty");

        String method = "delete";
        logDebug(method, "...");

        PageProperty pageProperty = pagePropertyRepository.findById(pagePropertyId).orElse(null);
        Assert.notNull(pageProperty, "Page Property not found! pagePropertyId: " + pagePropertyId);

        // property values
        PropertyValueSearchParams propertyValueSearchParams = new PropertyValueSearchParams();
        propertyValueSearchParams.addPagePropertyIds(pageProperty.getId());
        for (PropertyValue propertyValue : propertyValueService.getPropertyValueRepository()
                .findPropertyValues(propertyValueSearchParams))
            propertyValueService.delete(propertyValue.getId());

        // page property
        pagePropertyRepository.delete(pageProperty);

        logDebug(method, "pageProperty: %s has been deleted from configurationPage: %s", pagePropertyId,
                pageProperty.getConfigurationPageId());
    }

}

package com.arrow.pegasus.dashboard.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.dashboard.data.PropertyValue;
import com.arrow.pegasus.dashboard.repo.PropertyValueRepository;

@Service
public class PropertyValueService extends DashboardServiceAbstract {

    @Autowired
    private PropertyValueRepository propertyValueRepository;

    public PropertyValueService() {
        super();
    }

    public PropertyValueRepository getPropertyValueRepository() {
        return propertyValueRepository;
    }

    public PropertyValue create(PropertyValue propertyValue, String who) {
        Assert.notNull(propertyValue, "propertyValue is null");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logDebug(method, "...");

        if (StringUtils.isEmpty(propertyValue.getPagePropertyId())) {
            logDebug(method, "pagePropertyId is empty");
            throw new AcsLogicalException("pagePropertyId is empty");
        }

        propertyValue = propertyValueRepository.doInsert(propertyValue, who);

        logDebug(method, "propertyValue: %s has been created for pageProperty: %s", propertyValue.getId(),
                propertyValue.getPagePropertyId());

        return propertyValue;
    }

    public PropertyValue update(PropertyValue propertyValue, String who) {
        Assert.notNull(propertyValue, "PropertyValue is null");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logDebug(method, "...");

        if (StringUtils.isEmpty(propertyValue.getPagePropertyId())) {
            logDebug(method, "pagePropertyId is empty");
            throw new AcsLogicalException("pagePropertyId is empty");
        }

        propertyValue = propertyValueRepository.doSave(propertyValue, who);

        logDebug(method, "propertyValue: %s has been updated for pageProperty: %s", propertyValue.getId(),
                propertyValue.getPagePropertyId());

        return propertyValue;
    }

    public void delete(String propertyValueId) {
        Assert.hasText(propertyValueId, "propertyValueId is empty");

        String method = "delete";
        logDebug(method, "...");

        PropertyValue propertyValue = propertyValueRepository.findById(propertyValueId).orElse(null);
        Assert.notNull(propertyValue, "Property Value not found! propertyValueId: " + propertyValueId);

        propertyValueRepository.delete(propertyValue);

        logDebug(method, "propertyValue: %s has been deleted from pageProperty: %s", propertyValueId,
                propertyValue.getPagePropertyId());
    }

}

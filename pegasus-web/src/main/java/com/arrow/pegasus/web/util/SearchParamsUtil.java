package com.arrow.pegasus.web.util;

import com.arrow.pegasus.repo.params.AccessKeySearchParams;
import com.arrow.pegasus.web.model.SearchFilterModels;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * UtilMethods are building and configuring SearchParams objects
 */
@Component
public class SearchParamsUtil {

    public AccessKeySearchParams buildAccessKeyParams(SearchFilterModels.AccessKeySearchFilter searchFilter) {

        AccessKeySearchParams params = new AccessKeySearchParams();

        if (StringUtils.isNotEmpty(searchFilter.getName())) {
            params.setName(searchFilter.getName());
        }
        if (searchFilter.getAccessLevels() != null) {
            params.addAccessLevels(searchFilter.getAccessLevels());
        }
        if (searchFilter.getExpirationDateFrom() != null) {
            params.setExpirationDateFrom(Instant.ofEpochMilli(searchFilter.getExpirationDateFrom()));
        }
        if (searchFilter.getExpirationDateTo() != null) {
            params.setExpirationDateTo(Instant.ofEpochMilli(searchFilter.getExpirationDateTo()));
        }

        return params;
    }
}

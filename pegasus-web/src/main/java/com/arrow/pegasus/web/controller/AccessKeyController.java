package com.arrow.pegasus.web.controller;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.web.model.EntityPri;

@RestController
@RequestMapping("/api/pegasus/accesskey")
public class AccessKeyController extends PegasusControllerAbstract {

    @RequestMapping(value = "/{type}/{id}/pri", method = RequestMethod.GET)
    public EntityPri getPriForType(@PathVariable String type, @PathVariable String id) {

    	String pri = null;
    	
    	switch(type)
    	{
    	case "company":
    		Company company = getCoreCacheService().findCompanyById(id);
    		Assert.notNull(company, "Company not found! id=" + id);
    		
    		pri = company.getPri();
    		break;
    	case "subscription":
    		Subscription subscription = getCoreCacheService().findSubscriptionById(id);
    		Assert.notNull(subscription, "Subscription not found! id=" + id);
    		
    		pri = subscription.getPri();
    		break;
    	case "application":
    		Application application = getCoreCacheService().findApplicationById(id);
    		Assert.notNull(application, "Application not found! id=" + id);
    		
    		pri = application.getPri();
    		break;
    	}
    	
    	return new EntityPri(pri);
    }
}
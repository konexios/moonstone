package com.arrow.pegasus.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.profile.Product;

public class ProductRepositoryExtensionImpl extends RepositoryExtensionAbstract<Product>
        implements ProductRepositoryExtension {

    public ProductRepositoryExtensionImpl() {
        super(Product.class);
    }

    @Override
    public List<Product> findParentProducts(Boolean hidden, Boolean enabled) {
        String methodName = "findParentProducts";
        logInfo(methodName, "hidden: %s, enabled: %s", hidden, enabled);
        List<Criteria> criteria = new ArrayList<Criteria>();
        criteria.add(Criteria.where("parentProductId").is(null));
        if (hidden != null)
            criteria = addCriteria(criteria, "hidden", hidden);
        if (enabled != null)
            criteria = addCriteria(criteria, "enabled", enabled);

        return doFind(doProcessCriteria(criteria));
    }

    @Override
    public List<Product> findProductExtensions(Boolean hidden, Boolean enabled) {
        String methodName = "findProductExtensions";
        logInfo(methodName, "...");

        List<Criteria> criteria = new ArrayList<Criteria>();

        // not equal
        criteria.add(Criteria.where("parentProductId").ne(null));

        if (hidden != null)
            criteria = addCriteria(criteria, "hidden", hidden);
        if (enabled != null)
            criteria = addCriteria(criteria, "enabled", enabled);

        return doFind(doProcessCriteria(criteria));
    }

    @Override
    public List<Product> findProductExtensions(String parentProductId, Boolean hidden, Boolean enabled) {
        String methodName = "findProductExtensions";
        logInfo(methodName, "...");

        // List<Criteria> criteria = new ArrayList<Criteria>();
        // criteria = addCriteria(criteria, "parentProductId", parentProductId);
        // if (hidden != null)
        // criteria = addCriteria(criteria, "hidden", hidden);
        // if (enabled != null)
        // criteria = addCriteria(criteria, "enabled", enabled);
        //
        // return doFind(doProcessCriteria(criteria));

        ProductSearchParams params = new ProductSearchParams();
        params.addParentProductIds(parentProductId);
        params.setEnabled(enabled);
        params.setHidden(hidden);

        return doProcessQuery(buildCriteria(params));
    }

    @Override
    public List<Product> findProducts(ProductSearchParams params) {
        Assert.notNull(params, "params is null");

        String method = "findProducts";
        logInfo(method, "...");

        return doProcessQuery(buildCriteria(params));
    }

    private List<Criteria> buildCriteria(ProductSearchParams params) {

        List<Criteria> criteria = new ArrayList<>();
        criteria = addCriteria(criteria, "_id", params.getIds());
        criteria = addCriteria(criteria, "hid", params.getHids());
        criteria = addCriteria(criteria, "createdBy", params.getCreatedBys());
        criteria = addCriteria(criteria, "lastModifiedBy", params.getLastModifiedBys());
        criteria = addCriteria(criteria, "enabled", params.getEnabled());
        criteria = addCriteria(criteria, "apiSigningRequired", params.getApiSigningRequired());
        criteria = addCriteria(criteria, "parentProductId", params.getParentProductIds());
        criteria = addCriteria(criteria, "hidden", params.getHidden());

        if (!StringUtils.isEmpty(params.getName()))
            criteria.add(Criteria.where("name").regex(params.getName(), "i"));

        if (!StringUtils.isEmpty(params.getSystemName()))
            criteria.add(Criteria.where("sysytemName").regex(params.getSystemName(), "i"));

        return criteria;
    }
}

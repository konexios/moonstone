package com.arrow.rhea.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareVendor;
import com.arrow.rhea.repo.SoftwareProductSearchParams;
import com.arrow.rhea.service.SoftwareProductService;
import com.arrow.rhea.service.SoftwareVendorService;
import com.arrow.rhea.web.model.SearchFilterModels.SoftwareProductSearchFilterModel;
import com.arrow.rhea.web.model.SearchResultModels.SoftwareProductSearchResultModel;
import com.arrow.rhea.web.model.SoftwareProductModels.SoftwareProductModel;
import com.arrow.rhea.web.model.SoftwareVendorModels;
import com.arrow.rhea.web.model.SoftwareVendorModels.SoftwareVendorOption;

@RestController
@RequestMapping("/api/rhea/software-products")
public class SoftwareProductController extends ControllerAbstract {

    @Autowired
    private SoftwareProductService softwareProductService;

    @Autowired
    private SoftwareVendorService softwareVendorService;

    @PreAuthorize("hasAuthority('RHEA_READ_SOFTWARE_PRODUCTS')")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public SoftwareProductSearchResultModel find(@RequestBody SoftwareProductSearchFilterModel searchFilter) {

        // sorting & paging
        PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
                new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

        SoftwareProductSearchParams params = new SoftwareProductSearchParams();
        // implied/enforced filter
        params.addCompanyIds(getAuthenticatedUser().getCompanyId());
        // user defined filter
        params.addSoftwareVendorIds(searchFilter.getsoftwareVendorIds());
        params.setEnabled(searchFilter.isEnabled());
        params.setEditable(searchFilter.isEditable());

        Page<SoftwareProduct> softwareProducts = softwareProductService.getSoftwareProductRepository()
                .findSoftwareProducts(pageRequest, params);

        // convert to visual model
        List<SoftwareProductModel> models = new ArrayList<>();
        for (SoftwareProduct softwareProduct : softwareProducts) {
            SoftwareVendor softwareVendor = getRheaCacheService()
                    .findSoftwareVendorById(softwareProduct.getSoftwareVendorId());
            Assert.notNull(softwareVendor,
                    "Unable to find softwareVendor! softwareVendorId=" + softwareProduct.getSoftwareVendorId());
            models.add(new SoftwareProductModel(softwareProduct,
                    new SoftwareVendorModels.SoftwareVendorOption(softwareVendor)));
        }

        Page<SoftwareProductModel> result = new PageImpl<>(models, pageRequest, softwareProducts.getTotalElements());

        return new SoftwareProductSearchResultModel(result, searchFilter);
    }

    @PreAuthorize("hasAuthority('RHEA_CREATE_SOFTWARE_PRODUCT')")
    @RequestMapping(method = RequestMethod.POST)
    public SoftwareProductModel create(@RequestBody SoftwareProductModel model) {
        Assert.notNull(model, "model is null");
        Assert.notNull(model.getSoftwareVendor(), "softwareVendor in model is null");

        SoftwareVendor softwareVendor = softwareVendorService.getSoftwareVendorRepository()
                .findById(model.getSoftwareVendor().getId()).orElse(null);
        Assert.notNull(softwareVendor,
                "Unable to find softwareVendor! softwareVendorId=" + model.getSoftwareVendor().getId());
        Assert.isTrue(StringUtils.equals(getAuthenticatedUser().getCompanyId(), softwareVendor.getCompanyId()),
                "user and softwareVendor must have the same companyId");

        // populate
        SoftwareProduct softwareProduct = new SoftwareProduct();
        softwareProduct.setCompanyId(getAuthenticatedUser().getCompanyId());
        softwareProduct.setSoftwareVendorId(softwareVendor.getId());
        softwareProduct.setName(model.getName());
        softwareProduct.setDescription(model.getDescription());
        softwareProduct.setEnabled(model.isEnabled());
        softwareProduct.setEditable(model.isEditable());

        // persist
        softwareProduct = softwareProductService.create(softwareProduct, getUserId());

        // lookup
        softwareProduct = softwareProductService.populateRefs(
                softwareProductService.getSoftwareProductRepository().findById(softwareProduct.getId()).orElse(null));

        return new SoftwareProductModel(softwareProduct,
                new SoftwareVendorModels.SoftwareVendorOption(softwareProduct.getRefSoftwareVendor()));
    }

    @PreAuthorize("hasAuthority('RHEA_UPDATE_SOFTWARE_PRODUCT')")
    @RequestMapping(value = "/{softwareProductId}", method = RequestMethod.PUT)
    public SoftwareProductModel update(@PathVariable String softwareProductId,
            @RequestBody SoftwareProductModel model) {
        Assert.hasText(softwareProductId, "softwareProductId is empty");
        Assert.notNull(model, "model is null");
        Assert.notNull(model.getSoftwareVendor().getId(), "softwareVendorId in model is null");

        SoftwareVendor softwareVendor = softwareVendorService.getSoftwareVendorRepository()
                .findById(model.getSoftwareVendor().getId()).orElse(null);
        Assert.notNull(softwareVendor,
                "Unable to find softwareVendor! softwareVendorId=" + model.getSoftwareVendor().getId());
        Assert.isTrue(StringUtils.equals(getAuthenticatedUser().getCompanyId(), softwareVendor.getCompanyId()),
                "user and softwareVendor must have the same companyId");

        SoftwareProduct softwareProduct = getRheaCacheService().findSoftwareProductById(softwareProductId);
        Assert.notNull(softwareProduct, "softwareProduct is null");
        Assert.isTrue(getAuthenticatedUser().getCompanyId().equals(softwareProduct.getCompanyId()),
                "user and softwareProduct must have the same companyId");

        // populate
        softwareProduct.setSoftwareVendorId(softwareVendor.getId());
        softwareProduct.setName(model.getName());
        softwareProduct.setDescription(model.getDescription());
        softwareProduct.setEnabled(model.isEnabled());
        softwareProduct.setEditable(model.isEditable());

        // persist
        softwareProduct = softwareProductService.update(softwareProduct, getUserId());

        // lookup
        softwareProduct = softwareProductService.populateRefs(
                softwareProductService.getSoftwareProductRepository().findById(softwareProduct.getId()).orElse(null));

        return new SoftwareProductModel(softwareProduct,
                new SoftwareVendorModels.SoftwareVendorOption(softwareProduct.getRefSoftwareVendor()));
    }

    @PreAuthorize("hasAuthority('RHEA_CREATE_SOFTWARE_PRODUCT') or hasAuthority('RHEA_UPDATE_SOFTWARE_PRODUCT')")
    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public List<SoftwareVendorOption> options() {

        List<SoftwareVendor> softwareVendors = softwareVendorService.getSoftwareVendorRepository()
                .findAllByCompanyIdAndEnabled(getAuthenticatedUser().getCompanyId(), true);

        List<SoftwareVendorOption> options = new ArrayList<>(softwareVendors.size());
        for (SoftwareVendor softwareVendor : softwareVendors) {
            options.add(new SoftwareVendorOption(softwareVendor));
        }
        options.sort(Comparator.comparing(SoftwareVendorOption::getName, String.CASE_INSENSITIVE_ORDER));

        return options;
    }
}

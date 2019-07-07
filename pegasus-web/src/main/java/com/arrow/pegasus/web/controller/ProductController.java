package com.arrow.pegasus.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.ConfigurationPropertyDataType;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.web.model.ConfigurationModel;
import com.arrow.pegasus.web.model.ProductModels;
import com.arrow.pegasus.web.model.SearchFilterModels;
import com.arrow.pegasus.web.model.SearchResultModels;

@RestController
@RequestMapping("/api/pegasus/products")
public class ProductController extends PegasusControllerAbstract {

	@PreAuthorize("hasAuthority('PEGASUS_ACCESS')")
	@RequestMapping(value = "/{enabled}/options", method = RequestMethod.GET)
	public List<ProductModels.ProductOption> options(@PathVariable Boolean enabled) {

		User authenticatedUser = getAuthenticatedUser();

		return getProductOptions(authenticatedUser.isAdmin());
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_PRODUCT')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.ProductSearchResult find(
			@RequestBody SearchFilterModels.ProductSearchFilter searchFilter) {

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		// lookup
		Page<Product> productPage = getProductService().getProductRepository().findAll(pageRequest);

		// convert to visual model
		Page<ProductModels.ProductList> result = null;
		List<ProductModels.ProductList> productModels = new ArrayList<>();
		for (Product product : productPage) {
			String parentProductName = null;
			if (!StringUtils.isEmpty(product.getParentProductId())) {
				Product parentProduct = getCoreCacheService().findProductById(product.getParentProductId());
				parentProductName = parentProduct.getName();
			}
			productModels.add(new ProductModels.ProductList(product, parentProductName));
		}
		result = new PageImpl<>(productModels, pageRequest, productPage.getTotalElements());

		return new SearchResultModels.ProductSearchResult(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_PRODUCT')")
	@RequestMapping(value = "/{id}/product")
	public ProductModels.ProductUpsert product(@PathVariable String id) {
		Assert.hasText(id, "id is null");

		Product product = new Product();
		if (!id.equalsIgnoreCase("new")) {
			product = getProductService().getProductRepository().findById(id).orElse(null);
			Assert.notNull(product, "Unable to find product! productId=" + id);
		}

		List<ProductModels.ProductOption> productOptions = getParentProductOptions(getAuthenticatedUser().isAdmin());
		List<ConfigurationModel> configurationModels = getProductConfigurationModel(product);

		return new ProductModels.ProductUpsert(new ProductModels.ProductModel(product, configurationModels),
				productOptions, ConfigurationPropertyCategory.values(), ConfigurationPropertyDataType.values());
	}

	private List<ConfigurationModel> getProductConfigurationModel(Product product) {
		List<ConfigurationModel> configurationModels = new ArrayList<>();
		for (ConfigurationProperty cp : product.getConfigurations()) {
			configurationModels.add(new ConfigurationModel(cp));
		}
		return configurationModels;
	}

	@PreAuthorize("hasAuthority('PEGASUS_CREATE_PRODUCT')")
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public ProductModels.ProductModel create(@RequestBody ProductModels.ProductModel model) {

		Assert.notNull(model, "product is null");

		Product product = getPegasusModelUtil().toProduct(model);

		product = getProductService().create(product, getUserId());

		return new ProductModels.ProductModel(product, null);
	}

	@PreAuthorize("hasAuthority('PEGASUS_UPDATE_PRODUCT')")
	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public ProductModels.ProductModel update(@RequestBody ProductModels.ProductModel model) {
		Assert.notNull(model, "product is null");

		Product product = getCoreCacheService().findProductById(model.getId());
		Assert.notNull(product, "product not found :: productId=[" + model.getId() + "]");

		product = getPegasusModelUtil().toProduct(model, product);

		product = getProductService().update(product, getUserId());

		List<ConfigurationModel> configurationModels = getProductConfigurationModel(product);

		return new ProductModels.ProductModel(product, configurationModels);
	}

	@PreAuthorize("hasAuthority('PEGASUS_READ_PRODUCT_APPLICATIONS')")
	@RequestMapping(value = "{id}/applications", method = RequestMethod.GET)
	public List<ProductModels.ApplicationModel> applications(@PathVariable String id) {
		Assert.hasText(id, "productId is null");

		Product product = getCoreCacheService().findProductById(id);
		Assert.notNull(product, "product not found :: productId=[" + id + "]");

		List<Application> applications = getApplicationService().getApplicationRepository().findByProductId(id);
		List<ProductModels.ApplicationModel> applicationModels = new ArrayList<>();

		applications.forEach(appl -> {
			appl = getCoreCacheHelper().populateApplication(appl);

			List<String> productExtensionNames = new ArrayList<>();
			appl.getRefProductExtensions().forEach(pe -> productExtensionNames.add(pe.getName()));

			applicationModels.add(new ProductModels.ApplicationModel().withId(appl.getId()).withHid(appl.getHid())
					.withName(appl.getName()).withDescription(appl.getDescription()).withCode(appl.getCode())
					.withEnabled(appl.isEnabled()).withProductName(appl.getRefProduct().getName())
					.withProductExtensionNames(productExtensionNames));
		});

		return applicationModels;
	}
}

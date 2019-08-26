package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.repo.ProductRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class ProductService extends BaseServiceAbstract {

	@Autowired
	private ProductRepository productRepository;

	public ProductRepository getProductRepository() {
		return productRepository;
	}

	public Product create(Product product, String who) {
		String method = "create";

		// logical checks
		if (product == null) {
			logInfo(method, "product is null");
			throw new AcsLogicalException("product is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		product = productRepository.doInsert(product, who);

		// TODO revisit, currently not calling cache because newly created
		// products do not affect any other objects

		return product;
	}

	public Product update(Product product, String who) {
		String method = "update";

		// logical checks
		if (product == null) {
			logInfo(method, "product is null");
			throw new AcsLogicalException("product is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		product = productRepository.doSave(product, who);

		// re-cache
		Product cachedProduct = getCoreCacheService().findProductById(product.getId());
		if (cachedProduct != null) {
			getCoreCacheService().clearProduct(cachedProduct);
		}

		return product;
	}
}

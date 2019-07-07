package com.arrow.pegasus.api;

import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.profile.Product;

@RestController(value = "localPegasusProductApi")
@RequestMapping("/api/v1/local/pegasus/products")
public class ProductApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<Product> findAll() {
		return getProductService().getProductRepository().findAll();
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public Product findById(@PathVariable(name = "id", required = true) String id) {
		Product product = getProductService().getProductRepository().findById(id).orElse(null);
		Assert.notNull(product, "product not found");
		return product;
	}
}
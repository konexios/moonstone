package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.profile.Product;

import moonstone.acs.client.model.ListResultModel;
import moonstone.acs.client.model.ProductModel;

@RestController(value = "pegasusProductApi")
@RequestMapping("/api/v1/pegasus/products")
public class ProductApi extends BaseApiAbstract {

    @RequestMapping(path = "/{hid}", method = RequestMethod.GET)
    public ProductModel findByHid(@PathVariable String hid) {
        getValidatedAccessKey(getProductSystemName());
        Product product = getCoreCacheService().findProductByHid(hid);
        Assert.notNull(product, "product is null");
        return toProductModel(product);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ListResultModel<ProductModel> findAll() {
        getValidatedAccessKey(getProductSystemName());
        List<ProductModel> data = new ArrayList<>();
        getProductService().getProductRepository().findAll().forEach(product -> {
            data.add(toProductModel(product));
        });
        return new ListResultModel<ProductModel>().withSize(data.size()).withData(data);
    }
}

package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.profile.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>, ProductRepositoryExtension {

    Product findByName(String name);

    Product findBySystemName(String systemName);

    List<Product> findByEnabled(boolean enabled);

    Long deleteByParentProductId(String parentProductId);
}

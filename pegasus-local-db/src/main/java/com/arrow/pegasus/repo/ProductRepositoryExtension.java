package com.arrow.pegasus.repo;

import java.util.List;

import com.arrow.pegasus.data.profile.Product;

public interface ProductRepositoryExtension extends RepositoryExtension<Product> {

	/**
	 * @param hidden
	 * @param enabled
	 * @return
	 */
	public List<Product> findParentProducts(Boolean hidden, Boolean enabled);

	/**
	 * @param hidden
	 * @param enable
	 * @return
	 */
	public List<Product> findProductExtensions(Boolean hidden, Boolean enable);

	/**
	 * @param parentProductId
	 * @param hidden
	 * @param enabled
	 * @return
	 */
	public List<Product> findProductExtensions(String parentProductId, Boolean hidden, Boolean enabled);

	/**
	 * @param params
	 * @return
	 */
	public List<Product> findProducts(ProductSearchParams params);
}

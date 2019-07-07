package com.arrow.pegasus.webapi.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.ProductFeature;

public class CoreProductModels {

	public static class ProductOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 2690480965507786470L;

		private List<ProductFeature> features = new ArrayList<>();

		public ProductOption(Product product) {
			super(product.getId(), product.getHid(), product.getName());
			features = product.getFeatures();
		}

		public List<ProductFeature> getFeatures() {
			return features;
		}
	}

	public static class ProductList extends CoreDocumentModel {
		private static final long serialVersionUID = -6744384459296954265L;

		private String systemName;
		private String name;
		private String description;
		private boolean enabled;
		private boolean apiSigningRequired;

		public ProductList(Product product) {
			super(product.getId(), product.getHid());
			this.systemName = product.getSystemName();
			this.name = product.getName();
			this.description = product.getDescription();
			this.enabled = product.isEnabled();
			this.apiSigningRequired = product.isApiSigningRequired();
		}

		public String getSystemName() {
			return systemName;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public boolean isApiSigningRequired() {
			return apiSigningRequired;
		}
	}

	public static class ProductModel extends CoreDocumentModel {
		private static final long serialVersionUID = -284522051270070228L;

		private String name;
		private String description;
		private String systemName;
		private boolean apiSigningRequired;
		private boolean enabled;

		// TODO configurations

		public ProductModel() {
			super(null, null);

			// TODO init configurations
		}

		public ProductModel(Product product) {
			super(product.getId(), product.getHid());
			this.name = product.getName();
			this.description = product.getDescription();
			this.systemName = product.getSystemName();
			this.apiSigningRequired = product.isApiSigningRequired();
			this.enabled = product.isEnabled();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getSystemName() {
			return systemName;
		}

		public boolean isApiSigningRequired() {
			return apiSigningRequired;
		}

		public boolean isEnabled() {
			return enabled;
		}
	}

	public static class ProductUpsert implements Serializable {
		private static final long serialVersionUID = -599225419139851418L;

		private ProductModel product;

		public ProductUpsert(ProductModel product) {
			this.product = product;
		}

		public ProductModel getProduct() {
			return product;
		}
	}
}

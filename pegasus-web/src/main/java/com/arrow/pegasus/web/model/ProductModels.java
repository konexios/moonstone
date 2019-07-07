package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.ConfigurationPropertyDataType;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.ProductFeature;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.CoreProductModels;

public class ProductModels extends CoreProductModels {

	public static class ProductList extends CoreDocumentModel {
		private static final long serialVersionUID = -6744384459296954265L;

		private String systemName;
		private String name;
		private String description;
		private boolean enabled;
		private boolean hidden;
		private boolean apiSigningRequired;
		private String parentProductName;

		public ProductList(Product product, String parentProductName) {
			super(product.getId(), product.getHid());
			this.systemName = product.getSystemName();
			this.name = product.getName();
			this.description = product.getDescription();
			this.enabled = product.isEnabled();
			this.hidden = product.isHidden();
			this.apiSigningRequired = product.isApiSigningRequired();
			this.parentProductName = parentProductName;
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

		public boolean isHidden() {
			return hidden;
		}

		public boolean isApiSigningRequired() {
			return apiSigningRequired;
		}

		public String getParentProductName() {
			return parentProductName;
		}
	}

	public static class ProductModel extends CoreDocumentModel {
		private static final long serialVersionUID = -284522051270070228L;

		private String name;
		private String description;
		private String systemName;
		private boolean apiSigningRequired;
		private boolean enabled;
		private boolean hidden;
		private String parentProductId;
		private List<ConfigurationModel> configurations;
		private List<ProductFeature> features;

		public ProductModel() {
			super(null, null);

			configurations = new ArrayList<>();
			features = new ArrayList<>();
		}

		public ProductModel(Product product, List<ConfigurationModel> configurations) {
			super(product.getId(), product.getHid());
			this.name = product.getName();
			this.description = product.getDescription();
			this.systemName = product.getSystemName();
			this.apiSigningRequired = product.isApiSigningRequired();
			this.enabled = product.isEnabled();
			this.hidden = product.isHidden();
			this.parentProductId = product.getParentProductId();
			this.configurations = new ArrayList<>();
			if (configurations != null)
				this.configurations = configurations;
			this.features = product.getFeatures();
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

		public boolean isHidden() {
			return hidden;
		}

		public String getParentProductId() {
			return parentProductId;
		}

		public List<ConfigurationModel> getConfigurations() {
			return configurations;
		}

		public List<ProductFeature> getFeatures() {
			return features;
		}
	}

	public static class ProductUpsert implements Serializable {
		private static final long serialVersionUID = -599225419139851418L;

		private ProductModel product;
		private List<ProductModels.ProductOption> productOptions;
		private ConfigurationPropertyCategory[] categoryOptions;
		private ConfigurationPropertyDataType[] dataTypeOptions;

		public ProductUpsert(ProductModel product, List<ProductModels.ProductOption> productOptions,
		        ConfigurationPropertyCategory[] categoryOptions, ConfigurationPropertyDataType[] dataTypeOptions) {
			this.product = product;
			this.productOptions = productOptions;
			this.categoryOptions = categoryOptions;
			this.dataTypeOptions = dataTypeOptions;
		}

		public ProductModel getProduct() {
			return product;
		}

		public List<ProductModels.ProductOption> getProductOptions() {
			return productOptions;
		}

		public ConfigurationPropertyCategory[] getCategoryOptions() {
			return categoryOptions;
		}

		public ConfigurationPropertyDataType[] getDataTypeOptions() {
			return dataTypeOptions;
		}
	}

	public static class ApplicationModel extends ApplicationModelAbstract<ApplicationModel> {
		private static final long serialVersionUID = 1447761054078322222L;

		@Override
		protected ApplicationModel self() {
			return self();
		}
	}
}

package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.webapi.data.CoreApplicationEngineModels;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class ApplicationEngineModels extends CoreApplicationEngineModels {

	public static class ApplicationEngineList extends CoreDocumentModel {
		private static final long serialVersionUID = 3996203023103383516L;

		private String name;
		private String description;
		private String productName;
		private boolean enabled;

		public ApplicationEngineList(ApplicationEngine applicationEngine, Product product) {
			super(applicationEngine.getId(), applicationEngine.getHid());

			this.name = applicationEngine.getName();
			this.description = applicationEngine.getDescription();
			this.productName = product != null ? product.getName()
			        : "UNKNOWN (" + applicationEngine.getProductId() + ")";
			this.enabled = applicationEngine.isEnabled();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getProductName() {
			return productName;
		}

		public boolean isEnabled() {
			return enabled;
		}
	}

	public static class ApplicationEngineModel extends CoreDocumentModel {
		private static final long serialVersionUID = -1635379638899222023L;

		private String name;
		private String description;
		private boolean enabled;
		private String productId;

		public ApplicationEngineModel(Application application) {
			super(application.getId(), application.getHid());
			this.name = application.getName();
			this.description = application.getDescription();
			this.enabled = application.isEnabled();
			this.productId = application.getProductId();
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

		public String getProductId() {
			return productId;
		}
	}

	public static class ApplicationEngineUpsert implements Serializable {
		private static final long serialVersionUID = -9176323943106145970L;

		private ApplicationEngineModel applicationEngine;
		private List<ProductModels.ProductOption> productOptions;

		private ApplicationEngineUpsert() {
			productOptions = new ArrayList<>();
		}

		public ApplicationEngineUpsert(ApplicationEngineModel applicationEngine,
		        List<ProductModels.ProductOption> productOptions) {
			this();
			this.applicationEngine = applicationEngine;
			if (productOptions != null)
				this.productOptions = productOptions;
		}

		public ApplicationEngineModel getApplicationEngine() {
			return applicationEngine;
		}

		public List<ProductModels.ProductOption> getProductOptions() {
			return productOptions;
		}
	}
}

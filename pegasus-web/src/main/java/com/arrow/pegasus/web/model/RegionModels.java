package com.arrow.pegasus.web.model;

import java.io.Serializable;

import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class RegionModels {

	public static class RegionOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 5707253105878880171L;

		private String description;

		public RegionOption(Region region) {
			super(region.getId(), region.getHid(), region.getName());
			this.description = region.getDescription();
		}

		public String getDescription() {
			return description;
		}
	}

	public static class RegionList extends CoreDocumentModel {
		private static final long serialVersionUID = -3604428649610420453L;

		private String name;
		private String description;
		private boolean enabled;

		public RegionList(Region region) {
			super(region.getId(), region.getHid());
			this.name = region.getName();
			this.description = region.getDescription();
			this.enabled = region.isEnabled();
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
	}

	public static class RegionModel extends CoreDocumentModel {
		private static final long serialVersionUID = -3787343069759995000L;

		private String name;
		private String description;
		private boolean enabled;

		public RegionModel() {
			super(null, null);
		}

		public RegionModel(Region region) {
			super(region.getId(), region.getHid());
			this.name = region.getName();
			this.description = region.getDescription();
			this.enabled = region.isEnabled();
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
	}

	public static class RegionUpsert implements Serializable {
		private static final long serialVersionUID = -583879658316225956L;

		private RegionModel region;

		private RegionUpsert() {
		}

		public RegionUpsert(RegionModel region) {
			this();
			this.region = region;
		}

		public RegionModel getRegion() {
			return region;
		}
	}
}

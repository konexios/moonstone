package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class ZoneModels {

	public static class ZoneOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 5707253105878880171L;

		private String description;

		public ZoneOption(Zone zone) {
			super(zone.getId(), zone.getHid(), zone.getName());
			this.description = zone.getDescription();
		}

		public String getDescription() {
			return description;
		}
	}

	public static class ZoneRegionOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 5707253105878880171L;

		private String description;

		public ZoneRegionOption(Region region) {
			super(region.getId(), region.getHid(), region.getName());
			this.description = region.getDescription();
		}

		public String getDescription() {
			return description;
		}
	}

	public static class ZoneList extends CoreDocumentModel {
		private static final long serialVersionUID = -4085115801808219426L;

		private String name;
		private String description;
		private String regionName;
		private String systemName;
		private boolean enabled;
		private boolean hidden;

		public ZoneList(Zone zone, String regionName) {
			super(zone.getId(), zone.getHid());
			this.name = zone.getName();
			this.description = zone.getDescription();
			this.regionName = regionName;
			this.systemName = zone.getSystemName();
			this.enabled = zone.isEnabled();
			this.hidden = zone.isHidden();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getRegionName() {
			return regionName;
		}

		public String getSystemName() {
			return systemName;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public boolean isHidden() {
			return hidden;
		}
	}

	public static class ZoneModel extends CoreDocumentModel {
		private static final long serialVersionUID = -7945458910590966736L;

		private String name;
		private String description;
		private String systemName;
		private String regionId;
		private boolean enabled;
		private boolean hidden;

		public ZoneModel() {
			super(null, null);
		}

		public ZoneModel(Zone zone) {
			super(zone.getId(), zone.getHid());
			this.name = zone.getName();
			this.description = zone.getDescription();
			this.systemName = zone.getSystemName();
			this.regionId = zone.getRegionId();
			this.enabled = zone.isEnabled();
			this.hidden = zone.isHidden();
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

		public String getRegionId() {
			return regionId;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public boolean isHidden() {
			return hidden;
		}
	}

	public static class ZoneUpsert implements Serializable {
		private static final long serialVersionUID = -8925162753565991248L;

		private ZoneModel zone;
		private List<ZoneRegionOption> regionOptions;

		private ZoneUpsert() {
			this.regionOptions = new ArrayList<>();
		}

		public ZoneUpsert(ZoneModel zone, List<ZoneRegionOption> regionOptions) {
			this();
			this.zone = zone;
			if (regionOptions != null)
				this.regionOptions = regionOptions;
		}

		public ZoneModel getZone() {
			return zone;
		}

		public List<ZoneRegionOption> getRegionOptions() {
			return regionOptions;
		}
	}
}

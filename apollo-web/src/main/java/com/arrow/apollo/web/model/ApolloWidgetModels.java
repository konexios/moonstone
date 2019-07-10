package com.arrow.apollo.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.arrow.apollo.data.ApolloWidgetTypeCategories;
import com.arrow.apollo.data.IconTypes;
import com.arrow.pegasus.dashboard.data.WidgetSizes;
import com.arrow.pegasus.webapi.data.DefinitionModel;

public class ApolloWidgetModels {

	public static class ApolloWidgetUpsertModel implements Serializable {
		private static final long serialVersionUID = 2535568453039877888L;

		private ApolloWidgetModel apolloWidget;
		private EnumSet<ApolloWidgetTypeCategories> categories;
		private EnumSet<IconTypes> iconTypes;
		private List<WidgetTypeModels.WidgetTypeOptionModel> widgetTypes = new ArrayList<>();

		public ApolloWidgetModel getApolloWidget() {
			return apolloWidget;
		}

		public void setApolloWidget(ApolloWidgetModel apolloWidget) {
			this.apolloWidget = apolloWidget;
		}

		public EnumSet<ApolloWidgetTypeCategories> getCategories() {
			return categories;
		}

		public void setCategories(EnumSet<ApolloWidgetTypeCategories> categories) {
			this.categories = categories;
		}

		public EnumSet<IconTypes> getIconTypes() {
			return iconTypes;
		}

		public void setIconTypes(EnumSet<IconTypes> iconTypes) {
			this.iconTypes = iconTypes;
		}

		public List<WidgetTypeModels.WidgetTypeOptionModel> getWidgetTypes() {
			return widgetTypes;
		}

		public void setWidgetTypes(List<WidgetTypeModels.WidgetTypeOptionModel> widgetTypes) {
			if (widgetTypes != null)
				this.widgetTypes = widgetTypes;
		}

		public ApolloWidgetUpsertModel withApolloWidget(ApolloWidgetModel apolloWidgetModel) {
			setApolloWidget(apolloWidgetModel);

			return this;
		}

		public ApolloWidgetUpsertModel withCategories(EnumSet<ApolloWidgetTypeCategories> categories) {
			setCategories(categories);

			return this;
		}

		public ApolloWidgetUpsertModel withIconTypes(EnumSet<IconTypes> iconTypes) {
			setIconTypes(iconTypes);

			return this;
		}

		public ApolloWidgetUpsertModel withWidgetTypes(List<WidgetTypeModels.WidgetTypeOptionModel> widgetTypes) {
			setWidgetTypes(widgetTypes);

			return this;
		}
	}

	public static class ApolloWidgetListModel extends DefinitionModel {
		private static final long serialVersionUID = 4240900381392293733L;

		private String widgetTypeName;
		private ApolloWidgetTypeCategories category;
		private IconTypes iconType;
		private String icon;
		private boolean enabled;

		public ApolloWidgetListModel() {
			super(null, null, null);
		}

		public ApolloWidgetListModel(String id, String name, String description) {
			super(id, name, description);
		}

		public String getWidgetTypeName() {
			return widgetTypeName;
		}

		public void setWidgetTypeName(String widgetTypeName) {
			this.widgetTypeName = widgetTypeName;
		}

		public ApolloWidgetTypeCategories getCategory() {
			return category;
		}

		public void setCategory(ApolloWidgetTypeCategories category) {
			this.category = category;
		}

		public IconTypes getIconType() {
			return iconType;
		}

		public void setIconType(IconTypes iconType) {
			this.iconType = iconType;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class ApolloWidgetModel extends DefinitionModel {
		private static final long serialVersionUID = -3462641536913820849L;

		private String widgetTypeId;
		private ApolloWidgetTypeCategories category;
		private IconTypes iconType;
		private String icon;
		private boolean enabled;

		public ApolloWidgetModel() {
			super(null, null, null);
		}

		public ApolloWidgetModel(String id, String name, String description) {
			super(id, name, description);
		}

		public String getWidgetTypeId() {
			return widgetTypeId;
		}

		public void setWidgetTypeId(String widgetTypeId) {
			this.widgetTypeId = widgetTypeId;
		}

		public ApolloWidgetTypeCategories getCategory() {
			return category;
		}

		public void setCategory(ApolloWidgetTypeCategories category) {
			this.category = category;
		}

		public IconTypes getIconType() {
			return iconType;
		}

		public void setIconType(IconTypes iconType) {
			this.iconType = iconType;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class ApolloWidgetCountsModel implements Serializable {
		private static final long serialVersionUID = 5300276606070482177L;

		private long deviceWidgetCount = 0;
		private long gatewayWidgetCount = 0;
		private long usageWidgetCount = 0;
		private long generalWidgetCount = 0;

		public ApolloWidgetCountsModel() {
		}

		public long getDeviceWidgetCount() {
			return deviceWidgetCount;
		}

		public void setDeviceWidgetCount(long deviceWidgetCount) {
			this.deviceWidgetCount = deviceWidgetCount;
		}

		public long getGatewayWidgetCount() {
			return gatewayWidgetCount;
		}

		public void setGatewayWidgetCount(long gatewayWidgetCount) {
			this.gatewayWidgetCount = gatewayWidgetCount;
		}

		public long getUsageWidgetCount() {
			return usageWidgetCount;
		}

		public void setUsageWidgetCount(long usageWidgetCount) {
			this.usageWidgetCount = usageWidgetCount;
		}

		public long getGeneralWidgetCount() {
			return generalWidgetCount;
		}

		public void setGeneralWidgetCount(long generalWidgetCount) {
			this.generalWidgetCount = generalWidgetCount;
		}

	}

	public static class ApolloWidgetIconModel extends DefinitionModel {
		private static final long serialVersionUID = 7595988293433335199L;

		private String widgetTypeId;
		private ApolloWidgetTypeCategories category;
		private IconTypes iconType;
		private String icon;
		private ApolloWidgetSupportedSizesModel supportedSizes;

		public ApolloWidgetIconModel() {
			super(null, null, null);
		}

		public ApolloWidgetIconModel(String id, String name, String description) {
			super(id, name, description);
		}

		public String getWidgetTypeId() {
			return widgetTypeId;
		}

		public void setWidgetTypeId(String widgetTypeId) {
			this.widgetTypeId = widgetTypeId;
		}

		public ApolloWidgetTypeCategories getCategory() {
			return category;
		}

		public void setCategory(ApolloWidgetTypeCategories category) {
			this.category = category;
		}

		public IconTypes getIconType() {
			return iconType;
		}

		public void setIconType(IconTypes iconType) {
			this.iconType = iconType;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public ApolloWidgetSupportedSizesModel getSupportedSizes() {
			return supportedSizes;
		}

		public void setSupportedSizes(ApolloWidgetSupportedSizesModel supportedSizes) {
			this.supportedSizes = supportedSizes;
		}
	}

	public static class ApolloWidgetSizeModel implements Serializable {
		private static final long serialVersionUID = -6266339987430862333L;

		private int width;
		private int height;

		public ApolloWidgetSizeModel(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

	}

	public static class ApolloWidgetSupportedSizesModel implements Serializable {
		private static final long serialVersionUID = -6530673240834775254L;

		private ApolloWidgetSizeModel small;
		private ApolloWidgetSizeModel medium;
		private ApolloWidgetSizeModel large;
		private ApolloWidgetSizeModel xtraLarge;

		public ApolloWidgetSupportedSizesModel() {
		}

		public ApolloWidgetSupportedSizesModel(WidgetSizes widgetSizes) {

			if (widgetSizes.getSmall() != null)
				setSmall(new ApolloWidgetSizeModel(widgetSizes.getSmall().getWidth(),
				        widgetSizes.getSmall().getHeight()));

			if (widgetSizes.getMedium() != null)
				setMedium(new ApolloWidgetSizeModel(widgetSizes.getMedium().getWidth(),
				        widgetSizes.getMedium().getHeight()));

			if (widgetSizes.getLarge() != null)
				setLarge(new ApolloWidgetSizeModel(widgetSizes.getLarge().getWidth(),
				        widgetSizes.getLarge().getHeight()));

			if (widgetSizes.getXtraLarge() != null)
				setXtraLarge(new ApolloWidgetSizeModel(widgetSizes.getXtraLarge().getWidth(),
				        widgetSizes.getXtraLarge().getHeight()));
		}

		public ApolloWidgetSizeModel getSmall() {
			return small;
		}

		public void setSmall(ApolloWidgetSizeModel small) {
			this.small = small;
		}

		public ApolloWidgetSizeModel getMedium() {
			return medium;
		}

		public void setMedium(ApolloWidgetSizeModel medium) {
			this.medium = medium;
		}

		public ApolloWidgetSizeModel getLarge() {
			return large;
		}

		public void setLarge(ApolloWidgetSizeModel large) {
			this.large = large;
		}

		public ApolloWidgetSizeModel getXtraLarge() {
			return xtraLarge;
		}

		public void setXtraLarge(ApolloWidgetSizeModel xtraLarge) {
			this.xtraLarge = xtraLarge;
		}
	}
}

package com.arrow.pegasus.web.model;

import org.springframework.data.domain.Page;

import com.arrow.pegasus.webapi.data.CoreSearchResultModel;

public class SearchResultModels {

	public static class ProductSearchResult
	        extends CoreSearchResultModel<ProductModels.ProductList, SearchFilterModels.ProductSearchFilter> {
		private static final long serialVersionUID = 5304416202216845824L;

		public ProductSearchResult(Page<ProductModels.ProductList> result,
		        SearchFilterModels.ProductSearchFilter filter) {
			super(result, filter);
		}
	}

	public static class CompanySearchResult
	        extends CoreSearchResultModel<CompanyModels.CompanyListModel, SearchFilterModels.CompanySearchFilter> {
		private static final long serialVersionUID = 1842219432434339202L;

		public CompanySearchResult(Page<CompanyModels.CompanyListModel> result,
		        SearchFilterModels.CompanySearchFilter filter) {
			super(result, filter);
		}
	}

	public static class SubscriptionSearchResult extends
	        CoreSearchResultModel<SubscriptionModels.SubscriptionList, SearchFilterModels.SubscriptionSearchFilter> {
		private static final long serialVersionUID = 453190450144778994L;

		public SubscriptionSearchResult(Page<SubscriptionModels.SubscriptionList> result,
		        SearchFilterModels.SubscriptionSearchFilter filter) {
			super(result, filter);
		}
	}

	public static class ApplicationSearchResult extends
	        CoreSearchResultModel<ApplicationModels.ApplicationList, SearchFilterModels.ApplicationSearchFilter> {
		private static final long serialVersionUID = 5522334776142495352L;

		public ApplicationSearchResult(Page<ApplicationModels.ApplicationList> result,
		        SearchFilterModels.ApplicationSearchFilter filter) {
			super(result, filter);
		}
	}

	public static class UserSearchResult
	        extends CoreSearchResultModel<UserModels.UserList, SearchFilterModels.UserSearchFilter> {
		private static final long serialVersionUID = 2865367856410257892L;

		public UserSearchResult(Page<UserModels.UserList> result, SearchFilterModels.UserSearchFilter filter) {
			super(result, filter);
		}
	}

	public static class RoleSearchResult
	        extends CoreSearchResultModel<RoleModels.RoleList, SearchFilterModels.RoleSearchFilter> {
		private static final long serialVersionUID = -4244452591057790405L;

		public RoleSearchResult(Page<RoleModels.RoleList> result, SearchFilterModels.RoleSearchFilter filter) {
			super(result, filter);
		}
	}

	public static class PrivilegeSearchResult
	        extends CoreSearchResultModel<PrivilegeModels.PrivilegeList, SearchFilterModels.PrivilegeSearchFilter> {
		private static final long serialVersionUID = -3173574291932963388L;

		public PrivilegeSearchResult(Page<PrivilegeModels.PrivilegeList> result,
		        SearchFilterModels.PrivilegeSearchFilter filter) {
			super(result, filter);
		}
	}

	public static class RegionSearchResult
	        extends CoreSearchResultModel<RegionModels.RegionList, SearchFilterModels.RegionSearchFilter> {
		private static final long serialVersionUID = -3278082777807970065L;

		public RegionSearchResult(Page<RegionModels.RegionList> result, SearchFilterModels.RegionSearchFilter filter) {
			super(result, filter);
		}
	}

	public static class ZoneSearchResult
	        extends CoreSearchResultModel<ZoneModels.ZoneList, SearchFilterModels.ZoneSearchFilter> {
		private static final long serialVersionUID = 198550257183397941L;

		public ZoneSearchResult(Page<ZoneModels.ZoneList> result, SearchFilterModels.ZoneSearchFilter filter) {
			super(result, filter);
		}
	}
	
	public static class AccessKeySearchResult<T extends AccessKeyModelAbstract<T>> extends
	CoreSearchResultModel<T, SearchFilterModels.AccessKeySearchFilter> {
		private static final long serialVersionUID = -5027069867996188187L;

		public AccessKeySearchResult(Page<T> result,
				SearchFilterModels.AccessKeySearchFilter filter) {
			super(result, filter);
		}
	}
}
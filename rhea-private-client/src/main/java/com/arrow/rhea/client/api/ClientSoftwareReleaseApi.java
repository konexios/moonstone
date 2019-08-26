package com.arrow.rhea.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.arrow.rhea.client.model.SoftwareReleaseModel;
import com.arrow.rhea.client.search.SoftwareReleaseSearchCriteria;
import com.arrow.rhea.data.SoftwareRelease;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.PagingResultModel;

@Component("RheaClientSoftwareReleaseApi")
public class ClientSoftwareReleaseApi extends ClientApiAbstract {
	private static final String SOFTWARE_RELEASES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/software-releases";

	public List<SoftwareRelease> findAll() {
		try {
			URI uri = buildUri(SOFTWARE_RELEASES_ROOT_URL);
			List<SoftwareRelease> result = execute(new HttpGet(uri), new TypeReference<List<SoftwareRelease>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareRelease create(SoftwareRelease softwareRelease, String who) {
		try {
			URI uri = buildUri(SOFTWARE_RELEASES_ROOT_URL);
			SoftwareReleaseModel model = new SoftwareReleaseModel().withModel(softwareRelease).withWho(who);
			SoftwareRelease result = execute(new HttpPost(uri), JsonUtils.toJson(model), SoftwareRelease.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareRelease update(SoftwareRelease softwareRelease, String who) {
		try {
			URI uri = buildUri(SOFTWARE_RELEASES_ROOT_URL);
			SoftwareReleaseModel model = new SoftwareReleaseModel().withModel(softwareRelease).withWho(who);
			SoftwareRelease result = execute(new HttpPut(uri), JsonUtils.toJson(model), SoftwareRelease.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareRelease findById(String id) {
		try {
			URI uri = buildUri(SOFTWARE_RELEASES_ROOT_URL + "/ids/" + id);
			SoftwareRelease result = execute(new HttpGet(uri), SoftwareRelease.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<SoftwareRelease> findAll(String companyId, String softwareProductId, String[] deviceTypeIds,
	        String[] rtuTypes, Boolean enabled, Boolean noLongerSupported) {
		try {
			SoftwareReleaseSearchCriteria criteria = new SoftwareReleaseSearchCriteria().withCompanyId(companyId)
			        .withSoftwareProductId(softwareProductId).withDeviceTypeIds(deviceTypeIds)
			        .withRightToUseTypes(rtuTypes).withEnabled(enabled).withNoLongerSupported(noLongerSupported);
			URI uri = buildUri(SOFTWARE_RELEASES_ROOT_URL, criteria);
			List<SoftwareRelease> result = execute(new HttpGet(uri), criteria,
			        new TypeReference<List<SoftwareRelease>>() {
			        });
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	
	public List<SoftwareRelease> findAll(String[] deviceTypeIds, String[] rtuTypes, 
			Boolean enabled, String[] upgradeableFromIds) {
		try {
			SoftwareReleaseSearchCriteria criteria = new SoftwareReleaseSearchCriteria().withDeviceTypeIds(deviceTypeIds)
			        .withRightToUseTypes(rtuTypes).withEnabled(enabled)
			        .withUpgradeableFromIds(upgradeableFromIds);
			URI uri = buildUri(SOFTWARE_RELEASES_ROOT_URL, criteria);
			List<SoftwareRelease> result = execute(new HttpGet(uri), criteria,
			        new TypeReference<List<SoftwareRelease>>() {
			        });
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Page<SoftwareRelease> findAll(PageRequest pageRequest, String companyId, String softwareProductId,
	        String[] deviceTypeIds, String[] rtuTypes, Boolean enabled, Boolean noLongerSupported) {
		try {
			SoftwareReleaseSearchCriteria criteria = new SoftwareReleaseSearchCriteria().withCompanyId(companyId)
			        .withSoftwareProductId(softwareProductId).withDeviceTypeIds(deviceTypeIds)
			        .withRightToUseTypes(rtuTypes).withEnabled(enabled).withNoLongerSupported(noLongerSupported)
			        .withPage(pageRequest.getPageNumber()).withSize(pageRequest.getPageSize());
			Sort sort = pageRequest.getSort();
			if (sort != null) {
				sort.forEach(order -> criteria.withSortDirection(order.getDirection().toString())
				        .withSortProperty(order.getProperty()));
			}

			URI uri = buildUri(SOFTWARE_RELEASES_ROOT_URL + "/pages", criteria);

			PagingResultModel<SoftwareRelease> model = execute(new HttpGet(uri), criteria,
			        new TypeReference<PagingResultModel<SoftwareRelease>>() {
			        });
			Page<SoftwareRelease> result = new PageImpl<SoftwareRelease>(model.getData(), pageRequest,
			        model.getTotalSize());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}

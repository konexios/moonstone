package com.arrow.apollo.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.apollo.web.ApolloWebVersion;
import com.arrow.dashboard.PegasusDashboardApiVersion;
import com.arrow.dashboard.PegasusDashboardCoreVersion;
import com.arrow.kronos.KronosCoreCloudVersion;
import com.arrow.kronos.KronosCoreVersion;
import com.arrow.pegasus.PegasusCoreDbVersion;
import com.arrow.pegasus.PegasusCoreHubVersion;
import com.arrow.pegasus.PegasusCoreVersion;
import com.arrow.pegasus.PegasusCoreWebVersion;
import com.arrow.pegasus.client.api.PegasusPrivateClientVersion;
import com.arrow.pegasus.dashboard.PegasusDashboardDBVersion;
import com.arrow.rhea.RheaCoreVersion;
import com.arrow.rhea.client.api.RheaPrivateClientVersion;

import moonstone.acn.AcnCoreVersion;
import moonstone.acn.client.AcnClientVersion;
import moonstone.acs.AcsCoreVersion;
import moonstone.acs.client.api.AcsClientVersion;
import moonstone.acs.client.model.VersionAndLibraryModel;
import moonstone.acs.client.model.VersionModel;

@RestController
@RequestMapping("/api/apollo/webapp")
public class ApolloWebAppController extends ApolloControllerAbstract {

	@RequestMapping("/version")
	public VersionModel version() {
		return ApolloWebVersion.get();
	}

	@RequestMapping("/components/version")
	public VersionAndLibraryModel componentVersion() {

		VersionAndLibraryModel model = new VersionAndLibraryModel().withVersionModel(ApolloWebVersion.get());

		List<VersionModel> libraries = new ArrayList<>();
		libraries.add(AcnClientVersion.get());
		libraries.add(AcnCoreVersion.get());
		libraries.add(AcsCoreVersion.get());
		libraries.add(AcsClientVersion.get());
		libraries.add(KronosCoreVersion.get());
		libraries.add(KronosCoreCloudVersion.get());
		libraries.add(PegasusCoreVersion.get());
		libraries.add(PegasusCoreDbVersion.get());
		libraries.add(PegasusCoreHubVersion.get());
		libraries.add(PegasusCoreWebVersion.get());
		libraries.add(PegasusPrivateClientVersion.get());
		libraries.add(PegasusDashboardApiVersion.get());
		libraries.add(PegasusDashboardCoreVersion.get());
		libraries.add(PegasusDashboardDBVersion.get());
		// peg dboard web jar???
		libraries.add(RheaCoreVersion.get());
		libraries.add(RheaPrivateClientVersion.get());

		Collections.sort(libraries, new Comparator<VersionModel>() {

			@Override
			public int compare(VersionModel o1, VersionModel o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		model.withLibraries(libraries);

		return model;
	}
}

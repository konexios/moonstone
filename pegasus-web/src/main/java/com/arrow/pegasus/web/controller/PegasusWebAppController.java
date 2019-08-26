package com.arrow.pegasus.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.PegasusCoreDbVersion;
import com.arrow.pegasus.PegasusCoreHubVersion;
import com.arrow.pegasus.PegasusCoreVersion;
import com.arrow.pegasus.PegasusCoreWebVersion;
import com.arrow.pegasus.web.PegasusWebVersion;

import moonstone.acs.AcsCoreVersion;
import moonstone.acs.client.api.AcsClientVersion;
import moonstone.acs.client.model.VersionAndLibraryModel;
import moonstone.acs.client.model.VersionModel;

@RestController
@RequestMapping("/api/pegasus/webapp")
public class PegasusWebAppController extends com.arrow.pegasus.controller.ControllerAbstract {

	@RequestMapping("/version")
	public VersionModel version() {
		return PegasusWebVersion.get();
	}

	@RequestMapping("/components/version")
	public VersionAndLibraryModel componentVersion() {

		VersionAndLibraryModel model = new VersionAndLibraryModel().withVersionModel(PegasusWebVersion.get());

		List<VersionModel> libraries = new ArrayList<>();
		libraries.add(AcsCoreVersion.get());
		libraries.add(AcsClientVersion.get());
		libraries.add(PegasusCoreVersion.get());
		libraries.add(PegasusCoreDbVersion.get());
		libraries.add(PegasusCoreHubVersion.get());
		libraries.add(PegasusCoreWebVersion.get());

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
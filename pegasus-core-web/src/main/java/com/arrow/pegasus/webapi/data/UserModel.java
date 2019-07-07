package com.arrow.pegasus.webapi.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.util.CoreConfigurationPropertyUtil;

public class UserModel implements Serializable {
	private static final long serialVersionUID = 6009757929024960137L;

	private final String id;
	private final String login;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String companyId;
	private final String companyName;
	private final boolean admin;
	private List<ApplicationModel> applications = new ArrayList<>();

	public UserModel(User user, Product product, Zone zone, String companyName) {
		Assert.notNull(user, "user is null");
		Assert.notNull(product, "product is null");
		Assert.notNull(zone, "zone is null");

		id = user.getId();
		login = user.getLogin();
		companyId = user.getCompanyId();
		this.companyName = companyName;
		admin = user.isAdmin();
		firstName = user.getContact().getFirstName();
		lastName = user.getContact().getLastName();
		email = user.getContact().getEmail();

		Map<String, ApplicationModel> applicationMap = new HashMap<>();
		if (!StringUtils.isEmpty(product.getParentProductId())) {
			Assert.notNull(product.getRefParentProduct(),
			        "product.getRefParentProduct() is not populated: " + product.getParentProductId());
			// parent product
			applicationMap = populateRoles(applicationMap, product.getRefParentProduct(), product, user);
		}
		// product
		applicationMap = populateRoles(applicationMap, product, null, user);

		if (!applicationMap.isEmpty()) {
			for (ApplicationModel applicationModel : applicationMap.values()) {
				if (applicationModel.getZone().getZoneSystemName().equals(zone.getSystemName()))
					applications.add(applicationModel);
			}
			applications.sort(Comparator.comparing(ApplicationModel::getName, String.CASE_INSENSITIVE_ORDER));
		}
	}

	private Map<String, ApplicationModel> populateRoles(Map<String, ApplicationModel> applicationMap, Product product,
	        Product productExtension, User user) {
		Assert.notNull(product, "product is null");

		// the product (and the product extension) must be enabled
		if (!product.isEnabled() || (productExtension != null && !productExtension.isEnabled()))
			return applicationMap;

		for (Role role : user.getRefRoles()) {
			// role must be enabled
			if (role.isEnabled()) {
				Application roleApplication = role.getRefApplication();
				Assert.notNull(roleApplication,
				        "role.getRefApplication() is not populated: " + role.getApplicationId());

				// the application assigned to the role must be enabled
				if (!roleApplication.isEnabled())
					continue;

				Product roleApplicationProduct = roleApplication.getRefProduct();
				Assert.notNull(roleApplicationProduct,
				        "application.getRefProduct() is not populated: " + roleApplication.getProductId());

				// the product assigned to the role must match the product the
				// user is signing into
				if (!role.getProductId().equals(product.getId()))
					continue;

				// if we have a product extension, the application assigned to
				// the role must have the product extension assigned to it
				if (productExtension != null
				        && !roleApplication.getProductExtensionIds().contains(productExtension.getId()))
					continue;

				ApplicationModel applicationModel = applicationMap.get(role.getApplicationId());
				if (applicationModel == null) {
					// THIS IS A BAD IDEA!!! NEED TO FIX THIS AT SOME POINT
					// (srhode)
					String logoUrl = null;
					ConfigurationProperty logoUrlProperty = new CoreConfigurationPropertyUtil()
					        .getConfigurationProperty(roleApplication, ConfigurationPropertyCategory.Branding,
					                "logoUrl", false);
					if (logoUrlProperty != null)
						logoUrl = logoUrlProperty.strValue();

					applicationModel = new ApplicationModel(roleApplication.getId(), roleApplication.getName(),
					        roleApplication.getDescription(), roleApplication.getHid(), logoUrl);
					applicationModel.setProduct(new DefinitionModel(roleApplicationProduct.getId(),
					        roleApplicationProduct.getName(), roleApplicationProduct.getDescription()));

					Zone roleApplicationZone = roleApplication.getRefZone();
					Assert.notNull(roleApplicationZone,
					        "application.getRefZone() is not populated: " + roleApplication.getZoneId());
					applicationModel.setZone(new ZoneModel(roleApplicationZone.getId(), roleApplicationZone.getName(),
					        roleApplicationZone.getDescription(), roleApplicationZone.getSystemName()));
				}

				for (Privilege privilege : role.getRefPrivileges()) {
					if (privilege.isEnabled())
						applicationModel.getPrivileges().add(new DefinitionModel(privilege.getId(),
						        privilege.getSystemName(), privilege.getDescription()));
				}
				applicationMap.put(applicationModel.getId(), applicationModel);
			}
		}

		return applicationMap;
	}

	public List<ApplicationModel> getApplications() {
		return applications;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public boolean isAdmin() {
		return admin;
	}

	public String getLogin() {
		return login;
	}

	public String getId() {
		return id;
	}
}

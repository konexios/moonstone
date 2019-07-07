package com.arrow.pegasus;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import com.arrow.acs.AcsUtils;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.Loggable;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.PlatformConfig;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.service.ApplicationEngineService;
import com.arrow.pegasus.service.ApplicationService;
import com.arrow.pegasus.service.CompanyService;
import com.arrow.pegasus.service.PlatformConfigService;
import com.arrow.pegasus.service.PrivilegeService;
import com.arrow.pegasus.service.ProductService;
import com.arrow.pegasus.service.RegionService;
import com.arrow.pegasus.service.RoleService;
import com.arrow.pegasus.service.SubscriptionService;
import com.arrow.pegasus.service.UserService;
import com.arrow.pegasus.service.ZoneService;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class AutoInstallation extends Loggable implements CommandLineRunner {
	@Autowired
	private ApplicationContext context;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private ZoneService zoneService;
	@Autowired
	private ProductService productService;
	@Autowired
	private PlatformConfigService platformConfigService;
	@Autowired
	private ApplicationEngineService applicationEngineService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private MongoOperations mongo;

	@Override
	public void run(String... args) throws Exception {
		String method = "run";
		try {
			checkCreateProducts();
			checkCreateRegions();
			checkCreateZones();
			checkCreatePlatformConfig();
			checkCreateApplicationEngines();
			checkCreatePrivileges();
			checkCreateCompanies();
			checkCreateSubscriptions();
			checkCreateApplications();
			checkCreateRoles();
			checkCreateUsers();
		} catch (Exception e) {
			logError(method, e);
			throw e;
		}
	}

	void checkCreateProducts() throws Exception {
		String method = "checkCreateProducts";
		long productCount = productService.getProductRepository().count();
		if (productCount == 0) {
			List<Product> products = JsonUtils.fromJson(
					AcsUtils.streamToString(context.getResource("classpath:install/product.json").getInputStream(),
							StandardCharsets.UTF_8),
					new TypeReference<List<Product>>() {
					});
			logInfo(method, "products: %d", products.size());
			for (Product product : products) {
				product = productService.create(product, CoreConstant.ADMIN_USER);
				logInfo(method, "created product ==> name: %s, id: %s", product.getName(), product.getId());
			}

			// update apollo
			Product kronos = productService.getProductRepository().findBySystemName("kronos");
			Product apollo = productService.getProductRepository().findBySystemName("apollo");
			apollo.setParentProductId(kronos.getId());
			productService.update(apollo, CoreConstant.ADMIN_USER);

			mongo.indexOps(Product.class).ensureIndex(new Index("hid", Direction.ASC).named("hid").background());
		} else {
			logInfo(method, "productCount: %d", productCount);
		}
	}

	void checkCreateRegions() throws Exception {
		String method = "checkCreateRegions";
		long regionCount = regionService.getRegionRepository().count();
		if (regionCount == 0) {
			List<Region> regions = JsonUtils.fromJson(
					AcsUtils.streamToString(context.getResource("classpath:install/region.json").getInputStream(),
							StandardCharsets.UTF_8),
					new TypeReference<List<Region>>() {
					});
			logInfo(method, "regions: %d", regions.size());
			for (Region region : regions) {
				region = regionService.create(region, CoreConstant.ADMIN_USER);
				logInfo(method, "created region ==> name: %s, id: %s", region.getName(), region.getId());
			}
		} else {
			logInfo(method, "regionCount: %d", regionCount);
		}
	}

	void checkCreateZones() throws Exception {
		String method = "checkCreateZones";
		long zoneCount = zoneService.getZoneRepository().count();
		if (zoneCount == 0) {
			List<Zone> zones = JsonUtils.fromJson(
					AcsUtils.streamToString(context.getResource("classpath:install/zone.json").getInputStream(),
							StandardCharsets.UTF_8),
					new TypeReference<List<Zone>>() {
					});
			logInfo(method, "zones: %d", zones.size());
			Region region = regionService.getRegionRepository().findAll().get(0);
			for (Zone zone : zones) {
				zone.setRegionId(region.getId());
				zone = zoneService.create(zone, CoreConstant.ADMIN_USER);
				logInfo(method, "created zone ==> name: %s, id: %s", zone.getName(), zone.getId());
			}
		} else {
			logInfo(method, "zoneCount: %d", zoneCount);
		}
	}

	void checkCreatePlatformConfig() throws Exception {
		String method = "checkCreatePlatformConfig";
		long cfgCount = platformConfigService.getPlatformConfigRepository().count();
		if (cfgCount == 0) {
			List<PlatformConfig> cfgs = JsonUtils.fromJson(AcsUtils.streamToString(
					context.getResource("classpath:install/platform-config.json").getInputStream(),
					StandardCharsets.UTF_8), new TypeReference<List<PlatformConfig>>() {
					});
			logInfo(method, "cfgs: %d", cfgs.size());
			for (PlatformConfig cfg : cfgs) {
				cfg = platformConfigService.getPlatformConfigRepository().doInsert(cfg, CoreConstant.ADMIN_USER);
				logInfo(method, "created platformConfig ==> systemName: %s, id: %s", cfg.getZoneSystemName(),
						cfg.getId());
			}
		} else {
			logInfo(method, "cfgCount: %d", cfgCount);
		}
	}

	void checkCreateApplicationEngines() throws Exception {
		String method = "checkCreateApplicationEngines";
		long engineCount = applicationEngineService.getApplicationEngineRepository().count();
		if (engineCount == 0) {
			List<ApplicationEngine> engines = JsonUtils.fromJson(AcsUtils.streamToString(
					context.getResource("classpath:install/application-engine.json").getInputStream(),
					StandardCharsets.UTF_8), new TypeReference<List<ApplicationEngine>>() {
					});
			logInfo(method, "engines", engines.size());
			Product product = productService.getProductRepository().findBySystemName("kronos");
			Zone zone = zoneService.getZoneRepository().findBySystemName("default-zone");
			for (ApplicationEngine engine : engines) {
				engine.setProductId(product.getId());
				engine.setZoneId(zone.getId());
				engine = applicationEngineService.getApplicationEngineRepository().doInsert(engine,
						CoreConstant.ADMIN_USER);
				logInfo(method, "created applicationEngine ==> name: %s, id: %s", engine.getName(), engine.getId());
			}
		} else {
			logInfo(method, "engineCount: %d", engineCount);
		}
	}

	void checkCreatePrivileges() throws Exception {
		String method = "checkCreatePrivilege";
		long privCount = privilegeService.getPrivilegeRepository().count();
		if (privCount == 0) {
			doCreatePrivileges("classpath:install/privilege-pegasus.json", "pegasus");
			doCreatePrivileges("classpath:install/privilege-kronos.json", "kronos");
			doCreatePrivileges("classpath:install/privilege-rhea.json", "rhea");
			doCreatePrivileges("classpath:install/privilege-apollo.json", "apollo");
		} else {
			logInfo(method, "privCount: %d", privCount);
		}
	}

	void doCreatePrivileges(String resourceName, String productSystemName) throws Exception {
		String method = "createPrivileges";
		List<Privilege> privileges = JsonUtils.fromJson(
				AcsUtils.streamToString(context.getResource(resourceName).getInputStream(), StandardCharsets.UTF_8),
				new TypeReference<List<Privilege>>() {
				});
		logInfo(method, "productSystemName: %s, privileges: %d", productSystemName, privileges.size());
		Product product = productService.getProductRepository().findBySystemName(productSystemName);
		for (Privilege priv : privileges) {
			priv.setProductId(product.getId());
			priv = privilegeService.create(priv, CoreConstant.ADMIN_USER);
			logInfo(method, "created privilege ==> systemName: %s, name: %s, id: %s", priv.getSystemName(),
					priv.getName(), priv.getId());
		}
	}

	void checkCreateCompanies() throws Exception {
		String method = "checkCreateCompanies";
		long companyCount = companyService.getCompanyRepository().count();
		if (companyCount == 0) {
			List<Company> companies = JsonUtils.fromJson(
					AcsUtils.streamToString(context.getResource("classpath:install/company.json").getInputStream(),
							StandardCharsets.UTF_8),
					new TypeReference<List<Company>>() {
					});
			logInfo(method, "companies: %d", companies.size());
			for (Company company : companies) {
				company = companyService.create(company, CoreConstant.ADMIN_USER);
				logInfo(method, "created company ==> name: %s, id: %s", company.getName(), company.getId());
			}
		} else {
			logInfo(method, "companyCount: %d", companyCount);
		}
	}

	void checkCreateSubscriptions() throws Exception {
		String method = "checkCreateSubscriptions";
		long subscriptionCount = subscriptionService.getSubscriptionRepository().count();
		if (subscriptionCount == 0) {
			List<Subscription> subscriptions = JsonUtils.fromJson(
					AcsUtils.streamToString(context.getResource("classpath:install/subscription.json").getInputStream(),
							StandardCharsets.UTF_8),
					new TypeReference<List<Subscription>>() {
					});
			logInfo(method, "subscriptions: %d", subscriptions.size());
			Company company = companyService.getCompanyRepository().findAll().get(0);
			for (Subscription subscription : subscriptions) {
				subscription.setCompanyId(company.getId());
				subscription = subscriptionService.create(subscription, CoreConstant.ADMIN_USER);
				logInfo(method, "created subscription ==> name: %s, id: %s", subscription.getName(),
						subscription.getId());
			}
		} else {
			logInfo(method, "subscriptionCount: %d", subscriptionCount);
		}
	}

	void checkCreateApplications() throws Exception {
		String method = "checkCreateApplications";
		long applicationCount = applicationService.getApplicationRepository().count();
		if (applicationCount == 0) {
			doCreateApplication("classpath:install/application-pegasus.json", "pegasus");
			doCreateApplication("classpath:install/application-rhea.json", "rhea");
			doCreateApplication("classpath:install/application-kronos.json", "kronos");
			doCreateApplication("classpath:install/application-apollo.json", "apollo");
		} else {
			logInfo(method, "applicationCount: %d", applicationCount);
		}
	}

	void doCreateApplication(String resourceName, String productSystemName) throws Exception {
		String method = "doCreateApplication";
		List<Application> applications = JsonUtils.fromJson(
				AcsUtils.streamToString(context.getResource(resourceName).getInputStream(), StandardCharsets.UTF_8),
				new TypeReference<List<Application>>() {
				});
		logInfo(method, "resourceName: %s, applications: %d", resourceName, applications.size());
		Zone zone = zoneService.getZoneRepository().findAll().get(0);
		Company company = companyService.getCompanyRepository().findAll().get(0);
		Subscription subscription = subscriptionService.getSubscriptionRepository().findAll().get(0);
		Product product = productService.getProductRepository().findBySystemName(productSystemName);
		ApplicationEngine engine = applicationEngineService.getApplicationEngineRepository()
				.findByName("default-engine");
		for (Application application : applications) {
			application.setZoneId(zone.getId());
			application.setCompanyId(company.getId());
			application.setSubscriptionId(subscription.getId());
			application.setProductId(product.getId());
			if (product.getSystemName().equals("kronos"))
				application.setApplicationEngineId(engine.getId());
			application = applicationService.create(application, CoreConstant.ADMIN_USER);
			logInfo(method, "created application ==> name: %s, id: %s", application.getName(), application.getId());
		}
	}

	void checkCreateRoles() throws Exception {
		String method = "checkCreateRoles";
		long roleCount = roleService.getRoleRepository().count();
		if (roleCount == 0) {
			doCreateRole("classpath:install/role-pegasus.json", "pegasus", "default-pegasus");
			doCreateRole("classpath:install/role-rhea.json", "rhea", "default-rhea");
			doCreateRole("classpath:install/role-kronos.json", "kronos", "default-kronos");
			doCreateRole("classpath:install/role-apollo.json", "apollo", "default-apollo");
		} else {
			logInfo(method, "roleCount: %d", roleCount);
		}
	}

	void doCreateRole(String resourceName, String productSystemName, String applicationName) throws Exception {
		String method = "doCreateRole";
		List<Role> roles = JsonUtils.fromJson(
				AcsUtils.streamToString(context.getResource(resourceName).getInputStream(), StandardCharsets.UTF_8),
				new TypeReference<List<Role>>() {
				});
		logInfo(method, "roles: %d", roles.size());
		Application application = applicationService.getApplicationRepository().findByName(applicationName);
		Product product = productService.getProductRepository().findBySystemName(productSystemName);
		List<String> privIds = privilegeService.getPrivilegeRepository()
				.findByProductIdAndEnabled(product.getId(), true).stream().map(Privilege::getId)
				.collect(Collectors.toList());
		for (Role role : roles) {
			role.setApplicationId(application.getId());
			role.setProductId(product.getId());
			role.setPrivilegeIds(privIds);
			role = roleService.create(role, CoreConstant.ADMIN_USER);
			logInfo(method, "created role ===> name: %s, id: %s", role.getName(), role.getId());
		}
	}

	void checkCreateUsers() throws Exception {
		String method = "checkCreateUsers";
		long userCount = userService.getUserRepository().count();
		if (userCount == 0) {
			List<User> users = JsonUtils.fromJson(
					AcsUtils.streamToString(context.getResource("classpath:install/user.json").getInputStream(),
							StandardCharsets.UTF_8),
					new TypeReference<List<User>>() {
					});
			logInfo(method, "users: %d", users.size());
			Company company = companyService.getCompanyRepository().findAll().get(0);
			Role pegasusAdminRole = roleService.getRoleRepository().findFirstByNameAndApplicationId(
					"default-pegasus-admin",
					applicationService.getApplicationRepository().findByName("default-pegasus").getId());
			Role rheaAdminRole = roleService.getRoleRepository().findFirstByNameAndApplicationId("default-rhea-admin",
					applicationService.getApplicationRepository().findByName("default-rhea").getId());
			Role kronosAdminRole = roleService.getRoleRepository().findFirstByNameAndApplicationId(
					"default-kronos-admin",
					applicationService.getApplicationRepository().findByName("default-kronos").getId());
			Role apolloAdminRole = roleService.getRoleRepository().findFirstByNameAndApplicationId(
					"default-apollo-admin",
					applicationService.getApplicationRepository().findByName("default-apollo").getId());
			for (User user : users) {
				user.setCompanyId(company.getId());
				user.getRoleIds().add(pegasusAdminRole.getId());
				user.getRoleIds().add(rheaAdminRole.getId());
				user.getRoleIds().add(kronosAdminRole.getId());
				user.getRoleIds().add(apolloAdminRole.getId());
				user = userService.create(user, CoreConstant.ADMIN_USER);
				logInfo(method, "created user ==> email: %s, id: %s", user.getContact().getEmail(), user.getId());
			}
		} else {
			logInfo(method, "userCount: %d", userCount);
		}
	}
}

package com.arrow.pegasus.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.DuplicateLoginException;
import com.arrow.pegasus.InActiveAccountException;
import com.arrow.pegasus.InvalidLoginException;
import com.arrow.pegasus.LockedAccountException;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.RequiredChangePasswordException;
import com.arrow.pegasus.UnverifiedAccountException;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventBuilder;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.data.event.EventStatus;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserAuth;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.data.security.AuthType;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.itus.EventNames;
import com.arrow.pegasus.itus.EventQueues;
import com.arrow.pegasus.itus.GatewayLdapAuth;
import com.arrow.pegasus.repo.UserRepository;
import com.arrow.pegasus.security.Crypto;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.SamlAccountModel;

@Service
public class UserService extends BaseServiceAbstract implements AuthenticationService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EventService eventService;
	@Autowired
	private AuthService authService;
	@Autowired
	private PasswordService passwordService;
	@Autowired
	private AccessKeyService accessKeyService;

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void syncSamlAccounts(String applicationId, List<SamlAccountModel> models) {
		String method = "syncSamlAccount";
		logInfo(method, "applicationId: %s, models size: %d", applicationId, models.size());
		Application application = getCoreCacheService().findApplicationById(applicationId);
		checkEnabled(application, "application");
		Company company = getCoreCacheService().findCompanyById(application.getCompanyId());
		Assert.notNull(company, "company not found");
		Assert.hasText(application.getDefaultSamlEntityId(), "Application has no default SAML set up");
		Auth auth = authService.getAuthRepository().findBySamlIdp(application.getDefaultSamlEntityId());
		if (auth == null) {
			String error = "No SAML Auth found for IDP: " + application.getDefaultSamlEntityId();
			logInfo(method, error);
			throw new AcsLogicalException(error);
		}
		for (SamlAccountModel model : models) {
			try {
				logDebug(method, "processing: %s", model);
				if (!model.validate()) {
					logError(method, "model not valid: %s", model.getPrincipal());
					continue;
				}
				User user = userRepository.findByAuthsTypeAndAuthsRefIdAndAuthsPrincipal(AuthType.SAML, auth.getId(),
						model.getPrincipal());
				if (user == null) {
					// create
					user = new User();
					user.setAdmin(false);
					UserAuth ua = new UserAuth();
					ua.setEnabled(true);
					ua.setPrincipal(model.getPrincipal());
					ua.setRefId(auth.getId());
					ua.setType(AuthType.SAML);
					user.setAuths(Collections.singletonList(ua));
					user.setCompanyId(company.getId());
					Contact contact = new Contact();
					contact.setEmail(model.getEmail());
					contact.setFirstName(model.getFirstName());
					contact.setLastName(model.getLastName());
					user.setContact(contact);
					user.setLogin(model.getPrincipal());
					user.setPassword(passwordService.generateRandomPassword(company.getPasswordPolicy()));
					user.setStatus(UserStatus.Active);
					checkAndPopulateRoleIds(user, model);

					user = create(user, CoreConstant.ADMIN_USER);
					logDebug(method, "created new user: %s", model);
				} else {
					// update
					boolean update = false;
					Contact contact = user.getContact();
					if (!StringUtils.isEmpty(model.getFirstName())
							&& !StringUtils.equals(model.getFirstName(), contact.getFirstName())) {
						contact.setFirstName(model.getFirstName());
						update = true;
					}
					if (!StringUtils.isEmpty(model.getLastName())
							&& !StringUtils.equals(model.getLastName(), contact.getLastName())) {
						contact.setLastName(model.getLastName());
						update = true;
					}
					if (!StringUtils.isEmpty(model.getEmail())
							&& !StringUtils.equals(model.getEmail(), contact.getEmail())) {
						contact.setEmail(model.getEmail());
						update = true;
					}
					if (model.isEnabled()) {
						if (user.getStatus() != UserStatus.Active) {
							user.setStatus(UserStatus.Active);
							update = true;
						}
					} else {
						if (user.getStatus() != UserStatus.InActive) {
							user.setStatus(UserStatus.InActive);
							update = true;
						}
					}
					update |= checkAndPopulateRoleIds(user, model);

					if (update) {
						user = update(user, CoreConstant.ADMIN_USER);
						logInfo(method, "updated user: %s", model);
					} else {
						logInfo(method, "no update is needed for user: %s", model);
					}
				}
			} catch (Throwable e) {
				logError(method, "ERROR processing SAML account", e);
			}
		}
	}

	public User samlAuthenticate(String idp, String principal) {
		String method = "samlAuthenticate";
		Auth auth = authService.getAuthRepository().findBySamlIdp(idp);
		if (auth == null) {
			String error = "No SAML Auth found for IDP: " + idp;
			logInfo(method, error);
			throw new AcsLogicalException(error);
		}

		User user = userRepository.findByAuthsTypeAndAuthsRefIdAndAuthsPrincipal(AuthType.SAML, auth.getId(),
				principal);
		if (user != null) {
			checkAndClearAccountLock(user);
			if (user.getStatus() != UserStatus.Active) {
				logInfo(method, "bad user status for %s --> %s", user.getId(), user.getStatus());
				getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_FAILED)
						.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
						.parameter("reason", "bad status: " + user.getStatus().name()));
				return null;
			} else {
				getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_OK)
						.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
						.parameter("auth", "SAML"));
				logInfo(method, "SAML login success!");
			}
		} else {
			logInfo(method, "User Auth not found for refId: %s, principal: %s", auth.getId(), principal);
		}

		return getCoreCacheHelper().populateUser(user);
	}

	public User authenticate(String login, String password, Application app) {
		String method = "authenticate";

		if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
			logInfo(method, "empty login or password");
			throw new InvalidLoginException();
		}

		// lowercase login
		login = login.toLowerCase();

		User user = userRepository.doFindByLogin(login);
		if (user != null) {
			checkApplication(user, app);
			checkAndClearAccountLock(user);
			if (user.getStatus() != UserStatus.Active) {
				logInfo(method, "bad user status for %s --> %s", login, user.getStatus());
				getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_FAILED)
						.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
						.parameter("reason", "bad status: " + user.getStatus().name()));

				switch (user.getStatus()) {
				case Locked:
					throw new LockedAccountException();
				case InActive:
					throw new InActiveAccountException();
				case Pending:
				case PasswordReset:
				case Active:
					break;
				default:
					throw new AcsLogicalException("Unsupported status: " + user.getStatus().name());
				}
			}

			UserAuth userAuth = user.findFirstEnabledAuth();
			if (userAuth != null) {
				logInfo(method, "found userAuth of type: %s for user: %s", userAuth.getType(), login);
				if (userAuth.getType() == AuthType.LDAP) {
					Auth auth = getCoreCacheService().findAuthById(userAuth.getRefId());
					if (auth != null) {
						GatewayLdapAuth ldapAuth = new GatewayLdapAuth(auth.getLdap().getDomain(),
								auth.getLdap().getUrl(), login, password);

						Application application = getCoreCacheService()
								.findApplicationById(auth.getLdap().getApplicationId());
						Assert.notNull(application, "application not found: " + auth.getLdap().getApplicationId());

						AccessKey accessKey = accessKeyService.findOwnerKey(application.getPri());
						Assert.notNull(accessKey, "owner accessKey not found: " + application.getPri());

						EventBuilder builder = EventBuilder.create().applicationId(auth.getLdap().getApplicationId())
								.name(EventNames.ClientToGateway.AD_AUTHENTICATE).encrypted()
								.parameter(EventParameter.InString("auth", JsonUtils.toJson(ldapAuth)));
						Event event = eventService.sendToGateway(EventQueues.clientToGateway(application.getHid()),
								builder.build());
						Event result = eventService.waitForResult(event, 10000);
						if (result == null) {
							logInfo(method, "ldap login timeout");
							getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_FAILED)
									.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
									.parameter("reason", "ldap login timeout"));
							throw new InvalidLoginException();
						} else if (result.getStatus() == EventStatus.Failed) {
							logInfo(method, "ldap login failed: %s", result.getError());
							getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_FAILED)
									.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
									.parameter("reason", "ldap login failed: " + result.getError()));
							throw new InvalidLoginException();
						} else {
							getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_OK)
									.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
									.parameter("auth", userAuth.getType().name()));
							logInfo(method, "ldap login success!");
						}
					} else {
						logError(method, "No Auth found with id: %s for user %s", userAuth.getRefId(), login);
						getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_FAILED)
								.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
								.parameter("reason", "auth refId not found: " + userAuth.getRefId()));
						throw new InvalidLoginException();
					}
				} else {
					logInfo(method, "unsupported userAuth type: %s", userAuth.getType());
					getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_FAILED)
							.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
							.parameter("reason", "unsupported userAuth type: " + userAuth.getType()));
					throw new InvalidLoginException();
				}
			} else {
				String hashed = getCryptoService().getCrypto().hash(password, user.getSalt());
				if (!hashed.equals(user.getPassword())) {
					logInfo(method, "wrong password for %s", login);
					getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_FAILED)
							.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
							.parameter("reason", "wrong password"));
					checkAndLockAccount(user);
					throw new InvalidLoginException();
				} else {
					getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_OK)
							.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
							.parameter("auth", "default"));
					logInfo(method, "internal login success!");
					// clear previous failed attempts
					clearAccountFailedLogins(user);
				}
			}
		} else {
			logInfo(method, "user not found for %s", login);
			throw new InvalidLoginException();
		}

		if (user.getStatus() == UserStatus.PasswordReset)
			throw new RequiredChangePasswordException();
		else if (user.getStatus() == UserStatus.Pending) {
			// developer registration
			throw new UnverifiedAccountException();
		}

		return getCoreCacheHelper().populateUser(user);
	}

	public User authenticate(String login, String password) {
		return authenticate(login, password, null);
	}

	public User authenticateChangePassword(String login, String password) {
		// TODO should the return type be something simple like a boolean?
		String method = "authenticateChangePassword";
		if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
			logInfo(method, "empty login or password");
			return null;
		}

		// lowercase login
		login = login.toLowerCase();

		logDebug(method, "login: %s, password: %s", login, password);

		User user = userRepository.doFindByLogin(login);
		if (user != null) {
			UserAuth userAuth = user.findFirstEnabledAuth();
			if (userAuth != null) {
				logInfo(method, "found userAuth of type: %s for user: %s", userAuth.getType(), login);
				// TODO the user should not change his/her password in this
				// system
			} else {
				String hashed = getCryptoService().getCrypto().hash(password, user.getSalt());

				logDebug(method, "user.password: %s, user.salt: %s, password: %s, newHash: %s", user.getPassword(),
						user.getSalt(), password, hashed);

				if (!hashed.equals(user.getPassword())) {
					logInfo(method, "wrong password for %s", login);
					// TODO should we log some thing
					// getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LoginFailed)
					// .productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
					// .parameter("reason", "wrong password"));
					checkAndLockAccount(user);
					return null;
				} else {
					logInfo(method, "internal login success!");
				}
			}
		} else {
			logInfo(method, "user not found for %s", login);
		}
		return getCoreCacheHelper().populateUser(user);
	}

	public User loadUserByLogin(String login) {
		// lowercase login
		login = login.toLowerCase();
		return getCoreCacheHelper().populateUser(userRepository.doFindByLogin(login));
	}

	public User create(User user, String who) {
		String method = "create";

		// logical checks
		if (user == null) {
			logInfo(method, "user is null");
			throw new AcsLogicalException("user is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		if (StringUtils.isEmpty(user.getPassword())) {
			logInfo(method, "password is empty");
			throw new AcsLogicalException("passowrd is empty");
		}

		if (user.getContact() == null) {
			logInfo(method, "contact is null");
			throw new AcsLogicalException("contact is null");
		}

		if (user.getAddress() != null) {
			logInfo(method, "address1: %s, address2: %s, city: %s, state: %s, zip: %s, country: %s",
					user.getAddress().getAddress1(), user.getAddress().getAddress2(), user.getAddress().getCity(),
					user.getAddress().getState(), user.getAddress().getZip(), user.getAddress().getCountry());
		} else {
			logInfo(method, "address is null");
		}

		// lowercase login
		user.setLogin(user.getLogin().toLowerCase());

		User duplicateCheck = userRepository.doFindByLogin(user.getLogin());
		if (duplicateCheck != null) {
			logWarn(method, "duplicate login exception! login=%s", user.getLogin());
			throw new DuplicateLoginException();
		}

		// security
		Crypto crypto = getCryptoService().getCrypto();
		user.setHashedLogin(crypto.internalHash(user.getLogin()));
		user.setLogin(crypto.internalEncrypt(user.getLogin()));
		user.setSalt(crypto.randomToken());
		user.setPassword(crypto.hash(user.getPassword(), user.getSalt()));

		// persist user
		user = userRepository.doInsert(user, who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.CREATE_USER)
				.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(who));

		return user;
	}

	public User update(User user, String who) {
		String method = "update";

		// logical checks
		if (user == null) {
			logInfo(method, "user is null");
			throw new AcsLogicalException("user is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		if (user.getContact() == null) {
			logInfo(method, "contact is null");
			throw new AcsLogicalException("contact is null");
		}

		if (user.getAddress() != null) {
			logInfo(method, "address1: %s, address2: %s, city: %s, state: %s, zip: %s, country: %s",
					user.getAddress().getAddress1(), user.getAddress().getAddress2(), user.getAddress().getCity(),
					user.getAddress().getState(), user.getAddress().getZip(), user.getAddress().getCountry());
		} else {
			logInfo(method, "address is null");
		}

		// update
		userRepository.doSave(user, who);

		// TODO check for property changes??
		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.UPDATE_USER)
				.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(who));

		// clear cache
		getCoreCacheService().clearUser(user);

		return user;
	}

	public String resetPassword(User user, String who) {
		String method = "resetPassword";
		Assert.notNull(user, "user is missing");

		checkOnExternalSourceAuth(user);

		Company company = getCoreCacheService().findCompanyById(user.getCompanyId());
		Assert.notNull(company, "company not found: " + user.getCompanyId());

		String temporary = passwordService.generateRandomPassword(company.getPasswordPolicy());
		user.setPassword(getCryptoService().getCrypto().hash(temporary, user.getSalt()));
		if (user.getStatus() != UserStatus.Pending)
			user.setStatus(UserStatus.PasswordReset);

		logDebug(method, "temporary password: %s, salt: %s, hashed password: %s", temporary, user.getSalt(),
				user.getPassword());

		update(user, who);
		logInfo(method, "password has been reset for %s", user.getContact().getEmail());

		return temporary;
	}

	public List<String> setNewPassword(User user, String newPassword, String who) {
		String method = "setNewPassword";
		Assert.notNull(user, "user is missing");
		Assert.hasText(newPassword, "newPassword is missing");

		checkOnExternalSourceAuth(user);

		Company company = getCoreCacheService().findCompanyById(user.getCompanyId());
		Assert.notNull(company, "company not found: " + user.getCompanyId());

		List<String> result = passwordService.validatePassword(company.getPasswordPolicy(), user, newPassword);
		if (result.isEmpty()) {
			int max = company.getPasswordPolicy().getHistorical();
			if (max > 0) {
				List<String> list = user.getOldPasswords();
				list.add(0, user.getPassword());
				while (list.size() > max) {
					list.remove(list.size() - 1);
				}
			}
			user.setPassword(getCryptoService().getCrypto().hash(newPassword, user.getSalt()));
			if (user.getStatus() == UserStatus.PasswordReset)
				user.setStatus(UserStatus.Active);
			update(user, who);
			logInfo(method, "new password persisted");
		} else {
			result.forEach(error -> {
				logInfo(method, "---> %s", error);
			});
		}
		return result;
	}

	private void checkOnExternalSourceAuth(User user) {
		UserAuth authUserEnabled = user.getAuths().stream()
				.filter(auth -> (auth.getType().equals(AuthType.LDAP) || auth.getType().equals(AuthType.LDAP))
						&& auth.isEnabled())
				.findFirst().orElse(null);
		Assert.isNull(authUserEnabled, "User has auth type: LDAP or SAML");
	}

	private void checkAndLockAccount(User user) {
		String method = "checkAndLockAccount";

		// increment failed logins
		user.setFailedLogins(user.getFailedLogins() + 1);
		logInfo(method, "email: %s, failedLogins: %d", user.getContact().getEmail(), user.getFailedLogins());

		Company company = getCoreCacheService().findCompanyById(user.getCompanyId());
		Assert.notNull(company, "company not found: " + user.getCompanyId());

		if (user.getFailedLogins() > company.getLoginPolicy().getMaxFailedLogins()) {
			user.setStatus(UserStatus.Locked);
			user.setAccountLockTimeout(Instant.now().getEpochSecond() + company.getLoginPolicy().getLockTimeoutSecs());
			logInfo(method, "account %s is now locked!", user.getContact().getEmail());
		}
		update(user, user.getId());

		if (user.getStatus() == UserStatus.Locked)
			throw new LockedAccountException();
	}

	private void checkAndClearAccountLock(User user) {
		String method = "checkAndClearAccount";
		if (user.getStatus() == UserStatus.Locked) {
			if (Instant.now().getEpochSecond() > user.getAccountLockTimeout()) {
				user.setStatus(UserStatus.Active);
				clearAccountFailedLogins(user);
				// user.setFailedLogins(0);
				// user.setAccountLockTimeout(0);
				// update(user, user.getId());
				logInfo(method, "lock is now cleared for %s", user.getContact().getEmail());
			} else {
				logInfo(method, "lock is NOT cleared yet for %s", user.getContact().getEmail());
			}
		}
	}

	private void clearAccountFailedLogins(User user) {
		if (user != null) {
			user.setFailedLogins(0);
			user.setAccountLockTimeout(0);
			update(user, user.getId());
		}
	}

	private boolean checkAndPopulateRoleIds(User user, SamlAccountModel model) {
		String method = "checkAndPopulateRoleIds";
		boolean result = false;
		if (model.getDefaultRoleHids() != null) {
			for (String roleHid : model.getDefaultRoleHids()) {
				Role role = getCoreCacheService().findRoleByHid(roleHid);
				if (role != null) {
					if (!user.getRoleIds().contains(role.getId())) {
						user.getRoleIds().add(role.getId());
						result = true;
					}
				} else {
					logError(method, "invalid roleHid provided: %s", roleHid);
				}
			}
		}
		return result;
	}

	@Override
	public User findByLogin(String username) {
		return loadUserByLogin(username);
	}

	public Long deleteByCompanyId(String companyId, String who) {
		String method = "deleteByCompanyId";
		logInfo(method, "companyId: %s, who: %s", companyId, who);
		return userRepository.deleteByCompanyId(companyId);
	}

	private void checkApplication(User user, Application application) {
		String method = "checkApplication";
		if (application != null && !user.isAdmin() && !user.getCompanyId().equals(application.getCompanyId())) {
			logError(method, "user %s is not allowed to login to application %s: company does not match", user.getId(),
					application.getId());
			getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.User.LOGIN_FAILED)
					.productName(ProductSystemNames.PEGASUS).objectId(user.getId()).by(user.getId())
					.parameter("reason", "company does not match").parameter("application_id", application.getId())
					.parameter("application_companyId", application.getCompanyId())
					.parameter("user_companyId", user.getCompanyId()));
			throw new IllegalArgumentException("Company does not match");
		}
	}
}

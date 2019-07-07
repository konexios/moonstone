package com.arrow.kronos.service;

import static com.arrow.kronos.KronosAuditLog.Developer.ActivateDeveloperAccount;
import static com.arrow.kronos.KronosAuditLog.Developer.DeactivateDeveloperAccount;
import static com.arrow.kronos.KronosAuditLog.Developer.ExpireDeveloperAccount;
import static com.arrow.kronos.KronosAuditLog.Developer.ExpireUnverifiedDeveloperAccount;
import static com.arrow.kronos.KronosAuditLog.Developer.RegisterDeveloper;
import static com.arrow.kronos.KronosAuditLog.Developer.ResetDeveloperPassword;
import static com.arrow.kronos.KronosAuditLog.Developer.SetDeveloperPassword;
import static com.arrow.kronos.KronosAuditLog.Developer.VerifyDeveloperAccount;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.KioskSignup;
import com.arrow.kronos.data.KronosUser;
import com.arrow.kronos.data.UserRegistration;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.client.api.ClientApplicationApi;
import com.arrow.pegasus.client.api.ClientApplicationEngineApi;
import com.arrow.pegasus.client.api.ClientRoleApi;
import com.arrow.pegasus.client.api.ClientSubscriptionApi;
import com.arrow.pegasus.client.api.ClientUserApi;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.TempToken;
import com.arrow.pegasus.data.YesNoInherit;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.ProductFeature;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserEULA;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.service.BaseServiceAbstract;
import com.arrow.pegasus.service.PlatformConfigService;
import com.arrow.pegasus.service.TempTokenService;

@Service
public class DeveloperService extends BaseServiceAbstract {

    @Autowired
    private EmailService emailService;
    @Autowired
    private TempTokenService tempTokenService;
    @Autowired
    private KioskSignupService kioskSignupService;
    @Autowired
    private ClientSubscriptionApi clientSubscriptionApi;
    @Autowired
    private ClientApplicationApi clientApplicationApi;
    @Autowired
    private ClientApplicationEngineApi clientApplicationEngineApi;
    @Autowired
    private ClientRoleApi clientRoleApi;
    @Autowired
    private ClientUserApi clientUserApi;
    @Autowired
    private UserRegistrationService userRegistrationService;
    @Autowired
    private KronosUserService kronosUserService;
    @Autowired
    private KronosApplicationProvisioningService kronosApplicationProvisioningService;
    @Autowired
    private PlatformConfigService platformConfigService;

    private static final String NEW_SUBSCRIPTION_APPLICATION_NAME_DESCRIPTION = "Arrow Connect - Free Account";

    @Value("${com.arrow.pegasus.data.profile.companyId:}")
    private String companyIdConfig;
    @Value("${com.arrow.pegasus.data.profile.applicationName:}")
    private String applicationNameConfig;
    @Value("${com.arrow.pegasus.data.profile.applicationEngineName:}")
    private String applicationEngineNameConfig;
    @Value("${com.arrow.pegasus.data.profile.zoneSystemName:}")
    private String zoneSystemNameConfig;
    @Value("#{'${com.arrow.pegasus.data.profile.roleName:}'.split(',')}")
    private List<String> roleNameConfigs;
    @Value("${com.arrow.pegasus.data.profile.trialDuration:365}")
    private Integer trialDurationConfig;
    @Value("${com.arrow.pegasus.data.profile.periodWithoutActivation:7}")
    private Integer periodWithoutActivation;
    @Value("${com.arrow.pegasus.data.profile.token.expire.seconds:}")
    private Integer tokenExpireSeconds;

    public KioskSignup findPreRegistration(String tempTokenHID, String who) {

        String method = "findPreRegistration";
        logDebug(method, "...");

        TempToken tempToken = tempTokenService.getTempTokenRepository().findByHid(tempTokenHID);
        Assert.notNull(tempToken, "tempToken is null");

        // TODO move to TempTokenService isValidToken

        // case 1) already expired
        if (tempToken.isExpired())
            throw new AcsLogicalException("token has expired");

        // case 2) duration has exceed the defined time to expire, update to
        // expired
        Instant created = tempToken.getCreatedDate();
        Instant now = Instant.now();
        long duration = now.getEpochSecond() - created.getEpochSecond();
        if (duration > tempToken.getTimeToExpireSeconds()) {
            tempToken.setExpired(true);
            tempTokenService.update(tempToken, who);
            throw new AcsLogicalException("token has expired");
        }

        // case 3) single use, update as expired as token is being used
        if (tempToken.isSingleUse()) {
            tempToken.setExpired(true);
            tempTokenService.update(tempToken, who);
        }

        String kioskSignupId = tempToken.getProperties().get("kioskSignupId");
        Assert.hasText(kioskSignupId, "kioskSignupId is empty");

        return kioskSignupService.getKioskSignupRepository().findById(kioskSignupId).orElse(null);
    }

    public User register(UserRegistration userRegistration, String who) {
        Assert.notNull(userRegistration, "userRegistration is null");
        Assert.hasText(userRegistration.getFirstName(), "firstName is missing");
        Assert.hasText(userRegistration.getLastName(), "lastName is missing");
        Assert.hasText(userRegistration.getEmail(), "email is missing");
        Assert.hasText(userRegistration.getCompanyName(), "companyName is missing");
        Assert.hasText(userRegistration.getCompanyWebSite(), "companyWebSite is missing");
        Assert.hasText(userRegistration.getProjectDescription(), "projectDescription is missing");
        Assert.hasText(userRegistration.getTitle(), "title is missing");
        Assert.hasText(who, "who is empty");
        Assert.hasText(companyIdConfig, "companyIdConfig is missing");

        String method = "register";
        logDebug(method, "...");

        validateConfig();

        User userExist = clientUserApi.findByLogin(userRegistration.getEmail());
        if (userExist != null) {
            logInfo(method, "User with login %s already exists", userRegistration.getEmail());
            throw new AcsLogicalException("User with this login already exists");
        }

        userRegistration.setStatus(UserRegistrationStatus.Pending);
        userRegistration = userRegistrationService.create(userRegistration, who);

        // create Pegasus user (status = Pending)
        User user = new User();
        Contact contact = new Contact();
        contact.setEmail(userRegistration.getEmail());
        contact.setFirstName(userRegistration.getFirstName());
        contact.setLastName(userRegistration.getLastName());
        user.setContact(contact);
        user.setLogin(userRegistration.getEmail());
        user.setPassword(userRegistration.getEmail());
        user.setCompanyId(companyIdConfig);

        Product product = getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS);
        Assert.notNull(product, "product not found!");

        // eula
        UserEULA userEULA = new UserEULA();
        userEULA.setProductId(product.getId());
        userEULA.setAgreedDate(Instant.now());
        user.getEulas().add(userEULA);

        // create and persist user
        user = clientUserApi.create(user, who);

        logDebug(method, "create.. password: %s", user.getPassword());

        // generate temporary password
        clientUserApi.resetPassword(user, who);
        logDebug(method, "resetPassword.. password: %s", user.getPassword());

        // must lookup the user
        user = clientUserApi.findByLogin(userRegistration.getEmail());

        // update user (status = Pending) after reset password
        user.setStatus(UserStatus.Pending);
        user = clientUserApi.update(user, who);
        logDebug(method, "update.. password: %s", user.getPassword());

        // create tempToken
        TempToken tempToken = new TempToken();
        tempToken.setCompanyId(companyIdConfig);
        tempToken.setSingleUse(false);
        tempToken.setTimeToExpireSeconds(tokenExpireSeconds);

        // user properties
        tempToken.addProperty("userId", user.getId());
        tempToken = tempTokenService.create(tempToken, who);

        // send Email (account created, account verification & temporary
        // password)
        user.setLogin(getCryptoService().getCrypto().internalDecrypt(user.getLogin()));
        emailService.sendRegisteredDeveloperEmail(user.getContact().getEmail(), user.getContact().getFirstName(),
                tempToken);

        // audit log
        getAuditLogService()
                .save(AuditLogBuilder.create().type(RegisterDeveloper).productName(ProductSystemNames.KRONOS)
                        .objectId(userRegistration.getId()).by(who).parameter("email", userRegistration.getEmail()));

        return user;
    }

    public User resendVerifyEmail(String tempTokenHID, String who) {
        Assert.hasText(tempTokenHID, "tempTokenHID is null");

        String method = "resendVerifyEmail";
        logDebug(method, "...");

        validateConfig();

        // existing expired tempToken
        TempToken existingTempToken = tempTokenService.getTempTokenRepository().findByHid(tempTokenHID);
        Assert.notNull(existingTempToken, "existingTempToken is null");

        // userId
        String userId = existingTempToken.getProperties().get("userId");
        Assert.hasText(userId, "userId is empty");

        // lookup user
        User user = clientUserApi.findById(userId);
        Assert.notNull(user, "user not found!");

        // create tempToken
        TempToken tempToken = new TempToken();
        tempToken.setCompanyId(companyIdConfig);
        tempToken.setSingleUse(false);
        tempToken.setTimeToExpireSeconds(tokenExpireSeconds);

        // user properties
        tempToken.addProperty("userId", user.getId());
        tempToken = tempTokenService.create(tempToken, who);

        // send email
        emailService.sendRegisteredDeveloperEmail(user.getContact().getEmail(), user.getContact().getFirstName(),
                tempToken);

        return user;
    }

    public User verifyAccount(String tempTokenHID, String who) {
        Assert.hasText(tempTokenHID, "tempTokenHID is null");
        Assert.hasText(who, "who is empty");

        String method = "verifyAccount";
        logDebug(method, "...");

        validateConfig();

        TempToken tempToken = tempTokenService.getTempTokenRepository().findByHid(tempTokenHID);
        Assert.notNull(tempToken, "tempToken is null");

        // TODO move to TempTokenService isValidToken

        // case 1) already expired
        if (tempToken.isExpired())
            throw new AcsLogicalException("token has expired");

        // case 2) duration has exceed the defined time to expire, update to
        // expired
        Instant created = tempToken.getCreatedDate();
        Instant now = Instant.now();
        long duration = now.getEpochSecond() - created.getEpochSecond();
        if (duration > tempToken.getTimeToExpireSeconds()) {
            tempToken.setExpired(true);
            tempTokenService.update(tempToken, who);
            throw new AcsLogicalException("token has expired");
        }

        // case 3) single use, update as expired as token is being used
        if (tempToken.isSingleUse()) {
            tempToken.setExpired(true);
            tempTokenService.update(tempToken, who);
        }

        String userId = tempToken.getProperties().get("userId");
        Assert.hasText(userId, "userId is empty");

        User user = clientUserApi.findById(userId);
        Assert.notNull(user, "user not found");

        // update userRegistration status to Verified
        UserRegistration userRegistration = findUserRegistration(user.getContact().getEmail());
        Assert.notNull(userRegistration, "userRegistration not found");

        Application newApplication = null;
        if (userRegistration.getStatus() == UserRegistrationStatus.Pending) {

            userRegistration.setStatus(UserRegistrationStatus.Verified);
            userRegistration = userRegistrationService.update(userRegistration, who);
            logDebug(method, "updated user registration status, status: %s", userRegistration.getStatus());

            // update Pegasus user (status = PasswordReset)
            user.setStatus(UserStatus.PasswordReset);
            user = clientUserApi.update(user, who);

            user = clientUserApi.findById(user.getId());
            logDebug(method, "updated user status, status: %s", user.getStatus());

            // audit log
            getAuditLogService()
                    .save(AuditLogBuilder.create().type(VerifyDeveloperAccount).productName(ProductSystemNames.KRONOS)
                            .by(who).objectId(user.getId()).parameter("status", user.getStatus().toString()));

            // update userRegistration status to Activated
            userRegistration = findUserRegistration(user.getContact().getEmail());
            userRegistration.setStatus(UserRegistrationStatus.Activated);
            userRegistration = userRegistrationService.update(userRegistration, who);
            logDebug(method, "id: %s, email: %s, status: %s", userRegistration.getId(), userRegistration.getEmail(),
                    userRegistration.getStatus());

            // create subscription
            Subscription newSubscription = createSubscription(user, userRegistration.getEmail(), who);
            logDebug(method, "new subscription created... subscriptionId: %s", newSubscription.getId());

            // create application instance
            Application application = getCoreCacheService().findApplicationByName(applicationNameConfig);
            Assert.notNull(application, "Unable to find application! name=" + applicationNameConfig);
            newApplication = createApplication(newSubscription, application, userRegistration.getEmail(), who);
            logDebug(method, "new application created... applicationId: %s", newApplication.getId());

            // create role (copy existing administration role)
            // create role (default user role)
            for (String roleName : roleNameConfigs) {
                Role newRole = createRole(roleName, application.getId(), newApplication.getId(), who);
                logDebug(method, "new role created... name: %s, applicationId: %s", newRole.getName(),
                        newRole.getApplicationId());
                // assign administrator role to user
                user.getRoleIds().add(newRole.getId());
            }

            // provision kronosApplication
            kronosApplicationProvisioningService.provisionApplication(newApplication.getId(), true, who);

            // create kronosUser
            createKronosUser(user.getId(), newApplication.getId(), who);

            // must update to persist role association
            user = clientUserApi.update(user, who);

            // audit log
            getAuditLogService()
                    .save(AuditLogBuilder.create().type(ActivateDeveloperAccount).productName(ProductSystemNames.KRONOS)
                            .by(who).objectId(user.getId()).parameter("status", user.getStatus().toString()));
        } else {
            Role role = clientRoleApi.findById(user.getRoleIds().get(0));
            Assert.notNull(role, "role not found");
            newApplication = clientApplicationApi.findById(role.getApplicationId());
            Assert.notNull(newApplication, "application not found");
        }

        // generate temporary password
        String tempPassword = clientUserApi.resetPassword(user, who);
        logDebug(method, "resetPassword.. password: %s", user.getPassword());

        // must lookup the user
        user = clientUserApi.findByLogin(userRegistration.getEmail());

        // decrypt login
        user.setLogin(getCryptoService().getCrypto().internalDecrypt(user.getLogin()));
        user.setPassword(tempPassword);

        // send account activated email
        emailService.sendAccountActivatedEmail(user.getContact().getEmail(), user.getContact().getFirstName(),
                newApplication.getCode());

        return user;
    }

    private Subscription createSubscription(User user, String email, String who) {
        String method = "createSubscription";

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
        endDate = endDate.plusDays(trialDurationConfig);

        Subscription newSubscription = new Subscription();
        newSubscription.setCompanyId(companyIdConfig);
        newSubscription.setName(NEW_SUBSCRIPTION_APPLICATION_NAME_DESCRIPTION + " (" + email + ")");
        newSubscription.setDescription(NEW_SUBSCRIPTION_APPLICATION_NAME_DESCRIPTION + " for " + email + ".");
        newSubscription.setStartDate(startDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        newSubscription.setEndDate(endDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        newSubscription.setEnabled(true);
        newSubscription.setContact(user.getContact());
        newSubscription.setBillingContact(user.getContact());

        newSubscription = clientSubscriptionApi.create(newSubscription, who);
        logDebug(method, "id: %s, name: %s", newSubscription.getId(), newSubscription.getName());

        // lookup new subscription
        newSubscription = clientSubscriptionApi.findById(newSubscription.getId());
        Assert.notNull(newSubscription, "Unable to find subscription! subscriptionId=" + newSubscription.getId());

        return newSubscription;
    }

    private Application createApplication(Subscription subscription, Application application, String email,
            String who) {
        String method = "createApplication";

        Product product = getCoreCacheService().findProductById(application.getProductId());
        Assert.notNull(product, "Product not found! productId: " + application.getProductId());

        Application newApplication = new Application();
        newApplication.setName(NEW_SUBSCRIPTION_APPLICATION_NAME_DESCRIPTION + " (" + email + ")");
        newApplication.setDescription(NEW_SUBSCRIPTION_APPLICATION_NAME_DESCRIPTION + " for " + email + ".");
        newApplication.setCode(null);
        newApplication.setCompanyId(companyIdConfig);
        newApplication.setSubscriptionId(subscription.getId());
        newApplication.setProductId(application.getProductId());
        newApplication.setProductExtensionIds(null);

        // associate FIRMWARE MANAGEMENT feature to application
        for (ProductFeature pf : product.getFeatures()) {
            if (pf.getSystemName().equals(KronosConstants.ProductFeatures.FIRMWARE_MANAGEMENT)) {
                newApplication.getProductFeatures().add(pf.getSystemName());
                break;
            }
        }

        // TAM - use platform config
        // Zone applicationZone =
        // getCoreCacheService().findZoneBySystemName(zoneSystemNameConfig);
        Zone applicationZone = platformConfigService.getConfig().getRefZone();
        Assert.notNull(applicationZone, "Unable to find zone. systemName=" + zoneSystemNameConfig);
        newApplication.setZoneId(applicationZone.getId());

        newApplication.setApiSigningRequired(YesNoInherit.NO);

        // assign application engine
        ApplicationEngine applicationEngine = clientApplicationEngineApi.findByName(applicationEngineNameConfig);
        Assert.notNull(applicationEngine, "Unable to find application engine! name=" + applicationEngineNameConfig);
        newApplication.setApplicationEngineId(applicationEngine.getId());

        newApplication.setConfigurations(Collections.emptyList());
        newApplication.setEnabled(true);

        newApplication = clientApplicationApi.create(newApplication, who);
        logDebug(method, "id: %s, name: %s", newApplication.getId(), newApplication.getName());

        newApplication = clientApplicationApi.findById(newApplication.getId());

        return newApplication;
    }

    private Role createRole(String roleName, String applicationId, String newApplicationId, String who) {
        String method = "createRole";

        Role role = clientRoleApi.findByNameAndApplicationId(roleName, applicationId);
        Assert.notNull(role, "Unable to find role! name=" + roleName + " application=" + applicationNameConfig);

        Role newRole = new Role();
        newRole.setName(role.getName());
        newRole.setDescription(role.getDescription());
        newRole.setApplicationId(newApplicationId);
        newRole.setProductId(role.getProductId());
        newRole.setEditable(role.isEditable());
        newRole.setEnabled(role.isEnabled());
        newRole.setPrivilegeIds(role.getPrivilegeIds());

        newRole = clientRoleApi.create(newRole, who);
        logDebug(method, "id: %s, name: %s", newRole.getId(), newRole.getName());

        newRole = clientRoleApi.findByIdAndApplicationId(newRole.getId(), newApplicationId);
        Assert.notNull(newRole, "Unable to find role! roleId=" + newRole.getId());

        return newRole;
    }

    private KronosUser createKronosUser(String userId, String applicationId, String who) {
        String method = "createKronosUser";

        KronosUser kronosUser = new KronosUser();
        kronosUser.setUserId(userId);
        kronosUser.setApplicationId(applicationId);
        kronosUser = kronosUserService.create(kronosUser, who);
        logDebug(method, "id: %s, applicationId: %s", kronosUser.getId(), kronosUser.getApplicationId());

        kronosUser = kronosUserService.getKronosUserRepository().findById(kronosUser.getId()).orElse(null);
        Assert.notNull(kronosUser, "Unable to find kronosUser! id=" + kronosUser.getId());

        return kronosUser;
    }

    public void expireUnverifiedAccount() {
        String method = "expireUnverifiedAccount";
        validateConfig();

        Instant date = Instant.now().minus(periodWithoutActivation, ChronoUnit.DAYS);
        List<UserRegistration> userRegistrationList = userRegistrationService.getUserRegistrationRepository()
                .findByStatus(UserRegistrationStatus.Pending);
        userRegistrationList = userRegistrationList.stream()
                .filter(userRegistration -> (userRegistration.getCreatedDate().compareTo(date) <= 0))
                .collect(Collectors.toList());

        userRegistrationList.parallelStream().forEach(userRegistration -> {
            // update userRegistration status to NeverVerified
            userRegistration.setStatus(UserRegistrationStatus.NeverVerified);
            userRegistration = userRegistrationService.update(userRegistration, CoreConstant.ADMIN_USER);

            User user = clientUserApi.findByLogin(userRegistration.getEmail());
            if (user != null) {
                // deactivate account
                user = deactivateAccount(user, user.getId());

                // audit log
                getAuditLogService().save(AuditLogBuilder.create().type(ExpireUnverifiedDeveloperAccount)
                        .productName(ProductSystemNames.KRONOS).by(user.getId()).objectId(user.getId()));

                // send Email (account has been deactivated)
                String login = getCryptoService().getCrypto().internalDecrypt(user.getLogin());
                emailService.sendUnverifiedAccountExpiredEmail(user.getContact().getEmail(), login,
                        user.getContact().fullName());
            } else {
                logError(method, "user not found with login: %s", userRegistration.getEmail());
            }
        });
    }

    public User expireAccount(User user, String who) {
        validateConfig();

        // update userRegistration status to Expired
        UserRegistration userRegistration = findUserRegistration(user.getContact().getEmail());
        userRegistration.setStatus(UserRegistrationStatus.Expired);
        userRegistration = userRegistrationService.update(userRegistration, who);

        // deactivate account
        user = deactivateAccount(user, who);

        // audit log
        getAuditLogService().save(AuditLogBuilder.create().type(ExpireDeveloperAccount)
                .productName(ProductSystemNames.KRONOS).by(who).objectId(user.getId()));

        // send Email (account has been deactivated)
        String login = getCryptoService().getCrypto().internalDecrypt(user.getLogin());
        emailService.sendDeveloperAccountExpiredEmail(user.getContact().getEmail(), login,
                user.getContact().fullName());

        return user;
    }

    public User deactivateAccount(User user, String who) {
        Assert.notNull(user, "user is null");
        Assert.hasText(who, "who is empty");
        validateConfig();

        // update Pegasus user (status = InActive)
        user.setStatus(UserStatus.InActive);
        user = clientUserApi.update(user, who);

        // audit log (account inactive)
        getAuditLogService()
                .save(AuditLogBuilder.create().type(DeactivateDeveloperAccount).productName(ProductSystemNames.KRONOS)
                        .by(who).objectId(user.getId()).parameter("status", user.getStatus().toString()));

        return user;
    }

    // public User reactivateAccount(User user, String who) {
    // Assert.notNull(user, "user is null");
    // Assert.hasText(who, "who is empty");
    // validateConfig();
    //
    // // activate account
    // user = activateAccount(user, who);
    //
    // // send Email (account has been re-activated)
    // String login =
    // getCryptoService().getCrypto().internalDecrypt(user.getLogin());
    // emailService.sendReactivateDeveloperAccountEmail(user.getContact().getEmail(),
    // login,
    // user.getContact().fullName());
    //
    // // audit log
    // getAuditLogService().save(AuditLogBuilder.create().type(ReactivateDeveloperAccount)
    // .productName(ProductSystemNames.KRONOS).by(who).objectId(user.getId()));
    //
    // return user;
    // }

    public UserRegistration findUserRegistration(String email) {
        Assert.hasText(email, "email is empty");
        validateConfig();
        return userRegistrationService.getUserRegistrationRepository().findByEmail(email);
    }

    public List<String> setPassword(User user, String newPassword, String who) {
        Assert.notNull(user, "user is null");
        Assert.hasText(newPassword, "newPassword is empty");
        Assert.hasText(who, "who is empty");
        validateConfig();

        // update Pegasus user (password = collected from user)
        List<String> result = clientUserApi.setNewPassword(user, newPassword, who);
        if (result.isEmpty()) {
            // audit log (password updated)
            getAuditLogService().save(AuditLogBuilder.create().type(SetDeveloperPassword)
                    .productName(ProductSystemNames.KRONOS).by(who).objectId(user.getId()));

            // send Email (password has been updated)
            String login = getCryptoService().getCrypto().internalDecrypt(user.getLogin());
            emailService.sendPasswordUpdatedEmail(user.getContact().getEmail(), login, user.getContact().fullName());
        }
        return result;
    }

    public void resetPassword(User user, String who) {
        Assert.notNull(user, "user is null");
        Assert.hasText(who, "who is empty");
        validateConfig();

        // update Pegasus user (status = PasswordReset)
        String tempPassword = clientUserApi.resetPassword(user, who);

        // send Email (temporary password / url to Arrow Connect portal)
        String login = getCryptoService().getCrypto().internalDecrypt(user.getLogin());
        emailService.sendPasswordResetEmail(user.getContact().getEmail(), login, tempPassword,
                user.getContact().fullName());

        // audit log
        getAuditLogService().save(AuditLogBuilder.create().type(ResetDeveloperPassword)
                .productName(ProductSystemNames.KRONOS).by(who).objectId(user.getId()));
    }

    private void validateConfig() {
        Assert.hasText(companyIdConfig, "com.arrow.pegasus.data.profile.companyId is not defined");
        Assert.hasText(applicationNameConfig, "com.arrow.pegasus.data.profile.applicationName is not defined");
        Assert.hasText(applicationEngineNameConfig,
                "com.arrow.pegasus.data.profile.applicationEngineName is not defined");
        Assert.hasText(zoneSystemNameConfig, "com.arrow.pegasus.data.profile.zoneSystemName is not defined");
    }
}

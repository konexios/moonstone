var pegasus = angular.module("pegasus", [
    "ui.router", 
    "ui.bootstrap", 
    "ui.bootstrap.datetimepicker",
    "ui.dateTimeInput",
    "oc.lazyLoad",
    "ngSanitize", 
    "ngCookies",
    "ngMessages",
    "ngScrollbars",
    "toastr",
    "angularSpinner",
    "pegasus.directives", 
    "pegasus.filters", 
    "pegasus.services", 
    "pegasus.controllers"
]); 

pegasus.config(["$ocLazyLoadProvider", "toastrConfig", function($ocLazyLoadProvider, toastrConfig) {
    $ocLazyLoadProvider.config({
        // global configuration goes here
    });

    // toastr: see https://github.com/Foxandxss/angular-toastr#toastr-customization
    angular.extend(toastrConfig, {
        "closeButton": true,
        "debug": false,
        "newestOnTop": true,
        "progressBar": false,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    });
}]);

/* Setup global settings */
pegasus.factory("settings", ["$rootScope", function($rootScope) {
    // supported languages
    var settings = {};

    $rootScope.settings = settings;

    return settings;
}]);

var services = angular.module("pegasus.services", []);
var controllers = angular.module("pegasus.controllers", []);
var directives = angular.module("pegasus.directives", []);
var filters = angular.module("pegasus.filters", []);

/* Setup Routing For All Pages */
pegasus.config(["$stateProvider", "$urlRouterProvider", "$httpProvider",
    function($stateProvider, $urlRouterProvider, $httpProvider) {

    var xsrfTokenName = "XSRF-TOKEN-PEGASUS-WEB";
    $httpProvider.defaults.xsrfCookieName = xsrfTokenName;
    $httpProvider.defaults.xsrfHeaderName = "X-" + xsrfTokenName;

	$httpProvider.interceptors.push(["$rootScope", "$q", "$location", "$injector", "SecurityService",
        function($rootScope, $q, $location, $injector, SecurityService) {

        var loadingCount = 0;

		return {
            request: function (request) {
                if(++loadingCount === 1) $rootScope.$broadcast('loading:progress');
                return request;
            },
			response : function(response) {
                if(--loadingCount === 0) $rootScope.$broadcast('loading:finish');
				return response;
			},
			responseError : function(response) {
				console.log('$httpProvider.interceptors responseError entered...');
				console.log(response);
                if(--loadingCount === 0) $rootScope.$broadcast('loading:finish');

                var url = $location.url();

				// check for unauthorized response
				if (response.status === 401) {

				    if (url.indexOf("/signin") === -1) {
                        SecurityService.deleteSession();
                        $location.url("/signin?status=session-expired");
                    } else {
                        $location.url("/signin?status=fail-credential");
					}
				}

				if (response.status === 403) {
                    if (url.indexOf("/signin") === -1) {
                        var modalInstance = $injector.get('$uibModal')
                            .open({
                                animation: true,
                                templateUrl: 'partials/modals/forbiddenAccess.html',
                                size: 'md'
                            }).closed.then(function () {
                                $location.url("/dashboard");
                            });
                    }
                }

				return $q.reject(response);
			}
		};
	}]);

	var checkLoggedin = ['$rootScope', '$q', function($rootScope, $q) {
		console.log("checkLoggedIn entered...");
		if ($rootScope.user != null) {
			return true;			
		} else {
			return $q.reject('notAuthorized');			
		}
	}];

	$urlRouterProvider.otherwise("/signin");
    
    $stateProvider

    	// signin
    	.state("signin", {
    		url: "/signin",
    		params: {
    			redirect: null
		  	},
    		templateUrl: "partials/signin.html",
    		data: {pageTitle: "Sign In"},
    		controller: "LoginController",
            resolve: {
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/controllers/LoginController.js"
                        ] 
                    });
                }]
            }
    	})

        .state("forgotpassword", {
            url: "/forgotpassword",
            params: {
                redirect: null
            },
            templateUrl: "partials/forgotPassword.html",
            data: {pageTitle: "Forgot Password"},
            controller: "ForgotPasswordController",
            resolve: {
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/UserService.js",
                            "scripts/controllers/ForgotPasswordController.js"
                        ]
                    });
                }]
            }
        })
    
    	// dashboard
        .state("dashboard", {
            url: "/dashboard",
            templateUrl: "partials/dashboard.html",            
            data: { pageTitle: "Dashboard" },
            controller: "DashboardController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/controllers/DashboardController.js"
                        ] 
                    });
                }]
            }
        })

        // products
        .state("products", {
            url: "/provision/products",
            templateUrl: "partials/provision/products.html",            
            data: { 
            	pageTitle: "Products",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ProductsController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ProductService.js",
                            "scripts/controllers/ProductsController.js"
                        ] 
                    });
                }]
            }
        })        

        // product
        .state("product", {
            url: "/provision/products/product/:productId",
            templateUrl: "partials/provision/product/product.html",
            data: { 
            	pageTitle: "Product",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ProductController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ProductService.js",
                            "scripts/controllers/ProductController.js"
                        ] 
                    });
                }]
            }
        })

        // product information
        .state("product.information", {
            url: "/information",
            templateUrl: "partials/provision/product/information/information.html",
            data: {
                pageTitle: "Product",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ProductController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ProductService.js",
                            "scripts/controllers/ProductController.js"
                        ]
                    });
                }]
            }
        })

        // product information
        .state("product.configurations", {
            url: "/configurations",
            templateUrl: "partials/provision/configurations.html",
            data: {
                pageTitle: "Configurations of Product",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ConfigurationsController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/controllers/ConfigurationsController.js"
                        ]
                    });
                }]
            }
        })

        //features of product
        .state("product.features", {
            url: "/features",
            templateUrl: "partials/provision/product/features/features.html",
            data: {
                pageTitle: "Features of product",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ProductController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ProductService.js",
                            "scripts/controllers/ProductController.js"
                        ]
                    });
                }]
            }
        })
        
        // companies
        .state("companies", {
            url: "/provision/companies",
            templateUrl: "partials/provision/companies.html",            
            data: { 
            	pageTitle: "Tenants",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "CompaniesController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/CompanyService.js",
                            "scripts/controllers/CompaniesController.js"
                        ] 
                    });
                }]
            }
        })

        // company
        .state("company", {
            url: "/provision/companies/company/:companyId",
            templateUrl: "partials/provision/company/company.html",
            abstract: true,
            data: { 
            	pageTitle: "Tenant",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "CompanyController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/CompanyService.js",
                            "scripts/controllers/CompanyController.js"
                        ] 
                    });
                }]
            }
        })
        
        // subscriptions
        .state("subscriptions", {
            url: "/provision/subscriptions",
            templateUrl: "partials/provision/subscriptions.html",            
            data: { 
            	pageTitle: "Subscriptions",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "SubscriptionsController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/SubscriptionService.js",
                            "scripts/controllers/SubscriptionsController.js"
                        ] 
                    });
                }]
            }
        })

        // subscription
        .state("subscription", {
            url: "/provision/subscriptions/subscription/:subscriptionId",
            templateUrl: "partials/provision/subscription/subscription.html",
            data: { 
            	pageTitle: "Subscription",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "SubscriptionController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/CompanyService.js",
                            "scripts/services/SubscriptionService.js",
                            "scripts/controllers/SubscriptionController.js"
                        ] 
                    });
                }]
            }
        })

        .state("subscription.information", {
            url: "/information",
            templateUrl: "partials/provision/subscription/information/information.html",
            data: {
                pageTitle: "Subscription",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "SubscriptionController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/CompanyService.js",
                            "scripts/services/SubscriptionService.js",
                            "scripts/controllers/SubscriptionController.js"
                        ]
                    });
                }]
            }
        })

        .state("subscription.accesskeys", {
            url: "/accesskeys",
            templateUrl: "partials/provision/accesskey/accesskeys.html",
            data: {
                pageTitle: "Access Keys",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "AccessKeysController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/AccessKeyService.js",
                            "scripts/controllers/AccessKeysController.js"
                        ]
                    });
                }]
            }
        })

        .state("subscription.applications", {
            url: "/applications",
            templateUrl: "partials/provision/subscription/applications/applications.html",
            controller: "SubscriptionApplicationsController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/SubscriptionService.js",
                            "partials/provision/subscription/applications/applications.ctrl.js"
                        ]
                    });
                }]
            }
        })


        // regions
        .state("regions", {
            url: "/provision/regions",
            templateUrl: "partials/provision/regions.html",            
            data: { 
            	pageTitle: "Regions",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "RegionsController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/RegionService.js",
                            "scripts/controllers/RegionsController.js"
                        ] 
                    });
                }]
            }
        })        

        // region
        .state("region", {
            url: "/provision/regions/region/:regionId",
            templateUrl: "partials/provision/region.html",
            data: { 
            	pageTitle: "Region",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "RegionController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/RegionService.js",
                            "scripts/controllers/RegionController.js"
                        ] 
                    });
                }]
            }
        })         
        
        
        // zones
        .state("zones", {
            url: "/provision/zones",
            templateUrl: "partials/provision/zones.html",            
            data: { 
            	pageTitle: "Zones",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ZonesController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ZoneService.js",
                            "scripts/controllers/ZonesController.js"
                        ] 
                    });
                }]
            }
        })        

        // zone
        .state("zone", {
            url: "/provision/zones/zone/:zoneId",
            templateUrl: "partials/provision/zone.html",
            data: { 
            	pageTitle: "Zone",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ZoneController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ZoneService.js",
                            "scripts/controllers/ZoneController.js"
                        ] 
                    });
                }]
            }
        }) 
        
        
        // applications
        .state("applications", {
            url: "/provision/applications",
            templateUrl: "partials/provision/applications.html",            
            data: { 
            	pageTitle: "Applications",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ApplicationsController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ApplicationService.js",
                            "scripts/controllers/ApplicationsController.js"
                        ] 
                    });
                }]
            }
        })

        // application
        .state("application", {
            url: "/provision/applications/application/:applicationId",
            templateUrl: "partials/provision/application/application.html",
            data: {
            	pageTitle: "Application",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ApplicationController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ApplicationEngineService.js",
                            "scripts/services/ZoneService.js",
                            "scripts/services/CompanyService.js",
                            "scripts/services/ProductService.js",
                            "scripts/services/SubscriptionService.js",
                            "scripts/services/ApplicationService.js",
                            "scripts/controllers/ApplicationController.js"
                        ]
                    });
                }]
            }
        })

        // application
        .state("application.information", {
            url: "/information",
            templateUrl: "partials/provision/application/information/information.html",
            data: {
                pageTitle: "Application",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ApplicationController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ZoneService.js",
                            "scripts/services/CompanyService.js",
                            "scripts/services/ProductService.js",
                            "scripts/services/SubscriptionService.js",
                            "scripts/services/ApplicationService.js",
                            "scripts/controllers/ApplicationController.js"
                        ]
                    });
                }]
            }
        })

        // application's accessKeys
        .state("application.accesskeys", {
            url: "/accesskeys",
            templateUrl: "partials/provision/accesskey/accesskeys.html",
            data: {
                pageTitle: "Access Keys",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "AccessKeysController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/AccessKeyService.js",
                            "scripts/controllers/AccessKeysController.js"
                        ]
                    });
                }]
            }
        })

        // application's accessKeys
        .state("application.configurations", {
            url: "/configurations",
            templateUrl: "partials/provision/configurations.html",
            data: {
                pageTitle: "Application Configurations",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ConfigurationsController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/controllers/ConfigurationsController.js"
                        ]
                    });
                }]
            }
        })
        
        
        // users
        .state("users", {
            url: "/security/users",
            templateUrl: "partials/security/users.html",            
            data: { 
            	pageTitle: "Users",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "UsersController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/UserService.js",
                            "scripts/controllers/UsersController.js"
                        ] 
                    });
                }]
            }
        })

        // user
        .state("user", {
            url: "/security/users/user/:userId",
            templateUrl: "partials/security/user/user.html",
            data: {
                pageTitle: "User",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "UserController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/UserService.js",
                            "scripts/controllers/UserController.js"
                        ]
                    });
                }]
            }
        })

        //user profile
        .state("user.profile", {
            url: "/profile",
            templateUrl: "partials/security/user/profile/profile.html",
            data: {
            	pageTitle: "User Profile",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "UserProfileController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/UserService.js",
                            "scripts/controllers/UserProfileController.js"
                        ]
                    });
                }]
            }
        })

        // user account
        .state("user.account", {
            url: "/account",
            templateUrl: "partials/security/user/account/account.html",
            data: {
                pageTitle: "User Account",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "UserAccountController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/UserService.js",
                            "scripts/services/ApplicationService.js",
                            "scripts/services/CompanyService.js",
                            "scripts/controllers/UserAccountController.js"
                        ]
                    });
                }]
            }
        })

        // user authentication
        .state("user.authentication", {
            url: "/authentication",
            templateUrl: "partials/security/user/authentication/authentication.html",
            data: {
                pageTitle: "User Authentication",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "UserAuthenticationController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/UserService.js",
                            "scripts/services/CompanyService.js",
                            "scripts/controllers/UserAuthenticationController.js"
                        ]
                    });
                }]
            }
        })


        .state("changepassword", {
            url: "/changepassword/:login",
            templateUrl: "partials/security/change-password.html",
            data: {
                pageTitle: "Change password",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "ChangePasswordController",
            resolve: {
                //loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/UserService.js",
                            "scripts/controllers/ChangePasswordController.js"
                        ]
                    });
                }]
            }
        })

        // roles
        .state("roles", {
            url: "/security/roles",
            templateUrl: "partials/security/roles.html",            
            data: { 
            	pageTitle: "Roles",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "RolesController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/RoleService.js",
                            "scripts/controllers/RolesController.js"
                        ] 
                    });
                }]
            }
        })
        
        // role
        .state("role", {
            url: "/security/roles/role/:roleId",
            templateUrl: "partials/security/role.html",
            data: { 
            	pageTitle: "Role",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "RoleController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ApplicationService.js",
                            "scripts/services/PrivilegeService.js",
                            "scripts/services/RoleService.js",
                            "scripts/controllers/RoleController.js"
                        ] 
                    });
                }]
            }
        })
        
        // privileges
        .state("privileges", {
            url: "/security/privileges",
            templateUrl: "partials/security/privileges.html",            
            data: { 
            	pageTitle: "Privileges",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "PrivilegesController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/PrivilegeService.js",
                            "scripts/controllers/PrivilegesController.js"
                        ] 
                    });
                }]
            }
        })

        
        // privilege
        .state("privilege", {
            url: "/security/privileges/privilege/:privilegeId",
            templateUrl: "partials/security/privilege.html",
            data: { 
            	pageTitle: "Privilege",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "PrivilegeController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/ProductService.js",
                            "scripts/services/PrivilegeService.js",
                            "scripts/controllers/PrivilegeController.js"
                        ] 
                    });
                }]
            }
        })
        
        
        // cache
        .state("cache", {
            url: "/manage/cache",
            templateUrl: "partials/manage/cache.html",            
            data: { 
            	pageTitle: "Cahce",
            	pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "CacheController",
            resolve: {
            	loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/CacheService.js",
                            "scripts/controllers/CacheController.js"
                        ] 
                    });
                }]
            }
        })


        // company states:
        .state("company.information", {
                url: "/information",
                templateUrl: "partials/provision/company/information/information.html",
                controller: "CompanyInformationController",
                resolve: {
                    loggedin: checkLoggedin,
                    deps: ["$ocLazyLoad", function($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: "pegasus",
                            insertBefore: "#ng_load_plugins_before",
                            files: [
                                "partials/provision/company/information/information.ctrl.js"
                            ] 
                        });
                    }]
                }
            })
            
        .state("company.billing", {
            url: "/billing",
            templateUrl: "partials/provision/company/billing/billing.html",
            controller: "CompanyBillingController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "partials/provision/company/billing/billing.ctrl.js"
                        ]
                    });
                }]
            }
        })

        .state("company.authentication", {
            url: "/authentication",
            templateUrl: "partials/provision/company/authentication/authentication.html",
            controller: "CompanySecurityController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "partials/provision/company/authentication/authentication.ctrl.js"
                        ]
                    });
                }]
            }
        })

        .state("company.subscriptions", {
            url: "/subscriptions",
            templateUrl: "partials/provision/company/subscriptions/subscriptions.html",
            controller: "CompanySubscriptionsController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "partials/provision/company/subscriptions/subscriptions.ctrl.js"
                        ]
                    });
                }]
            }
        })

        .state("company.applications", {
            url: "/applications",
            templateUrl: "partials/provision/company/applications/applications.html",
            controller: "CompanyApplicationsController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "partials/provision/company/applications/applications.ctrl.js"
                        ]
                    });
                }]
            }
        })

            .state("company.hierarchy", {
                url: "/hierarchy",
                templateUrl: "partials/provision/company/hierarchy/hierarchy.html",
                controller: "CompanyHierarchyController",
                resolve: {
                    loggedin: checkLoggedin,
                    deps: ["$ocLazyLoad", function($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: "pegasus",
                            insertBefore: "#ng_load_plugins_before",
                            files: [
                                "partials/provision/company/hierarchy/hierarchy.ctrl.js"
                            ] 
                        });
                    }]
                }
            })

            .state("company.accesskeys", {
                url: "/accesskeys",
                templateUrl: "partials/provision/accesskey/accesskeys.html",
                data: {
                    pageTitle: "Access Keys",
                    pageSubTitle: "as of " + DateUtil.formatDate(new Date())
                },
                controller: "AccessKeysController",
                resolve: {
                    loggedin: checkLoggedin,
                    deps: ["$ocLazyLoad", function($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: "pegasus",
                            insertBefore: "#ng_load_plugins_before",
                            files: [
                                "scripts/services/AccessKeyService.js",
                                "scripts/controllers/AccessKeysController.js"
                            ]
                        });
                    }]
                }
            })

        // accessKey
        .state("accesskey", {
            url: "/provision/:entityTypes/:entityType/:entityId/accesskeys/:accessKeyId",
            templateUrl: "partials/provision/accesskey/accesskey.html",
            data: {
                pageTitle: "Access Key",
                pageSubTitle: "as of " + DateUtil.formatDate(new Date())
            },
            controller: "AccessKeyController",
            resolve: {
                loggedin: checkLoggedin,
                deps: ["$ocLazyLoad", function($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "pegasus",
                        insertBefore: "#ng_load_plugins_before",
                        files: [
                            "scripts/services/AccessKeyService.js",
                            "scripts/controllers/AccessKeyController.js"
                        ]
                    });
                }]
            }
        })
    ;
}]);

/* Initialize global settings and run the application */
pegasus.run(["$rootScope", "settings", "$state", "$location", '$uibModalStack', function($rootScope, settings, $state, $location, $uibModalStack) {
    $rootScope.$state = $state; // state to be accessed from view
    $rootScope.$settings = settings; // state to be accessed from view

    $rootScope.$on('$stateNotFound', 
    	function(event, unfoundState, fromState, fromParams){
		}
    );
    
    $rootScope.$on('$stateChangeSuccess', 
    	function(event, toState, toParams, fromState, fromParams) {
            if(fromState !== toState) {
                $uibModalStack.dismissAll();
            }
    	}
    );

    $rootScope.$on('$stateChangeError',
    	function(event, toState, toParams, fromState, fromParams, error){
			event.preventDefault();
    		console.log("$stateChangeError entered...");

    		console.log(event);
    		console.log(toState);
    		console.log(toParams);
    		console.log(fromState);
    		console.log(fromParams);
    		console.log(error);

    		if (error === "notAuthorized") {
    			$state.go("signin", { "redirect" : $location.url() });
    		}

    		console.log("$stateChangeError exit.");
    	}
    );

	$rootScope.scrollerConfig = {
		autoHideScrollbar: false,
		theme: 'dark',
		advanced: {
			updateOnContentResize: true
		},
		scrollButtons: {
			scrollAmount: 'auto',
			enable: false
		},
		axis: 'y',
		scrollInertia: 1000
	};
}]);
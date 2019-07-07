var kronos = angular.module('kronos',
    ['ngScrollbars', 'ngAnimate', 'ngRoute', 'ngSanitize', 'ngCookies', 'ngMessages', 'ui.bootstrap', 'ui.bootstrap.datetimepicker', 'ui.dateTimeInput', 'toastr', 'angularSpinner', 'angularMoment', 'googlechart', 'treeControl', 'luegg.directives', 'kronos.directives', 'kronos.filters', 'kronos.services', 'kronos.controllers', 'vcRecaptcha', 'textAngular', 'ladda'])
.config([ '$routeProvider', '$httpProvider', '$provide', 'toastrConfig', 'usSpinnerConfigProvider', "$cookiesProvider",
	function($routeProvider, $httpProvider, $provide, toastrConfig, usSpinnerConfigProvider, $cookiesProvider) {

        var xsrfTokenName = "XSRF-TOKEN-KRONOS-WEB";
        $httpProvider.defaults.xsrfCookieName = xsrfTokenName;
        $httpProvider.defaults.xsrfHeaderName = "X-" + xsrfTokenName;

		// intercept all HTTP responses
		$httpProvider.interceptors.push(['$q','$location', function($q, $location) {
			return {
				response : function(response) {
				  // do something on success
					return response;
				},
				responseError : function(response) {
          // check for unauthorized response
					if (response.status === 401) {
						// unauthorized response found, directing user to
						// sign in UI
                        var hash = '/signin?reason=401';
					    var url = $location.url();
					    if (url.indexOf('/signin') != 0) {
                            hash += '&redirect='+url;
					    } else {
                            var params = $location.search();
                            if (params.redirect) {
                                hash += '&redirect='+params.redirect;
                            }
                        }
                        $location.url(hash);
                        // force reloading from server
                        setTimeout(function() {
                            window.location.reload(true);
                        });
					}
					return $q.reject(response);
				}
			};
		}]);

        var checkLoggedin = ['$rootScope', '$q', 'SecurityService', 'AuthenticationService', function($rootScope, $q, SecurityService, AuthenticationService) {
            if ($rootScope.user !== null && $rootScope.user !== undefined) {
                return $q.resolve();
            }
            if (SecurityService.isLoggedIn()) {
                return AuthenticationService.user().then(function(response) {
                    return AuthenticationService.selectUserApplication(response.data).then(function() {
                        return $q.resolve();
                    });
                });
            } else {
                return $q.reject('notloggedin');
            }
        }];

		// route provider
		$routeProvider.when('/home', {
			templateUrl : '/partials/home.html',
			controller: 'HomeController',
            controllerAs: 'vm',
			resolve: { loggedin: checkLoggedin }
		}).when('/devices', {
            templateUrl : '/partials/list.html',
            controller: 'DevicesController',
            controllerAs: 'vm',
	        resolve: { loggedin: checkLoggedin }
		}).when('/device/:deviceId', {
            templateUrl : '/partials/device/device-details.html',
            resolve: { loggedin: checkLoggedin }
        }).when('/devicetypes', {
            templateUrl : '/partials/list.html',
            controller: 'DeviceTypesController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/actiontypes', {
            templateUrl : '/partials/list.html',
            controller: 'ActionTypesController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/globalactions', {
            templateUrl : '/partials/list.html',
            controller: 'GlobalActionsController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/deviceactiontypes', {
            templateUrl : '/partials/list.html',
            controller: 'DeviceActionTypesController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/grouptypes', {
            templateUrl : '/partials/list.html',
            controller: 'NodeTypesController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/grouplist', {
            templateUrl : '/partials/list.html',
            controller: 'NodesController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/nodetree', {
            templateUrl : '/partials/node/node-tree.html',
            controller: 'NodeTreeController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/gateways', {
            templateUrl : '/partials/list.html',
            controller: 'GatewaysController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/gateway/:gatewayId', {
            templateUrl : '/partials/gateway/gateway-details.html',
            resolve: { loggedin: checkLoggedin }
        }).when('/settings', {
            templateUrl : '/partials/settings/settings.html',
            resolve: { loggedin: checkLoggedin }
        }).when('/accesskeys', {
            templateUrl : '/partials/list.html',
            controller: 'AccessKeysController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/telemetryunits', {
            templateUrl : '/partials/list.html',
            controller: 'TelemetryUnitsController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/swreleaseschedule', {
            templateUrl : '/partials/list.html',
            controller: 'SoftwareReleaseScheduleController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/fmsummary/:tab?', {
            templateUrl : '/partials/firmwaremanagement/summary.html',
            controller: 'FMSummaryController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/availableupdates', {
          templateUrl : '/partials/firmwaremanagement/available-updates.html',
          controller: 'FMAvailableUpdatesController',
          controllerAs: 'vm',
          resolve: { loggedin: checkLoggedin }
        }).when('/fmrighttouse', {
            templateUrl : '/partials/firmwaremanagement/right-to-use.html',
            controller: 'FMRightToUseController',
            constrollerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/fmaudit/:id', {
            templateUrl : '/partials/firmwaremanagement/audit.html',
            controller: 'FMAuditController',
            resolve: { loggedin: checkLoggedin }
        }).when('/fmschedule/:id?', {
            templateUrl : '/partials/firmwaremanagement/jobwizard/jobWizard.html',
            controller: 'FMJobWizardController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/testprocedures', {
            templateUrl : '/partials/list.html',
            controller: 'TestProceduresController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/testresults', {
            templateUrl : '/partials/list.html',
            controller: 'TestResultsController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/changepassword', {
            templateUrl : '/partials/user/change-password.html'
        }).when('/forgotpassword', {
            templateUrl : '/partials/user/forgotpassword.html'
        }).when('/signup', {
            templateUrl : '/partials/signup.html',
            controller: 'RegistrationController',
            controllerAs: 'vm'
        }).when('/verify', {
            templateUrl : '/partials/verify.html',
            controller: 'VerifyController',
            controllerAs: 'vm'
        }).when('/signin', {
            templateUrl : '/partials/signin.html'
        }).when('/event', {
			templateUrl : '/partials/events/registration.html',
			controller: 'EventRegistrationController',
            controllerAs: 'vm'
		}).when('/event/verification', {
			templateUrl : '/partials/events/verification.html',
			controller: 'EventVerificationController',
            controllerAs: 'vm'
		}).when('/event/account-exist', {
			templateUrl : '/partials/events/account-exist.html'
		}).when('/event/registration-complete', {
			templateUrl : '/partials/events/registration-complete.html'
		}).when('/subscription-expired', {
          templateUrl : '/partials/expired.html'
        }).when('/devicetype/:deviceTypeId', {
            templateUrl : '/partials/devicetype/device-type-details.html',
            controller: 'DeviceTypeDetailsController',
            resolve: { loggedin: checkLoggedin }
        }).otherwise({
			redirectTo : '/home'
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

        // spinner: see https://github.com/urish/angular-spinner#configuring-default-spinner-options and http://fgnass.github.io/spin.js
        usSpinnerConfigProvider.setDefaults({
            lines: 13, // The number of lines to draw
            length: 28, // The length of each line
            width: 14, // The line thickness
            radius: 42, // The radius of the inner circle
            scale: 1.00, // Scales overall size of the spinner
            corners: 1.0, // Corner roundness (0..1)
            color: '#000', // #rgb or #rrggbb or array of colors
            opacity: 0.25, // Opacity of the lines
            rotate: 0, // The rotation offset
            direction: 1, // 1: clockwise, -1: counterclockwise
            speed: 1.0, // Rounds per second
            trail: 60, // Afterglow percentage
            fps: 20, // Frames per second when using setTimeout() as a fallback for CSS
            zIndex: 2e9, // The z-index (defaults to 2000000000)
            className: 'spinner', // The CSS class to assign to the spinner
            top: '50%', // Top position relative to parent
            left: '50%', // Left position relative to parent
            shadow: false, // Whether to render a shadow
            hwaccel: false, // Whether to use hardware acceleration
            position: 'absolute' // Element positioning
        });

        $provide.decorator('taOptions', ['$delegate', function(taOptions){
            taOptions.toolbar = [
                ['h1', 'h2', 'h3', 'p'],
                ['bold', 'italics', 'underline', 'ul', 'ol'],
                ['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],
                ['html', 'insertLink']
            ];
            return taOptions;
        }]);
	}
])
.config(["$logProvider", function ($logProvider) {
    $logProvider.debugEnabled(true); // set this to 'false' for production
}])
.config(['$compileProvider', function ($compileProvider) {
    // See https://docs.angularjs.org/guide/production
    $compileProvider.debugInfoEnabled(true);
}])
.run(['$rootScope', '$location', '$modalStack', function($rootScope, $location, $modalStack) {
	$rootScope.$on("$routeChangeStart", function(event, next, current) {
	});
	$rootScope.$on("$routeChangeSuccess", function(event, next, current) {
        if(angular.element("#app-top-navbar-collapse").attr('aria-expanded')=='true') {
            angular.element(".navbar-header button.navbar-toggle").trigger("click");
        }
        if (current !== next) {
            $modalStack.dismissAll();
        }
	});
    $rootScope.$on("$routeChangeError", function(event, next, current, rejection) {
        if (rejection === 'notloggedin') {
            $location.url('/signin?redirect='+$location.url());
        }
    });
    $rootScope.scrollerConfig = {
        autoHideScrollbar: false,
        theme: 'light-thin',
        advanced: {
            updateOnContentResize: true
        },
        scrollButtons:{ enable: false },
        axis: 'y',
        scrollInertia: 300
    };
}]);

var services = angular.module('kronos.services', []);
var controllers = angular.module('kronos.controllers', []);
var directives = angular.module('kronos.directives', []);
var filters = angular.module('kronos.filters', []);
var components = angular.module('kronos.components', []);

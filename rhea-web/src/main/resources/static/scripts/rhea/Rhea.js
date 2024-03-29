var rhea = angular.module('rhea',
    ['ngScrollbars', 'ngAnimate', 'ngRoute', 'ngSanitize', 'ngCookies', 'ngMessages', 'ui.bootstrap', 'ui.bootstrap.datetimepicker', 'ui.dateTimeInput', 'toastr', 'angularSpinner', 'angularMoment', 'rhea.directives', 'rhea.filters', 'rhea.services', 'rhea.controllers'])
.config([ '$routeProvider', '$httpProvider', 'toastrConfig', 'usSpinnerConfigProvider',
	function($routeProvider, $httpProvider, toastrConfig, usSpinnerConfigProvider) {

	    var xsrfTokenName = "XSRF-TOKEN-RHEA-WEB";
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
                        window.location.reload(true);
					}
					return $q.reject(response);
				}
			};
		}]);

        var checkLoggedin = ['$rootScope', '$q', function ($rootScope, $q) {
            if( $rootScope.user != null ) {
                return true;
            } else {
                return $q.reject('notloggedin');
            }
        }];

		// route provider
		$routeProvider.when('/home', {
			templateUrl : '/partials/home.html',
			controller: 'HomeController',
			resolve: { loggedin: checkLoggedin }
		}).when('/deviceproducts', {
            templateUrl : '/partials/list.html',
            controller: 'DeviceProductsController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/devicetypes', {
            templateUrl : '/partials/list.html',
            controller: 'DeviceTypesController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/softwarevendors', {
            templateUrl : '/partials/list.html',
            controller: 'SoftwareVendorsController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/softwareproducts', {
            templateUrl : '/partials/list.html',
            controller: 'SoftwareProductsController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/manufacturers', {
            templateUrl : '/partials/list.html',
            controller: 'ManufacturersController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/softwarereleases', {
            templateUrl : '/partials/list.html',
            controller: 'SoftwareReleasesController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/righttouserequests', {
            templateUrl : '/partials/righttouserequests/righttouserequests.html',
            controller: 'RightToUseRequestsController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
        }).when('/signin', {
            templateUrl : '/partials/signin.html'
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

var services = angular.module('rhea.services', []);
var controllers = angular.module('rhea.controllers', []);
var directives = angular.module('rhea.directives', []);
var filters = angular.module('rhea.filters', []);
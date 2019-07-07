var apollo = angular.module('apollo',
    ['ngScrollbars', 'ngAnimate', 'ngRoute', 'ngSanitize', 'ngCookies', 'ngMessages', 'ui.bootstrap', 'ui.bootstrap.datetimepicker', 'ui.dateTimeInput', 'angular-flexslider', 'toastr', 'angularSpinner', 'angularMoment', 'googlechart', 'apollo.directives', 'apollo.filters', 'apollo.services', 'apollo.controllers', 'gridster', 'widgets' ])
.config([ '$routeProvider', '$httpProvider', 'toastrConfig', 'usSpinnerConfigProvider', 'WidgetOptions',
	function($routeProvider, $httpProvider, toastrConfig, usSpinnerConfigProvider, WidgetOptions) {

        // widgets configuration:
        // WidgetOptions.gridster.columns = 4;

	    var xsrfTokenName = "XSRF-TOKEN-APOLLO-WEB";
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
		$routeProvider.when('/forgotpassword', {
            templateUrl : '/partials/forgotpassword.html'
        }).when('/changepassword', {
        		templateUrl: "partials/changePassword.html",
            controller: "ChangePasswordController"
        }).when('/signin', {
            templateUrl : '/partials/signin.html'
		}).when('/all-boards', {
            templateUrl : '/partials/allBoards.html',
            controller: 'AllBoardsController',
            resolve: { loggedin: checkLoggedin }
		}).when('/board/:boardId', {
            templateUrl : '/partials/board.html',
            controller: 'BoardController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
		}).when('/settings/widget-types', {
            templateUrl : '/partials/settings/widget-types.html',
            controller: 'WidgetTypesController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
		}).when('/settings/widgets', {
            templateUrl : '/partials/settings/apollo-widgets.html',
            controller: 'ApolloWidgetsController',
            controllerAs: 'vm',
            resolve: { loggedin: checkLoggedin }
		}).when('/404', {
            templateUrl : '/partials/errors/404.html',
            controller: 'NotFoundController',
            controllerAs: 'vm'
		}).otherwise({
			redirectTo : '/board/default'
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
.run(['$rootScope', '$location', '$uibModalStack', function($rootScope, $location, $uibModalStack) {
	$rootScope.$on("$routeChangeStart", function(event, next, current) {
	});
	$rootScope.$on("$routeChangeSuccess", function(event, next, current) {
        if(angular.element("#app-top-navbar-collapse").attr('aria-expanded')=='true') {
            angular.element(".navbar-header button.navbar-toggle").trigger("click");
        }
        if (current !== next) {
            $uibModalStack.dismissAll();
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

var services = angular.module('apollo.services', []);
var controllers = angular.module('apollo.controllers', []);
var directives = angular.module('apollo.directives', []);
var filters = angular.module('apollo.filters', []);

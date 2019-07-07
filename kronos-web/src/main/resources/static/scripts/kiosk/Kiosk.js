var kiosk = angular.module('kiosk',
    ['ngScrollbars', 'ngAnimate', 'ngRoute', 'ngSanitize', 'ngCookies', 'ngMessages', 'ui.bootstrap', 'toastr', 'angularSpinner', 'angularMoment', 'kiosk.directives', 'kiosk.filters', 'kiosk.services', 'kiosk.controllers'])
.config([ '$routeProvider', '$httpProvider', 'toastrConfig', 'usSpinnerConfigProvider',
	function($routeProvider, $httpProvider, toastrConfig, usSpinnerConfigProvider) {

		// route provider
		$routeProvider.when('/setup', {
			templateUrl : '/partials/kiosk/setup.html',
			controller: 'KioskSetupController',
            controllerAs: 'vm'
		}).when('/kiosk', {
            templateUrl : '/partials/kiosk/kiosk.html',
            controller: 'KioskViewController',
            controllerAs: 'vm'
        }).otherwise({
			redirectTo : '/setup'
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
.run(['$rootScope', '$location', function($rootScope, $location) {
	$rootScope.$on("$routeChangeStart", function(event, next, current) {
	});
	$rootScope.$on("$routeChangeSuccess", function(event, next, current) {
        if(angular.element("#app-top-navbar-collapse").attr('aria-expanded')=='true')
            angular.element(".navbar-header button.navbar-toggle").trigger("click");
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

var services = angular.module('kiosk.services', []);
var controllers = angular.module('kiosk.controllers', []);
var directives = angular.module('kiosk.directives', []);
var filters = angular.module('kiosk.filters', []);

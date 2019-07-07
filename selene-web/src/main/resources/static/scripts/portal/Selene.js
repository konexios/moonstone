var selene = angular.module('selene',
  ['ngAnimate',
    'ngRoute',
    'ngSanitize',
    'ngCookies',
    'ngMessages',
    'ui.bootstrap',
    
    'toastr',
    'angularSpinner',
    'selene.directives',
    'selene.filters',
    'selene.services',
    'selene.controllers'
  ])
  .config(['$routeProvider', '$httpProvider', 'toastrConfig', 'usSpinnerConfigProvider',
    function ($routeProvider, $httpProvider, toastrConfig, usSpinnerConfigProvider) {

      // intercept all HTTP responses
      $httpProvider.interceptors.push(function ($q, $location) {
        return {
          response: function (response) {
            // do something on success
            return response;
          },
          responseError: function (response) {
            // check for unauthorized response
            if (response.status === 401) {
              // unauthorized response found, directing user to
              // sign in UI
              var url = $location.url();
              if (url.indexOf('/signin') != 0) {
                $location.url('/signin?redirect=' + url);
              }
            }
            return $q.reject(response);
          }
        };
      });

      function checkLoggedin($rootScope, $q) {
        if ($rootScope.principal != null) {
          return true;
        } else {
          return $q.reject('notloggedin');
        }
      }

      // route provider
      $routeProvider.when('/home', {
        templateUrl: '/partials/home.html',
        resolve: {loggedin: checkLoggedin}
      }).when('/gateway', {
        templateUrl: '/partials/gateway.html',
        resolve: {loggedin: checkLoggedin}
      }).when('/devices', {
        templateUrl: '/partials/devices.html',
        resolve: {loggedin: checkLoggedin}
      }).when('/device/:deviceId', {
        templateUrl: '/partials/device.html',
        resolve: {loggedin: checkLoggedin}
      }).when('/createdevice', {
        templateUrl: '/partials/createDevice.html',
        resolve: {loggedin: checkLoggedin}
      }).when('/device/:deviceId/registrationtranspose', {
          templateUrl: '/partials/registrationTranspose.html',
          resolve: {loggedin: checkLoggedin}
      }).when('/device/:deviceId/telemetrytranspose', {
          templateUrl: '/partials/telemetryTranspose.html',
          resolve: {loggedin: checkLoggedin}
      }).when('/device/:deviceId/statetranspose', {
          templateUrl: '/partials/stateTranspose.html',
          resolve: {loggedin: checkLoggedin}
      }).when('/aws', {
         templateUrl: '/partials/aws.html',
         resolve: {loggedin: checkLoggedin}
      }).when('/azure', {
         templateUrl: '/partials/azure.html',
         resolve: {loggedin: checkLoggedin}
      }).when('/ibm', {
         templateUrl: '/partials/ibm.html',
         resolve: {loggedin: checkLoggedin}
      }).when('/messages', {
        templateUrl: '/partials/messages.html',
        resolve: {loggedin: checkLoggedin}
      }).when('/message/:messageId', {
        templateUrl: '/partials/message.html',
        resolve: {loggedin: checkLoggedin}
      }).when('/logs', {
          templateUrl: '/partials/logs.html',
          resolve: {loggedin: checkLoggedin}
       }).when('/upload', {
           templateUrl: '/partials/uploadSelene.html',
           resolve: {loggedin: checkLoggedin}
        }).when('/cbk', {
           templateUrl: '/partials/centralKnowledgebank.html',
           resolve: {loggedin: checkLoggedin}
        }).when('/signin', {
        templateUrl: '/partials/signin.html'
      }).otherwise({
        redirectTo: '/signin'
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
  .run(function ($rootScope, $location, $document) {
    $rootScope.$on("$routeChangeStart", function (event, next, current) {
    });
    $rootScope.$on("$routeChangeError", function (event, next, current, rejection) {
      if (rejection === 'notloggedin') {
        $location.url('/signin?redirect=' + $location.url());
      }
    });
    angular.element(window).on("resize.doResize", function () {
      $rootScope.$apply(function () {
        $rootScope.showSmallTable = $document.width() < 768;
      });
    });
    $rootScope.showSmallTable = $document.width() < 768;

    $rootScope.$on("$destroy", function () {
      angular.element(window).off("resize.doResize");
    });

  });

var services = angular.module('selene.services', []);
var controllers = angular.module('selene.controllers', []);
var directives = angular.module('selene.directives', []);
var filters = angular.module('selene.filters', []);

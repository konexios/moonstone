(function() {
    'use strict';

    angular
        .module('pegasus')
        .controller('SubscriptionApplicationsController', SubscriptionApplicationsController);

    function SubscriptionApplicationsController($scope, $stateParams, SubscriptionService) {
        var subscriptionId = $stateParams.subscriptionId;
        $scope.applications = null;

        init();

        function init() {
            console.log(SubscriptionService);
            SubscriptionService.applications(subscriptionId).then(function(response) {
                $scope.applications = response.data;
            });
        }
    }

})();
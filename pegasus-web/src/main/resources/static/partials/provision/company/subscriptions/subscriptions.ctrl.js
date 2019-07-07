(function() {
    'use strict';

    angular
        .module('pegasus')
        .controller('CompanySubscriptionsController', CompanySubscriptionsController);

    function CompanySubscriptionsController($scope, $stateParams, CompanyService) {
        var companyId = $stateParams.companyId;
        $scope.subscriptions = null;

        init();

        function init() {
            CompanyService.subscriptions(companyId).then(function(response) {
                $scope.subscriptions = response.data;
            });
        }
    }

})();
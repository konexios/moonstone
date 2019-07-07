(function() {
    'use strict';

    angular
        .module('pegasus')
        .controller('CompanyApplicationsController', CompanyApplicationsController);

    function CompanyApplicationsController($scope, $stateParams, CompanyService) {
        var companyId = $stateParams.companyId;
        $scope.applications = null;

        init();

        function init() {
            CompanyService.applications(companyId).then(function(response) {
                $scope.applications = response.data;
            });
        }
    }

})();
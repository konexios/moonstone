(function() {
    'use strict';

    angular
        .module('pegasus')
        .controller('CompanyHierarchyController', CompanyHierarchyController);

    function CompanyHierarchyController($scope, $stateParams, CompanyService) {
        var companyId = $stateParams.companyId;
        $scope.hierarchy = null;

        init();

        function init() {
            CompanyService.hierarchy(companyId).then(function(response) {
                $scope.hierarchy = response.data;
            });
        }
    }

})();
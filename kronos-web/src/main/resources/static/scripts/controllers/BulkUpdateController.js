controllers.controller('BulkUpdateController', ['$scope', '$uibModalInstance', 'modalOptions', 'fields', BulkUpdateController]);

function BulkUpdateController($scope, $uibModalInstance, modalOptions, fields) {
    $scope.modalOptions = angular.extend({
        title: 'Bulk Update',
        update: 'Update',
        cancel: 'Cancel',
    }, modalOptions);

    $scope.fields = fields;

    $scope.bulkUpdate = function(fields) {
        $uibModalInstance.close(fields);
    };

    $scope.cancel = function() {
        $uibModalInstance.dismiss('cancel');
    };
}
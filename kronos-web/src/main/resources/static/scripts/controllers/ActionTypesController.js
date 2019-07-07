controllers.controller('ActionTypesController', ['$uibModal', 'GlobalActionsService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', 'CommonService', ActionTypesController]);


// Global Actions Types List
function ActionTypesController($uibModal, GlobalActionsService, ErrorService, ToastrService, SecurityService, SpinnerService, CommonService) {
    var vm = this;

    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'systemName'
        }
    }, {
        pageTitle: 'Global Action Types',
        resultTitle: 'Global Action Types',
        columnHeaders: [
            { label: 'Name', value: 'name', sortable: true },
            { label: 'System Name', value: 'systemName', sortable: true },
            { label: 'Description', value: 'description', sortable: true },
            { label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val) { return val ? 'Yes' : 'No'; } },
            // { label: 'Editable', value: 'editable', sortable: false, renderFunc: function(val) { return val ? 'Yes' : 'No'; } },
            { label: 'Last Modified', value: 'lastModifiedDate', sortable: false, renderFunc: CommonService.getFormatredDate },
            { label: 'Last Modified By', value: 'lastModifiedBy', sortable: false, renderFunc: function(val) { return val === "" ? 'Unknown' : val; } }
        ],
        tfootTopic: 'global action types',
        canAdd: SecurityService.canCreateGlobalActionType(),
        canEdit: true, // user can always open action type details to see parameters
        openDetails: openDetails
    });

    function openDetails(actionType) {
        actionType = actionType || {
            name: '',
            systemName: '',
            validationType: '',
            description: '',
            enabled: true,
            editable: true,
            parameters: []
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/actions/global-action-type.html',
            controller: 'ActionTypeDetailsController',
            controllerAs: 'vm',
            size: 'lg',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                actionType: actionType
            }
        });

        modalInstance.result.then(function(actionType) {
            SpinnerService.wrap(GlobalActionsService.saveGlobalActionType, actionType)
                .then(function(response) {
                    ToastrService.popupSuccess('Action Type ' + actionType.name + ' has been saved successfully');
                    vm.find();
                })
                .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(GlobalActionsService.getGlobalActionTypes, vm.pagination)
            .then(function(response) {
                vm.update(response.data.result);
            })
            .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
ActionTypesController.prototype = Object.create(ListController.prototype);


// Global Action Type Add\Edit
controllers.controller('ActionTypeDetailsController', ['$scope', '$filter', '$uibModal', '$uibModalInstance', 'SecurityService', 'ToastrService', 'actionType',
    function($scope, $filter, $uibModal, $uibModalInstance, SecurityService, ToastrService, actionType) {
        vm = this;

        vm.actionType = actionType = angular.copy(actionType);
        vm.isAdmin = SecurityService.isAdmin();
        vm.canSaveActionType = (vm.actionType.editable || vm.isAdmin) && (SecurityService.canEditGlobalActionType() || SecurityService.canCreateGlobalActionType());

        // make valid sorting: double check for sorting (if something was broken or changed in another places)
        $filter('orderBy')(vm.actionType.parameters, 'order').forEach(function(val, index) { val.order = index; });

        vm.openParameterDetails = function(parameter, isEdit) {

            var modalInstance = $uibModal.open({
                animation: false,
                templateUrl: 'partials/actions/global-action-type-parameter.html',
                controller: 'ActionTypeParameterController',
                controllerAs: 'paramVm',
                backdrop: 'static',
                keyboard: false,
                resolve: {
                    actionTypeParameter: function() { return parameter; },
                    isEditParameter: function() { return isEdit; },
                    actionTypeParameters: function() { return actionType.parameters; }
                }
            });

            modalInstance.result.then(function(actionTypeParameter) {
                if (isEdit) {
                    actionType.parameters[actionType.parameters.indexOf(parameter)] = actionTypeParameter;
                } else {
                    actionTypeParameter.order = actionType.parameters.length === 0 ? 0 : Math.max.apply(Math, actionType.parameters.map(function(p) { return p.order; })) + 1;
                    actionType.parameters.push(actionTypeParameter);
                }
            });
        };

        vm.deleteParameter = function(parameter) {
            vm.actionType.parameters.splice(vm.actionType.parameters.indexOf(parameter), 1);
        };

        vm.orderDown = function(parameter) {
            var sorted = $filter('orderBy')(vm.actionType.parameters, 'order');
            sorted[sorted.indexOf(parameter) + 1].order--;
            parameter.order++;
        };

        vm.orderUp = function(parameter) {
            var sorted = $filter('orderBy')(vm.actionType.parameters, 'order');
            sorted[sorted.indexOf(parameter) - 1].order++;
            parameter.order--;
        };

        vm.save = function(form, model) {
            form.$setSubmitted();
            if (form.$invalid) {
                ToastrService.popupError('Parameter cannot be saved because of invalid fields, please check errors.');
            } else {
                $uibModalInstance.close(model);
            }
        };

        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
]);


// Global Actions Parameter Add\Edit
controllers.controller('ActionTypeParameterController', ['$scope', '$uibModalInstance', 'SecurityService', 'ToastrService', 'actionTypeParameter', 'isEditParameter', 'actionTypeParameters',
    function($scope, $uibModalInstance, SecurityService, ToastrService, actionTypeParameter, isEditParameter, actionTypeParameters) {
        paramVm = this;

        paramVm.isEdit = isEditParameter;
        // get other parameter names to avoid names dublication
        paramVm.exestParametersNames = actionTypeParameters.map(function(p) { return p.name.toLowerCase(); });
        if (actionTypeParameter && isEditParameter) {
            paramVm.exestParametersNames = paramVm.exestParametersNames.filter(function(name) { return name !== actionTypeParameter.name.toLowerCase(); });
        }
        // note: probably later this should coming from server
        paramVm.validationTypes = {
            'STRING': { name: 'String', checked: false, defaultValue: null },
            'MULTILINE_STRING': { name: 'Multiline String', checked: false, defaultValue: null },
            'EMAIL': { name: 'Email', checked: false, defaultValue: null },
            'SELECT': { name: 'Select', checked: false, defaultValue: null, data: [''] },
            'JSON': { name: 'JSON', checked: false, defaultValue: null },
            'XML': { name: 'XML', checked: false, defaultValue: null },
            'KEY_VALUE_PAIRS': { name: 'Key Value pairs', checked: false, defaultValue: null },
            'HTML': { name: 'Html', checked: false, defaultValue: null },
        };
        // if edit: setup selected types
        if (actionTypeParameter && actionTypeParameter.validationTypes && actionTypeParameter.validationTypes.length > 0) {
            actionTypeParameter.validationTypes.forEach(function(type) {
                paramVm.validationTypes[type.type].checked = true;
                paramVm.validationTypes[type.type].defaultValue = type.defaultValue;
                // setup data if exist
                if (paramVm.validationTypes[type.type].data) {
                    paramVm.validationTypes[type.type].data = JSON.parse(type.data);
                }
            });
        }
        paramVm.selectValidationTypes = [];
        paramVm.parameter = angular.copy(actionTypeParameter);

        // update selection types model on checkbox updates
        $scope.$watch('paramVm.validationTypes', function(obj) {
            paramVm.selectValidationTypes = [];
            for (var key in obj) {
                if (obj[key].checked) {
                    paramVm.selectValidationTypes.push({
                        type: key,
                        defaultValue: obj[key].defaultValue,
                        data: obj[key].data ? JSON.stringify(obj[key].data) : null
                    });
                }
            }
        }, true);


        paramVm.nonDuplicatingNamesValidation = {
            key: 'duplicatingNames',
            func: function(mv, vv) {
                if (!vv || vv === '') {
                    return true;
                }
                return paramVm.exestParametersNames.indexOf(vv.toLowerCase()) >= 0 ? false : true;
            }
        };

        paramVm.save = function(form) {
            form.$setSubmitted();
            if (form.$invalid || paramVm.selectValidationTypes.length < 1) {
                ToastrService.popupError('Parameter cannot be saved because of invalid fields, please check errors.');
            } else {
                paramVm.parameter.validationTypes = paramVm.selectValidationTypes;
                $uibModalInstance.close(paramVm.parameter);
            }
        };

        paramVm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
]);
controllers.controller('GlobalActionsController', ['$uibModal', 'GlobalActionsService', 'ErrorService', 'ToastrService', 'SecurityService', 'SpinnerService', 'CommonService', GlobalActionsController]);


// Global Actions List
function GlobalActionsController($uibModal, GlobalActionsService, ErrorService, ToastrService, SecurityService, SpinnerService, CommonService) {
    var vm = this;

    vm.globalAction = null;
    ListController.call(vm, {
        itemsPerPage: 10,
        sort: {
            property: 'systemName'
        }
    }, {
        pageTitle: 'Global Actions',
        resultTitle: 'Global Actions',
        columnHeaders: [
            { label: 'Name', value: 'name', sortable: true },
            { label: 'System Name', value: 'systemName', sortable: true },
            { label: 'Type', value: 'globalActionType', sortable: true, renderFunc: function(val) { return val.name; } },
            { label: 'Description', value: 'description', sortable: true },
            { label: 'Enabled', value: 'enabled', sortable: false, renderFunc: function(val) { return val ? 'Yes' : 'No'; } },
            { label: 'Last Modified', value: 'lastModifiedDate', sortable: false, renderFunc: CommonService.getFormatredDate },
            { label: 'Last Modified By', value: 'lastModifiedBy', sortable: false, renderFunc: function(val) { return val === "" ? 'Unknown' : val; } }
        ],
        tfootTopic: 'global actions',
        canAdd: SecurityService.canCreateGlobalAction(),
        canEdit: SecurityService.canEditGlobalAction(),
        openDetails: openDetails
    });

    function openDetails(globalAction) {
        globalAction = globalAction || {
            globalActionType: { id: null },
            name: null,
            systemName: null,
            description: null,
            enabled: true,
            editable: true,
            input: []
        };

        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'partials/actions/global-action.html',
            controller: 'GlobalActionDetailsController',
            controllerAs: 'vm',
            size: 'lg',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                globalAction: globalAction
            }
        });

        modalInstance.result.then(function(action) {
            SpinnerService.wrap(GlobalActionsService.saveGlobalAction, action)
                .then(function(response) {
                    ToastrService.popupSuccess('Action Type ' + action.name + ' has been saved successfully');
                    vm.find();
                })
                .catch(ErrorService.handleHttpError);
        });
    }

    vm.find = function() {
        SpinnerService.wrap(GlobalActionsService.getGlobalActions, vm.pagination)
            .then(function(response) {
                vm.update(response.data.result);
            })
            .catch(ErrorService.handleHttpError);
    };

    vm.find();
}
GlobalActionsController.prototype = Object.create(ListController.prototype);


// Global Action Add\Edit
controllers.controller('GlobalActionDetailsController', ['$scope', '$uibModal', '$uibModalInstance', 'GlobalActionsService', 'SecurityService', 'SpinnerService', 'ErrorService', 'ToastrService', 'globalAction',
    function($scope, $uibModal, $uibModalInstance, GlobalActionsService, SecurityService, SpinnerService, ErrorService, ToastrService, globalAction) {
        vm = this;

        vm.globalAction = angular.copy(globalAction);
        vm.savedModel = angular.copy(globalAction);
        vm.actionTypes = [];
        vm.selectedActionType = null;
        vm.canSaveAction = SecurityService.canCreateGlobalAction() || SecurityService.canEditGlobalAction();
        // vm.inputTypes = ['STRING','MULTILINE_STRING','EMAIL','SELECT','JSON','XML','KEY_VALUE_PAIRS','HTML'];
        // nth: get this variables from the server
        vm.inputTypes = [{ type: 'String', name: 'String' }, { type: 'Map', name: 'Map' }, { type: 'List', name: 'List' }, { type: 'Long', name: 'Long' }];

        function init() {
            SpinnerService.wrap(GlobalActionsService.getGlobalActionTypesList)
                .then(function(response) {
                    vm.actionTypes = response.data;
                    // set type selection (if edit mode)
                    if (vm.globalAction.globalActionType.id) {
                        vm.actionTypeSelectChanged(vm.globalAction.globalActionType.id);
                    }
                })
                .catch(ErrorService.handleHttpError);
        }


        vm.actionTypeSelectChanged = function(selectedId) {
            vm.selectedActionType = vm.actionTypes.filter(function(el) {
                return el.id === selectedId;
            })[0];

            vm.globalAction.properties = [];
            vm.selectedActionType.parameters.forEach(function(param) {
                var property = {
                    parameterName: param.name,
                    parameterValidationTypes: param.validationTypes,
                    parameterIsRequired: param.required,
                    parameterOrder: param.order
                };
                // restore data from initial model, or setup on first select
                var sProps = vm.savedModel.globalActionType.id === selectedId ? vm.savedModel.properties.filter(function(sProp) { return sProp.parameterName === param.name; })[0] : null;
                if (sProps) {
                    property.parameterType = sProps.parameterType;
                    property.parameterValue = sProps.parameterValue;
                    var validationType = property.parameterValidationTypes.filter(function(p) { return p.type === property.parameterType; })[0];
                    if (validationType) {
                        property.parameterData = validationType.data ? JSON.parse(validationType.data) : null;
                    }
                } else {
                    property.parameterType = param.validationTypes[0].type;
                    property.parameterValue = param.validationTypes[0].defaultValue;
                    property.parameterData = param.validationTypes[0].data ? JSON.parse(param.validationTypes[0].data) : null;
                }
                vm.globalAction.properties.push(property);
            });
        };

        vm.propertyValidationTypeChanged = function(property, type) {
            var validationType = property.parameterValidationTypes.filter(function(t) { return t.type === type; })[0];
            if (validationType.defaultValue) {
                property.parameterValue = validationType.defaultValue;
            }
            property.parameterData = validationType.data ? JSON.parse(validationType.data) : null;
        };

        vm.showDuplicatedNamesAlert = false;
        $scope.$watch('vm.globalAction.input', function(newCol) {
            var result = false;
            for (var i = 0; i < newCol.length; i++) {
                for (var j = 0; j < newCol.length; j++) {
                    if (!result && i !== j) {
                        result = newCol[i].name === newCol[j].name;
                    }
                }
            }
            vm.showDuplicatedNamesAlert = result;
        }, true);

        vm.addVariable = function() {
            // if (!vm.globalAction.input || !Array.isArray(vm.globalAction.input)) {
            //     vm.globalAction.input = [];
            // }
            vm.globalAction.input.push({ name: '', type: vm.inputTypes[0].type, required: false, editing: true });
        };

        vm.deleteVariable = function(index) {
            vm.globalAction.input.splice(index, 1);
        };


        vm.save = function(form, model) {
            form.$setSubmitted();
            if (form.$invalid) {
                ToastrService.popupError('Variable cannot be saved because of invalid fields, please check errors.');
            } else {
                $uibModalInstance.close(model);
            }
        };

        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };

        init();
    }
]);
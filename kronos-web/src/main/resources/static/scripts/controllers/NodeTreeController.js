controllers.controller('NodeTreeController', ['$scope', '$timeout', '$location', '$uibModal', 'NodeService', 'DeviceService', 'EventService', 'ErrorService', 'ToastrService', 'SpinnerService', 'SecurityService', NodeTreeController]);

function NodeTreeController($scope, $timeout, $location, $uibModal, NodeService, DeviceService, EventService, ErrorService, ToastrService, SpinnerService, SecurityService) {
    var vm = this;

    vm.pageTitle = 'Device Hierarchy';
    vm.pageSubTitle = '';

    vm.canViewAllDevices = SecurityService.canViewAllDevices();

    vm.toolbar = [{
        caption: 'List view',
        icon: 'fa fa-table',
        onClick: function() {
            $location.path('/devices');
        }
    }];
    var rootNode = {
        type: 'node',
        loaded: false,
        children: [],
        model: {id: null}
    };
    vm.treeModel = rootNode.children;
    vm.treeOptions = {
        nodeChildren: "children",
        dirSelectable: false,
        equality: function(node1, node2) {
            return node1 === node2;
        },
        isLeaf: function(node) {
            return node.type != 'node';
        },
        injectClasses: {
            ul: '',
            li: '',
            liSelected: '',
            iExpanded: 'fa fa-folder-open-o',
            iCollapsed: 'fa fa-folder-o',
            iLeaf: 'fa fa-file-o',
            label: '',
            labelSelected: ''
        }
    };

    vm.filterString = '';
    vm.filterOption = 'enabled';
    var RECENTLY_UPDATED_LIMIT = 24 * 3600 * 1000; // 24 hours in milliseconds

    vm.filter = function(node) {
        var isIncluded = true;
        if (isIncluded && vm.filterString.length > 0) {
            // substring, ignore case
            var filterString = vm.filterString.toUpperCase();
            isIncluded = node.model.name.toUpperCase().indexOf(filterString) >= 0; // node name or device name
            if (!isIncluded && node.type == 'device' && node.model.ownerName) {
                isIncluded = node.model.ownerName.toUpperCase().indexOf(filterString) >= 0; // device owner
            }
        }
        if (isIncluded) {
            switch (vm.filterOption) {
                case 'enabled':
                    isIncluded = node.model.enabled;
                    break;
                case 'disabled':
                    isIncluded = !node.model.enabled;
                    break;
                case 'my':
                    isIncluded = node.type == 'device' && node.model.myDevice;
                    break;
                case 'updated':
                    isIncluded = node.type == 'device' && (Date.now() - node.model.lastModifiedDate < RECENTLY_UPDATED_LIMIT);
                    break;
                default:
                    // unsupported
                    isIncluded = false;
                    break;
            }
        }
        if (isIncluded || node.type != 'node') {
            return isIncluded;
        }
        // if node.type == 'node' include this node if any its children should be included
        for(var i=0; i<node.children.length; i++) {
            if (vm.filter(node.children[i])) {
                isIncluded = true;
                break;
            }
        }
        return isIncluded;
    };

    vm.selectedNode = null;
    vm.activeTab = 'info';
    vm.openDevice = function(treeNode) {
        vm.selectedNode = treeNode;
        vm.activeTab = 'info';
    };

    EventTrackerMixin.call(vm, $scope, $timeout, EventService, ToastrService);

    vm.startDevice = function(node) {
        SpinnerService.wrap(DeviceService.startDevice, node.model.id)
        .then(function(response) {
            ToastrService.popupSuccess('Start event has been queued for the device ' + node.model.name);
            vm.trackEvent(response.data, 'start', node.model);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.stopDevice = function(node) {
        SpinnerService.wrap(DeviceService.stopDevice, node.model.id)
        .then(function(response) {
            ToastrService.popupSuccess('Stop event has been queued for the device ' + node.model.name);
            vm.trackEvent(response.data, 'stop', node.model);
        })
        .catch(ErrorService.handleHttpError);
    };

    vm.nodeToggle = function(node, expanded) {
        if (expanded && !node.loaded) {
            findChildNodes(node);
        }
    };

    function findChildNodes(node) {
        SpinnerService.wrap(NodeService.findChildNodes, node.model.id)
        .then(function(response) {
            var data = response.data;

            // empty list
            node.children.splice(0, node.children.length);

            // add nodes
            var i;
            if (data.nodes) {
                for(i=0; i<data.nodes.length; i++) {
                    node.children.push({
                        type: 'node',
                        loaded: false,
                        children: [],
                        model: data.nodes[i]
                    });
                }
            }

            // add devices
            if (data.devices) {
                for(i=0; i<data.devices.length; i++) {
                    node.children.push({
                        type: 'device',
                        model: data.devices[i]
                    });
                }
            }

            node.loaded = true;
        })
        .catch(ErrorService.handleHttpError);
    }

    findChildNodes(rootNode);
}

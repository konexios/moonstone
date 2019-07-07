function EventTrackerMixin($scope, $timeout, EventService, ToastrService) {
    $scope.monitoredEvents = {};
    $scope.processedEvents = [];
    $scope.monitoredEventIds = [];
    var timer = null;

    function scheduleEventMonitoring() {
        if (Object.keys($scope.monitoredEvents).length == 0) {
        	$scope.monitoredEventIds = [];
            // cancel event status polling
            if (timer) {
                $timeout.cancel(timer);
                timer = null;
            }
            // else no timer, no need to cancel it
        } else {
            // start event status polling
            if (!timer) {
                timer = $timeout(checkEventStatus, 1000); // poll every 1 sec
            }
            // else there is a timer, no need to re-schedule
        }
    }

    function checkEventStatus() {
        timer = null; // timer fired, drop it
        $scope.monitoredEventIds = Object.keys($scope.monitoredEvents);
        // double check
        if ($scope.monitoredEventIds.length == 0) {
            return;
        }
        EventService.getStatuses($scope.monitoredEventIds)
            .then(function (response) {
                var statuses = response.data;
                for (var i = 0; i < $scope.monitoredEventIds.length; i++) {
                    var eventId = $scope.monitoredEventIds[i];
                    $scope.monitoredEvents[$scope.monitoredEventIds[i]].event = statuses[i];
                    switch (statuses[i].status) {
                        case 'Received':
                        case 'Pending':
                            // keep waiting on
                            break;
                        case 'Succeeded':
                            popupEventSucceeded($scope.monitoredEvents[eventId]);
                            $scope.processedEvents.push($scope.monitoredEvents[eventId]);
                            delete $scope.monitoredEvents[eventId];
                            break;
                        case 'Failed':
                            popupEventFailed($scope.monitoredEvents[eventId]);
                            $scope.processedEvents.push($scope.monitoredEvents[eventId]);
                            delete $scope.monitoredEvents[eventId];
                            break;
                        default:
                        	$scope.processedEvents.push($scope.monitoredEvents[eventId]);
                            // unexpected status or event not found
                            // skip event checking silently
                            delete $scope.monitoredEvents[eventId];
                            break;
                    }
                }
            })
            .finally(scheduleEventMonitoring);
    }

    function popupEventSucceeded(event) {
        if (!event) {
            return;
        }
        switch (event.type) {
            case 'propertyChange':
                ToastrService.popupSuccess('Settings have been applied on the device ' + event.device.name + ' successfully');
                break;
            case 'start':
                ToastrService.popupSuccess('Device ' + event.device.name + ' has been started successfully');
                break;
            case 'stop':
                ToastrService.popupSuccess('Device ' + event.device.name + ' has been stopped successfully');
                break;
            case 'command':
                ToastrService.popupSuccess('Command ' + event.param + ' has been processed by device ' + event.device.name + ' successfully');
                break;
            case 'device-state-update':
                ToastrService.popupSuccess('State update of device ' + event.device.name + ' has been requested successfully');
                break;
            case 'syncConfigToDevice':
                ToastrService.popupSuccess('Cloud device configuration for device ' + event.device.name + ' has been sync successfully');
                break;
            case 'syncDeviceConfigToCloud':
                ToastrService.popupSuccess('Current device configuration for device ' + event.device.name + ' has been sync successfully');
                break;
            case 'syncConfigToGateway':
                ToastrService.popupSuccess('Cloud gateway configuration for gateway ' + event.device.name + ' has been sync successfully');
                break;
            case 'syncGatewayConfigToCloud':
                ToastrService.popupSuccess('Current gateway configuration for gateway ' + event.device.name + ' has been sync successfully');
                break;
        }
    }

    function popupEventFailed(event) {
        if (!event) {
            return;
        }
        switch (event.type) {
            case 'propertyChange':
                ToastrService.popupError('Failed to apply settings on the device ' + event.device.name);
                break;
            case 'start':
                ToastrService.popupError('Failed to start device ' + event.device.name);
                break;
            case 'stop':
                ToastrService.popupError('Failed to stop device ' + event.device.name);
                break;
            case 'command':
                ToastrService.popupError('Command ' + event.param + ' has failed on device ' + event.device.name);
                break;
            case 'device-state-update':
                ToastrService.popupError('Failed to request state update of device ' + event.device.name);
                break;
            case 'syncConfigToDevice':
                ToastrService.popupError('Failed sync cloud device configuration for device ' + event.device.name);
                break;
            case 'syncDeviceConfigToCloud':
                ToastrService.popupError('Failed sync current device configuration for device ' + event.device.name);
                break;
            case 'syncConfigToGateway':
                ToastrService.popupError('Failed sync cloud gateway configuration for gateway ' + event.device.name);
                break;
            case 'syncGatewayConfigToCloud':
                ToastrService.popupError('Failed sync current gateway configuration for gateway ' + event.device.name);
                break;

        }
    }

    $scope.$on('$destroy', function () {
        $scope.monitoredEvents = {}; // cancel updating
        if (timer) {
            $timeout.cancel(timer);
            timer = null;
        }
    });

    // external API
    this.trackEvent = function (eventId, eventType, device, param) {
        $scope.monitoredEvents[eventId] = {
            type: eventType,
            device: device,
            param: param,
            event: {}
        };
        scheduleEventMonitoring();
    };
}

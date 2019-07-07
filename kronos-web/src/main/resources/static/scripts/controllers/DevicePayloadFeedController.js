(function() {
    'use strict';

    controllers.controller("DevicePayloadFeedController", DevicePayloadFeedController);

    DevicePayloadFeedController.$inject = [
        "$scope", "$routeParams", "WebSocketsService"
    ];

    function DevicePayloadFeedController($scope, $routeParams, WebSocketsService) {
        var vm = this;

        var payloadIndex = 0;
        var wsSubscription = null;
        vm.defaultPayloadLimit = 7;
        vm.payloadsLimit = vm.defaultPayloadLimit;
        vm.payloadFeed = [];
        vm.payloadDetails = null;
        vm.payloadState = 'stoped';

        vm.start = start;
        vm.stop = stop;
        vm.clear = clear;
        vm.selectPayload = selectPayload;
        vm.payloadLimitUpdated = PayloadLimitUpdated;


        var wsConnection = WebSocketsService.connect("/api/kronos/ws/device", {
            onConnect: function() {
                vm.payloadState = "started";
                wsSubscribe();
                $scope.$apply();
            }
        });


        function wsSubscribe(){
            wsSubscription = wsConnection.subscribe("/topic/device/" + $routeParams.deviceId, OnPayloadRecive);
        }


        function wsUnSubscribe(){
            wsConnection.unsubscribe(wsSubscription);
            wsSubscription = null;
        }


        function OnPayloadRecive(data) {
            PushToPayloadFeed(data);
            $scope.$apply();
        }


        function PushToPayloadFeed(data) {
            payloadIndex++;
            vm.payloadFeed.unshift({
                index: payloadIndex,
                date: new Date(),
                data: data,
                isSelected: false
            });

            CheckPayloadStack();
        }

        function PayloadLimitUpdated() {
            CheckPayloadStack();
        }

        function CheckPayloadStack() {
            var limit = !!vm.payloadsLimit ? vm.payloadsLimit : vm.defaultPayloadLimit;
            if (vm.payloadFeed.length > limit) {
                var difference = vm.payloadFeed.length - limit;
                vm.payloadFeed.splice(limit, difference);
            }
        }


        function start() {
            if (vm.payloadState === "started") {
                return;
            }
            vm.payloadState = "started";
            vm.payloadDetails = null;
            wsSubscribe();
        }

        function stop() {
            if (vm.payloadState === "stoped") {
                return;
            }
            vm.payloadState = "stoped";
            wsUnSubscribe();
        }

        function clear() {
            vm.payloadFeed = [];
            vm.payloadDetails = null;
            payloadIndex = 0;
        }


        function selectPayload(payload) {
            vm.payloadDetails = payload;
            vm.payloadFeed.forEach(function(p) { p.isSelected = false; });
            payload.isSelected = true;
            stop();
        }


        $scope.$on("$destroy", function() {
            // disconnect from web sockets
            if (wsConnection) {
                wsConnection.disconnect();
            }
        });
    }

})();
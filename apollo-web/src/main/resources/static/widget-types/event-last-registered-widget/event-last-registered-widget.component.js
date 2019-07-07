(function() {
    'use strict';

    angular
        .module('widgets')
        .component('eventLastRegisteredWidget', {
            templateUrl: '/widget-types/event-last-registered-widget/event-last-registered-widget.tmp.html',
            controller: eventLastRegisteredController,
            controllerAs: 'vm',
            bindings: {
                widget: '='
            }
        });

    function eventLastRegisteredController($scope, $interval, $filter, moment) {
        var vm = this;
        vm.data = {
        	waiting: true
        };

        // consts and flags
        vm.displayListSize = 4;
        vm.displayOnTopSeconds = 10;
        vm.displayRegistrationTime = true;

        vm.toDispleyQueue = [];
        vm.displayQueue = [];

        vm.$onInit = function() {

            // TODO: handle error
            //vm.widget.subscribe("/widget-error", function(message) { });

            vm.widget.subscribe("/widget-state", function(message) {});

            // receive registered persons from server
            vm.widget.subscribe("/last-registered", function(resp) {
                resp = JSON.parse(resp);
                vm.data.waiting = false;
                if (resp.data && resp.data.length > 0) {
                    // serialize date and pushed order by timestamp
                    resp.data.forEach(function(person) {
                        person.timestamp = moment(person.timestamp);
                    });
                    // nth: order queue by timestamp
                    //$filter('orderBy')(resp.data, 'timestamp');
                    resp.data.forEach(function(person) {
                		vm.toDispleyQueue.push(person);
                    });
                }
            });
        };
        
        vm.includesPerson = function(person)
        {
        	var result = false;
        	for(var i = 0; i < vm.displayQueue.length; i++)
        	{
        		var existingPerson = vm.displayQueue[i];
        		if (existingPerson.name === person.name)
        		{
        			result = true;
        			break;
        		}
        	}
        	
        	return result;
        }

        // update displayed persons on widget
        $interval(function() {
            if (vm.toDispleyQueue.length > 0) {
                // if top person stayed on 1st more than 'vm.displayOnTopSeconds', push other person on 1st if exist
                // or if display queue clear, just put first person to display
                if (vm.displayQueue.length === 0 || moment().subtract(vm.displayOnTopSeconds, 'seconds') > vm.displayQueue[0].pushedAt) {
                    var toDisplayPerson = vm.toDispleyQueue.shift();
                    toDisplayPerson.pushedAt = moment();
                    vm.displayQueue.unshift(toDisplayPerson);
                    // if we have lot of limit persons to display, just remove them
                    if (vm.displayQueue.length > vm.displayListSize) {
                        vm.displayQueue.pop();
                    }
                }
            }
        }, 500);

    }

})();
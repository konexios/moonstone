controllers.controller('KioskSetupController',
    [ '$rootScope', '$scope', '$location', 'ErrorService', 'KioskService',
    function($rootScope, $scope, $location, ErrorService, KioskService) {
    	var vm = this;

    	vm.settingsModel = {
        	rCode: null,
        	eCode:  null
    	};
    	
    	vm.runKiosk = function(model) {
            vm.kioskForm.$setSubmitted();
            vm.errorgMessage = null;
    		
            if (vm.kioskForm.$valid) {
            	var myUrl = "/kiosk";
            	
            	if (vm.settingsModel.rCode != null && vm.settingsModel.eCode != null)
            		myUrl += "?rcode=" + vm.settingsModel.rCode + "&ecode=" + vm.settingsModel.eCode;
            	else if (vm.settingsModel.rCode != null)
            		myUrl += "?rcode=" + vm.settingsModel.rCode;
            	else if (vm.settingsModel.eCode != null)
            		myUrl += "?ecode=" + vm.settingsModel.eCode;
            	
            	$location.url(myUrl);
            }
    	}
    }
]);

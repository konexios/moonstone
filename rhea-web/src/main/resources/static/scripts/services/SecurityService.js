services.factory('SecurityService',
    [ '$cookies', '$rootScope',
    function($cookies, $rootScope) {
        var RHEA_ME = 'rheame';
        var RHEA_APP = 'rheaapp';

        function isLoggedIn() {
            return getRheaMe() != null && getRheaApp() != null;
        }

        function getRheaMe() {
            return $cookies.get(RHEA_ME) || null;
        }

        function getRheaApp() {
            return $cookies.get(RHEA_APP) || null;
        }

        function saveSession(login, hid) {
            deleteSession();

            if (login != null && login != undefined && login != "")
                $cookies.put(RHEA_ME, login);

            if (hid != null && hid != undefined && hid != "")
                $cookies.put(RHEA_APP, hid);
        }

        function deleteSession() {
            $cookies.remove(RHEA_ME);
            $cookies.remove(RHEA_APP);
        }

        function getUser() {
            return $rootScope.user;
        }

        function hasPrivilege(privilegeName) {
            var user = getUser();

            return (user != null && userHasPrivilege(user, privilegeName));
        }

        function isAdmin() {
            var user = getUser();

            return (user != null && user.admin);
        }

        function userHasPrivilege(user, privilegeName) {
            if (user == null || user == undefined || privilegeName == null || privilegeName == undefined || privilegeName == "")
                return false;

            var applicationInstance = $rootScope.app;
            if (applicationInstance == null || applicationInstance == undefined)
               return false;

            var result = false;

            outerLoop: for (var i = 0; i < user.applications.length; i++) {
                var application = user.applications[i];
                // Find privileges in all applications
                if (application.id === applicationInstance.id) {
                    for (var p = 0; p < application.privileges.length; p++) {
                        var privilege = application.privileges[p];
                        if (privilege.name == privilegeName) {
                            result = true;
                            break outerLoop;
                        }
                    }
                }
            }

            return result;
        }

        // privileges

        function canReadDeviceCategories() {
            return isAdmin();
        }

        function canCreateDeviceCategory() {
        	return isAdmin();
        }

        function canEditDeviceCategory() {
        	return isAdmin();
        }
        
        function canReadDeviceManufacturers() {
            return hasPrivilege('RHEA_READ_DEVICE_MANUFACTURERS');
        }

        function canCreateDeviceManufacturer() {
            return hasPrivilege('RHEA_CREATE_DEVICE_MANUFACTURER');
        }

        function canEditDeviceManufacturer() {
            return hasPrivilege('RHEA_UPDATE_DEVICE_MANUFACTURER');
        }
        
        function canReadDeviceProducts() {
            return hasPrivilege('RHEA_READ_DEVICE_PRODUCTS');
        }

        function canCreateDeviceProduct() {
            return hasPrivilege('RHEA_CREATE_DEVICE_PRODUCT');
        }

        function canEditDeviceProduct() {
            return hasPrivilege('RHEA_UPDATE_DEVICE_PRODUCT');
        }
        
        function canReadDeviceTypes() {
            return hasPrivilege('RHEA_READ_DEVICE_TYPES');
        }

        function canCreateDeviceType() {
            return hasPrivilege('RHEA_CREATE_DEVICE_TYPE');
        }

        function canEditDeviceType() {
            return hasPrivilege('RHEA_UPDATE_DEVICE_TYPE');
        }
        
        function canReadSoftwareVendors() {
            return hasPrivilege('RHEA_READ_SOFTWARE_VENDORS');
        }

        function canCreateSoftwareVendor() {
            return hasPrivilege('RHEA_CREATE_SOFTWARE_VENDOR');
        }

        function canEditSoftwareVendor() {
            return hasPrivilege('RHEA_UPDATE_SOFTWARE_VENDOR');
        }
        
        function canReadSoftwareProducts() {
            return hasPrivilege('RHEA_READ_SOFTWARE_PRODUCTS');
        }

        function canCreateSoftwareProduct() {
            return hasPrivilege('RHEA_CREATE_SOFTWARE_PRODUCT');
        }

        function canEditSoftwareProduct() {
            return hasPrivilege('RHEA_UPDATE_SOFTWARE_PRODUCT');
        }
        
        function canReadSoftwareReleases() {
            return hasPrivilege('RHEA_READ_SOFTWARE_RELEASES');
        }

        function canReadRightToUseRequest() {
            return hasPrivilege('RHEA_READ_RIGHT_TO_USE_REQUEST');
        }

        function canCreateSoftwareRelease() {
            return hasPrivilege('RHEA_CREATE_SOFTWARE_RELEASE');
        }

        function canEditSoftwareRelease() {
            return hasPrivilege('RHEA_UPDATE_SOFTWARE_RELEASE');
        }

        function canReadManufacturers() {
            return hasPrivilege('RHEA_READ_DEVICE_MANUFACTURERS');
        }

        function canCreateManufacturer() {
            return hasPrivilege('RHEA_CREATE_DEVICE_MANUFACTURER');
        }

        function canEditManufacturer() {
            return hasPrivilege('RHEA_UPDATE_DEVICE_MANUFACTURER');
        }

        function canUpdateRTURequests() {
            return hasPrivilege('RHEA_UPDATE_RTU_REQUEST');
        }

        function canReadRTURequest() {
          return hasPrivilege('RHEA_READ_RTU_REQUEST');
        }
        
        return {
            isLoggedIn: isLoggedIn,
            saveSession: saveSession,
            deleteSession: deleteSession,
            getUserLogin: getRheaMe,
            getUserApp: getRheaApp,
            // privileges
            isAdmin: isAdmin,
            canReadDeviceCategories: canReadDeviceCategories,
            canCreateDeviceCategory: canCreateDeviceCategory,
            canEditDeviceCategory: canEditDeviceCategory,
            canReadDeviceManufacturers: canReadDeviceManufacturers,
            canCreateDeviceManufacturer: canCreateDeviceManufacturer,
            canEditDeviceManufacturer: canEditDeviceManufacturer,
            canReadDeviceProducts: canReadDeviceProducts,
            canCreateDeviceProduct: canCreateDeviceProduct,
            canEditDeviceProduct: canEditDeviceProduct,
            canReadDeviceTypes: canReadDeviceTypes,
            canCreateDeviceType: canCreateDeviceType,
            canEditDeviceType: canEditDeviceType,
            canReadSoftwareVendors: canReadSoftwareVendors,
            canCreateSoftwareVendor: canCreateSoftwareVendor,
            canEditSoftwareVendor: canEditSoftwareVendor,
            canReadSoftwareProducts: canReadSoftwareProducts,
            canCreateSoftwareProduct: canCreateSoftwareProduct,
            canEditSoftwareProduct: canEditSoftwareProduct,
            canReadSoftwareReleases: canReadSoftwareReleases,
            canReadRightToUseRequest: canReadRightToUseRequest,
            canCreateSoftwareRelease: canCreateSoftwareRelease,
            canEditSoftwareRelease: canEditSoftwareRelease,
            canReadManufacturers: canReadManufacturers,
            canCreateManufacturer: canCreateManufacturer,
            canEditManufacturer: canEditManufacturer,
            canUpdateRTURequests: canUpdateRTURequests,
            canReadRTURequest: canReadRTURequest
        };
    }
]);

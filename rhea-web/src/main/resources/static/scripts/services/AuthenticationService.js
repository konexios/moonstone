services.factory('AuthenticationService',
    [ '$http', '$q', '$rootScope', '$uibModal', 'SecurityService', 'SpinnerService',
    function($http, $q, $rootScope, $uibModal, SecurityService, SpinnerService) {

        function serializeData(data) {
            // If this is not an object, defer to native stringification.
            if (!angular.isObject(data)) {
                return ((data == null) ? "" : data.toString());
            }
            var buffer = [];
            // Serialize each key in the object.
            for ( var name in data) {
                if (!data.hasOwnProperty(name)) {
                    continue;
                }
                var value = data[name];
                buffer.push(encodeURIComponent(name) + "=" + encodeURIComponent((value == null) ? "" : value));
            }
            // Serialize the buffer and clean it up for transportation.
            var source = buffer.join("&").replace(/%20/g, "+");
            return (source);
        }

        function login(credentials) {
            return $http({
                method : "post",
                url : "/api/v1/core/security/login",
                transformRequest : serializeData,
                data : {
                    username : credentials.username,
                    password : credentials.password
                },
                headers : {
                    "Content-type" : "application/x-www-form-urlencoded; charset=utf-8"
                }
            });
        }

        function logout() {
            return $http.post('/logout', {});
        }

        function user() {
            return $http.get('/api/rhea/security/user', {});
        }

        function application(applicationId) {
            return $http.get('/api/v1/core/security/application/' + applicationId, {});
        }

        function selectUserApplication(user, force) {
            if (user && user.login) {
                if (user.applications.length >= 1) {
                    var promise;
                    if (user.applications.length > 1) {
                        // more than 1 application
                        // see if there is stored app hid unless user forces to select the app
                        var userAppHid = SecurityService.getUserApp();
                        if (userAppHid != null && !force) {
                            // there is saved app hid in cookies, try to find it
                            for(var i=0; i<user.applications.length; i++) {
                                if (user.applications[i].hid == userAppHid) {
                                    // select the app
                                    promise = $q.resolve(user.applications[i]);
                                    break;
                                }
                            }
                        }
                        if (!promise) {
                            // let user select the app
                            promise = $uibModal.open({
                                animation: false,
                                templateUrl: 'partials/user/select-application.html',
                                controller: 'SelectApplicationController',
                                size: 'lg',
                                resolve: {
                                    applications: function() {
                                        return user.applications;
                                    }
                                }
                            }).result.catch(function() {
                                throw 'You must select the application.';
                            });
                        }
                    } else {
                        // just one application - select it
                        promise = $q.resolve(user.applications[0]);
                    }
                    return promise.then(function(app) {
                        return SpinnerService.wrap(application, app.id)
                        .then(function(response) {
                            $rootScope.user = user;
                            $rootScope.app = response.data;
                            SecurityService.saveSession(user.login, app.hid);
                            
                            // TODO discuss w/Dmity M., not sure this is the best place for this code...
                            if ($rootScope.app) {
                                for(var i = 0; i < $rootScope.app.configurations.length; i++) {
                                    if ($rootScope.app.configurations[i].category == "Branding" && $rootScope.app.configurations[i].name == "logoUrl") {
                                        $rootScope.app.logoUrl = $rootScope.app.configurations[i].value;
                                        break;
                                    }
                                }
                            }
                            
                            return app;
                        });
                    });
                } else {
                    return $q.reject('You do not have permission to access this application.');
                }
            } else {
                return $q.reject('Your login and/or password was either misspelled or incorrect.');
            }
        }

        return {
            login: login,
            logout: logout,
            user: user,
            application: application,
            selectUserApplication: selectUserApplication
        };
    }
]);

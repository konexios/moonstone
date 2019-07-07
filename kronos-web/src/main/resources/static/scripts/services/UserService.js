services.factory('UserService',
    [ '$http', '$q', 'SecurityService',
    function($http, $q, SecurityService) {

        function getPasswordPolicy(login) {
            var url = '/api/kronos/user/password-policy';
            if (!SecurityService.isLoggedIn()) {
            	if (login != undefined && login != null && login != '') {
            		url += '/'+encodeURIComponent(login);
            	} else if (SecurityService.getUserLogin()) {
                    url += '/'+encodeURIComponent(SecurityService.getUserLogin());
                } else {
                    return $q.reject('Not authorized');
                }
            }
            return $http.get(url);
        }

        function changePassword(login, currentPassword, newPassword) {
            var url = '/api/kronos/user/change-password';
            if (!SecurityService.isLoggedIn()) {
            	if (login != undefined && login != null && login != '') {
            		url += '/'+encodeURIComponent(login);
            	} else if (SecurityService.getUserLogin()) {
                    url += '/'+encodeURIComponent(SecurityService.getUserLogin());
                } else {
                    return $q.reject('Not authorized');
                }
            }

            return $http.post(url, {
                currentPassword: currentPassword,
                newPassword: newPassword
            });
        }

        function resetPassword(login) {
            return $http.post('/api/kronos/user/reset-password/'+login);
        }

        function preRegistration(token) {
            return $http.get('/api/kronos/registration/' + token + '/preregistration');
        }
        
        function register(userRegistration) {
    		return $http({
    			"url": "/api/kronos/registration/register",
    			"method": "POST",
    			"headers": {"Content-Type" : "application/json"},
    			"data": userRegistration
    		});
        }
        
        function verifyAccount(token) {
            return $http.get('/api/kronos/registration/' + token + '/verify');
        }
        
        function resendVerifyEmail(token) {
            return $http.get('/api/kronos/registration/' + token + '/resendVerifyEmail');
        }

        return {
            getPasswordPolicy: getPasswordPolicy,
            changePassword: changePassword,
            resetPassword: resetPassword,
            preRegistration: preRegistration,
            register: register,
            verifyAccount: verifyAccount,
            resendVerifyEmail: resendVerifyEmail
        };
    }
]);

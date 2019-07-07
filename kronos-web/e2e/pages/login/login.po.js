(function() {
    'use strict';

    var LoginPage = function() {

        // form
        this.signInFrom = element(by.css('form#loginForm'));

        // form title
        this.pageHeader = this.signInFrom.element(by.css('.panel-login .hd h3.title'));



        // login label
        this.loginLabel = this.signInFrom.element(by.css('label[for="username"]'));

        // login input
        this.loginInput = this.signInFrom.element(by.css('input#username'));



        // password label
        this.passwordLabel = this.signInFrom.element(by.css('label[for="password"]'));

        // password input
        this.passwordInput = this.signInFrom.element(by.css('input#password'));
        


        // login button
        this.loginButton = this.signInFrom.element(by.css('button[type="submit"]'));



        // form error message
        this.formErrorMessage = this.signInFrom.element(by.css('.alert.alert-danger'));

    };

    module.exports = new LoginPage();
})();
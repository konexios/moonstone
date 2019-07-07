(function() {
    'use strict';

    var RegistrationPage = function() {

        // form
        this.signupFrom = element(by.css('form#registrationForm'));

        // form title
        this.pageHeader = this.signupFrom.element(by.css('.panel-registration .hd h4.title'));



        // email label
        this.emailLabel = this.signupFrom.element(by.css('label[for="email"]'));

        // email input
        this.emailInput = this.signupFrom.element(by.css('input#email'));

        // email required
        this.emailHelpBlock = this.signupFrom.element(by.css('input#email + .help-block'));



        // recaptcha
        this.recaptcha = this.signupFrom.element(by.css('div[vc-recaptcha]'));



        // login button
        this.signupButton = this.signupFrom.element(by.css('button[type="submit"]'));



        // form error message
        this.formErrorMessage = this.signupFrom.element(by.css('.alert.alert-danger'));

    };

    module.exports = new RegistrationPage();
})();
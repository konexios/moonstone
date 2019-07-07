(function() {
    'use strict';

    var LoginPage = function() {
        // form
        this.signInForm = element(by.css('form#loginForm'));

        // form title
        this.formTitle = this.signInForm.element(by.css('h3.title'));

        // login label
        this.loginLabel = this.signInForm.element(by.css('label[for="username"]'));

        // login input
        this.loginInput = this.signInForm.element(by.css('input#username'));

        // password label
        this.passwordLabel = this.signInForm.element(by.css('label[for="password"]'));

        // password input
        this.passwordInput = this.signInForm.element(by.css('input#password'));

        // login button
        this.loginButton = this.signInForm.element(by.css('button[type="submit"]'));

        // form error message
        this.formErrorMessage = this.signInForm.element(by.css('.alert.alert-danger'));

        // navigation
        this.navigationMenu = element(by.css('#app-top-navbar-collapse'));

        // links to pages
        this.pagesLinksUl = this.navigationMenu.element(by.css('.nav.navbar-nav'));
        this.pagesLinks = this.pagesLinksUl.all(by.css('li'));

        // user block
        this.userNav = this.navigationMenu.element(by.css('.nav.navbar-nav.navbar-right li'));
    };

    module.exports = new LoginPage();
})();
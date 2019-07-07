(function() {
    'use strict';

    describe('The login view', function() {
        var page;

        beforeEach(function() {
            browser.get('/#/signin');
            page = require('./login.po');
        });


        // -> display login form
        it('should display login form', function() {

            expect(page.signInFrom.isDisplayed()).toBe(true);

            expect(page.loginLabel.isDisplayed()).toBe(true);
            expect(page.loginInput.isDisplayed()).toBe(true);

            expect(page.passwordLabel.isDisplayed()).toBe(true);
            expect(page.passwordInput.isDisplayed()).toBe(true);

            expect(page.loginButton.isDisplayed()).toBe(true);
        });


        // click login -> message "login and password empty"
        it('should display error message on empty input fields - "login and password empty"', function() {

            page.loginButton.click();
            browser.waitForAngular();
            expect(page.formErrorMessage.isDisplayed()).toBe(true);
            expect(page.formErrorMessage.getText()).not.toContain('Unexpected error'); // is expected error - no "Unexpected error"
        });


        // click login -> incorrect password
        it('should display error message - "incorrect password"', function() {

            page.loginInput.click();
            page.loginInput.sendKeys('Baduser');
            page.passwordInput.click();
            page.passwordInput.sendKeys('Badpassword');
            page.loginButton.click();
            browser.waitForAngular();
            expect(page.formErrorMessage.isDisplayed()).toBe(true);
            expect(page.formErrorMessage.getText()).not.toContain('Unexpected error');
        });


        // click login -> correct pasword
        // ToDo: test with test-user in dev env.
        it('should sign in success', function() {

            page.loginInput.click();
            page.loginInput.sendKeys('E2EUser');
            page.passwordInput.click();

            page.passwordInput.sendKeys('E2EUser$!');
            page.loginButton.click();
            browser.waitForAngular();

            expect(browser.getCurrentUrl()).toContain('/#/home');
        });

    });

})();
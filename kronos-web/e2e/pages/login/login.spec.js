(function() {
    'use strict';

    describe('The login view', function() {
        var page, modal;

        beforeEach(function() {
            browser.get('/#/signin');
            page = require('./login.po');
            modal = require('./../../common/page.po');
        });


        // -> display login form
        it('should display login form', function() {

            expect(page.signInFrom.isDisplayed()).toBe(true);
            expect(page.pageHeader.getText()).toBe('SIGN IN');

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


        // click login -> correct pasword -> select application
        it('should display error message - "incorrect password"', function() {

            page.loginInput.click();
            page.loginInput.sendKeys('E2EUser');

            page.passwordInput.click();
            page.passwordInput.sendKeys('E2EUser$!');
            
            page.loginButton.click();
            browser.waitForAngular();

            // displaying list of applications
            expect(modal.activeModalHeader.isDisplayed()).toBe(true);
            expect(modal.activeModalHeader.getText()).toEqual('Select Application Instance');
            
            // select first application
            expect(modal.firstAppLink.isDisplayed()).toBe(true);
            modal.firstAppLink.click();
            browser.waitForAngular();

            expect(browser.getCurrentUrl()).toContain('/#/home');
        });

    });

})();
(function() {
    'use strict';

    describe(
        'The login view',
        function() {
            var page = require('./login.po');

            beforeEach(function() {
                browser.get('/#/signin');
            });

            it(
                'should display login form',
                function() {
                    expect(page.signInForm.isDisplayed()).toBe(true);
                    expect(page.formTitle.getText()).toBe('SIGN IN');
                    expect(page.loginLabel.isDisplayed()).toBe(true);
                    expect(page.loginInput.isDisplayed()).toBe(true);
                    expect(page.passwordLabel.isDisplayed()).toBe(true);
                    expect(page.passwordInput.isDisplayed()).toBe(true);
                    expect(page.loginButton.isDisplayed()).toBe(true);
                }
            );

            it(
                'should display navigation menu',
                function() {
                    expect(page.pagesLinks.count()).toBe(0);
                    expect(page.userNav.isDisplayed()).toBe(true);
                    expect(page.userNav.getAttribute('class')).toMatch(/active/);
                }
            );
        }
    );

    describe(
        'Try to login',
        function () {
            var page = require('./login.po');

            afterEach(function () {
                page.loginButton.click();
                browser.waitForAngular();
                expect(page.formErrorMessage.isDisplayed()).toBe(true);
                expect(page.formErrorMessage.getText())
                    .toBe('Either your username and/or password was either misspelled or incorrect.');
            });

            it(
                'should display error message on empty login and password fields - "Either your username and/or password was either misspelled or incorrect."',
                function() {
                    page.loginInput.click();
                    page.loginInput.sendKeys('');
                    page.passwordInput.click();
                    page.passwordInput.sendKeys('');
                }
            );

            it(
                'should display error message on empty password field - "Either your username and/or password was either misspelled or incorrect."',
                function() {
                    page.loginInput.click();
                    page.loginInput.sendKeys('Baduser');
                    page.passwordInput.click();
                    page.passwordInput.sendKeys('');
                }
            );

            it(
                'should display error message on empty login field - "Either your username and/or password was either misspelled or incorrect."',
                function() {
                    page.loginInput.click();
                    page.loginInput.sendKeys('');
                    page.passwordInput.click();
                    page.passwordInput.sendKeys('Badpassword');
                }
            );

            it(
                'should display error message on wrong authentication data - "Either your username and/or password was either misspelled or incorrect."',
                function() {
                    page.loginInput.click();
                    page.loginInput.sendKeys('Baduser');
                    page.passwordInput.click();
                    page.passwordInput.sendKeys('Badpassword');
                }
            );
        }
    );
})();
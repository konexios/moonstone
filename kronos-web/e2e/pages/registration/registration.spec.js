(function() {
    'use strict';

    describe('The registration view', function() {
        var page;

        beforeEach(function() {
            browser.get('/#/signup');
            page = require('./registration.po');
        });


        // -> display registration form
        it('should display registration form', function() {
            expect(page.signupFrom.isDisplayed()).toBe(true);
            expect(page.pageHeader.getText()).toBe('DEVELOPER REGISTRATION');

            expect(page.emailLabel.isDisplayed()).toBe(true);
            expect(page.emailInput.isDisplayed()).toBe(true);

            expect(page.recaptcha.isDisplayed()).toBe(true);

            expect(page.signupButton.isDisplayed()).toBe(true);
        });


        // click registration -> message "fields required"
        it('should display errors message on empty input fields', function() {
            page.signupButton.click();
            browser.waitForAngular();

            expect(page.emailHelpBlock.isDisplayed()).toBe(true);
            expect(page.emailHelpBlock.getText()).toBe('Email required');
        });


        // ToDo: email validation, success registration (recaptcha?), inputs (count)


    });

})();
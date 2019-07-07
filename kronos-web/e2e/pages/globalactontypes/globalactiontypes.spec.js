(function() {
    'use strict';

    describe('The global action types view', function() {
        var page, list, globalActionPage;

        beforeEach(function() {
            browser.get('/#/actiontypes');
            globalActionPage = require('./globalactiontypes.po');
            list = require('./../../common/list.po');
            page = require('./../../common/page.po');
        });


        it('should display view', function() {
            expect(browser.getCurrentUrl()).toContain('/#/actiontypes');

            expect(list.pageHeader.isDisplayed()).toBe(true);
            expect(list.pageHeader.getText()).toBe('Global Action Types');

            expect(list.panelHeader.isDisplayed()).toBe(true);
            expect(list.panelHeader.getText()).toContain('Global Action Types');
        });


        it('should not display any error modal', function() {
            expect(page.errorModal.isDisplayed()).toBe(false);
        });


        it('should open add action modal', function() {
            list.getButtonByText('Add').click();
            browser.waitForAngular();
            expect(list.activeModalHeader.isDisplayed()).toBe(true);
            expect(list.activeModalHeader.getText()).toEqual('Add Action Type');
        });


        it('validation on add action modal should works', function() {
            // Open modal
            // Click save
            // Fields with * should display error
            list.getButtonByText('Add').click();
            browser.waitForAngular();

            expect(globalActionPage.errorAlert.isPresent()).toBe(false);
            globalActionPage.submitButton.click();

            expect(globalActionPage.errorAlert.isPresent()).toBe(true);
            expect(globalActionPage.requiredLabels.count()).toEqual(globalActionPage.deisplayedErrors.count());
        });


        /* TODO: create action, check creation, edit, check edit

        it('should add action', function() {
            // Enter name with time stamp
            // Enter other fields
            // Click save
            // Start checking entity with this name in Actions list
            var actionName = 'Test Action ' + new Date().getTime();
            list.getButtonByText('Add').click();
            browser.waitForAngular();
        });


        it('should edit action', function() {
            // Find and open entity
            // Update name with time stamp
            // Click save
            // Start checking
            var newActionName = 'Test Action ' + new Date().getTime();
            list.getButtonByText('Add').click();
            browser.waitForAngular();
        });
        */

    });

})();
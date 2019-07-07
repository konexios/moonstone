    (function() {
    'use strict';

    describe('The Hardware Products view', function() {
        var page;

        beforeEach(function() {
            browser.get('/#/deviceproducts');
            page = require('../list/list.po');
        });


        // -> display page
        it('should display page', function() {
            expect(page.pageHeader.isDisplayed()).toBe(true);
            expect(page.pageHeader.getText()).toContain('Hardware Products');

            expect(page.panel.isDisplayed()).toBe(true);
            expect(page.panelHeader.isDisplayed()).toBe(true);
            expect(page.panelHeader.getText()).toContain('Hardware Products');
        });


        // click Search -> display modal
        it('should display Search modal', function() {
            page.searchButton.click();
            browser.waitForAngular();
            expect(page.activeModalHeader.isDisplayed()).toBe(true);
            expect(page.activeModalHeader.getText()).toEqual('Search');
        });


        // click Add -> display modal
        it('should display Add modal', function() {
            page.addButton.click();
            browser.waitForAngular();
            expect(page.activeModalHeader.isDisplayed()).toBe(true);
            expect(page.activeModalHeader.getText()).toEqual('Add Hardware Product');
        });


    });

})();
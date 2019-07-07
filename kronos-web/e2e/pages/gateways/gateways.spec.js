    (function() {
    'use strict';

    describe('The Gateways view', function() {
        var page;

        beforeEach(function() {
            browser.get('/#/gateways');
            page = require('./../../common/list.po');
        });


        // -> display page
        it('should display page', function() {
            //browser.waitForAngular();
            expect(browser.getCurrentUrl()).toContain('/#/gateways');
            
            expect(page.pageHeader.isDisplayed()).toBe(true);
            expect(page.pageHeader.getText()).toContain('Gateways');

            expect(page.panel.isDisplayed()).toBe(true);
            expect(page.panelHeader.isDisplayed()).toBe(true);
            expect(page.panelHeader.getText()).toContain('Gateway');
        });


        // click Search -> display modal
        it('should display Search modal', function() {
            page.getButtonByText('Search').click();
            browser.waitForAngular();
            expect(page.activeModalHeader.isDisplayed()).toBe(true);
            expect(page.activeModalHeader.getText()).toEqual('Search');
        });


        // click Add -> display modal
        it('should display Add modal', function() {
            page.getButtonByText('Add').click();
            browser.waitForAngular();
            expect(page.activeModalHeader.isDisplayed()).toBe(true);
            expect(page.activeModalHeader.getText()).toEqual('Create New Gateway');
        });


    });

})();
angular.module('kronos')
    .constant('BingMapKey', 'Ap5HnEuvjWCQ4B4R-0gMW9FU2C3A8AdmhJay6zlCCld39cvZuNUodXQfQ7pKZang')
    // Special types of Kronos exception to handle logic when exceptions occur.
    .constant('KronosExceptions', {
        // Exception related with some input in the form (with error object as result)
        InvalidInputException: 'com.arrow.kronos.api.exception.InvalidInputException',
        AccountExistsException: 'com.arrow.kronos.api.exception.AccountExistsException'
    });
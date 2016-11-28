/**
 * Created by Viktor Dobroselsky on 29.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .factory('Generator', Generator);

    Generator.$inject = ['$resource'];

    function Generator ($resource) {
        var service = $resource('api/offers/generate', {}, {
            'generate': { method:'POST' }
        });

        return service;
    }
})();

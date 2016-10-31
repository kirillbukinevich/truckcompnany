/**
 * Created by Viktor Dobroselsky on 29.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .factory('Truck', Truck);

    Truck.$inject = ['$resource'];

    function Truck ($resource) {
        var service = $resource('api/trucks/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();

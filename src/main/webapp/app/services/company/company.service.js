/**
 * Created by Vladimir on 23.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .factory('Company', Company);

    Company.$inject = ['$resource'];

    function Company ($resource) {
        var service = $resource('api/companies/:id', {}, {
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

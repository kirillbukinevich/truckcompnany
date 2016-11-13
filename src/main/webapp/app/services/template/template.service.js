/**
 * Created by Viktor Dobroselsky on 28.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .factory('Template', Template);

    Template.$inject = ['$resource'];

    function Template ($resource) {
        var service = $resource('api/templates/:id', {}, {
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

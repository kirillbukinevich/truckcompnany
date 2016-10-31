
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .factory('RouteList', RouteList);

    RouteList.$inject = ['$resource'];

    function RouteList ($resource) {
        var service = $resource('api/routelists/:id', {}, {
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

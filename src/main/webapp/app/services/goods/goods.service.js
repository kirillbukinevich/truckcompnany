(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .factory('Goods1', Goods1);

    Goods1.$inject = ['$resource'];

    function Goods1 ($resource) {
        var service = $resource('api/goods/:id', {}, {
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

(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .factory('WriteOffAct', WriteOffAct);

    WriteOffAct.$inject = ['$resource'];

    function WriteOffAct ($resource) {
        var service = $resource('api/writeoffs/:id', {}, {
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

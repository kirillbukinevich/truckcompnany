(function () {
    'use strict';

    angular.module('truckCompanyApp')
        .factory('Checkpoint',Checkpoint);

    Checkpoint.$inject = ['$resource'];

    function Checkpoint($resource) {
        var service = $resource('api/checkpoint/:id',{},{
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

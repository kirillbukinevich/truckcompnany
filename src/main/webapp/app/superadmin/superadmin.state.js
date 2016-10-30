/**
 * Created by Vladimir on 22.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('superadmin', {
            abstract: true,
            parent: 'app',
            views: {
                'content@': {
                    templateUrl: 'app/superadmin/superadmin.html',
                    controller: 'SuperadminController',
                    controllerAs: 'vm'
                },
            }
        });
    }
})();

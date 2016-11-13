/**
 * Created by Dmitry on 11.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('manager', {
            abstract: true,
            parent: 'roles',
            views: {
                'content@': {
                    templateUrl: 'app/roles/manager/manager.html',
                    controller: 'ManagerController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();

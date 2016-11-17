/**
 * Created by Dmitry on 12.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('manager.routelists', {
                    parent: 'manager',
                    url: '/manager/routelists',

                    data: {
                        authorities: ["ROLE_MANAGER"],
                        pageTitle: 'activate.title'
                    },
                    views: {
                        'page@roles': {
                            templateUrl: 'app/roles/manager/routelist/manager.routelist.html',
                            controller: 'ManagerRoutelistsController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('activate');
                            return $translate.refresh();
                        }]
                    }
                }
            )
    }
})();

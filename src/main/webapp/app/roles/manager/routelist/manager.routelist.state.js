/**
 * Created by Dmitry on 12.11.2016.
 */
(function () {
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
                        pageTitle: 'manger.routelists'
                    },
                    views: {
                        'page@roles': {
                            templateUrl: 'app/roles/manager/routelist/manager.routelist.html',
                            controller: 'ManagerRoutelistController',
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
            .state('manager.routelist-direction', {
                parent: 'manager',
                url: '/manager/routelist-for-waybill/{id}',

                data: {
                    authorities: ["ROLE_MANAGER"],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/manager/routelist/manager.routelist-direction.html',
                        controller: 'ManagerRoutelistDirectionController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                }
            })
    }
})();

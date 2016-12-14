(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('driver.routelist', {
                    parent: 'driver',
                    url: '/driver/routelist', /*'/activate?key',*/

                    data: {
                        authorities: ["ROLE_DRIVER"],
                        pageTitle: 'activate.title'
                    },
                    views: {
                        'page@roles': {
                            templateUrl: 'app/roles/driver/routelist/driver.routelist.html',
                            controller: 'DriverRoutelistController',
                            controllerAs: 'vm'
                        },
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('driver');
                            return $translate.refresh();
                        }]
                    }
                }
            ).state('driver.complete', {
            parent: 'driver',
            url: '/driver/complete', /*'/activate?key',*/

            data: {
                authorities: ["ROLE_DRIVER"],
                pageTitle: 'activate.title'
            },
            views: {
                'page@roles': {
                    templateUrl: 'app/roles/driver/deliveryCompleted/driver.completed.html',
                    controller: 'DriverCompletedController',
                    controllerAs: 'vm'
                },
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('driver');
                    return $translate.refresh();
                }]
            }
        })
    }
})();

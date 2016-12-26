(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('driver.history', {
                parent: 'driver',
                url: '/driver/history', /*'/activate?key',*/

                data: {
                    authorities: ["ROLE_DRIVER"],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/driver/history/driver.history.html',
                        controller: 'DriverHistoryController',
                        controllerAs: 'vm'
                    },
                },
                params: {
                    page: 1,
                    size: 5,
                    startDate: null,
                    endDate: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            size: PaginationUtil.parsePage($stateParams.size)
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('companyowner');
                        return $translate.refresh();
                    }]
                }
            }).state('driver.history.routelist-details', {
            parent: 'driver',
            url: '/driver/history/routelistDetails/{id}',

            data: {
                authorities: ["ROLE_DRIVER"],
                pageTitle: 'activate.title'
            },
            views: {
                'page@roles': {
                    templateUrl: 'app/roles/driver/history/driver.history.routelist-details.html',
                    controller: 'DriverHistoryRoutelistDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyowner');
                    return $translate.refresh();
                }]
            }

        })
    }
})();


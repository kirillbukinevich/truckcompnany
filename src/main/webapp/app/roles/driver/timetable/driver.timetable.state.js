(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('driver.timetable', {
                parent: 'driver',
                url: '/driver/timetable', /*'/activate?key',*/

                data: {
                    authorities: ["ROLE_DRIVER"],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/driver/timetable/driver.timetable.html',
                        controller: 'DriverTimetableController',
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
            }).state('driver.timetable.routelist-details', {
            parent: 'driver',
            url: '/driver/timetable/routelistDetails/{id}',

            data: {
                authorities: ["ROLE_DRIVER"],
                pageTitle: 'activate.title'
            },
            views: {
                'page@roles': {
                    templateUrl: 'app/roles/driver/timetable/driver.timetable.routelist-details.html',
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


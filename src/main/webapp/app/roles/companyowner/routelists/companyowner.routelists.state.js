
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('companyowner.routelists', {
                parent: 'companyowner',
                url: '/companyowner/routelists',

                data: {
                    authorities: ['ROLE_COMPANYOWNER'],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/companyowner/routelists/companyowner.routelists.html',
                        controller: 'CompanyownerRouteListsController',
                        controllerAs: 'vm'
                    }
                },
                params:{
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
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                }
            })
            .state('companyowner.routelist-details', {
                parent: 'companyowner',
                url: '/companyowner/routelistDetails/{id}',

                data: {
                    authorities: ["ROLE_COMPANYOWNER"],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/companyowner/routelists/companyowner.routelist-details.html',
                        controller: 'CompanyownerRoutelistDetailsController',
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

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
        $stateProvider
            .state('superadmin.companies', {
                parent: 'superadmin',
                url: '/superadmin/companies',

                data: {
                    authorities: ["ROLE_SUPERADMIN"],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/superadmin/companies/superadmin.companies.html',
                        controller: 'SuperadminCompaniesController',
                        controllerAs: 'vm'
                    },
                },
                params: {
                    page: 1,
                    size: 5
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
            .state('superadmin.company',{
                parent: 'superadmin',
                url: '/superadmin/companies/:id',

                data: {
                    authorities: ["ROLE_SUPERADMIN"],
                    pageTitle: 'activate.title'
                },

                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/superadmin/companies/superadmin.company.html',
                        controller: 'SuperadminCompanyController',
                        controllerAs: 'vm'
                    },
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                }
            })
            .state('superadmin.companycreate',{
                parent: 'superadmin',
                url: '/superadmin/company/create',

                data: {
                    authorities: ["ROLE_SUPERADMIN"],
                    pageTitle: 'activate.title'
                },

                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/superadmin/companies/superadmin.companycreate.html',
                        controller: 'SuperadminCompanyCreateController',
                        controllerAs: 'vm'
                    },
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                }
            });
        }
})();

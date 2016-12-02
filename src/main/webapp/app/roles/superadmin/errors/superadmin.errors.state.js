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
            .state('superadmin.errors', {
                parent: 'superadmin',
                url: '/superadmin/errors',

                data: {
                    authorities: ["ROLE_SUPERADMIN"],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/superadmin/errors/superadmin.errors.html',
                        controller: 'SuperadminErrorsController',
                        controllerAs: 'vm'
                    },
                },/*
                params: {
                    page: 1,
                    size: 5
                },*/
                resolve: {
                    /*pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            size: PaginationUtil.parsePage($stateParams.size)
                        };
                    }],*/
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                }
            });
        }
})();

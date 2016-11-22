/**
 * Created by Vladimir on 30.10.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('admincompany.storages', {
                parent: 'admincompany',
                url: '/admincompany/storages',

                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'activate.title',
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/admincompany/storages/admincompany.storages.html',
                        controller: 'AdmincompanyStoragesController',
                        controllerAs: 'vm'
                    },
                },
                params: {
                    page: 1,
                    size: 10,
                    sort: {
                        value: 'id,asc',
                        squash: true
                    }
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            size: PaginationUtil.parsePage($stateParams.size),
                            sort: $stateParams.sort,
                             predicate: PaginationUtil.parsePredicate($stateParams.sort),
                             ascending: PaginationUtil.parseAscending($stateParams.sort)
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                }
            })
            .state('admincompany.storage', {
                parent: 'admincompany',
                url: '/admincompany/storages/:id',

                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/admincompany/storages/admincompany.storage.html',
                        controller: 'AdmincompanyStorageController',
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
            .state('admincompany.createstorage', {
                parent: 'admincompany',
                url: '/admincompany/storagecreate',

                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/admincompany/storages/admincompany.storagecreate.html',
                        controller: 'AdmincompanyStorageCreateController',
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

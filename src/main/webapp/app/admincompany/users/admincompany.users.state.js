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
            .state('admincompany.users', {
                parent: 'admincompany',
                url: '/admincompany/users',

                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@admincompany': {
                        templateUrl: 'app/admincompany/users/admincompany.users.html',
                        controller: 'AdmincompanyUsersController',
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
            .state('admincompany.user', {
                parent: 'admincompany',
                url: '/admincompany/users/:id',

                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@admincompany': {
                        templateUrl: 'app/admincompany/users/admincompany.user.html',
                        controller: 'AdmincompanyUserController',
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
            .state('admincompany.createuser', {
                parent: 'admincompany',
                url: '/admincompany/usercreate',

                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@admincompany': {
                        templateUrl: 'app/admincompany/users/admincompany.usercreate.html',
                        controller: 'AdmincompanyUserCreateController',
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

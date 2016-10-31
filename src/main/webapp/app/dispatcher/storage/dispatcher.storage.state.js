/**
 * Created by Viktor Dobroselsky on 28.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('dispatcher.storage', {
                    parent: 'dispatcher',
                    url: '/dispatcher/storage', /*'/activate?key',*/

                    data: {
                        authorities: ["ROLE_DISPATCHER"],
                        pageTitle: 'activate.title'
                    },
                    views: {
                        'page@dispatcher': {
                            templateUrl: 'app/dispatcher/storage/dispatcher.storage.html',
                            controller: 'DispatcherStorageController',
                            controllerAs: 'vm'
                        },
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('activate');
                            return $translate.refresh();
                        }]
                    }
                }
            )
            .state('dispatcher.storagecreate',{
                parent: 'dispatcher',
                url: '/dispatcher/storage/create',

                data: {
                    authorities: ["ROLE_DISPATCHER"],
                    pageTitle: 'activate.title'
                },

                views: {
                    'page@dispatcher': {
                        templateUrl: 'app/dispatcher/storage/dispatcher.storagecreate.html',
                        controller: 'DispatcherStorageCreateController',
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

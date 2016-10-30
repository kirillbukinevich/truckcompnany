/**
 * Created by Viktor Dobroselsky on 29.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('dispatcher.trucks', {
                    parent: 'dispatcher',
                    url: '/dispatcher/trucks', /*'/activate?key',*/

                    data: {
                        authorities: ["ROLE_DISPATCHER"],
                        pageTitle: 'activate.title'
                    },
                    views: {
                        'page@dispatcher': {
                            templateUrl: 'app/dispatcher/trucks/dispatcher.trucks.html',
                            controller: 'DispatcherTrucksController',
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
            );
    }
})();

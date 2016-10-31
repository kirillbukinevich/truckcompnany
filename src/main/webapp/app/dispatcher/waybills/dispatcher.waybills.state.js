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
            .state('dispatcher.waybills', {
                parent: 'dispatcher',
                url: '/dispatcher/waybills', /*'/activate?key',*/

                data: {
                    authorities: ["ROLE_DISPATCHER"],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@dispatcher': {
                        templateUrl: 'app/dispatcher/waybills/dispatcher.waybills.html',
                        controller: 'DispatcherWaybillsController',
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

/**
 * Created by Viktor Dobroselsky on 31.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('dispatcher.drivers', {
                    parent: 'dispatcher',
                    url: '/dispatcher/drivers', /*'/activate?key',*/

                    data: {
                        authorities: ["ROLE_DISPATCHER"],
                        pageTitle: 'activate.title'
                    },
                    views: {
                        'page@dispatcher': {
                            templateUrl: 'app/dispatcher/drivers/dispatcher.drivers.html',
                            controller: 'DispatcherDriversController',
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

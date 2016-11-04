/**
 * Created by Viktor Dobroselsky on 04.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('dispatcher.offer', {
                    parent: 'dispatcher',
                    url: '/dispatcher/offer', /*'/activate?key',*/

                    data: {
                        authorities: ["ROLE_DISPATCHER"],
                        pageTitle: 'activate.title'
                    },
                    views: {
                        'page@dispatcher': {
                            templateUrl: 'app/dispatcher/offer/dispatcher.offer.html',
                            controller: 'DispatcherOfferController',
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

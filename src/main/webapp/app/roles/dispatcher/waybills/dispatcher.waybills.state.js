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
                url: '/dispatcher/waybills',

                data: {
                    authorities: ["ROLE_DISPATCHER"],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/dispatcher/waybills/dispatcher.waybills.html',
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
        ).state('dispatcher.waybills-details', {
            parent: 'dispatcher.waybills',
            url: '/details/{id}',
            data: {
                authorities: ['ROLE_DISPATCHER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/roles/dispatcher/waybills/dispatcher.waybills-details.html',
                    controller: 'DispatcherWaybillDetailsController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Waybill', function(Waybill) {
                            return Waybill.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('dispatcher.waybills', null, { reload: true });
                }, function() {
                    $state.go('dispatcher.waybills');
                });
            }]
        });
    }
})();

/**
 * Created by Dmitry on 12.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('manager.waybills', {
                    parent: 'manager',
                    url: '/manager/waybills',

                    data: {
                        authorities: ["ROLE_MANAGER"],
                        pageTitle: 'activate.title'
                    },
                    views: {
                        'page@roles': {
                            templateUrl: 'app/roles/manager/waybill/manager.waybill.html',
                            controller: 'ManagerWaybillsController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('activate');
                            return $translate.refresh();
                        }]
                    }
                }
            )
            .state('manager.waybill-details', {
                parent: 'manager.waybills',
                url: '/details/{id}',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/roles/manager/waybill/manager.waybill-details.html',
                        controller: 'ManagerWaybillDetailsController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Waybill', function(Waybill) {
                                return Waybill.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('manager.waybills', null, { reload: true });
                    }, function() {
                        $state.go('manager.waybills');
                    });
                }]
            })
    }
})();

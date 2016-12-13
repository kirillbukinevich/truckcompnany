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
                    pageTitle: 'dispatcher.waybill.name'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/dispatcher/waybills/dispatcher.waybills.html',
                        controller: 'DispatcherWaybillsController',
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
                        $translatePartialLoader.addPart('dispatcher');
                        return $translate.refresh();
                    }]
                }
            }
        ).state('dispatcher.waybills-details', {
            parent: 'dispatcher.waybills',
            url: '/{id}/details',
            data: {
                authorities: ['ROLE_DISPATCHER'],
                pageTitle: 'dispatcher.waybill.details.name'
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

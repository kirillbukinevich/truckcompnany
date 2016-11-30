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
                        pageTitle: 'dispatcher.offers'
                    },
                    views: {
                        'page@roles': {
                            templateUrl: 'app/roles/dispatcher/offer/dispatcher.offer.html',
                            controller: 'DispatcherOfferController',
                            controllerAs: 'vm'
                        },
                    },
                    params: {
                        page: 1,
                        size: 10
                    },
                    resolve: {
                        pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                            return {
                                page: PaginationUtil.parsePage($stateParams.page),
                                size: PaginationUtil.parsePage($stateParams.size)
                            };
                        }],
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('activate');
                            return $translate.refresh();
                        }]
                    }
                }
            )
            .state('dispatcher.offer-details', {
                parent: 'dispatcher.offer',
                url: '/{id}/details',
                data: {
                    authorities: ['ROLE_DISPATCHER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/roles/dispatcher/offer/dispatcher.offer-details.html',
                        controller: 'DispatcherOfferDetailsController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Offer', function(Offer) {
                                return Offer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('dispatcher.offer', null, { reload: true });
                    }, function() {
                        $state.go('dispatcher.offer');
                    });
                }]
            })
            .state('dispatcher.offer-submit', {
                parent: 'dispatcher.offer',
                url: '/{id}/submit', /*'/activate?key',*/

                data: {
                    authorities: ["ROLE_DISPATCHER"],
                    pageTitle: 'dispatcher.confirm'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/dispatcher/offer/dispatcher.offer-submit.html',
                        controller: 'DispatcherOfferSubmitController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dispatcher');
                        return $translate.refresh();
                    }]
                }
            });
    }
})();

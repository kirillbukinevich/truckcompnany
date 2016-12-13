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
                    url: '/dispatcher/offer',

                    data: {
                        authorities: ["ROLE_DISPATCHER"],
                        pageTitle: 'dispatcher.offer.name'
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
            )
            .state('dispatcher.offer-details', {
                parent: 'dispatcher.offer',
                url: '/{id}/details',
                data: {
                    authorities: ['ROLE_DISPATCHER'],
                    pageTitle: 'dispatcher.offer.details.name'
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
                url: '/{id}/submit',

                data: {
                    authorities: ["ROLE_DISPATCHER"],
                    pageTitle: 'dispatcher.offer.submitOffer'
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

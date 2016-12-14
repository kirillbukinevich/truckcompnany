(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('manager.deliveredGoods', {
                    parent: 'manager',
                    url: '/manager/deliveredGoods',

                    data: {
                        authorities: ["ROLE_MANAGER"],
                        pageTitle: 'manager.goods'
                    },
                    views: {
                        'page@roles': {
                            templateUrl: 'app/roles/manager/deliveredGoods/manager.deliveredGoods.html',
                            controller: 'ManagerDeliveredGoodsController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('manager');
                            return $translate.refresh();
                        }]
                    }
                }
            );
    }
})();

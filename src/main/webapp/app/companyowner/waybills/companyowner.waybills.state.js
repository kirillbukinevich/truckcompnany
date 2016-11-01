(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('companyowner.waybills', {
                parent: 'companyowner',
                url: '/companyowner/waybills',

                data: {
                    authorities: ['ROLE_COMPANYOWNER'],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@companyowner': {
                        templateUrl: 'app/companyowner/waybills/companyowner.waybills.html',
                        controller: 'CompanyownerWaybillsController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                }
            });
    }
})();

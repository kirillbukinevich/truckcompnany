
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('companyowner.routelists', {
                parent: 'companyowner',
                url: '/companyowner/routelists',

                data: {
                    authorities: ['ROLE_COMPANYOWNER'],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@companyowner': {
                        templateUrl: 'app/companyowner/routelists/companyowner.routelists.html',
                        controller: 'CompanyownerRouteListsController',
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

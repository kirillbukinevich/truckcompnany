(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('companyowner.statistics', {
                parent: 'companyowner',
                url: '/companyowner/statistics',

                data: {
                    authorities: ["ROLE_COMPANYOWNER"],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@companyowner': {
                        templateUrl: 'app/companyowner/statistics/companyowner.statistics.html',
                        /*controller: 'CompanyownerStatisticsController',
                        controllerAs: 'vm'*/
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

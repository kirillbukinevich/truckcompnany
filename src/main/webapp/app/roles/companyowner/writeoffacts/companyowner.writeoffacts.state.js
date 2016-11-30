(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('companyowner.writeoffacts', {
                parent: 'companyowner',
                url: '/companyowner/writeoffacts',

                data: {
                    authorities: ['ROLE_COMPANYOWNER'],
                    pageTitle: 'activate.title'
                },
                views: {
                    'page@roles': {
                        templateUrl: 'app/roles/companyowner/writeoffacts/companyowner.writeoffacts.html',
                        controller: 'CompanyownerWriteOffActsController',
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

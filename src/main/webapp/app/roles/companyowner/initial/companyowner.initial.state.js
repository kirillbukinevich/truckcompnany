(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('companyowner.initial', {
            parent: 'companyowner',
            url: '/companyowner/initial',

            data: {
                authorities: ['ROLE_COMPANYOWNER'],
                pageTitle: 'activate.title'
            },
            views: {
                'page@roles': {
                    templateUrl: 'app/roles/companyowner/initial/companyowner.initial.html',
                    controller: 'CompanyownerInitialController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyowner');
                    return $translate.refresh();
                }]
            }
        });
    }
})();

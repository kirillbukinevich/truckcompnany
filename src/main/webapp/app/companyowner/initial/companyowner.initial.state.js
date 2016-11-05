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
                'page@companyowner': {
                    templateUrl: 'app/companyowner/initial/companyowner.initial.html',
                    /*controller: 'AdmincompanyUsersController',
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


(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('companyowner', {
            abstract: true,
            parent: 'app',
            views: {
                'content@': {
                    templateUrl: 'app/companyowner/companyowner.html',
                    controller: 'CompanyownerController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();

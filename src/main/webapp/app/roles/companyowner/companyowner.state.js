
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('companyowner', {
            abstract: true,
            parent: 'roles',
            views: {
                'sidebar@roles':{
                    templateUrl: 'app/roles/companyowner/sidebar.html'
                }
            }
        });
    }
})();

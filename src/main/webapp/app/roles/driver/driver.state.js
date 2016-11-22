(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('driver', {
            abstract: true,
            parent: 'roles',
            views: {
                'sidebar@roles': {
                    templateUrl: 'app/roles/driver/sidebar.html'
                }
            }
        });
    }
})();

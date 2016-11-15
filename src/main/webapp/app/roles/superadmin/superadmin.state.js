/**
 * Created by Vladimir on 22.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('superadmin', {
            abstract: true,
            parent: 'roles',
            views: {
                'sidebar@roles': {
                    templateUrl: 'app/roles/superadmin/sidebar.html',
                },
            }
        });
    }
})();

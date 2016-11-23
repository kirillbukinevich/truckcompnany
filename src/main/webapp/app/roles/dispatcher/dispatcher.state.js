/**
 * Created by Viktor Dobroselsky on 28.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('dispatcher', {
            abstract: true,
            parent: 'roles',
            views: {
                'sidebar@roles': {
                    templateUrl: 'app/roles/dispatcher/sidebar.html',
                },
            }
        });
    }
})();

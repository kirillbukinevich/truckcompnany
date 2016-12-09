(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('driver.routelist', {
                    parent: 'driver',
                    url: '/driver/routelist', /*'/activate?key',*/

                    data: {
                        authorities: ["ROLE_DRIVER"],
                        pageTitle: 'activate.title'
                    },
                    views: {
                        'page@roles': {
                            templateUrl: 'app/roles/driver/routelist/driver.routelist.html',
                            controller: 'DriverRoutelistController',
                            controllerAs: 'vm'
                        },
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('activate');
                            return $translate.refresh();
                        }]
                    }
                }
            )
    }
})();

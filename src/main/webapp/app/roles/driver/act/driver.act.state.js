(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('driver.act', {
                    parent: 'driver',
                    url: '/driver/act', /*'/activate?key',*/

                    data: {
                        authorities: ["ROLE_DRIVER"],
                        pageTitle: 'activate.title'
                    },
                    views: {
                        'page@roles': {
                            templateUrl: 'app/roles/driver/act/driver.act.html',
                            controller: 'DriverCreateActController',
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

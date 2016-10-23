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
        $stateProvider.state('superadmin.initial', {
            parent: 'superadmin',
            url: '/superadmin/initial',/*'/activate?key',*/

            data: {
                authorities: [],
                pageTitle: 'activate.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/superadmin/initial/superadmin.initial.html'
                    /*controller: 'ActivationController',
                    controllerAs: 'vm'*/
                },
               /* 'sidebar@':{
                    template: 'TEST',
                }*/
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

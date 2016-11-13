/**
 * Created by Dmitry on 12.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('manager.initial', {
            parent: 'manager',
            url: '/manager/initial',

            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'activate.title'
            },
            view: {
                'page@roles': {
                    templateUrl: 'app/roles/manager/initial/manager.initial.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activate');
                    return $translate.refresh();
                }]
            }
        })
    }
});

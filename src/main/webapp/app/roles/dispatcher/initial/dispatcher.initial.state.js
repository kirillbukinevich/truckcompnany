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
        $stateProvider.state('dispatcher.initial', {
            parent: 'dispatcher',
            url: '/dispatcher/initial',

            data: {
                authorities: ['ROLE_DISPATCHER'],
                pageTitle: 'dispatcher.initialTitle'
            },
            views: {
                'page@roles': {
                    templateUrl: 'app/roles/dispatcher/initial/dispatcher.initial.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dispatcher');
                    return $translate.refresh();
                }]
            }
        });
    }
})();

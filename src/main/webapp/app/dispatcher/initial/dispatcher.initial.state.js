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
            url: '/dispatcher/initial',/*'/activate?key',*/

            data: {
                authorities: [],
                pageTitle: 'activate.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/dispatcher/initial/dispatcher.initial.html'
                },
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

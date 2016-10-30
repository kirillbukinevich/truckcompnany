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
        $stateProvider.state('admincompany.initial', {
            parent: 'admincompany',
            url: '/admincompany/initial',/*'/activate?key',*/

            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'activate.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admincompany/initial/admincompany.initial.html'
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

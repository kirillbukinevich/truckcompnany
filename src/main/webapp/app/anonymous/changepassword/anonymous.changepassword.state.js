/**
 * Created by Vladimir on 27.10.2016.
 */
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
        $stateProvider
            .state('anonymous.changepassword', {
                parent: 'anonymous',
                url: '/anonymous/changepassword?key',/*'/activate?key',*/

                data: {
                    authorities: [],
                    pageTitle: 'activate.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/anonymous/changepassword/anonymous.changepassword.html',
                        controller: 'ChangePassword',
                        controllerAs: 'vm'
                    },
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                }
            })
    }
})();

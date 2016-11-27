/**
 * Created by Vladimir on 20.11.2016.
 */



(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .directive('checkValidEmail', CheckValidEmail);


    CheckValidEmail.$inject = ['$http'];
    function CheckValidEmail($http) {
        return {
            require: 'ngModel',
            link: function (scope, elem, attr, ngModel) {

                //For DOM -> model validation
                ngModel.$parsers.unshift(function (value) {
                    $http({
                        method: 'GET',
                        url: '/api/users/validemail/' + value.trim()
                    }).then(function successCallback(response) {
                            ngModel.$setValidity('existingemail', true);
                        },
                        function errorCallback(response) {
                            ngModel.$setValidity('existingemail', false);
                        });
                    return value;
                });

                //For model -> DOM validation
                /* ngModel.$formatters.unshift(function (value) {
                 ngModel.$setValidity('blacklist', blacklist.indexOf(value) === -1);
                 return value;
                 });*/
            }
        };
    }
})();

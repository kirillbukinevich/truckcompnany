/**
 * Created by Vladimir on 20.11.2016.
 */



(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .directive('checkValidLogin', CheckValidLogin);


    CheckValidLogin.$inject = ['$http'];
    function CheckValidLogin($http) {
        return {
            require: 'ngModel',
            link: function (scope, elem, attr, ngModel) {

                //For DOM -> model validation
                ngModel.$parsers.unshift(function (value) {
                    if (value.trim()=="") {
                        ngModel.$setValidity('existinglogin', true);
                        return value;
                    }
                    $http({
                        method: 'GET',
                        url: '/api/users/validlogin/' + value.trim()
                    }).then(function successCallback(response) {
                            ngModel.$setValidity('existinglogin', true);
                        },
                        function errorCallback(response) {
                            ngModel.$setValidity('existinglogin', false);
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

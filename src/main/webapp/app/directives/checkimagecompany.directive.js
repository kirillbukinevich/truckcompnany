/**
 * Created by Vladimir on 29.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .directive('checkImageCompany', CheckImageCompany);

    CheckImageCompany.$inject = ['$http'];

    function CheckImageCompany ($http) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                attrs.$observe('ngSrc', function(ngSrc) {
                   $http.get(ngSrc).success(function(){

                    }).error(function(){
                        element.attr('src', '/content/images/no-logo.jpg'); // set default image
                    });
                });
            }
        };
    }
})();

/**
 * Created by Vladimir on 29.10.2016.
 */
/**
 * Created by Vladimir on 29.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .directive('checkImageUser', CheckImageUser);

    CheckImageUser.$inject = ['$http'];

    function CheckImageUser ($http) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                attrs.$observe('ngSrc', function(ngSrc) {
                    $http.get(ngSrc).success(function(){

                    }).error(function(){
                        element.attr('src', '/content/images/logo-user.png'); // set default image
                    });
                });
            }
        };
    }
})();

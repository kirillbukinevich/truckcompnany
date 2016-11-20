/**
 * Created by Vladimir on 20.11.2016.
 */



(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .directive('checkValidDate', CheckValidDate);



    function CheckValidDate () {
        return {
            require: 'ngModel',
            link: function(scope, elem, attr, ngModel) {
                alert(attr);
                console.log(attr);
                //var blacklist = attr.blacklist.split(',');


                //For DOM -> model validation
                ngModel.$parsers.unshift(function (value) {
                    console.log(value);
                    var input = value.split("-");
                    console.log(input)
                    return value;


                    /*var valid = blacklist.indexOf(value) === -1;
                    ngModel.$setValidity('blacklist', valid);
                    return valid ? value : undefined;*/
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

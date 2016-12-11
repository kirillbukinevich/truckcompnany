/**
 * Created by Vladimir on 20.11.2016.
 */



(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .directive('checkMaxDate', CheckMaxDate);


    CheckMaxDate.$inject = ['$http'];
    function CheckMaxDate($http) {
        return {
            require: 'ngModel',
            link: function (scope, elem, attr, ngModel) {
                var currentDate = moment(new Date()).subtract(1,"d").toDate();
                currentDate.setHours(0);
                currentDate.setMinutes(0);
                currentDate.setSeconds(0);

                //For DOM -> model validation
                ngModel.$parsers.unshift(function (value) {
                    console.log(value);
                    if (moment(value, "YYYY/MM/DD").isValid()) {
                        var assignDate = moment(value, "YYYY/MM/DD").toDate();
                        if (assignDate >= currentDate) {
                            ngModel.$setValidity('maxDate', false);
                        } else {
                            ngModel.$setValidity('maxDate', true);
                        }
                    }
                    return value;
                });

            }
        };
    }
})();

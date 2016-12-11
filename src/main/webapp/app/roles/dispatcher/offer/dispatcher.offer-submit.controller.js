/**
 * Created by Viktor Dobroselsky on 09.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherOfferSubmitController', DispatcherOfferSubmitController);

    DispatcherOfferSubmitController.$inject = ['$stateParams', 'Offer', 'Waybill', '$scope', '$state', '$http'];

    function DispatcherOfferSubmitController ($stateParams, Offer, Waybill, $scope, $state, $http) {
        var vm = this;

        vm.offer = Offer.get({id : $stateParams.id});
        vm.drivers = [];
        vm.trucks = [];
        vm.createWaybill = createWaybill;
        vm.setTrucks = setTrucks;
        vm.setDrivers = setDrivers;

        vm.driver = null;
        vm.truck = null;
        vm.saveObj = {};
        vm.dt = {};
        vm.arrivalDate = null;
        vm.leaveDate = null;
        vm.error = false;
        vm.deliveryTime = 0;

        function setTrucks() {
            if (vm.arrivalDate instanceof Date && vm.leaveDate instanceof Date) {
                $http({
                    method: 'GET',
                    url: '/api/trucks/bydate',
                    params: {
                        from: vm.leaveDate.getTime(),
                        to: vm.arrivalDate.getTime()
                    }

                }).then(function successCallback(response) {
                        console.log(response);
                        vm.trucks = response.data;

                        if (vm.trucks.indexOf(vm.truck) == -1) {
                            vm.truck = null;
                        }
                    },
                    function errorCallback(response) {
                        vm.trucks = null;
                        vm.truck = null;
                    });
            }
        }

        function setDrivers() {
            if (vm.arrivalDate instanceof Date && vm.leaveDate instanceof Date){
                $http({
                    method: 'GET',
                    url: '/api/drivers/bydate',
                    params: {
                        from: vm.leaveDate.getTime(),
                        to: vm.arrivalDate.getTime()
                    }

                }).then(function successCallback(response) {
                        console.log(response);
                        vm.drivers = response.data;

                        if (vm.drivers.indexOf(vm.driver) == -1)
                            vm.driver = null;
                    },
                    function errorCallback(response) {
                        vm.drivers = null;
                        vm.driver = null;
                    });
            }
        }

        function createWaybill() {
            console.log("Create new Waybill");
            vm.offer.state = "ACCEPTED";

            vm.saveObj = {
                driver: vm.driver,
                offer: vm.offer,
                routeList: {
                    truck: vm.truck,
                    leavingStorage: vm.offer.leavingStorage,
                    arrivalStorage: vm.offer.arrivalStorage,
                    leavingDate: vm.leaveDate.getTime(),
                    arrivalDate: vm.arrivalDate.getTime()
                }
            };
            console.log(vm.saveObj);

            Waybill.save(vm.saveObj,
                function () {
                    $state.go('dispatcher.offer');
                },
                function (resp) {
                    vm.error = true;
                    vm.messageError = 'Error on server side. Please, try later.';
                }
            );
        }

        //**==========TimePicker===================

        vm.arrivalPicker = {
            date: new Date(),
            datepickerOptions : {
                maxDate: null,
                showWeeks: false
            },
            timepickerOptions: {
                showMeridian: false
            }
        };

        vm.leavingPicker = {
            date: new Date(),
            datepickerOptions : {
                minDate: new Date(),
                showWeeks: false
            },
            timepickerOptions: {
                showMeridian: false
            }
        };


        this.openCalendar = function(e, picker) {
            vm[picker].open = true;
        };

        // watch min and max dates to calculate difference
        var unwatchMinMaxValues = $scope.$watch(function() {
            return [vm.leavingPicker, vm.arrivalPicker];
        }, function() {
            vm.arrivalPicker.datepickerOptions.minDate = vm.leaveDate;
            vm.leavingPicker.datepickerOptions.maxDate = vm.arrivalDate;
        }, true);


        // destroy watcher
        $scope.$on('$destroy', function() {
            unwatchMinMaxValues();
        });

    }
})();

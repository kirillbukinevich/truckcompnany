/**
 * Created by Viktor Dobroselsky on 09.11.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherOfferSubmitController', DispatcherOfferSubmitController);

    DispatcherOfferSubmitController.$inject = ['$stateParams', 'Offer', 'Driver', 'Truck', 'Waybill', '$scope', '$state'];

    function DispatcherOfferSubmitController ($stateParams, Offer, Driver, Truck, Waybill, $scope, $state) {
        var vm = this;

        vm.offer = Offer.get({id : $stateParams.id});
        vm.drivers = Driver.query();
        vm.trucks = Truck.query();
        vm.createWaybill = createWaybill;

        vm.driver = {};
        vm.truck = {};
        vm.saveObj = {};
        vm.dt = {};
        vm.arrivalDate = {};
        vm.leaveDate = {};
        vm.error = false;
        vm.messageError;

        console.log(vm.drivers);
        console.log(vm.trucks);

        function createWaybill() {
            console.log("Create new Waybill");
            vm.saveObj = {
                driverId: vm.driver.id,
                routeList: {
                    truckId: vm.truck.id,
                    leavingStorageId: vm.offer.leavingStorage.id,
                    arrivalStorageId: vm.offer.arrivalStorage.id,
                    leaving: vm.leaveDate.getTime(),
                    arrival: vm.arrivalDate.getTime()
                }
            };
            console.log(vm.saveObj);

            Waybill.save(vm.saveObj,
                function () {
                    console.log("Create new Waybill");
                    $state.go('dispatcher.offer');
                },
                function (resp) {
                    vm.error = true;
                    vm.messageError = 'You don\'t fill all fields.';
                }
            );

            vm.offer.state = "ACCEPTED";
            Offer.update(vm.offer);

        }

        //**==========TimePicker===================

        vm.arrivalPicker = {
            date: new Date(),
            datepickerOptions : {
                maxDate: null,
                showWeeks: false
            }
        };

        vm.leavingPicker = {
            date: new Date(),
            datepickerOptions : {
                minDate: new Date(),
                showWeeks: false
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
        }, true);


        // destroy watcher
        $scope.$on('$destroy', function() {
            unwatchMinMaxValues();
        });

    }
})();

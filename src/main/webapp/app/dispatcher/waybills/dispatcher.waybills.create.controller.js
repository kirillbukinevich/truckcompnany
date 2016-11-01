/**
 * Created by Viktor Dobroselsky on 31.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherWaybillCreateController', DispatcherWaybillCreateController);

    DispatcherWaybillCreateController.$inject = ['$state', 'Storage', 'Driver'];

    function DispatcherWaybillCreateController ($state, Storage, Driver) {
        var vm = this;
        vm.storage = {};
        vm.drivers = Driver.query();

        vm.create = function(){
            console.log("Create new storage with name = " + vm.storage.name);
            console.log(vm.storage);

            Storage.save({
                    name: vm.storage.name
                },
                function () {
                    console.log("Create new Storage: " + vm.storage.name);
                    $state.go('dispatcher.storage');
                });
        };
    }
})();

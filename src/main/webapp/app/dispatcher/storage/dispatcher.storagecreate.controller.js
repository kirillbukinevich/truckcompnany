/**
 * Created by Viktor Dobroselsky on 29.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherStorageCreateController', DispatcherStorageCreateController);

    DispatcherStorageCreateController.$inject = ['$state', 'Storage'];

    function DispatcherStorageCreateController ($state, Storage) {
        var vm = this;
        vm.storage = {};

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

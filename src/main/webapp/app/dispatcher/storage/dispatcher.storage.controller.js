/**
 * Created by Viktor Dobroselsky on 28.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherStorageController', DispatcherStorageController);

    DispatcherStorageController.$inject = ['$stateParams', 'Storage'];

    function DispatcherStorageController ($stateParams, Storage) {
        var vm = this;
        vm.storages = Storage.query();
        console.log(vm.storages);

        vm.delete = function(id){
            console.log(id);
            Storage.delete({id: id}, function() {
                console.log("Delete Storage with id: " + id);
                vm.storages = Storage.query();
            });
        }
    }
})();

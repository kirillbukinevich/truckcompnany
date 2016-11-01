/**
 * Created by Viktor Dobroselsky on 31.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherStorageUpdateController', DispatcherStorageUpdateController);

    DispatcherStorageUpdateController.$inject = ['$stateParams', '$state','Storage'];

    function DispatcherStorageUpdateController ($stateParams, $state, Storage) {
        var vm = this;

        Storage.get({id: $stateParams.id}, function(result) {
            vm.storage = result;
            console.log(vm.storage);
        });

        vm.update = update;
        vm.storage = {};
        vm.error = false;
        vm.messageError = '';

        function update(){
            console.log("Update Storage: " + vm.storage.name);
            Storage.update(
                {
                    id: vm.storage.id,
                    name: vm.storage.name,
                    company: vm.storage.company
                },
                function(result){
                    console.log("Update storage with id = " + vm.storage.id);
                    $state.go('dispatcher.storage');
                }
            )
        }

    }
})();

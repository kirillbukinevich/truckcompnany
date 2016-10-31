/**
 * Created by Viktor Dobroselsky on 29.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherStorageCreateController', DispatcherStorageCreateController);

    DispatcherStorageCreateController.$inject = ['$state', 'Storage', 'Principal'];

    function DispatcherStorageCreateController ($state, Storage, Principal) {
        var vm = this;
        vm.storage = {};
        getAccount();
        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                console.log(account);
            });
        }

        vm.create = function(){
            console.log("Create new storage with name = " + vm.storage.name);
            console.log(vm.storage);

            Storage.save({
                name: vm.storage.name,
                companyId: vm.account.companyId
            },
            function () {
                console.log("Create new Storage: " + vm.storage.name);
                $state.go('dispatcher.storage');
            });
        };
    }
})();

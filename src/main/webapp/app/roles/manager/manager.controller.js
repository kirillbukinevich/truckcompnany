/**
 * Created by Dmitry on 11.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerController', ManagerController);

    ManagerController.$inject = ['Principal'];

    function ManagerController(Principal) {
        var vm = this;

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                console.log(account);
            });
        }
    }
})();

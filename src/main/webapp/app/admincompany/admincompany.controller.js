/**
 * Created by Vladimir on 30.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyController', AdmincompanyController);

    AdmincompanyController.$inject = ['Principal'];

    function AdmincompanyController (Principal) {
        var vm = this;

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                console.log(account);
            });
        }


    }
})();

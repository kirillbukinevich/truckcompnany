/**
 * Created by Viktor Dobroselsky on 31.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DispatcherController', DispatcherController);

    DispatcherController.$inject = ['Principal'];

    function DispatcherController (Principal) {
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

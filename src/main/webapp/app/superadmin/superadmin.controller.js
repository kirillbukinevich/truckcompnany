/**
 * Created by Vladimir on 29.10.2016.
 */
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('SuperadminController', SuperadminController);

    SuperadminController.$inject = ['Principal'];

    function SuperadminController (Principal) {
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

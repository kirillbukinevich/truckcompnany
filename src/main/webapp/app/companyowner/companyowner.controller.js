
(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerController', CompanyownerController);

    CompanyownerController.$inject = ['Principal'];

    function CompanyownerController (Principal) {
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

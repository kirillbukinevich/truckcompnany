(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerWriteOffActsController', CompanyownerWriteOffActsController);

    CompanyownerWriteOffActsController.$inject = ['$stateParams', 'WriteOffAct'];

    function CompanyownerWriteOffActsController ($stateParams, WriteOffAct) {
        var vm = this;
        vm.writeoffacts = WriteOffAct.query();
        console.log(vm.writeoffacts);

    }
})();

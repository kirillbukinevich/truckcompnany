(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DriverCreateActController', DriverCreateActController);

    DriverCreateActController.$inject = ['$state', 'WriteOffAct'];

    function DriverCreateActController ($state, WriteOffAct) {
        var vm = this;
        vm.act = {};
        vm.driver = {};
        vm.logo = null;
        vm.error = false;



        vm.create = function(){
            console.log("Create new write-off certificate by driver " + vm.driver.firstName + vm.driver.lastName);
            WriteOffAct.save(
                vm.act,
                function () {
                    $state.go('driver.routelist');
                },
                function(resp){
                    vm.error = true;
                            vm.messageError = 'You don\'t fill all fields.';
                });
        }



    }
})();


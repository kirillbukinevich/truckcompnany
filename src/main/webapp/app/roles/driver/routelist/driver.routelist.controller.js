(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DriverRoutelistController', DriverRoutelistController);

    DriverRoutelistController.$inject = ['$stateParams', 'Waybill','Checkpoint','$http','$location'];

    function DriverRoutelistController ($stateParams, Waybill,Checkpoint,$http,$location) {
        var vm = this;
        vm.waybills = Waybill.query();
        vm.checkpoints = Checkpoint.query();
        vm.markDate = markDate;
        console.log(vm.waybills);


        function markDate(id) {
            for (var i=0; vm.checkpoints.length;i++) {
                if (vm.checkpoints[i].id == id) {
                    if(i==0 || vm.checkpoints[i-1].checkDate) {
                        var index = i;
                        $http({
                            method: 'GET',
                            url: '/api/checkpoint_mark_date/' + id,
                        }).then(function successCallback(response) {
                            console.log("date changed");
                            var today = new Date();
                            var date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
                            var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
                            var dateTime = date+' '+time;
                            for (var j in vm.checkpoints) {
                                if (vm.checkpoints[j].id == id) {
                                    vm.checkpoints[j].checkDate = dateTime;
                                }
                            }
                            checkLastCheckpoint(index);

                        });
                    }else {
                        window.alert("Mark previous checkpoint");
                    }

                }
            }



            function checkLastCheckpoint(index) {
                if((index+1)===vm.checkpoints.length){
                    for (var j in vm.waybills) {
                    $http({
                        method: 'GET',
                        url: '/api/waybills/change_status/' + vm.waybills[j].id
                    })
                    }
                    $location.path('/driver/complete'); // path not hash


                }

            }



        }
    }
})();

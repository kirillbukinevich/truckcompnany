(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DriverRoutelistController', DriverRoutelistController);

    DriverRoutelistController.$inject = ['$stateParams', 'Goods1', 'Waybill', 'Checkpoint',
        'RouteList', '$http', '$location', '$scope'];

    function DriverRoutelistController($stateParams, Goods1, Waybill, Checkpoint, RouteList, $http, $location, $scope) {
        $scope.sortType = 'name'; // set the default sort type
        $scope.sortReverse = false;  // set the default sort order
        var vm = this;
        var tempRating = 0;
        vm.routeList = {};
        vm.checkpoints = [];
        vm.goods1 = [];
        vm.checkpointNames = [];
        vm.imageWaypoints = [];
        vm.driverGoods = [];
        vm.markDate = markDate;
        vm.checkJob = checkJob;


        //for google map
        vm.travelMode = 'DRIVING';
        //for driver rating
        vm.currentRate = 0;
        vm.readOnly = true;
        //
        vm.waybills = Waybill.query(function () {
            var indexDriverGood = 0;
            angular.forEach(vm.waybills, function (value) {
                vm.driverGoods[indexDriverGood] = Goods1.query({id: value.id});
                indexDriverGood++;
                console.log("imageWaypoints: " + vm.imageWaypoints);
                if (value.state === "CHECKED") {
                    RouteList.get({id: value.routeList.id}, function (result) {
                        vm.routeList = result;
                        console.log(vm.routeList);
                    });
                    vm.checkpoints = Checkpoint.query({id: value.routeList.id}, function () {
                        var i = 0;
                        angular.forEach(vm.checkpoints, function (value) {
                            vm.checkpointNames[i] = {location: value.name, stopover: true};
                            vm.imageWaypoints[i] = {
                                position: vm.checkpoints[i].name,
                                image: 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
                            };
                            i++;
                        });
                    });
                    console.log("WaybillID " + value.id);
                    vm.goods1 = Goods1.query({id: value.id});
                }

            });

        });


        function markDate(id) {
            for (var i = 0; vm.checkpoints.length; i++) {
                if (vm.checkpoints[i].id == id) {
                    if (i == 0 || vm.checkpoints[i - 1].checkDate) {
                        var index = i;
                        $http({
                            method: 'GET',
                            url: '/api/checkpoint_mark_date/' + id,
                        }).then(function successCallback(response) {
                            console.log("date changed");
                            var today = new Date();
                            var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
                            var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
                            var dateTime = date + ' ' + time;
                            for (var j in vm.checkpoints) {
                                if (vm.checkpoints[j].id == id) {
                                    vm.checkpoints[j].checkDate = dateTime;
                                    vm.imageWaypoints[j] = {
                                        position: vm.checkpoints[j].name,
                                        image: 'http://inspire.ecoachmanager.com/images/32x32/accept_item.png'
                                    };
                                }
                            }
                            checkLastCheckpoint(index);

                        });
                    } else {
                        window.alert("Mark previous checkpoint");
                    }

                }
            }
            function checkLastCheckpoint(index) {
                if ((index + 1) === vm.checkpoints.length) {
                    for (var j in vm.waybills) {


                        if (vm.waybills[j] != true) {
                            vm.waybills[j].state = "DELIVERED";
                            $http({
                                method: 'PUT',
                                url: '/api/waybills',
                                data: vm.waybills[j]
                            })
                        }
                    }

                    $location.path('/driver/complete'); // path not hash
                }
            }
        }

        vm.update = function () {
            for (var i in vm.goods1) {

                if (vm.goods1[i] != true) {
                    vm.goods1[i].state = "UNCHECKED_DELIVERED";
                }
            }
            $http({
                method: 'PUT',
                url: '/api/goods',
                data: vm.goods1
            }).then(function successCallback(response) {
                console.log("date changed");

            });
            vm.routeList.state = "DELIVERED";
            vm.routeList.arrivalDate = Date.now();
            $http({
                method: 'PUT',
                url: '/api/routelists',
                data: vm.routeList
            });
            $location.path('/driver/routelist');
        };

        function checkJob() {
            countRating();
            for (var j in vm.waybills) {
                if (vm.waybills[j].state === "CHECKED") {
                    return false;
                }
            }
            return true;
        }
        function countRating() {
            var index = 0;
            vm.currentRate = 0;
            console.log(vm.driverGoods);
            for (var i = 0; i < vm.driverGoods.length; i++) {
                for (var j = 0; j < vm.driverGoods[i].length; j++) {
                    if(vm.driverGoods[i][j].deliveredNumber !=null){
                        if(vm.driverGoods[i][j].acceptedNumber!=null) {
                            vm.currentRate += vm.driverGoods[i][j].deliveredNumber / vm.driverGoods[i][j].acceptedNumber;
                            index++;
                        }
                    }
                }
            }
            vm.currentRate =  vm.currentRate/index*10;
            console.log("Rating: " + vm.currentRate);
        }
    }
})();

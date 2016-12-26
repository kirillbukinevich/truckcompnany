(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('DriverRoutelistController', DriverRoutelistController);

    DriverRoutelistController.$inject = ['Waybill', 'Checkpoint',
        'RouteList', '$http', '$location', '$scope','$timeout','$uibModal'];

    function DriverRoutelistController(Waybill, Checkpoint, RouteList, $http, $location,
                                       $scope,$timeout,$uibModal) {
        var vm = this;
        vm.routeList = {};
        vm.checkpoints = [];
        vm.checkpointNames = [];
        vm.imageWaypoints = [];
        vm.driverGoods = [];
        vm.waybills = [];
        vm.markDate = markDate;
        vm.completeTrip = completeTrip;

        $scope.render = false;
        $timeout(function () {
            $scope.render = true;
        }, 1000);

        //for google map
        vm.travelMode = 'DRIVING';
        var imageCursor = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png';
        //for driver rating
        vm.currentRate = 0;
        vm.readOnly = true;
        //
        $timeout(function () {
            Waybill.query(function (data) {
                vm.waybills = data;
                angular.forEach(vm.waybills, function (value) {
                    Checkpoint.query({id: value.routeList.id}, function (data) {
                        vm.checkpoints = data;
                        var i = 0;
                        angular.forEach(vm.checkpoints, function (value) {
                            vm.checkpointNames[i] = {location: value.name, stopover: true};
                            if (value.checkDate != null) {
                                imageCursor = 'http://inspire.ecoachmanager.com/images/32x32/accept_item.png';
                            } else {
                                imageCursor = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png';
                            }
                            vm.imageWaypoints[i] = {
                                position: vm.checkpoints[i].name,
                                image: imageCursor
                            };
                            i++;
                        });
                    });
                    RouteList.get({id: value.routeList.id}, function (result) {
                        vm.routeList = result;
                        vm.checkpointNames[vm.checkpointNames.length] = {
                            location: vm.routeList.arrivalStorage.address,
                            stopover: true
                        };
                        imageCursor = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png';
                        vm.imageWaypoints[vm.imageWaypoints.length] = {
                            position: vm.routeList.arrivalStorage.address,
                            image: imageCursor
                        };
                    });
                });

            });
        }, 1000);



        function markDate(id) {
            for (var i = 0; i < vm.checkpoints.length; i++) {
                if (vm.checkpoints[i].id == id) {
                    if (i == 0 || vm.checkpoints[i - 1].checkDate) {
                        var index = i;
                        $http({
                            method: 'GET',
                            url: '/api/checkpoint_mark_date/' + id,
                        }).then(function successCallback(response) {
                            var today = new Date();
                            for (var j in vm.checkpoints) {
                                if (vm.checkpoints[j].id == id) {
                                    vm.checkpoints[j].checkDate = today;
                                    vm.imageWaypoints[j] = {
                                        position: vm.checkpoints[j].name,
                                        image: 'http://inspire.ecoachmanager.com/images/32x32/accept_item.png'
                                    };
                                }
                            }

                        });
                    } else {
                        window.alert("Mark previous checkpoint");
                    }

                }
            }
        }
        function completeTrip() {
            if(vm.checkpoints.length==0 || vm.checkpoints[vm.checkpoints.length-1].checkDate !=null) {
                for (var j in vm.waybills) {
                    if (vm.waybills[j] != true && vm.waybills[j].state == "CHECKED") {
                        vm.waybills[j].state = "DELIVERED";
                        $http({
                            method: 'PUT',
                            url: '/api/waybills',
                            data: vm.waybills[j]
                        });

                        $location.path("/driver/complete");
                    }
                }
            }else {
                window.alert("Mark all checkpoints");
            }
        }

        vm.showModalGoodsCurrent = function (id) {
            vm.modalConfirmWaybill = $uibModal.open({
                templateUrl: 'app/roles/driver/goods/driver.goods.html',
                controller: 'DriverGoodsController',
                controllerAs: 'vm',
                scope: $scope,
                backdrop: 'static',
                size: 'md',
                resolve: {
                    entity: ['Waybill', function (Waybill) {
                        return Waybill.get({id: id});
                    }]
                }
            });
        };

        $scope.closeModalWaybillDetails = function () {
            vm.modalConfirmWaybill.close();
            $location.path('/driver/routelist');
        };



    }
})();

(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerDeliveredGoodsController', ManagerDeliveredGoodsController);

    ManagerDeliveredGoodsController.$inject = ['Waybill', '$uibModal', '$scope'];

    function ManagerDeliveredGoodsController(Waybill, $uibModal, $scope) {
        var vm = this;
        vm.unconfirmed = [];

        $scope.$on('waybillState', function (event, data) {
            for (var i = 0; i < vm.unconfirmed.length; i++) {
                if (vm.unconfirmed[i].id == data.id) {
                    vm.unconfirmed.splice(i, 1);
                }
            }
        });

        vm.waybills = Waybill.query(function () {
            console.log(vm.waybills);
            angular.forEach(vm.waybills, function (waybill) {
                for (var j = 0; j < waybill.goods.length; j++) {
                    if (waybill.goods[j].state == 'UNCHECKED_DELIVERED') {
                        vm.unconfirmed.push(waybill);
                        break;
                    }
                }
            });
            console.log(vm.unconfirmed);
        });

        vm.showModalGoodsDetails = function (id) {
            vm.modalConfirmGoods = $uibModal.open({
                templateUrl: 'app/roles/manager/deliveredGoods/manager.goods-details.html',
                controller: 'ManagerGoodsDetailsController',
                controllerAs: 'vm',
                scope: $scope,
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: ['Waybill', function (Waybill) {
                        return Waybill.get({id: id});
                    }]
                }
            });
        };

    }
})();

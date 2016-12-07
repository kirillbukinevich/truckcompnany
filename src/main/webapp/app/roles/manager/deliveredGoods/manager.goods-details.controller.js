/**
 * Created by Dmitry on 17.11.2016.
 */
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('ManagerGoodsDetailsController', ManagerGoodsDetailsController);

    ManagerGoodsDetailsController.$inject = ['entity', '$uibModalInstance', '$http', '$scope'];

    function ManagerGoodsDetailsController(entity, $uibModalInstance, $http, $scope) {
        var vm = this;
        vm.clear = clear;
        vm.waybill = entity;
        vm.changeGoodsState = changeGoodsState;

        function changeGoodsState() {
            angular.forEach(vm.waybill.goods, function (value) {
                value.state = 'DELIVERED';
            });
            $http.put('/api/goods', vm.waybill.goods);
            //Waybill.update(vm.waybill);
            console.log(vm.waybill.goods);
            $scope.$emit('waybillState', vm.waybill);
        }

        function clear() {
            $uibModalInstance.close();
        }
    }
})();

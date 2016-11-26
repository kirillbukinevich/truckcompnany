
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerInitialController', CompanyownerInitialController);

    CompanyownerInitialController.$inject = ['Principal', '$http'];

    function CompanyownerInitialController(Principal, $http) {
        var vm = this;

        vm.loadData = loadData;

        vm.loadData();

        function loadData() {
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/consumption'
            }).then(function successCallback(response) {
                console.log("Data load successfully")
                vm.consumptionChartData = response.data;
                vm.commonChartConfig.series.push({
                    id: 1,
                    data: vm.consumptionChartData
                });
                console.log(vm.consumptionChartData);
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }


        vm.commonChartConfig = {
            options: {
                chart: {
                    type: 'areaspline'
                },
                xAxis: {
                    type: 'datetime',
                    labels: {
                        format: '{value:%e of %b}',
                    }
                }
            },
            title :{
                text: "Month statistics"
            },
            series:[{
                id: 1,
                name: 'Consumption',
                data: []
            }]

        };

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                console.log(account);
            });
        };


    }
})();

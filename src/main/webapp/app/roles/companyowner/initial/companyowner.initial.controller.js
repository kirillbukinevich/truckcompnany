
(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerInitialController', CompanyownerInitialController);

    CompanyownerInitialController.$inject = ['Principal', '$http','$timeout', '$scope'];

    function CompanyownerInitialController(Principal, $http, $timeout, $scope) {
        var vm = this;

        vm.loadData = loadData;
        vm.loadConsumptionData = loadConsumptionData;
        vm.downloadConsumptionReport = downloadConsumptionReport;
        vm.loadLossDate = loadLossData;
        vm.downloadLossReport = downloadLossReport;

        vm.datePicker = {
            startDate: null,
            endDate: null,
        };

        vm.datePickerOpts = {
            locale : {
                format: "MMMM D, YYYY",
                customRangeLabel: 'Custom range'
            },
            ranges: {
                'Last week' : [moment().subtract(1, "weeks").startOf("week"),
                    moment().subtract(1, "weeks").endOf("week")],
                'Last month' : [moment().subtract(1, "months").startOf("month"),
                    moment().subtract(1, "months").endOf("month")]
            },
            eventHandlers : {
                'apply.daterangepicker' : function (ev, picker) {
                    $('div[name="datepicker"] span').html(vm.datePicker.startDate.format('MMMM D, YYYY') + ' - '
                        + vm.datePicker.endDate.format('MMMM D, YYYY'));
                    vm.loadConsumptionData();
                }
            },
            maxDate: moment().endOf("day"),
            opens: 'left'
        };

        vm.lossDatePicker = {

        };

        vm.loadData();

        // graph redrawing

        $scope.render = false;
        $timeout(function () {
            $scope.render = true;
        }, 500);

        function loadData() {
            vm.loadConsumptionData();
            vm.loadLossDate();
        }

        function loadConsumptionData(){
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/consumption',
                params: {
                    startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.toISOString() : null,
                    endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.toISOString() : null
                }
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

        function loadLossData(){
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/loss',
                params: {
                    startDate: !!vm.lossDatePicker.startDate? vm.lossDatePicker.startDate.toISOString() : null,
                    endDate: !!vm.lossDatePicker.endDate? vm.lossDatePicker.endDate.toISOString() : null
                }
            }).then(function successCallback(response) {
                vm.lossChartData = response.data;
                vm.lossChartConfig.series.push({
                    id: 1,
                    data: vm.lossChartData
                });
                console.log('Data load: ' + vm.lossChartData);
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }

        function downloadConsumptionReport(){
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/xls/consumption',
                params : {
                    startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.toISOString() : null,
                    endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.toISOString() : null
                },
                responseType: 'arraybuffer'
            }).
            success(function(data) {
                var url = URL.createObjectURL(new Blob([data]));
                var a = document.createElement('a');
                a.href = url;
                a.download = 'consumptionReport.xls';
                a.target = '_blank';
                a.click();
            }).
            error(function(data, status, headers, config) {
                // handle error
            });
        }

        function downloadLossReport() {
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/xls/loss',
                params : {
                    startDate: !!vm.lossDatePicker.startDate? vm.lossDatePicker.startDate.toISOString() : null,
                    endDate: !!vm.lossDatePicker.endDate? vm.lossDatePicker.endDate.toISOString() : null
                },
                responseType: 'arraybuffer'
            }).
            success(function(data) {
                var url = URL.createObjectURL(new Blob([data]));
                var a = document.createElement('a');
                a.href = url;
                a.download = 'loss-report.xls';
                a.target = '_blank';
                a.click();
            }).
            error(function(data, status, headers, config) {
                // handle error
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

        vm.lossChartConfig = {
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
                text: "Loss"
            },
            series:[{
                id: 1,
               // name: 'Consumption',
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

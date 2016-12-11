
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
        vm.loadLossData = loadLossData;
        vm.downloadLossReport = downloadLossReport;
        vm.downloadLossReportWithRP = downloadLossReportWithRP;
        vm.loadIncomeData = loadIncomeData;
        vm.loadProfitData = loadProfitData;
        vm.downloadCommonReport = downloadCommonReport;
        vm.loadTopBestDriversData = loadTopBestDriversData;
        vm.downloadTopBestDriversReport = downloadTopBestDriversReport;

        vm.updateDatePickersValue = function () {
            $('span[name="datepicker"] span').html(vm.datePicker.startDate.format('MMMM D, YYYY') + ' - '
                + vm.datePicker.endDate.format('MMMM D, YYYY'));
            $('span[name="lossDatepicker"] span').html(vm.lossDatePicker.startDate.format('MMMM D, YYYY') + ' - '
                + vm.lossDatePicker.endDate.format('MMMM D, YYYY'));
        };

        vm.topBestDriversAmount = 5;
        vm.profitChartData = [];
        vm.incomeChartData = [];
        vm.consumptionChartData = [];

        vm.datePicker = {
            startDate: moment().subtract(1, "months").startOf('month'),
            endDate: moment().subtract(1, 'months').endOf('month')
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
                    $('span[name="datepicker"] span').html(vm.datePicker.startDate.format('MMMM D, YYYY') + ' - '
                        + vm.datePicker.endDate.format('MMMM D, YYYY'));
                    vm.loadConsumptionData();
                    vm.loadIncomeData();
                    vm.loadProfitData();
                    vm.loadLossData(vm.datePicker.startDate, vm.datePicker.endDate, setLossDataToCommonChart)
                }
            },
            maxDate: moment().endOf("day"),
            opens: 'left'
        };

        vm.lossDatePicker = {
            startDate: moment().subtract(1, "months").startOf('month'),
            endDate: moment().subtract(1, 'months').endOf('month')
        };

        vm.lossDatePickerOpts = {
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
                    $('span[name="lossDatepicker"] span').html(vm.lossDatePicker.startDate.format('MMMM D, YYYY') + ' - '
                        + vm.lossDatePicker.endDate.format('MMMM D, YYYY'));
                    vm.loadLossData(vm.lossDatePicker.startDate, vm.lossDatePicker.endDate, setLossDataToLossChart);
                }
            },
            maxDate: moment().endOf("day"),
            opens: 'left'
        };



        $scope.render = false;
        $timeout(function () {
            $scope.render = true;
        }, 500);

        function setLossDataToCommonChart(data) {
            vm.commonChartConfig.series[3].data = angular.copy(data);
            vm.commonChartConfig.series[3].data.forEach(function(item, i, arr){
                item[1] = - item[1];
            });
        }

        function setLossDataToLossChart(data) {
            vm.lossChartData = angular.copy(data);
            vm.lossChartConfig.series[0].data = vm.lossChartData;
        }

        function loadData() {
            vm.loadConsumptionData();
            vm.loadIncomeData();
            vm.loadProfitData();
            vm.loadLossData(vm.datePicker.startDate, vm.datePicker.endDate, setLossDataToCommonChart);

            vm.loadTopBestDriversData();

            vm.loadLossData(vm.lossDatePicker.startDate, vm.lossDatePicker.endDate, setLossDataToLossChart);
        }

        function loadConsumptionData(){
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/consumption',
                params: {
                    startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.format('YYYY-MM-DD') : null,
                    endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.format('YYYY-MM-DD') : null
                }
            }).then(function successCallback(response) {
                vm.commonChartConfig.series[0].data = response.data;
                console.log("Consumption data: " + response.data);
            }, function errorCallback(response) {
                console.log("Consumption data wasn\'t load: " + response.status);
            });
        }

        function loadLossData(startDate, endDate, callback){
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/loss',
                params: {
                    startDate: !!startDate? startDate.format('YYYY-MM-DD') : null,
                    endDate: !!endDate? endDate.format('YYYY-MM-DD') : null
                }
            }).then(function successCallback(response) {
                callback(response.data);
                console.log('Loss data load: ' + response.data);
            }, function errorCallback(response) {
                console.log('Loss data wasn\'t load: ' + response.statusText);
            });
        }

        function loadIncomeData() {
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/income',
                params: {
                    startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.format('YYYY-MM-DD') : null,
                    endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.format('YYYY-MM-DD') : null
                }
            }).then(function successCallback(response) {
                vm.commonChartConfig.series[1].data = response.data;
                console.log('Income data load: ' + response.data);
            }, function errorCallback(response) {
                console.log('Income data wasn\'t load: ' + response.status);
            });
        }

        function loadProfitData() {
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/profit',
                params: {
                    startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.format('YYYY-MM-DD')  : null,
                    endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.format('YYYY-MM-DD')  : null
                }
            }).then(function successCallback(response) {
                vm.profitChartData = response.data;
                vm.commonChartConfig.series[2].data = response.data;
                console.log('Profit data load: ' + vm.profitChartData);
            }, function errorCallback(response) {
                console.log('Profit data wasn\'t load: ' + response.statusText);
            });
        }

        function loadTopBestDriversData() {
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/topbestdrivers',
                params: {
                    amount: vm.topBestDriversAmount
                }
            }).then(function successCallback(response) {
                vm.topBestDriversChartConfig.series = [{data: response.data}];
                console.log('Top best drivers data load;');
            }, function errorCallback(response) {
                console.log('Profit data wasn\'t load: ' + response.statusText);
            });
        }

        function downloadCommonReport(){
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/xls/common',
                params : {
                    startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.format('YYYY-MM-DD') : null,
                    endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.format('YYYY-MM-DD') : null
                },
                responseType: 'arraybuffer'
            }).
            success(function(data) {
                saveReport(data, 'common-report.xls');
            }).
            error(function(data, status, headers, config) {
                console.log('Common report wasn\'t load: ' + status);
            });
        }

        function downloadLossReport() {
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/xls/loss',
                params : {
                    startDate: !!vm.lossDatePicker.startDate? vm.lossDatePicker.startDate.format('YYYY-MM-DD') : null,
                    endDate: !!vm.lossDatePicker.endDate? vm.lossDatePicker.endDate.format('YYYY-MM-DD') : null
                },
                responseType: 'arraybuffer'
            }).
            success(function(data) {
                saveReport(data, 'loss-report.xls');
            }).
            error(function(data, status, headers, config) {
                console.log('Loss report wasn\'t load: ' + status);
            });
        }

        function downloadLossReportWithRP(){
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/xls/losswithrp',
                params : {
                    startDate: !!vm.lossDatePicker.startDate? vm.lossDatePicker.startDate.format('YYYY-MM-DD') : null,
                    endDate: !!vm.lossDatePicker.endDate? vm.lossDatePicker.endDate.format('YYYY-MM-DD') : null
                },
                responseType: 'arraybuffer'
            }).
            success(function(data) {
                saveReport(data, 'loss-with-responsible-persons.xls')
            }).
            error(function(data, status, headers, config) {
                console.log('LossWithRP report wasn\'t load: ' + status);
            });
        }

        function downloadTopBestDriversReport(){
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/xls/topbestdrivers',
                params : {
                    amount: vm.topBestDriversAmount
                },
                responseType: 'arraybuffer'
            }).
            success(function(data) {
                saveReport(data, 'top-best-drivers-report.xls');
            }).
            error(function(data, status, headers, config) {
                console.log('Top best drivers report wasn\'t load: ' + status);
            });
        }

        function saveReport(data, reportName){
            var url = URL.createObjectURL(new Blob([data]));
            var a = document.createElement('a');
            a.href = url;
            a.download = reportName;
            a.target = '_blank';
            a.click();
        }



        vm.commonChartConfig = {
            options: {
                xAxis: {
                    type: 'datetime',
                    labels: {
                        format: '{value:%e of %b}'
                    }
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>${point.y:.2f}</b>',
                    valueDecimals: 2
                }
            },
            title :{
                text: 'Common'
            },

            series: [
                {
                    id: 1,
                    name: 'Consumption',
                    type: 'column',
                    data: []
                },
                {
                    name: 'Income',
                    type: 'column',
                    id: 2,
                    data : []
                },
                {
                    id: 3,
                    name: 'Profit',
                    data: []
                },
                {
                    id: 4,
                    name: 'Loss',
                    type: 'column',
                    data: []
                }
            ]
        };

        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });

        vm.pie = {
            chart: {
                type: 'pie',
                margin: [15, 15, 15, 15],
                marginRight: 200,
                marginTop: 50
            },
            tooltip: {
                pointFormat: '{series.data.name} <b>${point.y:.2f}</b>',
                valueDecimals: 2
            },
            plotOptions: {
                pie: {
                    size: '100%',
                    slicedOffset: 0,
                    innerSize: '50%',
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            legend: {
                enabled: true,
                verticalAlign: 'middle',
                layout: 'vertical',
                align: 'right',
                labelFormat: '{name} ${y:.2f}'
            }
        };
        vm.topBestDriversChartConfig = {
            options : vm.pie,
            title: {
                text: 'Top 5 best drivers'
            },
            series: [
                {
                    data: [{
                        name: 'test1',
                        y: 1
                    }, {
                        name: 'test2',
                        y: 2
                    }]
                }
            ]
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
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>${point.y:.2f}</b>',
                    valueDecimals: 2
                }
            },
            title :{
                text: "Loss"
            },
            series:[{
                id: 1,
                name: 'Loss',
                data: []
            }]
        };

        vm.loadData();
        vm.updateDatePickersValue();

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                console.log(account);
            });
        };



    }
})();

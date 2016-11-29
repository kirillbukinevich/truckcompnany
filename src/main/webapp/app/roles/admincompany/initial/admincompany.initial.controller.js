/**
 * Created by Vladimir on 30.10.2016.
 */

(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyInitialController', AdmincompanyInitialController);

    AdmincompanyInitialController.$inject = ['Principal', '$http', '$timeout', '$scope'];

    function AdmincompanyInitialController(Principal, $http, $timeout, $scope) {
        var vm = this;

        vm.load = load;
        vm.load();

        vm.getColorStyle = function(color){
            return {
                background: color,
            }
        }





        var colors = Highcharts.getOptions().colors;





        vm.pie = {
            chart: {
                type: 'pie',
                margin: [15, 15, 15, 15],
                height: 180,
            },
            tooltip: {
                pointFormat: '{series.data.name} <b>{point.percentage:.1f}%</b>'
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
                enabled: false,
                verticalAlign: 'middle',
                layout: 'vertical',
                align: 'right',
                labelFormat: '{name} {y}%'
            },
        };


        vm.chartConfigUserRole = {
            options: vm.pie,
            title:" ",
            series: [{
                data: [],
            }],
        };

        vm.chartConfigTruckStatus = {
            options: vm.pie,
            title:" ",
            series: [{
                data: [],
            }],
        }

        vm.stackedBar = {
            chart: {
                type: 'bar'
            },
            title: null,
            xAxis: {
                categories: ['Africa', 'America', 'Asia', 'Europe', 'Oceania'],
                /*title: {
                 text: null
                 }*/
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Trucks (unique)',
                    align: 'high'
                },
                labels: {
                    overflow: 'justify'
                }
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: true
                    }
                }
            },
            legend: {
                enabled: false,
            }
        }

        vm.chartConfigTruckModel = {
            options: vm.stackedBar,
            series: [{
                data: [107, 31, 635, 203, 2]
            }]
        }


        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                console.log(account);
            });
        }

        function load() {
            $http({
                method: 'GET',
                url: '/api/admin/statistic'
            }).then(function successCallback(response) {
                vm.statistic = response.data;

                console.log(vm.statistic);

                vm.usersRole =getDataForPie(vm.statistic.statisticEmployeeRole, vm.statistic.totalEmployees);
                vm.chartConfigUserRole.series = [{data: vm.usersRole}];

                vm.truckStatus = getDataForPie(vm.statistic.statisticTruckStatus, vm.statistic.totalTrucks);
                vm.chartConfigTruckStatus.series = [{data:  vm.truckStatus}]



                vm.chartConfigTruckModel.series = [{data: getValue(vm.statistic.statisticTruckModel)}];
                vm.chartConfigTruckModel.options.xAxis.categories = getKyes(vm.statistic.statisticTruckModel)
                console.log(getKyes(vm.statistic.statisticTruckModel));

                var chartTruckModel = vm.chartConfigTruckModel.getHighcharts();


                console.log(vm.chartConfigTruckModel.getHighcharts())



            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }



        function getDataForPie(userStatistic, totalUser) {
            var i = 0;
            return Object.keys(userStatistic).map(key => ({
                    name: key,
                    color: colors[i++],
                    y: userStatistic[key]*100/totalUser,
                    count: userStatistic[key]
                }));
        }

        function getKyes(object){
            return Object.keys(object).map(key => key);
        }
        function getValue(object){
            return Object.keys(object).map(key => object[key])
        }

    }
})();

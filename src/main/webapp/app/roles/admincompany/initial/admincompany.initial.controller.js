/**
 * Created by Vladimir on 30.10.2016.
 */

(function () {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('AdmincompanyInitialController', AdmincompanyInitialController);

    AdmincompanyInitialController.$inject = ['Principal', '$http'];

    function AdmincompanyInitialController(Principal, $http) {
        var vm = this;

        vm.load = load;
        vm.load();

        function load() {
            $http({
                method: 'GET',
                url: '/api/admin/statistic'
            }).then(function successCallback(response) {
                console.log("Success")
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        }

        var colors = Highcharts.getOptions().colors;
        vm.categories = ['MSIE', 'Firefox', 'Chrome', 'Safari', 'Opera'];
        vm.data = [{
            y: 56.33,
            color: colors[0],
            name: "Dispatcher"
        }, {
            y: 10.38,
            color: colors[1],
            name: "Driver"
        }, {
            y: 24.03,
            color: colors[2],
            name: "Owner"
        }, {
            y: 4.77,
            color: colors[3],
            name: "Driver"
        }, {
            y: 0.91,
            color: colors[4],
            name: "Manager",
            count: 5,

        }, {
            y: 0.2,
            color: colors[5],

        }];


        vm.pie ={
            chart: {
                type: 'pie',
                margin: [15,15,15,15],
                height: 180,

            },
            title: 'Employee',
            tooltip: {
                pointFormat: '{series.data.name} <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    size: '100%',
                    slicedOffset: 0,
                    innerSize:'50%',
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




        vm.chartConfig = {
            options: vm.pie,
            series: [{
                data: vm.data,
            }],

            func: function (chart) {
                //setup some logic for the chart
            }
        };

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

        vm.chartConfigStackedBar = {
            options: vm.stackedBar,
            series: [{
                data: [107, 31, 635, 203, 2]
            }]
        }




        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                /*var role = (vm.account.authorities != undefined) ? vm.account.authorities[0] : '';
                 vm.role = getNameRoleForUser(role);*/
                console.log(account);
            });
        }


    }
})();

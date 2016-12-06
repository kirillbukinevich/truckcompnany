(function() {
    'use strict';

    angular
        .module('truckCompanyApp')
        .controller('CompanyownerRouteListsController', CompanyownerRouteListsController);


    CompanyownerRouteListsController.$inject = ['Waybill', 'pagingParams', '$state', '$http', '$stateParams'];

    function CompanyownerRouteListsController (Waybill, pagingParams, $state, $http, $stateParams) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.updateDatePicker = updateDatePicker;
        vm.transition = transition;
        vm.changeItemsPerPage = changeItemsPerPage;
        vm.downloadReport = downloadReport;

        vm.itemsPerPage = pagingParams.size;
        vm.availableItemsPerPage = [5, 10, 15, 20];
        vm.page = 1;

        vm.datePicker = {
            startDate: $stateParams.startDate,
            endDate: $stateParams.endDate
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
                    vm.startDate = vm.datePicker.startDate;
                    vm.endDate = vm.datePicker.endDate;
                    vm.loadPage();
                }
            },
            maxDate: moment().endOf("day"),
            opens: 'left'
        };

        vm.loadPage();
        vm.updateDatePicker();


        function loadPage() {
            Waybill.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.toISOString() : null,
                endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.toISOString() : null
            }, onSuccess, onError);
        }

        function updateDatePicker(){
            if (!!$stateParams.startDate && !!$stateParams.endDate) {
                $('div[name="datepicker"] span').html(vm.datePicker.startDate.format('MMMM D, YYYY') + ' - '
                    + vm.datePicker.endDate.format('MMMM D, YYYY'));
            }
        }

        function onSuccess(data, headers){
            vm.error = false;
            vm.waybills = data;
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
        }

        function onError(error){
            vm.error = true;
            vm.messageError = 'Problems with connection.'
        }


        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                size:  vm.itemsPerPage,
                startDate: vm.datePicker.startDate,
                endDate: vm.datePicker.endDate
            });
        }

        function changeItemsPerPage(){
            $state.transitionTo($state.$current, {
                page: 1,
                size:  vm.itemsPerPage,
            });
        }

        function downloadReport(){
            $http({
                method: 'GET',
                url: '/api/companyowner/statistic/xls/routelists',
                params : {
                    startDate: !!vm.datePicker.startDate? vm.datePicker.startDate.format('YYYY-MM-DD') : null,
                    endDate: !!vm.datePicker.endDate? vm.datePicker.endDate.format('YYYY-MM-DD') : null
                },
                responseType: 'arraybuffer'
            }).
            success(function(data) {
                var url = URL.createObjectURL(new Blob([data]));
                var a = document.createElement('a');
                a.href = url;
                a.download = 'routeListsReport.xls';
                a.target = '_blank';
                a.click();
            }).
            error(function(data, status, headers, config) {
                // handle error
            });
        }

    }


})();

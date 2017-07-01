(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('ListSuratJalanCtrl', ListSuratJalanCtrl);

    function ListSuratJalanCtrl($scope, $window, $uibModal, $filter, SuratJalanService, KotaService, toastr) {
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.options = {format: 'DD/MM/YYYY', showClear: false};
        $scope.vm = {tglAwal: new Date(), tglAkhir: new Date(), kota: null, cari: ""};
        KotaService.cariSemua().success(function (data) {
            $scope.listKota = data;
        });
        $scope.reloadData = function () {
            console.log('vm', $scope.vm);
            console.log('tglAwal', $filter('date')(new Date($scope.vm.tglAwal), 'yyyy-MM-dd'));
            $scope.dataPage = SuratJalanService.queryComposite($filter('date')(new Date($scope.vm.tglAwal), 'yyyy-MM-dd'), $filter('date')(new Date($scope.vm.tglAkhir), 'yyyy-MM-dd'), ($scope.vm.kota == null || $scope.vm.kota.id == undefined || $scope.vm.kota.id == null ? 0 : $scope.vm.kota.id), ($scope.vm.cari == '' ? 'null' : $scope.vm.cari), $scope.paging.currentPage - 1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
        };
        $scope.reloadData();

        $scope.edit = function (x) {
            $window.open('#/transaksi/suratjalan/' + x.id, '_blank');
        };

        $scope.baru = function (x) {
            window.location.href = '#/transaksi/suratjalan';
        };

        $scope.hapus = function (x) {
            console.log('hapus', x);
            SuratJalanService.hapus(x.id).success(function (d) {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            });
        };



    }
}
)();
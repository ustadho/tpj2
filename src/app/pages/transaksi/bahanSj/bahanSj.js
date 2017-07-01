(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('BahanSjCtrl', BahanSjCtrl)
            .controller('BahanSjModalController', BahanSjModalController);

    /** @ngInject */
    function BahanSjCtrl($scope, $uibModal, $log, toastr, BahanSjService) {
        $scope.search = "";
        $scope.oldSearch = "";
        $scope.modalTitle = "Tambah Bahan Surat Jalan";
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.vm = {};
        $scope.param = {tglAwal: new Date(), tglAkhir: new Date(), outstand: false, cari: ""};
        $scope.ori = {};

        $scope.reloadData = function () {
            $scope.dataPage = BahanSjService.queryComposite($filter('date')($scope.param.tglAwal, 'yyyy-MM-dd'), $filter('date')($scope.param.tglAkhir, 'yyyy-MM-dd'), $scope.param.outstand, ($scope.param.cari == '' ? 'null' : $scope.param.cari), $scope.paging.currentPage - 1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
        };

        $scope.clear = function () {
            $scope.modalTitle = "";
            $scope.vm = {};
            $scope.ori = {};
            $scope.reloadData();
        };

        $scope.clear();

        $scope.baru = function () {
            $scope.modalTitle = "Tambah Bahan Surat Jalan";
            console.log('Baru');
            $scope.vm = {};
            $scope.ori = {};
        };

        $scope.edit = function (x) {
            $scope.ori = angular.copy(x);
            $scope.modalTitle = "Edit Bahan Surat Jalan";
            console.log('edit', x);
            BahanSjService.cariSatu("kode", x.id).success(function (data) {
                $scope.vm = angular.copy(data);
                $scope.ori = angular.copy($scope.vm);
            });
        };

        $scope.hapus = function (x) {
            BahanSjService.hapus(x).success(function () {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            });
        };

        $scope.simpan = function () {
            BahanSjService.simpan($scope.vm, $scope.ori).success(function (d) {
                toastr.success('Simpan data sukses!');
                $scope.clear();
            });
        };
    }

    function BahanSjModalController($uibModalInstance, toastr, $scope, BahanSjService, data) {
        $scope.ori = angular.copy(data);
        $scope.modalTitle = "Edit Bahan Surat Jalan";
        console.log('edit', data);
        $scope.vm = angular.copy(data);


        $scope.simpan = function () {
            BahanSjService.simpan($scope.vm, $scope.ori).success(function (d) {
                $uibModalInstance.close($scope.vm);
                toastr.success('Simpan data sukses!');
            })
        }
    }
})();


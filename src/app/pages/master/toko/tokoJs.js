(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('TokoCtrl', TokoCtrl)
            .controller('TokoModalController', TokoModalController);

    /** @ngInject */
    function TokoCtrl($scope, $uibModal, $log, toastr, TokoService, KotaService) {
        $scope.search = "";
        $scope.oldSearch = "";
        $scope.modalTitle = "Tambah Toko";
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.vm = {};
        $scope.param = {kota: {}, cari: ''};
        $scope.ori = {};        

        $scope.reloadData = function () {
            $scope.dataPage = TokoService.queryComposite(($scope.param.kota == null || $scope.param.kota.id == undefined || $scope.param.kota.id == null ? 0 : $scope.param.kota.id), ($scope.param.cari == '' ? 'null' : $scope.param.cari), $scope.paging.currentPage - 1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
        };

        KotaService.cariSemua().success(function (data) {
            $scope.listKota = data;
        });

        $scope.clear = function () {
            $scope.modalTitle = "";
            $scope.vm = {};
            $scope.ori = {};
            $scope.reloadData();
        };
        $scope.clear();

        $scope.baru = function () {
            $scope.vm = {};
            $scope.ori = {};
            $scope.modalTitle = "Tambah Toko";
            console.log('Baru');
        };

        $scope.merkBaru = function () {
            console.log('Merk Baru');
            if ($scope.vm.listMerk == null || $scope.vm.listMerk == undefined) {
                $scope.vm.listMerk = [];
            }
            $scope.vm.listMerk.push(
                    {
                        nama: null
                    });
        };

        $scope.edit = function (x) {
            $scope.ori = angular.copy(x);
            $scope.modalTitle = "Edit Toko";
            console.log('edit', x);
            TokoService.cariSatu("kode", x.id).success(function (data) {
                $scope.vm = angular.copy(data);
                $scope.ori = angular.copy($scope.vm);
            });
        };

        $scope.hapusMerk = function (idx) {
            $scope.vm.listMerk.splice(idx, 1);
        };
        $scope.hapus = function (x) {
            TokoService.hapus(x).success(function () {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            });
        };

        $scope.simpan = function () {
            TokoService.simpan($scope.vm, $scope.ori).success(function (d) {
                toastr.success('Simpan data sukses!');
                $scope.clear();
            });
        };
    }

    function TokoModalController($uibModalInstance, toastr, $scope, TokoService, data) {
        $scope.ori = angular.copy(data);
        $scope.modalTitle = "Edit Toko";
        console.log('edit', data);
        $scope.vm = angular.copy(data);


        $scope.simpan = function () {
            TokoService.simpan($scope.vm, $scope.ori).success(function (d) {
                $uibModalInstance.close($scope.vm);
                toastr.success('Simpan data sukses!');
            })
        }
    }
})();


(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('KapalBerangkatCtrl', KapalBerangkatCtrl)
            .controller('KapalBerangkatModalController', KapalBerangkatModalController);

    /** @ngInject */
    function KapalBerangkatCtrl($scope, $uibModal, $log, $filter, toastr, KapalBerangkatService, KotaService, KapalService) {
        $scope.search = "";
        $scope.oldSearch = "";
        $scope.modalTitle = "Tambah KapalBerangkat";
        $scope.opened = false;
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.opened = false;
        $scope.openedAwal = false;
        $scope.openedAkhir = false;        
        $scope.dateOptions = {format: 'DD/MM/YYYY', showClear: false};        
        $scope.vm = {};
        $scope.ori = {};
        $scope.param = {tglAwal: new Date(), tglAkhir: new Date(), kota: null, kapal: null, cari: ""};

        $scope.reloadData = function () {
            $scope.dataPage = KapalBerangkatService.queryComposite($filter('date')(new Date($scope.param.tglAwal), 'yyyy-MM-dd'), $filter('date')(new Date($scope.param.tglAkhir), 'yyyy-MM-dd'), ($scope.param.kota == null || $scope.param.kota.id == undefined || $scope.param.kota.id == null ? 0 : $scope.param.kota.id), ($scope.param.kapal == null || $scope.param.kapal.id == undefined || $scope.param.kapal.id == null ? 0 : $scope.param.kapal.id), ($scope.param.cari == '' ? 'null' : $scope.param.cari), $scope.paging.currentPage - 1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
        };

        KotaService.cariSemua().success(function (data) {
            $scope.listKota = data;
        });

        KapalService.cariSemua().success(function (data) {
            $scope.listKapal = data;
        });

        $scope.clear = function () {
            $scope.modalTitle = "";
            $scope.vm = {};
            $scope.ori = {};
            $scope.reloadData();
        };

        $scope.clear();

        $scope.baru = function () {
            $scope.modalTitle = "Tambah KapalBerangkat";
            $scope.vm = {};
            $scope.ori = {};
            console.log('Baru');
        };

        $scope.edit = function (x) {
            $scope.ori = angular.copy(x);
            $scope.modalTitle = "Edit KapalBerangkat";
            console.log('edit', x);
            KapalBerangkatService.cariSatu("kode", x.id).success(function (data) {
                if (data.tglBerangkat != null && data.tglBerangkat != undefined) {
                    data.tglBerangkat = new Date(data.tglBerangkat);
                }
                $scope.vm = angular.copy(data);
            });
        };

        $scope.hapus = function (x) {
            KapalBerangkatService.hapus(x).success(function () {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            });
        };

        $scope.simpan = function () {
            KapalBerangkatService.simpan($scope.vm, $scope.ori).success(function (d) {
                toastr.success('Simpan data sukses!');
                $scope.clear();
            });
        };
    }

    function KapalBerangkatModalController($uibModalInstance, toastr, $scope, KapalBerangkatService, data) {
        $scope.ori = angular.copy(data);
        $scope.modalTitle = "Edit KapalBerangkat";
        console.log('edit', data);
        $scope.vm = angular.copy(data);


        $scope.simpan = function () {
            KapalBerangkatService.simpan($scope.vm, $scope.ori).success(function (d) {
                $uibModalInstance.close($scope.vm);
                toastr.success('Simpan data sukses!');
            })
        }
    }
})();


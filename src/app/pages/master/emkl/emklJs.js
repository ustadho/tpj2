(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('EmklCtrl', EmklCtrl)
            .controller('EmklModalController', EmklModalController);

    /** @ngInject */
    function EmklCtrl($scope, $uibModal, $log, toastr, EmklService, KotaService) {
        $scope.search = "";
        $scope.oldSearch = "";
        $scope.modalTitle = "Tambah Emkl";
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.vm = {};
        $scope.param = {kota: {}, cari: ''};
        $scope.ori = {};

        $scope.reloadData = function () {
            $scope.dataPage = EmklService.queryComposite(($scope.param.kota == null || $scope.param.kota.id == undefined || $scope.param.kota.id == null ? 0 : $scope.param.kota.id), ($scope.param.cari == '' ? 'null' : $scope.param.cari), $scope.paging.currentPage - 1, function () {
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

//        $scope.baru = function () {
//            $scope.modalTitle = "Tambah Emkl";
//            console.log('Baru');
//            $scope.vm = {};
//            $scope.ori = {};
//        };

        $scope.baru = function () {
            console.log('Baru');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/master/emkl/emkl-modal.html',
                controller: 'EmklModalController',
                size: 'md',
                resolve: {
                    data: function () {
                        return {};
                    }
                }
            });
            modalInstance.result.then(function (selectedItem) {
                $scope.reloadData();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.edit = function (x) {
            $scope.ori = angular.copy(x);
            $scope.modalTitle = "Edit Emkl";
            console.log('edit', x);
            EmklService.cariSatu("kode", x.id).success(function (data) {
                $scope.vm = angular.copy(data);
                $scope.ori = angular.copy($scope.vm);
            });
        };

        $scope.hapus = function (x) {
            EmklService.hapus(x).success(function () {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            });
        };

        $scope.simpan = function () {
            EmklService.simpan($scope.vm, $scope.ori).success(function (d) {
                toastr.success('Simpan data sukses!');
                $scope.clear();
            });
        };
    }

    function EmklModalController($uibModalInstance, toastr, $scope, EmklService, data) {
        $scope.ori = angular.copy(data);
        $scope.modalTitle = "Edit Emkl";
        console.log('edit', data);
        $scope.vm = angular.copy(data);


        $scope.simpan = function () {
            EmklService.simpan($scope.vm, $scope.ori).success(function (d) {
                $uibModalInstance.close($scope.vm);
                toastr.success('Simpan data sukses!');
            })
        }
    }
})();


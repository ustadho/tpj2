(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('ItemController', ItemController)
            .controller('ItemModalController', ItemModalController)
            .controller('ItemUkuranModalController', ItemUkuranModalController);

    /** @ngInject */
    function ItemController($scope, $uibModal, $log, toastr, ItemService, JenisItemService) {
        $scope.search = "";
        $scope.oldSearch = "";
        $scope.modalTitle = "Tambah Item";
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.vm = {};
        $scope.ori = {};

        $scope.reloadData = function () {
            $scope.dataPage = ItemService.query($scope.search, $scope.paging.currentPage - 1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
        };

        JenisItemService.cariSemua().success(function (data) {
            $scope.listJenisItem = data;
        });

        $scope.reloadData();

        $scope.clear = function () {
            $scope.modalTitle = "Tambah Item";
            $scope.vm = {aktif: true};
            $scope.ori = {};
            $scope.reloadData();
        };

        $scope.baru = function () {
            console.log('Baru');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/master/item/item-modal.html',
                controller: 'ItemModalController',
                size: 'md',
                resolve: {
                    data: {aktif: true}
                }
            });
            modalInstance.result.then(function (selectedItem) {
                $scope.reloadData();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.edit = function (x, page, size) {
            console.log('Open modal');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: page,
                controller: 'ItemModalController',
                size: size,
                resolve: {
                    data: function () {
                        return x;
                    }
                }
            });
            modalInstance.result.then(function (selectedItem) {
                $scope.reloadData();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.hapus = function (x) {
            ItemService.hapus(x).success(function () {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            });
        };

        $scope.simpan = function () {
            ItemService.simpan($scope.vm, $scope.ori).success(function (d) {
                toastr.success('Simpan data sukses!');
                $scope.clear();
            });
        };

        $scope.cariUkuran = function (x) {
            console.log('cariUkuran');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/master/item/itemUkuranModal.html',
                controller: 'ItemUkuranModalController',
                size: 'md',
                resolve: {
                    data: function () {
                        return x;
                    }
                }
            });
            modalInstance.result.then(function (selectedItem) {

            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
    }

    function ItemModalController($uibModalInstance, toastr, $scope, ItemService, JenisItemService, data) {
        $scope.ori = angular.copy(data);
        $scope.modalTitle = "Edit Item";
        console.log('edit', data);
        $scope.vm = angular.copy(data);

        JenisItemService.cariSemua().success(function (data) {
            $scope.listJenisItem = data;
        });
        $scope.simpan = function () {
            ItemService.simpan($scope.vm, $scope.ori).success(function (d) {
                $uibModalInstance.close($scope.vm);
                toastr.success('Simpan data sukses!');
            })
        }
    }

    function ItemUkuranModalController($uibModalInstance, toastr, $scope, ItemUkuranService, data) {
        $scope.modalTitle = "Ukuran untuk Item " + data.nama;
        console.log('edit ukuran ', data);
        $scope.oldSearch = "";
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.vm = {};
        $scope.ori = {};

        $scope.reloadData = function () {
            $scope.dataPage = ItemUkuranService.filterByItem(data.id, $scope.paging.currentPage - 1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
        };

        $scope.reloadData();

        $scope.clear = function () {
            $scope.modalTitle = "Tambah Ukuran untuk Item " + data.nama;
            $scope.vm = {active: true, item: data};
            $scope.ori = {active: true, item: data};
        };

        $scope.baru = function () {
            console.log('Baru');
            $scope.clear();
            $scope.isNew = true;
        };

//        $scope.baru();
        $scope.edit = function (x) {
            $scope.ori = angular.copy(x);
            $scope.modalTitle = "Edit Ukuran untuk Item " + data.nama;
            console.log('edit', x);
            $scope.vm = angular.copy(x);
        };

        $scope.hapus = function (x) {
            ItemUkuranService.hapus(x).success(function () {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            });
        };

        $scope.reloadData();
        $scope.simpan = function () {
            ItemUkuranService.simpan($scope.vm, $scope.ori).success(function (d) {
                toastr.success('Simpan data sukses!');
                $scope.clear();
                $scope.reloadData();
                $scope.isNew = false;
            });
        };
        $scope.batal = function () {
            $scope.isNew = false;
        }
    }
})();


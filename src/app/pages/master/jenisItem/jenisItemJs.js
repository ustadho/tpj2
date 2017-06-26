(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('JenisItemCtrl', JenisItemCtrl)
            .controller('JenisItemModalController', JenisItemModalController);

    /** @ngInject */
    function JenisItemCtrl($scope, $uibModal, $log, toastr, JenisItemService) {
        $scope.search = "";
        $scope.oldSearch = "";
        $scope.paging = {
            currentPage: 1,
            totalItems: 0,
        };

        $scope.reloadData = function () {
            $scope.dataPage = JenisItemService.query($scope.search, $scope.paging.currentPage-1, function () {
                console.log('$scope.dataPage', $scope.dataPage);
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
        };

        $scope.reloadData();

        $scope.baru = function () {
            console.log('Baru');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/master/jenisItem/jenis-item-modal.html',
                controller: 'JenisItemModalController',
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

        $scope.edit = function (x, page, size) {
            console.log('Open modal');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: page,
                controller: 'JenisItemModalController',
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
            JenisItemService.hapus(x).success(function () {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            });
        }
    }

    function JenisItemModalController($uibModalInstance, toastr, $scope, JenisItemService, data) {
        $scope.ori = angular.copy(data);
        $scope.modalTitle = "Edit JenisItem";
        console.log('edit', data);
        $scope.vm = angular.copy(data);


        $scope.simpan = function () {
            JenisItemService.simpan($scope.vm, $scope.ori).success(function (d) {
                $uibModalInstance.close($scope.vm);
                toastr.success('Simpan data sukses!');
            })
        }
    }
})();


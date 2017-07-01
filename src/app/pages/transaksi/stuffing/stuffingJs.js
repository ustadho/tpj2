(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('StuffingCtrl', StuffingCtrl)
            .controller('StuffingModalController', StuffingModalController);

    /** @ngInject */
    function StuffingCtrl($scope, $uibModal, $log, $filter, toastr, StuffingService, KotaService, KapalBerangkatService, EmklService, SatuanKirimService) {
        $scope.search = "";
        $scope.oldSearch = "";
        $scope.deskKapalBerangkat = "";
        $scope.paging = {
            currentPage: 1,
            totalItems: 0
        };
        $scope.options = {format: 'DD/MM/YYYY', showClear: false};
        $scope.param = {tglAwal: new Date(), tglAkhir: new Date(), cari: ""};
        $scope.reloadData = function () {
            $scope.dataPage = StuffingService.queryComposite($filter('date')(new Date($scope.param.tglAwal), 'yyyy-MM-dd'), $filter('date')(new Date($scope.param.tglAkhir), 'yyyy-MM-dd'), ($scope.param.cari == '' ? 'null' : $scope.param.cari), $scope.paging.currentPage - 1, function () {
                $scope.paging.maxSize = ($scope.dataPage.size);
                $scope.paging.totalItems = $scope.dataPage.totalElements;
                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
                $scope.paging.maxPage = $scope.dataPage.totalPages;
            });
//            $scope.dataPage = StuffingService.query($scope.search, $scope.paging.currentPage - 1, function () {
//                $scope.paging.maxSize = ($scope.dataPage.size);
//                $scope.paging.totalItems = $scope.dataPage.totalElements;
//                $scope.paging.currentPage = parseInt($scope.dataPage.number) + 1;
//                $scope.paging.maxPage = $scope.dataPage.totalPages;
//            });
        };

        $scope.clear = function () {
            $scope.modalTitle = "";
            $scope.vm = {};
            $scope.ori = {};
            $scope.reloadData();
        };

        $scope.clear();

        $scope.baru = function () {
            $scope.vm = {aktif: true};
            $scope.ori = {};
            $scope.modalTitle = "Tambah Stuffing";
            console.log('Baru');
        };

        $scope.edit = function (x) {
            $scope.ori = angular.copy(x);
            $scope.modalTitle = "Edit Stuffing";
            console.log('edit', x);
            StuffingService.cariSatu("kode", x.id).success(function (data) {
                if (data.tglClosing !== null && data.tglClosing !== undefined) {
                    data.tglClosing = new Date(data.tglClosing);
                }
                $scope.vm = angular.copy(data);
                if ($scope.vm.kapalBerangkat !== null && $scope.vm.kapalBerangkat !== undefined) {
                    $scope.deskKapalBerangkat = $scope.vm.kapalBerangkat.kapal.nama + ' Tgl ' + $filter('date')(new Date($scope.vm.kapalBerangkat.tglBerangkat), "dd/MM/yyyy");
                }
            });
        };

        $scope.hapus = function (x) {
            StuffingService.hapus(x.id).success(function () {
                toastr.success('Hapus data sukses!');
                $scope.reloadData();
            }).error(function () {
                toastr.error('Maaf Anda tidak dapat menghapus Stuffing yg sudah dibuatkan Surat Jalannya!');
            });
        };
        $scope.opened = false;
        $scope.openDate = openDate;
        function openDate() {
            $scope.opened = true;
        }

        $scope.fillListKapalBerangkat = function (kota) {
            KapalBerangkatService.filterByIdKotaAndAktif(kota.id).success(function (data) {
                $scope.listKapalBerangkat = data;
                console.log('$scope.listKapalBerangkat', $scope.listKapalBerangkat);
            });
        };

        $scope.resetKapalBerangkat = function () {
            $scope.vm.kapalBerangkat = null;
            $scope.deskKapalBerangkat = '';
        };
        $scope.lookupKapalBerangkat = function () {
            console.log('lookupKapalBerangkat', $scope.vm);
            console.log('kota', $scope.vm.kota);
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/tpl/lookup-kapal-berangkat.html',
                controller: 'lookupKapalBerangkatCtrl',
                size: 'lg',
                resolve: {
                    param: function () {
                        return {
                            kota: $scope.vm.kota,
                        }
                    },
                }
            });
            modalInstance.result.then(function (sd) {
                console.log('selectedStuffing', sd);
                $scope.vm.kapalBerangkat = sd;
                $scope.deskKapalBerangkat = $scope.vm.kapalBerangkat.kapal.nama + ' Tgl ' + $filter('date')(new Date(sd.tglBerangkat), "dd/MM/yyyy");
                console.log('vm.kapalBerangkat', $scope.vm.kapalBerangkat);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
        
        $scope.lookupKontainer = function () {
            console.log('lookupKontainer', $scope.vm);
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/tpl/lookup-kontainer.html',
                controller: 'LookupKontainerController',
                size: 'lg',
                resolve: {
                    param: function () {
                        return {
                            kontainer: $scope.vm.kontainer,
                        }
                    },
                }
            });
            modalInstance.result.then(function (sd) {
                console.log('selectedKontainer', sd);
                $scope.vm.kontainer = sd;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        KotaService.cariSemua().success(function (data) {
            $scope.listKota = data;
            console.log('$scope.listKota', $scope.listKota);
        });
        EmklService.cariSemua().success(function (data) {
            $scope.listEmkl = data;
            console.log('$scope.listEmkl', $scope.listEmkl);
        });
        SatuanKirimService.cariSemua().success(function (data) {
            $scope.listSatuanKirim = data;
            console.log('$scope.listSatuanKirim', $scope.listSatuanKirim);
        });

        $scope.simpan = function () {
            StuffingService.simpan($scope.vm, $scope.ori).success(function (d) {
                toastr.success('Simpan data sukses!');
                $scope.clear();
            });
        };

        $scope.cetak = function (c, ex, tipe) {
            var link = 'api/report/per-stuffing.' + tipe + '?id=' + c.id + '&ex=' + ex;
            if (tipe == 'pdf') {
//                    window.open(link, '_blank', 'width=screen.width, height=screen.height');
                window.open(link, '_blank', 'width=1024, height=768');
            } else {
                location.href = link;
            }
        }
    }

    function StuffingModalController($uibModalInstance, toastr, $scope, StuffingService, KotaService, KontainerService, KapalBerangkatService, EmklService, SatuanKirimService, data) {
        $scope.ori = angular.copy(data);
        $scope.modalTitle = "Edit Stuffing";
        console.log('edit', data);
        $scope.vm = angular.copy(data);

        if ($scope.vm.id === undefined || $scope.vm.id === null) {
            $scope.modalTitle = "Tambah Stuffing";
        }

        $scope.opened = false;
        $scope.dateOptions = {
            showWeeks: false
        };
        $scope.options = {format: 'DD/MM/YYYY', showClear: false};
        $scope.openDate = openDate;
        function openDate() {
            $scope.opened = true;
        }

        KotaService.cariSemua().success(function (data) {
            $scope.listKota = data;
            console.log('$scope.listKota', $scope.listKota);
        });
        KapalBerangkatService.cariSemua().success(function (data) {
            $scope.listKapalBerangkat = data;
            console.log('$scope.listKapalBerangkat', $scope.listKapalBerangkat);
        });
        EmklService.cariSemua().success(function (data) {
            $scope.listEmkl = data;
            console.log('$scope.listEmkl', $scope.listEmkl);
        });
        KontainerService.cariSemua().success(function (data) {
            $scope.listKontainer = data;
            console.log('$scope.listKontainer', $scope.listKontainer);
        });
        SatuanKirimService.cariSemua().success(function (data) {
            $scope.listSatuanKirim = data;
            console.log('$scope.listSatuanKirim', $scope.listSatuanKirim);
        });

        $scope.simpan = function () {
            StuffingService.simpan($scope.vm, $scope.ori).success(function (d) {
                $uibModalInstance.close($scope.vm);
                toastr.success('Simpan data sukses!');
            });
        }
    }
})();


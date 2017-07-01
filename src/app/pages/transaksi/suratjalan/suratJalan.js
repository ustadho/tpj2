(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('SuratJalanCtrl', SuratJalanCtrl)

    function SuratJalanCtrl($scope, $log, $uibModal, $stateParams, SuratJalanService, KotaService,
            KondisiService, TokoService, toastr, $timeout) {
        $scope.open = open;
        $scope.opened = false;
        $scope.format = 'dd-MM-yyyy';
        $scope.kota = {};
        $scope.totalColi = 0;
        $scope.totalMetrik = 0.0;

        $scope.ori = {};
        $scope.options = {format: 'DD/MM/YYYY', showClear: false};

        function open() {
            $scope.opened = true;
        }



        KotaService.cariSemua().success(function (data) {
            $scope.listKota = data;
        });
        KondisiService.cariSemua().success(function (data) {
            $scope.listKondisi = data;
        });

        function cekSebelumSimpan() {
            if ($scope.vm.merk == undefined || $scope.vm.merk == null) {
                toastr.error('Silahkan masukkan Merk terlebih dulu!');
                return false;
            } else {
                if ($scope.vm.tanggal == undefined || $scope.vm.tanggal == null) {
                    toastr.error('Silahkan Pilih tanggal Surat Jalan terlebih dulu!');
                    return false;
                } else {
                    var adaToko = 'id' in $scope.vm.toko;
                    console.log('cek pengirim', adaToko);
                    if (!adaToko) {
                        console.log('pengirim baru');
                        TokoService.cariSatu('nama', $scope.vm.toko.nama).success(function (d) {
                            if (d.id == null || d.id == undefined) {
                                console.log('pengirim baru');
                                TokoService.simpan({nama: $scope.vm.toko.nama, aktif: true}, {})
                                        .success(function (d) {
                                            console.log('hasil toko baru', d);
                                            $scope.vm.toko = {
                                                id: d.id,
                                                nama: d.nama
                                            };
                                            return true;
                                        })
                                        .error(function (d) {
                                            return false;
                                            toastr.error('Gagal menyimpan toko baru');
                                        });
                            } else {
                                console.log('sudah-ada-toko', d);
                                $scope.vm.toko = {
                                    id: d.id,
                                    nama: d.nama
                                };
                                return true;
                            }
                        });
                    } else {
                        return true;
                    }
                }
            }
            //return true;
        }

        $scope.simpan = function (pisahKontainer) {
            if (cekSebelumSimpan()) {
                for (var i = 0; i < $scope.vm.listSuratJalanDetail.length; i++) {
                    $scope.vm.listSuratJalanDetail[i].suratJalan = null;
                }
                console.log('simpan', $scope.vm);
                SuratJalanService.simpan($scope.vm, $scope.ori).success(function (d) {
                    toastr.success('Simpan Surat Jalan Sukses!');
                    if (pisahKontainer === false) {
                        $scope.clear();
                    } else {
                        $scope.vm = d;
                    }
                    if ($stateParams.idSj === null || $stateParams.idSj === undefined || $stateParams.idSj === 0 || $stateParams.idSj === '') {
                        $scope.clear();
                    } else {
                        window.close();
                    }
                }).error(function(error){
                    toastr.error('SImpan Surat Jalan Gagal!!!')
                });
            }
        };
        $scope.clear = function () {
            $scope.vm = {
                tanggal: new Date(),
                kondisi: {id: 1, nama: 'PORT TO DOOR'},
                merk: {nama: ''},
                toko: {nama: ''},
                stuffing: {},
                listSuratJalanDetail: []
            };
            $scope.totalColi = 0;
            $scope.totKubikasi = 0;
        };
        $scope.hitungSubTotalMetrik = function (c) {
            var subTotal = 0;
            var totKubikasi = 0;
            if (c.paket != true) {
                if (c.t == 0) {
                    if (c.l == 0 && c.t == 0) {
                        subTotal = c.p;
                    } else {
                        subTotal = c.p * c.l / 1000;
                    }
                } else {
                    subTotal = c.p * c.l * c.t / 1000000;
                }
                totKubikasi += c.coli * subTotal;
            }
            return totKubikasi;
        };
        $scope.hitungTotalColiMetrik = function () {
            $scope.totalColi = 0;
            $scope.totalMetrik = 0.0;
            $scope.totKubikasi = 0;
            for (var i = 0; i < $scope.vm.listSuratJalanDetail.length; i++) {
                $scope.totalColi += $scope.vm.listSuratJalanDetail[i].coli;
                $scope.subTotal = 0;
                if ($scope.vm.listSuratJalanDetail[i].paket != true) {
                    if ($scope.vm.listSuratJalanDetail[i].fixVolume > 0) {
                        $scope.subTotal = $scope.vm.listSuratJalanDetail[i].fixVolume;
                        $scope.totKubikasi += $scope.subTotal;
                    } else {
                        if ($scope.vm.listSuratJalanDetail[i].t == 0) {
                            if ($scope.vm.listSuratJalanDetail[i].l == 0 && $scope.vm.listSuratJalanDetail[i].t == 0) {
                                $scope.subTotal = $scope.vm.listSuratJalanDetail[i].p;
                            } else {
                                $scope.subTotal = $scope.vm.listSuratJalanDetail[i].p * $scope.vm.listSuratJalanDetail[i].l / 1000;
                            }
                        } else {
                            $scope.subTotal = $scope.vm.listSuratJalanDetail[i].p * $scope.vm.listSuratJalanDetail[i].l * $scope.vm.listSuratJalanDetail[i].t / 1000000;
                        }
                        $scope.totKubikasi += $scope.vm.listSuratJalanDetail[i].coli * $scope.subTotal;
                    }
                } else {
                    $scope.vm.listSuratJalanDetail[i].p = 0;
                    $scope.vm.listSuratJalanDetail[i].l = 0;
                    $scope.vm.listSuratJalanDetail[i].t = 0;
                    $scope.subTotal = 0;
                    $scope.totKubikasi += $scope.subTotal;
                }
            }
        };
        $scope.tambahItem = function () {
            console.log('Baru');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/transaksi/suratjalan/suratJalanItemModal.html',
                controller: 'SuratJalanItemModalCtrl',
                size: 'md',
                resolve: {
                    data: null
                }
            });
            modalInstance.result.then(function (item) {
                console.log('modalInstanceResult', item);
                item.sjStufings = [{
                        stuffing: $scope.vm.stuffing,
                        coli: item.coli
                    }];
                $scope.vm.listSuratJalanDetail.push(item);
                $scope.hitungTotalColiMetrik();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
        $scope.editItem = function (c, idx) {
            $scope.idxItem = idx;
            $scope.oldColi = c.coli;
            console.log('Baru');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/transaksi/suratjalan/suratJalanItemModal.html',
                controller: 'SuratJalanItemModalCtrl',
                size: 'md',
                resolve: {
                    data: c
                }
            });
            modalInstance.result.then(function (item) {
                console.log('modalInstanceResult', item);
//                for (var i = 0; i < $scope.vm.listSuratJalanDetail.length; i++) {
//                    if ($scope.vm.listSuratJalanDetail[i].id === item.id) {
//                        $scope.vm.listSuratJalanDetail[i] = item;
//                    }
//                }
                if ($scope.oldColi !== item.coli) {
                    item.sjStufings = [{
                            stuffing: $scope.vm.stuffing,
                            coli: item.coli
                        }];
                }
                $scope.vm.listSuratJalanDetail[$scope.idxItem] = item;
                $scope.hitungTotalColiMetrik();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
        $scope.hapusItem = function (idx) {
            $scope.vm.listSuratJalanDetail.splice(idx, 1);
        };
        $scope.lookupStuffing = function () {
            console.log('lookupStuffing', $scope.vm);
            console.log('kota', $scope.kota);
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/tpl/lookup-stuffing.html',
                controller: 'LookupStuffingCtrl',
                size: 'md',
                resolve: {
                    param: function () {
                        return {
                            kota: $scope.kota.selected,
                        }
                    },
                }
            });
            modalInstance.result.then(function (sd) {
                console.log('selectedStuffing', sd);
                $scope.vm.stuffing = {
                    id: sd.id,
                    kontainer: {
                        id: sd.id_kontainer,
                        nomor: sd.nomor
                    }
                };
                console.log('$scope.vm.stuffing', $scope.vm.stuffing);
                for (var i = 0; i < $scope.vm.listSuratJalanDetail.length; i++) {
                    $scope.vm.listSuratJalanDetail[i].sjStufings = [{
                            stuffing: $scope.vm.stuffing,
                            coli: $scope.vm.listSuratJalanDetail[i].coli
                        }];
                }
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
        $scope.editSjStuffing = function (c) {
//            $scope.vm.listSuratJalanDetail[idx].suratJalan = $scope.vm;
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/transaksi/suratjalan/sj-stuffing-modal.html',
                controller: 'SjStuffingModalCtrl',
                size: 'lg',
                resolve: {
                    param: function () {
                        return {
                            kota: $scope.kota.selected,
                            sjDetail: c //$scope.vm.listSuratJalanDetail[idx]
                        }
                    },
                }
            });
            modalInstance.result.then(function (sd) {
//                $scope.initForm($stateParams.idSj);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
        $scope.lookupMerk = function () {
            console.log('lookupMerk', $scope.vm);
            console.log('kota', $scope.kota);
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/tpl/lookup-merk.html',
                controller: 'LookupMerkCtrl',
                size: 'md',
                resolve: {
                    param: function () {
                        return {
                            merk: $scope.vm.merk.nama,
                            kota: $scope.kota.selected,
                        }
                    },
                }
            });
            modalInstance.result.then(function (sd) {
                console.log('selectedMerk', sd);
                $scope.vm.merk = {
                    id: sd.id,
                    nama: sd.merk,
                    toko: {
                        id: sd.id_toko,
                        nama: sd.toko
                    },
                };
                console.log('merk', $scope.vm.merk);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        }
        $scope.lookupToko = function () {
            console.log('lookupToko', $scope.vm);
            console.log('kota', $scope.kota);
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/tpl/lookup-toko.html',
                controller: 'LookupTokoCtrl',
                size: 'md',
                resolve: {
                    param: function () {
                        return {
                            cari: $scope.vm.toko.nama,
                            kota: $scope.kota.selected,
                        }
                    },
                }
            });
            modalInstance.result.then(function (sd) {
                console.log('selectedToko', sd);
                $scope.vm.toko = {
                    id: sd.id,
                    nama: sd.nama
                };
                console.log('toko', $scope.vm.toko);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
        $scope.initForm = function (idSj) {
            SuratJalanService.cariSatu('kode', idSj).success(function (data) {
                console.log('edit', data);
                if (data.id !== undefined) {
                    $scope.ori = angular.copy(data);
                    $scope.kota.selected = data.stuffing.kota;
                    $scope.vm = data;
                    if ($scope.vm.tanggal !== null && $scope.vm.tanggal !== undefined) {
                        $scope.vm.tanggal = new Date($scope.vm.tanggal);
                        $scope.kota.selected = $scope.vm.stuffing.kota;
                    }
                    console.log('data edit', data);
                    $scope.hitungTotalColiMetrik();
                }
            });
        };
        console.log('data edit', $stateParams);
        if ($stateParams.idSj === null || $stateParams.idSj === undefined || $stateParams.idSj === 0 || $stateParams.idSj === '') {
            $scope.clear();
        } else {
            $scope.initForm($stateParams.idSj);
        }

//        $scope.$watch('vm.stuffing', function (newValue, oldValue) {
//            if($scope.vm!=undefined && $scope.vm!=null && $scope.vm.listSuratJalanDetail!=undefined && $scope.vm.listSuratJalanDetail!=null){
//                for (var i = 0; i < $scope.vm.listSuratJalanDetail.length; i++) {
//                    $scope.vm.listSuratJalanDetail[i].sjStufings = [{
//                        stuffing: $scope.vm.stuffing,
//                        coli: $scope.vm.listSuratJalanDetail[i].coli
//                    }];
//                }
//            }
//        });
    }

}
)();
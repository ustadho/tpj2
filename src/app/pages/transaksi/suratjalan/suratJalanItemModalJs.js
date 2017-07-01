(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('SuratJalanItemModalCtrl', suratJalanItemModalCtrl);

    function suratJalanItemModalCtrl($scope, $log, $uibModal, $uibModalInstance, $ngBootbox, data, ItemService, toastr) {
        $scope.vm = {
            coli: 1,
            paket: false,
            fixVolume: 0,
            item: {},
            p: 0,
            l: 0,
            t: 0,
        };

        $scope.subTotal = 0;
        $scope.oldColi = null;

        $scope.modalTitle = (data == null || data == undefined || data == {} ? "Tambah" : "Edit") + " Item";
        console.log('$scope.modalTitle', data);
        if (data == null || data == undefined || data == {}) {
            console.log('baru', $scope.vm);
        } else {
            $scope.vm = angular.copy(data);
            $scope.oldColi = data.coli;
            $scope.totKubikasi = data.fixVolume;
            $scope.vm.fixVolume = data.fixVolume > 0;
            console.log('edit');
        }

        $scope.lookupItem = function () {
            console.log('Baru', $scope.vm);
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/tpl/lookup-item.html',
                controller: 'LookupItemCtrl',
                size: 'md',
                resolve: {
                    cari: function () {
                        return $scope.vm.item.nama;
                    }
                }
            });
            modalInstance.result.then(function (selectedItem) {
                console.log('selectedItem', selectedItem);
                $scope.vm.item = {
                    id: selectedItem.id,
                    nama: selectedItem.nama,
                };
                $scope.vm.p = selectedItem.p;
                $scope.vm.l = selectedItem.l;
                $scope.vm.t = selectedItem.t;

            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.simpan = function () {
            if ($scope.vm.item == null || $scope.vm.item == undefined || $scope.vm.item.id == undefined) {
                ItemService.cariSatu('nama', $scope.vm.item.nama).success(function (d) {
                    if (d.id == null || d.id == undefined) {
                        console.log('item baru');
                        ItemService.simpan({nama: $scope.vm.item.nama, aktif: true}, {})
                                .success(function (d) {
                                    console.log('hasil item baru', d);
                                    $scope.vm.item = d;
                                    $scope.vm.fixVolume = $scope.vm.fixVolume == true ? $scope.totKubikasi : 0;
                                    $uibModalInstance.close($scope.vm);
                                })
                                .error(function (d) {
                                    toastr.error('Gagal menyimpan item baru');
                                });
                    } else {
                        console.log('sudah-ada-item', d);
                        $scope.vm.item = d;
                        $scope.vm.fixVolume = $scope.vm.fixVolume == true ? $scope.totKubikasi : 0;
                        $uibModalInstance.close($scope.vm);
                    }
                });
            } else {
                $scope.vm.fixVolume = $scope.vm.fixVolume == true ? $scope.totKubikasi : 0;
                $uibModalInstance.close($scope.vm);
            }
        };

        $scope.tambahkan = function () {
            console.log('item', $scope.vm.item);
            if ($scope.oldColi !== null && $scope.vm.coli !== $scope.oldColi) {
                $ngBootbox.confirm({message: "<b>Jumlah coli berubah dari "+$scope.oldColi+" menjadi "+$scope.vm.coli+" maka setting pembagian continer akan direset. Anda yakin akan menyimpannya?</b>", title: 'Perhatian!!!'})
                        .then(function () {
                            $scope.simpan();
                        }, function () {
                            console.log('Confirm dismissed!');
                        });
            } else {
                $scope.simpan();
            }
        }

        $scope.$watchGroup(['vm.p', 'vm.l', 'vm.t', 'vm.coli'], function (newValue, oldValue, scope) {
            if ($scope.vm.paket == null) {
                return;
            }
            if ($scope.vm.paket != true) {
                if ($scope.vm.fixVolume == true) {

                } else {
                    if ($scope.vm.t == 0) {
                        if ($scope.vm.l == 0 && $scope.vm.t == 0) {
                            $scope.subTotal = $scope.vm.p;
                        } else {
                            $scope.subTotal = $scope.vm.p * $scope.vm.l / 1000;
                        }
                    } else {
                        $scope.subTotal = $scope.vm.p * $scope.vm.l * $scope.vm.t / 1000000;
                    }
                    $scope.totKubikasi = $scope.vm.coli * $scope.subTotal;
                }
            }
        });

        $scope.$watch('vm.paket', function () {
            if ($scope.vm.paket == null) {
                return;
            }
            if ($scope.vm.paket == true) {
                $scope.vm.p = 0;
                $scope.vm.l = 0;
                $scope.vm.t = 0;
                $scope.subTotal = 0;
                $scope.totKubikasi = 0;
            }
        })
        $scope.$watch('vm.fixVolume', function () {
            if ($scope.vm.fixVolume == null) {
                return;
            }
            if ($scope.vm.fixVolume == true) {
                $scope.vm.p = 0;
                $scope.vm.l = 0;
                $scope.vm.t = 0;
                $scope.subTotal = 0;
//                $scope.totKubikasi = 0;
            }
        })
    }
}
)();
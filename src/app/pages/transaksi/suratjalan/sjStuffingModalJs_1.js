(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('SjStuffingModalCtrl_1', SjStuffingModalCtrl);

    function SjStuffingModalCtrl($scope, $log, $uibModal, $uibModalInstance, toastr, SuratJalanService, param) {

        $scope.modalTitle = (param == null || param == undefined || param == {} ? "Tambah" : "Edit") + " Item";
        console.log('$scope.modalTitle', param);

        SuratJalanService.getSjStuffing(param.sjDetail.id).success(function (data) {
            $scope.listSjStuffing = data;
            $scope.listSjStuffing[0].sjDetail.suratJalan = param.sjDetail.suratJalan;
            console.log('$scope.listSjStuffing', data);
        });

        $scope.tambah = function () {
            $scope.lookupStuffing(-1);
        };

        $scope.edit = function (idx) {
            $scope.lookupStuffing(idx);
        };

        $scope.hapus = function (idx) {
            $scope.listSjStuffing.splice(idx, 1);
        };

        $scope.lookupStuffing = function (idx) {
            var stuffing = {};
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
                            kota: param.kota,
                        }
                    },
                }
            });
            modalInstance.result.then(function (sd) {
                console.log('selectedStuffing', sd);
                if (idx === -1) {
                    stuffing = {
                        id: sd.id,
                        kontainer: {
                            id: sd.id_kontainer,
                            nomor: sd.nomor
                        },
                        kapalBerangkat: {
                            id: sd.id_kapal_berangkat,
                            tglBerangkat: sd.tgl_berangkat,
                            kapal: {
                                id: sd.id_kapal,
                                nama: sd.nama_kapal
                            },
                        }
                    };
                    $scope.listSjStuffing.push({
                        stuffing: stuffing,
                        sjDetail: $scope.listSjStuffing[0].sjDetail
                    });
                } else {
                    stuffing = {
                        id: sd.id,
                        kontainer: {
                            id: sd.id_kontainer,
                            nomor: sd.nomor
                        },
                        kapalBerangkat: {
                            id: sd.id_kapal_berangkat,
                            tglBerangkat: sd.tgl_berangkat,
                            kapal: {
                                id: sd.id_kapal,
                                nama: sd.nama_kapal
                            },
                        }
                    };
                    $scope.listSjStuffing[idx].stuffing = stuffing;
                }
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.simpan = function () {
            var totalColi = 0;
            for (var i = 0; i < $scope.listSjStuffing.length; i++) {
                totalColi += ($scope.listSjStuffing[i].coli == null ? 0 : $scope.listSjStuffing[i].coli);
                if ($scope.listSjStuffing[i].sjDetail !== null) {
                    $scope.listSjStuffing[i].sjDetail.suratJalan = {id: $scope.listSjStuffing[i].sjDetail.suratJalan.id};
                }
            }
            console.log('totalColi = ' + totalColi + ', param.sjDetail.coli = ' + param.sjDetail.coli);
            console.log('$scope.listSjStuffing',$scope.listSjStuffing);
            if (param.sjDetail.coli === totalColi) {
                SuratJalanService.postSjStuffings($scope.listSjStuffing).success(function (data) {
                    $uibModalInstance.close($scope.vm);
                });
            } else {
                toastr.warning('Perhatian!! jumlah total Coli tidak sesuai');
            }
        };

    }
}
)();
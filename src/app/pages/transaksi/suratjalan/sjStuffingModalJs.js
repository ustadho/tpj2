(function () {
    'use strict';

    angular.module('tpjApp')
            .controller('SjStuffingModalCtrl', SjStuffingModalCtrl);

    function SjStuffingModalCtrl($scope, $log, $uibModal, $uibModalInstance, toastr, SuratJalanService, param) {
        $scope.modalTitle = (param == null || param == undefined || param == {} ? "Tambah" : "Edit") + " Item";
        console.log('$scope.param', param);

//        SuratJalanService.getSjStuffing(param.sjDetail.id).success(function (data) {
//            $scope.listSjStuffing = data;
//            $scope.listSjStuffing[0].sjDetail.suratJalan = param.sjDetail.suratJalan;
//            console.log('$scope.listSjStuffing', data);
//        });
        $scope.sjDetail = param.sjDetail;
        $scope.tambah = function () {
            $scope.lookupStuffing(-1);
        };

        $scope.edit = function (idx) {
            $scope.lookupStuffing(idx);
        };

        $scope.hapus = function (idx) {
            $scope.sjDetail.sjStufings.splice(idx, 1);
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
                    $scope.sjDetail.sjStufings.push({
                        stuffing: stuffing,
                        coli: 0
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
                    $scope.sjDetail[idx].stuffing = stuffing;
                }
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.simpan = function () {
            var totalColi = 0;
            for (var i = 0; i < $scope.sjDetail.sjStufings.length; i++) {
                totalColi += ($scope.sjDetail.sjStufings[i].coli == null ? 0 : $scope.sjDetail.sjStufings[i].coli);
//                if ($scope.sjDetail.sjStufings[i].sjDetail !== null) {
//                    $scope.sjDetail.sjStufings[i].sjDetail.suratJalan = {id: $scope.sjDetail[i].sjDetail.suratJalan.id};
//                }
            }
            console.log('totalColi = ' + totalColi + ', param.sjDetail.coli = ' + param.sjDetail.coli);
            console.log('$scope.listSjStuffing',$scope.sjDetail);
            if (param.sjDetail.coli === totalColi) {
//                SuratJalanService.postSjStuffings($scope.sjDetail).success(function (data) {
//                    $uibModalInstance.close($scope.vm);
                    $uibModalInstance.close($scope.sjDetail);
//                });
            } else {
                toastr.warning('Perhatian!! jumlah total Coli tidak sesuai');
            }
        };

    }
}
)();
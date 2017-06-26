(function () {
    'use strict';

    angular.module('tpjApp')
            .factory('KapalBerangkatService', KapalBerangkatService);

    /** @ngInject */
    KapalBerangkatService.inject = ['$http', '$resource'];

    function KapalBerangkatService($http, $resource) {
        var url = 'api/master/kapal-berangkat';
        return {
            entity: $resource(url + '/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            query: function (search, p, callback) {
                return this.entity.queryPage({"a": search, "page": p, "size": 10}, callback)
            },
            entityComposite: $resource(url + '/tglawal/tglakhir/idkota/idkapal/cari/:tglawal/:tglakhir/:idkota/:idkapal/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            queryComposite: function (tglawal, tglakhir, idkota, idkapal, search, p, callback) {
                return this.entityComposite.queryPage({"a": search, "tglawal": tglawal, "tglakhir": tglakhir, "idkota": idkota, "idkapal": idkapal, "page": p, "size": 10}, callback)
            },
            simpan: simpan,
            cariSatu: cariSatu,
            cariSemua: cariSemua,
            hapus: hapus,
            filterByIdKotaAndAktif: filterByIdKotaAndAktif
        }

//        return service;

        ;

        function simpan(data, ori) {
            if (ori.id == null || ori.id == undefined) {
                return $http.post(url, data)
            } else {
                return $http.put(url + '/' + ori.id, data)
            }
        }
        function cariSatu(column, value) {
            return $http.get(url + '/' + column + '/' + value);
        }
        function cariSemua() {
            return $http.get(url + '/all');
        }
        function filterByIdKotaAndAktif(idKota) {
            return $http.get(url + '/all-aktif-by-kota/' + idKota);
        }
        function hapus(obj) {
            if (obj.id != null) {
                return $http.delete(url + "/" + obj.id);
            }
        }
    }

})();


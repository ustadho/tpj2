(function () {
    'use strict';

    angular.module('tpjApp')
            .factory('BahanSjService', BahanSjService);

    /** @ngInject */
    BahanSjService.inject = ['$http', '$resource'];

    function BahanSjService($http, $resource) {
        var url = 'api/transaksi/bahan-sj';
        return {
            entity: $resource(url + '/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            query: function (search, p, callback) {
                return this.entity.queryPage({"a": search, "page": p, "size": 10}, callback)
            },
            entityComposite: $resource(url + '/tglawal/tglakhir/outstand/cari/:tglawal/:tglakhir/:outstand/:cari', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            queryComposite: function (tglAwal, tglAkhir, outstand, search, p, callback) {
                return this.entityComposite.queryPage({"tglAwal": tglAwal, "tglAkhir": tglAkhir, "outstand": outstand, "cari": search, "page": p, "size": 10}, callback)
            },
            simpan: simpan,
            cariSatu: cariSatu,
            cariSemua: cariSemua,
            hapus: hapus,
        };

        function simpan(data, ori) {
            if (ori.id == null || ori.id == undefined) {
                return $http.post(url, data)
            } else {
                return $http.put(url + '/' + ori.id, data)
            }
        }
        function cariSatu(column, id) {
            return $http.get(url + '/' + column + '/' + id);
        }
        function cariSemua() {
            return $http.get(url + '/all');
        }
        function hapus(obj) {
            if (obj.id != null) {
                return $http.delete(url + "/" + obj.id);
            }
        }
    }

})();


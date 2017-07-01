(function () {
    'use strict';

    angular.module('tpjApp')
            .factory('StuffingService', StuffingService);

    /** @ngInject */
    StuffingService.inject = ['$http', '$resource'];

    function StuffingService($http, $resource) {
        var url = 'api/transaksi/stuffing';
        return {
            entity: $resource(url + '/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            query: function (search, p, callback) {
                return this.entity.queryPage({"a": search, "page": p, "size": 10}, callback)
            },
            entityComposite: $resource(url + '/tglawal/tglakhir/cari/:tglawal/:tglakhir/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            queryComposite: function (tglawal, tglakhir, search, p, callback) {
                return this.entityComposite.queryPage({"a": search, "tglawal": tglawal, "tglakhir": tglakhir, "page": p, "size": 10}, callback)
            },
            simpan: simpan,
            cariSatu: cariSatu,
            lookup: lookup,
            hapus: hapus,
            listStuffingPerKapalBerangkat: listStuffingPerKapalBerangkat
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
        function cariSatu(column, id) {
            return $http.get(url + '/' + column + '/' + id);
        }
        function lookup(ik) {
            return $http.get(url + "/aktif/lookup/" + ik);
        }
        function listStuffingPerKapalBerangkat(ik) {
            return $http.get(url + "/stuffing/list/kapal-berangkat/" + ik);
        }
        function hapus(id) {
            return $http.delete(url + '/' + id);
        }
    }

})();


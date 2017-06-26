(function () {
    'use strict';

    angular.module('tpjApp')
            .factory('ItemUkuranService', ItemUkuranService);

    /** @ngInject */
    ItemUkuranService.inject = ['$http', '$resource'];

    function ItemUkuranService($http, $resource) {
        var url = 'api/master/item-ukuran';
        return {
            entity: $resource(url + '/filter-by-item/:a', {}, {
                queryPage: {method: 'GET', isArray: false}
            }),
            filterByItem: function (search, p, callback) {
                return this.entity.queryPage({"a": search, "page": p, "size": 10}, callback)
            },
            simpan: simpan,
            cariSatu: cariSatu,
            cariSemua: cariSemua,
            hapus: hapus,
        }

//        return service;

        ;

        function simpan(data, ori) {
            if (ori.id == undefined || ori.id == null) {
                return $http.post(url, data)
            } else {
                return $http.put(url + '/' + ori.id, data)
            }
        }
        function cariSatu(column, id) {
            return $http.get(url + '/cek?col=' + column + '&val=' + id);
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


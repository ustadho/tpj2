/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
    'use strict';

    angular.module('tpjApp')
            .config(routeConfig);

    /** @ngInject */
    function routeConfig($stateProvider) {
        $stateProvider
                .state('master', {
                    parent: 'pages',
                    url: '/master',
                    template: '<ui-view autoscroll="true" autoscroll-body-top></ui-view>',
                    abstract: true,
                    title: 'Master',
                    sidebarMeta: {
                        icon: 'ion-compose',
                        order: 100,
                    },
                })
                .state('master.item', {
                    url: '/item',
                    parent: 'master',
//                    templateUrl: 'app/pages/master/item/item.html',
//                    controller: 'ItemCtrl',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/master/item/item.html',
                            controller: 'ItemCtrl'
                        }
                    },
                    title: 'Item',
                    sidebarMeta: {
                        order: 0,
                    },
                })
                .state('master.kontainer', {
                    url: '/kontainer',
                    parent: 'master',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'Jenis Item'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/master/kontainer/kontainer.html',
                            controller: 'KontainerCtrl',
                        }
                    },
                    resolve: {
                    }
                })
                .state('master.jenisItem', {
                    url: '/jenisItem',
//                    templateUrl: 'app/pages/master/jenisItem/jenis-item.html',
//                    controller: 'JenisItemCtrl',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/master/jenisItem/jenis-item.html',
                            controller: 'JenisItemCtrl',
                        }
                    },
//          controllerAs: 'vm',
                    title: 'Jenis Item',
                    sidebarMeta: {
                        order: 200,
                    },
                })
                .state('master.kapal', {
                    url: '/kapal',
//                    templateUrl: 'app/pages/master/kapal/kapal.html',
//                    controller: 'KapalCtrl',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/master/kapal/kapal.html',
                            controller: 'KapalCtrl',
                        }
                    },
//          controllerAs: 'vm',
                    title: 'Kapal',
                    sidebarMeta: {
                        order: 300,
                    },
                })
                .state('master.kota', {
                    url: '/kota',
//                    templateUrl: 'app/pages/master/kota/kota.html',
//                    controller: 'KotaCtrl',
//          controllerAs: 'vm',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/master/kota/kota.html',
                            controller: 'KotaCtrl',
                        }
                    },
                    title: 'Kota',
                    sidebarMeta: {
                        order: 400,
                    },
                })
                .state('master.satuanKirim', {
                    url: '/satuanKirim',
//                    templateUrl: 'app/pages/master/satuanKirim/satuan-kirim.html',
//                    controller: 'SatuanKirimCtrl',
//          controllerAs: 'vm',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/master/satuanKirim/satuan-kirim.html',
                            controller: 'SatuanKirimCtrl',
                        }
                    },
                    title: 'Satuan Kirim',
                    sidebarMeta: {
                        order: 500,
                    },
                })
                .state('master.kondisi', {
                    url: '/kondisi',
                    templateUrl: 'app/pages/master/kondisi/kondisi.html',
                    controller: 'KondisiCtrl',
//          controllerAs: 'vm',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/master/kondisi/kondisi.html',
                            controller: 'KondisiCtrl',
                        }
                    },
                    title: 'Kondisi',
                    sidebarMeta: {
                        order: 600,
                    },
                })
                .state('master.toko', {
                    url: '/toko',
//                    templateUrl: 'app/pages/master/toko/toko.html',
//                    controller: 'TokoCtrl',
//          controllerAs: 'vm',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/master/toko/toko.html',
                            controller: 'TokoCtrl',
                        }
                    },
                    title: 'Toko',
                    sidebarMeta: {
                        order: 700,
                    },
                })
                .state('master.emkl', {
                    url: '/emkl',
                    templateUrl: 'app/pages/master/emkl/emkl.html',
                    controller: 'EmklCtrl',
//          controllerAs: 'vm',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/master/emkl/emkl.html',
                            controller: 'EmklCtrl',
                        }
                    },
                    title: 'Emkl',
                    sidebarMeta: {
                        order: 800,
                    },
                })
                .state('master.kapalBerangkat', {
                    url: '/kapalBerangkat',
                    templateUrl: 'app/pages/master/kapalBerangkat/kapal-berangkat.html',
//                    controller: 'KapalBerangkatCtrl',
                    views: {
                        'content@': {
                            url: '/kapalBerangkat',
                            templateUrl: 'app/pages/master/kapalBerangkat/kapal-berangkat.html'
                        }
                    },
//          controllerAs: 'vm',
                    title: 'Kapal Berangkat',
                    sidebarMeta: {
                        order: 900,
                    },
                })
    }
})();

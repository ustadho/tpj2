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
                .state('laporan', {
                    parent: 'app',
                    url: '/laporan',
                    template: '<ui-view autoscroll="true" autoscroll-body-top></ui-view>',
                    abstract: true,
                    title: 'Laporan',
                    sidebarMeta: {
                        icon: 'ion-compose',
                        order: 300,
                    },
                })
                .state('laporan.permerk', {
                    url: '/per-merk',
//          controllerAs: 'vm',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/laporan/permerk/laporan-permerk.html',
                            controller: 'LaporanPerMerkController'
                        }
                    },
                    title: 'Per Merk',
                    sidebarMeta: {
                        order: 0,
                    },
                })
                .state('laporan.bast', {
                    url: '/bast',
//          controllerAs: 'vm',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/laporan/bast/laporan-bast.html',
                            controller: 'LaporanBastController'
                        }
                    },
                    title: 'Per Merk',
                    sidebarMeta: {
                        order: 0,
                    },
                })
                .state('laporan.perkapal', {
                    url: '/per-kapal',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/laporan/perkapal/laporan-perkapal.html',
                            controller: 'LaporanPerKapalCtrl',
                        }
                    },
                    title: 'Per Kapal',
                    sidebarMeta: {
                        order: 0,
                    },
                })
                .state('laporan.rekap-merk', {
                    url: '/rekap-merk',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/laporan/rekap-merk-perstuffing/laporan-rekap-merk-stuffing.html',
                            controller: 'LaporanRekapMerkController'
                        }
                    },
                    title: 'Rekap Merk',
                    sidebarMeta: {
                        order: 0,
                    },
                })
                .state('laporan.dashboard', {
                    url: '/dashboard',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/laporan/dashboard/dashboard.html',
                            controller: 'DashboardCtrl'
                        }
                    },
                    title: 'Dashboard',
                    sidebarMeta: {
                        order: 0,
                    },
                })
                .state('laporan.infoitem', {
                    url: '/infoitem',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/laporan/infoitem/infoItem.html',
                            controller: 'InfoItemCtrl'
                        }
                    },
                    title: 'Info Item',
                    sidebarMeta: {
                        order: 0,
                    },
                })
                .state('laporan.container', {
                    url: '/jmlcontainer',
                    views: {
                        'content@': {
                            templateUrl: 'app/pages/laporan/jmlContainerBulanan/jmlContainerBulanan.html',
                            controller: 'JmlContainerBulananCtrl'
                        }
                    },
                    title: 'Jml Container Bulanan',
                    sidebarMeta: {
                        order: 0,
                    },
                })
    }
})();

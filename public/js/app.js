'use strict';

angular.module('modtools', [
  'config',
  'ngRoute',
  'ngCookies',
  'ngMaterial',
  'modtools.services',
  'modtools.controllers',
  'modtools.filters',
  'modtools.directives'
])

  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {templateUrl: 'partials/home.html', controller: 'HomeController'});
    $routeProvider.otherwise({redirectTo: '/'});
  }])

  .config(['$mdThemingProvider', '$mdIconProvider', function($mdThemingProvider, $mdIconProvider) {

    $mdThemingProvider.theme('default')
      .primaryPalette('red')
      .accentPalette('orange');

    $mdIconProvider
      .icon('menu'        , 'img/icons/ic_menu_24px.svg')
      .icon('arrow_back'  , 'img/icons/ic_arrow_back_24px.svg')
      .icon('share'       , 'img/icons/ic_share_24px.svg')
      .icon('thumb_up'    , 'img/icons/ic_thumb_up_24px.svg')
      .icon('thumb_down'  , 'img/icons/ic_thumb_down_24px.svg')
      .icon('more_horiz'  , 'img/icons/ic_more_horiz_24px.svg')
      .icon('more_vert'   , 'img/icons/ic_more_vert_24px.svg')
      .icon('lock'        , 'img/icons/ic_lock_24px.svg')
      .icon('lock_open'   , 'img/icons/ic_lock_open_24px.svg')
      .icon('cloud_upload', 'img/icons/ic_cloud_upload_24px.svg');
  }]);
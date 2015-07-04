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
      .primaryPalette('indigo', {
        'default': '700'
      })
      .accentPalette('orange');

    $mdIconProvider
      .icon('menu'          , 'img/icons/ic_menu_24px.svg')
      .icon('arrow_back'    , 'img/icons/ic_arrow_back_24px.svg')
      .icon('share'         , 'img/icons/ic_share_24px.svg')
      .icon('comment'       , 'img/icons/ic_comment_24px.svg')
      .icon('filter_list'   , 'img/icons/ic_filter_list_24px.svg')
      .icon('face'          , 'img/icons/ic_face_24px.svg')
      .icon('person'        , 'img/icons/ic_person_24px.svg')
      .icon('people'        , 'img/icons/ic_people_24px.svg')
      .icon('thumb_up'      , 'img/icons/ic_thumb_up_24px.svg')
      .icon('thumb_down'    , 'img/icons/ic_thumb_down_24px.svg')
      .icon('more_horiz'    , 'img/icons/ic_more_horiz_24px.svg')
      .icon('more_vert'     , 'img/icons/ic_more_vert_24px.svg')
      .icon('lock'          , 'img/icons/ic_lock_24px.svg')
      .icon('lock_open'     , 'img/icons/ic_lock_open_24px.svg')
      .icon('thumbs_up_down', 'img/icons/ic_thumbs_up_down_24px.svg')
      .icon('mail'          , 'img/icons/ic_mail_24px.svg')
      .icon('info'          , 'img/icons/ic_info_24px.svg')
      .icon('exit_to_app'   , 'img/icons/ic_exit_to_app_24px.svg')
      .icon('cloud_upload'  , 'img/icons/ic_cloud_upload_24px.svg');
  }]);
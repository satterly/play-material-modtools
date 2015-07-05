'use strict';

/* Controllers */

angular.module('modtools.controllers', ['ngMaterial'])

  .controller('SidenavController', ['$scope', '$mdSidenav', function ($scope, $mdSidenav) {

    $scope.queues = [
      'Central',
      'Central (watched)',
      'Comment is Free',
      'CiF (watched)'
    ];

    $scope.menu = function () {
      $mdSidenav('left').toggle();
    };
  }])

  .controller('HomeController', ['$scope', function ($scope) {


  }])

  .controller('CommentController', ['$scope', '$mdBottomSheet', function ($scope, $mdBottomSheet) {

    $scope.queue = undefined;
    $scope.comment = undefined;

    // get next comment and display it for moderation

    $scope.buttons = function($event) {
      $mdBottomSheet.show({
        templateUrl: 'partials/comment-buttons.html',
        controller: 'CommentButtonController'
      }).then(function (clickedItem) {
        $scope.alert = clickedItem.name + ' clicked!';
      });
    }
  }])

  .controller('CommentButtonController', ['$scope', '$mdBottomSheet', function ($scope, $mdBottomSheet) {

  }]);
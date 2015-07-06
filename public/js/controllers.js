'use strict';

/* Controllers */

angular.module('modtools.controllers', ['ngMaterial'])

  .controller('SidenavController', ['$scope', '$location', '$mdSidenav', function ($scope, $location, $mdSidenav) {

    $scope.queues = [
      'Central',
      'Central (watched)',
      'Comment is Free',
      'CiF (watched)'
    ];

    $scope.menu = function () {
      $mdSidenav('left').toggle();
    };
    $scope.goto = function(queue, event) {
      console.log(queue);
      console.log(event);
      $location.path('/queue/' + queue)
    };
  }])

  .controller('HomeController', ['$scope', function ($scope) {


  }])

  .controller('QueueController', ['$scope', '$routeParams', '$mdBottomSheet', 'Moderation', function ($scope, $routeParams, $mdBottomSheet, Moderation) {

    $scope.queue = $routeParams.queue;
    $scope.comment = undefined;

    // get next comment and display it for moderation

    var next = function () {
      Moderation.next({queue: $scope.queue}, function (response) {
        console.log(response);
        $scope.comment = response.data;
      })
    };
    next();

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
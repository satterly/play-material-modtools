'use strict';

/* Controllers */

angular.module('modtools.controllers', ['ngMaterial', 'ngSanitize'])

  .controller('SidenavController', ['$scope', '$location', '$mdSidenav', 'Moderation',
    function ($scope, $location, $mdSidenav, Moderation) {

    var refresh = function() {
      Moderation.queues({}, function (response) {
        $scope.queues = response.data;
        console.log(response);
      });
    };
    refresh();

    $scope.menu = function () {
      $mdSidenav('left').toggle();
    };
    $scope.goto = function(queue, event) {
      console.log(queue);
      // console.log(event);
      $location.path('/queues/' + queue)
    };
  }])

  .controller('HomeController', ['$scope', function ($scope) {

  }])

  .controller('QueueController', ['$scope', '$routeParams', '$mdBottomSheet', 'Moderation', 'Comment', 'Content',
    function ($scope, $routeParams, $mdBottomSheet, Moderation, Comment, Content) {

    $scope.queue = $routeParams.queue;
    $scope.comment = undefined;

    console.log('qc'+$scope.queue);

    // get next comment and display it for moderation

    var next = function () {
      Moderation.next({queue: $scope.queue}, function (response) {
        console.log(response);
        $scope.request = response.data;
        Content.lookup({path: $scope.comment.discussion.key.replace("/p/","")}, function (response) {
          $scope.content = response.response.content;
        });
      });
    };
    next();
//
//    $scope.$watch('comment', function(current, old){
//      if (!angular.isDefined(current)) {
//        $scope.showButtons();
//      }
//      //console.log('---start----');
//      //console.log(current);
//      //console.log(old);
//      //console.log('---end----');
//    });
//
//    $scope.showButtons = function ($event) {
//      $mdBottomSheet.show({
//        scope: $scope,
//        preserveScope: true,
//        targetEvent: $event,
//        templateUrl: 'partials/comment-buttons.html',
//        controller: function ($scope, $mdBottomSheet) {
//          $scope.approve = function () {
//            // console.log($scope.comment.id);
//            Comment.approve({id: $scope.comment.id}, {}, function (response) {
//              console.log(response);
//              // Moderation.action({...})
//              console.log($scope.request.next);
//              Moderation.delete({id: $scope.request.next.requestId}, function(response) {
//                console.log('deleted ' + $scope.request.next.requestId);
//              });
//              next();
//            });
//          };
//          $scope.pick = function () {
//            Comment.pick({id: $scope.comment.id}, {}, function (response) {
//              console.log(response);
//              // Moderation.action({...})
//              Moderation.delete({id: $scope.request.next.requestId}, function(response) {
//                console.log('deleted ' + $scope.request.next.requestId);
//              });
//              next();
//            });
//          };
//          $scope.block = function () {
//            Comment.block({id: $scope.comment.id}, {}, function (response) {
//              console.log(response);
//              // Moderation.action({...})
//              Moderation.delete({id: $scope.request.next.requestId}, function(response) {
//                console.log('deleted ' + $scope.request.next.requestId);
//              });
//              next();
//            });
//          };
//          $scope.remove = function () {
//            Comment.remove({id: $scope.comment.id}, {}, function (response) {
//              console.log(response);
//              // Moderation.action({...})
//              Moderation.delete({id: $scope.request.next.requestId}, function(response) {
//                console.log('deleted ' + $scope.request.next.requestId);
//              });
//              next();
//            });
//          };
//
//          console.log($scope);
//          console.log($mdBottomSheet);
//        }
//      }).then(function (item) {
//        console.log('then');
//        console.log(item);
//      });
//    }
  }]);

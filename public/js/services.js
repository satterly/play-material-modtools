'use strict';

/* Services */

angular.module('modtools.services', ['config', 'ngResource'])

  .factory('Moderation', ['$resource', 'config', function ($resource, config) {
    return $resource(config.moderationApiUrl + '/comment', {}, {
      'next': {method: 'GET', url: config.moderationApiUrl + '/queues/:queue/next'},
      'moderate': {method: 'POST', url: config.moderationApiUrl + '/comment/:commentId/status'}
    })
  }]);
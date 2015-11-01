'use strict';

/* Services */

angular.module('modtools.services', ['config', 'ngResource'])

  .factory('Moderation', ['$resource', 'config', function ($resource, config) {
    return $resource('/comment', {}, {
      'queues':   {method:'GET', url:'/queues'},
      'next':     {method:'GET', url:'/queues/:queue/next'}
    })
  }])

  .factory('Comment', ['$resource', 'config', function ($resource, config) {
    return $resource('/comment', {}, {
      'queues':   {method:'GET', url:'/queues'},
      'next':     {method:'POST', url:'/queues/:queue/next'}
    })
  }])

  .factory('Content', ['$resource', 'config', function ($resource, config) {
    return $resource(config.contentApiUrl, {'orderBy':'newest', 'page-size':100, 'api-key':config.contentApiKey}, {
      'query':    {method:'GET', url: config.contentApiUrl+'/search'},
      'lookup':   {method:'GET', params: {'show-fields': 'all'}, url:config.contentApiUrl+'/p/:path'}
    })
  }]);

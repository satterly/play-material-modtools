'use strict';

angular.module('config', [])
  .constant('config', {
    'moderationApiUrl': "",  // served from same domain (no CORS)
    'discussionApiUrl': "http://discussion.code.dev-guardianapis.com/discussion-api",
    'identityApiUrl'  : "https://id.guardianapis.com",
    'contentApiUrl'   : "http://content.guardianapis.com",
    'contentApiKey'   : "r9fv4d8mpzngbvat2rkaeuez"
  });

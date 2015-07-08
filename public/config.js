'use strict';

angular.module('config', [])
  .constant('config', {
    'moderationApiUrl': "",  // served from same domain (no CORS)
    'discussionApiUrl': "http://localhost:8900/discussion-api",
    'identityApiUrl'  : "https://id.guardianapis.com",
    'contentApiUrl'   : "http://content.guardianapis.com",
    'contentApiKey'   : "r9fv4d8mpzngbvat2rkaeuez"
  });

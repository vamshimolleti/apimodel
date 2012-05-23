* tmp

    Name = tmp
    BasePath = http://api.twitter.com/1

    resource statuses/public_timeline.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid rss (application/rss+xml)
          valid atom (application/atom+xml)
    

Returns the 20 most recent statuses,
					 including retweets if they exist, from non-protected users.

[http://dev.twitter.com/doc/get/statuses/public_timeline](http://dev.twitter.com/doc/get/statuses/public_timeline)

      operation GET
    
    resource statuses/home_timeline.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid atom (application/atom+xml)
        queryParam count (string, optional, default="5")
        queryParam page (string, optional, default="2")
        queryParam max_id (string, optional, default="54321")
        queryParam since_id (string, optional, default="12345")
        queryParam skip_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
    

Returns the 20 most recent statuses, including retweets if they exist,
					posted by the authenticating user and the user's they follow.

[http://dev.twitter.com/doc/get/statuses/home_timeline](http://dev.twitter.com/doc/get/statuses/home_timeline)

      operation GET
    
    resource statuses/user_timeline.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid rss (application/rss+xml)
          valid atom (application/atom+xml)
        queryParam count (string, optional, default="200")
        queryParam user_id (string, optional, default="10330576396")
        queryParam screen_name (string, optional, default="apigee")
        queryParam since_id (string, optional, default="12345")
        queryParam page (string, optional, default="2")
        queryParam max_id (string, optional, default="54321")
        queryParam skip_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
        queryParam include_rts (string, optional, default="true")
    

Returns the 20 most recent statuses posted by the authenticating user.
					It is also possible to request another user's timeline by using the screen_name or user_id parameter.

[http://dev.twitter.com/doc/get/statuses/user_timeline](http://dev.twitter.com/doc/get/statuses/user_timeline)

      operation GET
    
    resource statuses/mentions.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid rss (application/rss+xml)
          valid atom (application/atom+xml)
        queryParam count (string, optional, default="200")
        queryParam max_id (string, optional, default="54321")
        queryParam page (string, optional, default="2")
        queryParam since_id (string, optional, default="12345")
        queryParam include_entities (string, optional, default="true")
        queryParam include_rts (string, optional, default="true")
        queryParam skip_user (string, optional, default="true")
    

Returns the 20 most recent mentions (status containing @username) for the authenticating user.

[http://dev.twitter.com/doc/get/statuses/mentions](http://dev.twitter.com/doc/get/statuses/mentions)

      operation GET
    
    resource statuses/retweeted_by_me.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid atom (application/atom+xml)
        queryParam count (string, optional, default="200")
        queryParam page (string, optional, default="2")
        queryParam max_id (string, optional, default="54321")
        queryParam since_id (string, optional, default="12345")
    

Returns the 20 most recent retweets posted by the authenticating user.

[http://dev.twitter.com/doc/get/statuses/retweeted_by_me](http://dev.twitter.com/doc/get/statuses/retweeted_by_me)

      operation GET
    
    resource statuses/retweeted_to_me.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid atom (application/atom+xml)
        queryParam count (string, optional, default="200")
        queryParam page (string, optional, default="2")
        queryParam max_id (string, optional, default="54321")
        queryParam since_id (string, optional, default="12345")
    

Returns the 20 most recent retweets posted by the authenticating user's friends.

[http://dev.twitter.com/doc/get/statuses/retweeted_to_me](http://dev.twitter.com/doc/get/statuses/retweeted_to_me)

      operation GET
    
    resource statuses/retweets_of_me.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid atom (application/atom+xml)
        queryParam count (string, optional, default="200")
        queryParam page (string, optional, default="2")
        queryParam max_id (string, optional, default="54321")
        queryParam since_id (string, optional, default="12345")
    

Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.

[http://dev.twitter.com/doc/get/statuses/retweets_of_me](http://dev.twitter.com/doc/get/statuses/retweets_of_me)

      operation GET
    
    resource statuses/{id}/retweeted_by.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="12164581432")
        queryParam trim_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
        queryParam count (string, optional, default="200")
        queryParam page (string, optional, default="2")
    

Show user objects of up to 100 members who retweeted the status.

[http://dev.twitter.com/doc/get/statuses/:id/retweeted_by](http://dev.twitter.com/doc/get/statuses/:id/retweeted_by)

      operation GET
    
    resource statuses/{id}/retweeted_by/ids.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="12164581432")
        queryParam count (string, optional, default="200")
        queryParam page (string, optional, default="2")
        queryParam trim_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
    

Show user ids of up to 100 users who retweeted the status.

[http://dev.twitter.com/doc/get/statuses/:id/retweeted_by/ids](http://dev.twitter.com/doc/get/statuses/:id/retweeted_by/ids)

      operation GET
    
    resource statuses/show/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="10279582992")
        queryParam trim_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
    

Returns a single status, specified by the id parameter below. The status's author will be
 returned inline.

[http://dev.twitter.com/doc/get/statuses/show/:id](http://dev.twitter.com/doc/get/statuses/show/:id)

      operation GET
    
    resource statuses/update.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam status (string, required, default="Posting from @apigee's API test console. It's like a command line for the Twitter API! ")
        queryParam place_id (string, optional, default="7695dd2ec2f86f2b")
        queryParam long (string, optional, default="-122.40060")
        queryParam in_reply_to_status_id (string, optional, default="11432445742")
        queryParam display_coordinates (string, optional, default="false")
        queryParam lat (string, optional, default="37.78215")
        queryParam trim_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
        queryParam annotations (string, optional, default="[{'TYPE':{'ATTRIBUTE':'VALUE'}}]")
    

Updates the authenticating user's status. Requires the status parameter specified below. Request
 must be a POST. A status update with text identical to the authenticating user's current status will
 be ignored to prevent duplicates.

[http://dev.twitter.com/doc/post/statuses/update](http://dev.twitter.com/doc/post/statuses/update)

      operation POST
    
    resource statuses/destroy/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="1472669360")
        queryParam trim_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
    

Destroys the status specified by the required ID parameter. The authenticating user must be the
 author of the specified status.

[http://dev.twitter.com/doc/post/statuses/destroy/:id](http://dev.twitter.com/doc/post/statuses/destroy/:id)

      operation POST
    
    resource statuses/retweet/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="10279582992")
        queryParam trim_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
    

Retweets a tweet. Requires the id parameter of the tweet you are retweeting. Returns the original tweet with retweet details embedded.

[http://dev.twitter.com/doc/post/statuses/retweet/:id](http://dev.twitter.com/doc/post/statuses/retweet/:id)

      operation POST
    
    resource statuses/retweets/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="11432445742")
        queryParam count (string, optional, default="100")
        queryParam trim_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
    

Returns up to 100 of the first retweets of a given tweet.

[http://dev.twitter.com/doc/get/statuses/retweets/:id](http://dev.twitter.com/doc/get/statuses/retweets/:id)

      operation GET
    
    resource users/show.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam include_entities (string, optional, default="true")
        queryParam user_id (, required)
    

Returns extended information of a given user, specified by ID or screen name as per the required
 id parameter. The author's most recent status will be returned inline.

[http://dev.twitter.com/doc/get/users/show](http://dev.twitter.com/doc/get/users/show)

      operation GET
    
    resource users/lookup.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam include_entities (string, optional, default="true")
        queryParam user_id (, required)
    

Return up to 100 users worth of extended information, specified by either ID, screen name, or
 combination of the two. The author's most recent status (if the authenticating user has permission)
 will be returned inline.

[http://dev.twitter.com/doc/get/users/lookup](http://dev.twitter.com/doc/get/users/lookup)

      operation GET
    
    resource users/search.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam q (string, required, default="Marsh Gardiner")
        queryParam per_page (string, optional, default="20")
        queryParam page (string, optional, default="2")
        queryParam include_entities (string, optional, default="true")
    

Runs a search for users similar to Find People button on Twitter.com.
 The results returned by people search on Twitter.com are the same as those returned by this API request. Only the first 1000 matches are available.

[http://dev.twitter.com/doc/get/users/search](http://dev.twitter.com/doc/get/users/search)

      operation GET
    
    resource users/suggestions.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
    

Access to Twitter's suggested user list. This returns the list of suggested user categories.
 The category can be used in the users/suggestions/category endpoint to get the users in that category.

[http://dev.twitter.com/doc/get/users/suggestions](http://dev.twitter.com/doc/get/users/suggestions)

      operation GET
    
    resource users/suggestions/{slug}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        templateParam slug (string, required, default="twitter")
          valid art-design
          valid books
          valid business
          valid charity
          valid cuisine
          valid entertainment
          valid family
          valid fashion
          valid funny
          valid health
          valid music
          valid news
          valid politics
          valid science
          valid sports
          valid staff-picks
          valid staff-picks-for-haiti
          valid technology
          valid travel
          valid twitter
    

Access the users in a given category of the Twitter suggested user list. It is recommended that
 end clients cache this data for no more than one hour.

[http://dev.twitter.com/doc/get/users/suggestions/:slug](http://dev.twitter.com/doc/get/users/suggestions/:slug)

      operation GET
    
    resource users/profile_image/{screen_name}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam screen_name (string, required, default="apigee")
        queryParam size (string, optional, default="bigger")
    

Access the profile image in various sizes for the user with the indicated screen_name.
 If no size is provided the normal image is returned.

[http://dev.twitter.com/doc/get/users/profile_image/:screen_name](http://dev.twitter.com/doc/get/users/profile_image/:screen_name)

      operation GET
    
    resource direct_messages.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid rss (application/rss+xml)
          valid atom (application/atom+xml)
        queryParam count (string, optional, default="200")
        queryParam max_id (string, optional, default="54321")
        queryParam page (string, optional, default="2")
        queryParam since_id (string, optional, default="12345")
    

Returns a list of the 20 most recent direct messages sent to the authenticating user. The XML and
 JSON versions include detailed information about the sending and recipient users.

[http://dev.twitter.com/doc/get/direct_messages](http://dev.twitter.com/doc/get/direct_messages)

      operation GET
    
    resource direct_messages/sent.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid rss (application/rss+xml)
          valid atom (application/atom+xml)
        queryParam count (string, optional, default="200")
        queryParam max_id (string, optional, default="54321")
        queryParam page (string, optional, default="2")
        queryParam since_id (string, optional, default="12345")
    

Returns a list of the 20 most recent direct messages sent by the authenticating user. The XML and
 JSON versions include detailed information about the sending and recipient users.

[http://dev.twitter.com/doc/get/direct_messages/sent](http://dev.twitter.com/doc/get/direct_messages/sent)

      operation GET
    
    resource direct_messages/new.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam text (string, required, default="Check it out... I'm sending this direct message from Apigee's test console for Twitter!")
        queryParam screen_name (, required)
    

Sends a new direct message to the specified user from the authenticating user. Requires both the
 user and text parameters. Request must be a POST. Returns the sent message in the requested format
 when successful.

[http://dev.twitter.com/doc/post/direct_messages/new](http://dev.twitter.com/doc/post/direct_messages/new)

      operation POST
    
    resource direct_messages/destroy/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="12345")
    

Destroys the direct message specified in the required ID parameter. The authenticating user must
 be the recipient of the specified direct message.

[http://dev.twitter.com/doc/post/direct_messages/destroy](http://dev.twitter.com/doc/post/direct_messages/destroy)

      operation DELETE
    
    resource friendships/create.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
        queryParam follow (string, optional, default="true")
    

Allows the authenticating users to follow the user specified in the ID parameter. Returns the
 befriended user in the requested format when successful. Returns a string describing the failure
 condition when unsuccessful. If you are already friends with the user an HTTP 403 will be returned.

[http://dev.twitter.com/doc/post/friendships/create](http://dev.twitter.com/doc/post/friendships/create)

      operation POST
    
    resource friendships/destroy.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
    

Allows the authenticating users to unfollow the user specified in the ID parameter. Returns the
 unfollowed user in the requested format when successful. Returns a string describing the failure
 condition when unsuccessful.

[http://dev.twitter.com/doc/post/friendships/destroy](http://dev.twitter.com/doc/post/friendships/destroy)

      operation DELETE
    
    resource friendships/exists.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_b (string, required, default="apigee")
        queryParam user_a (string, required, default="sonoa")
    

Tests for the existence of friendship between two users. Will return true if user_a follows
 user_b, otherwise will return false. Also try the friendship/show method, which gives even more
 information with a single call!

[http://dev.twitter.com/doc/get/friendships/exists](http://dev.twitter.com/doc/get/friendships/exists)

      operation GET
    
    resource friendships/show.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam source_screen_name (, required)
        queryParam target_screen_name (, required)
    

Returns detailed information about the relationship between two users.

[http://dev.twitter.com/doc/get/friendships/incoming](http://dev.twitter.com/doc/get/friendships/incoming)

      operation GET
    
    resource friendships/incoming.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam cursor (string, required, default="-1")
    

Returns an array of numeric IDs for every user who has a pending request to follow the
 authenticating user.

[http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships-incoming](http://apiwiki.twitter.com/Twitter-REST-API-Method:-friendships-incoming)

      operation GET
    
    resource friendships/outgoing.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam cursor (string, required, default="-1")
    

Returns an array of numeric IDs for every user who has a pending request to follow the
 authenticating user.

[http://dev.twitter.com/doc/get/friendships/outgoing](http://dev.twitter.com/doc/get/friendships/outgoing)

      operation GET
    
    resource friends/ids.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
        queryParam cursor (string, required, default="-1")
    

Returns an array of numeric IDs for every user the specified user is following.

[http://dev.twitter.com/doc/get/friends/ids](http://dev.twitter.com/doc/get/friends/ids)

      operation GET
    
    resource followers/ids.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
        queryParam cursor (string, required, default="-1")
    

Returns an array of numeric IDs for every user following the specified user.

[http://dev.twitter.com/doc/get/followers/ids](http://dev.twitter.com/doc/get/followers/ids)

      operation GET
    
    resource account/verify_credentials.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
    

Returns an HTTP 200 OK response code and a representation of the requesting user if
 authentication was successful; returns a 401 status code and an error message if not. Use this
 method to test if supplied user credentials are valid.

[http://dev.twitter.com/doc/get/account/verify_credentials](http://dev.twitter.com/doc/get/account/verify_credentials)

      operation GET
    
    resource account/rate_limit_status.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
    

Returns the remaining number of API requests available to the requesting user before the API
 limit is reached for the current hour. Calls to rate_limit_status do not count against the rate
 limit. If authentication credentials are provided, the rate limit status for the authenticating user
 is returned. Otherwise, the rate limit status for the requester's IP address is returned. Learn more
 about the<a xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" href="http://apiwiki.twitter.com/Rate-limiting">REST API rate limiting</a>.

[http://dev.twitter.com/doc/get/account/rate_limit_status](http://dev.twitter.com/doc/get/account/rate_limit_status)

      operation GET
    
    resource lists.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
        queryParam cursor (string, optional, default="12893764510938")
    

Returns the lists of the specified (or authenticated) user.
 Private lists will be included if the authenticated user is the same as the user whose lists are being returned.

[http://dev.twitter.com/doc/get/lists](http://dev.twitter.com/doc/get/lists)

      operation GET
    
    resource lists/memberships.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
        queryParam cursor (string, optional, default="12893764510938")
        queryParam filter_to_owned_lists (string, optional, default="true")
    

Returns the lists the specified (or authenticated) user has been added to.

[http://dev.twitter.com/doc/get/lists/memberships](http://dev.twitter.com/doc/get/lists/memberships)

      operation GET
    
    resource lists/subscriptions.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
        queryParam cursor (string, optional, default="12893764510938")
    

Returns the lists the specified (or authenticated) user follows.

[http://dev.twitter.com/doc/get/lists/subscriptions](http://dev.twitter.com/doc/get/lists/subscriptions)

      operation GET
    
    resource lists/show.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam slug (string, required, default="team")
        queryParam cursor (string, optional, default="12893764510938")
    

Returns the specified list. Private lists will only be shown if the authenticated user owns the specified list.

[http://dev.twitter.com/doc/get/lists/show](http://dev.twitter.com/doc/get/lists/show)

      operation GET
    
    resource lists/statuses.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid atom (application/atom+xml)
        queryParam slug (string, required, default="team")
        queryParam list_id (string, required, default="2031945")
        queryParam owner_screen_name (string, optional, default="twitterapi")
        queryParam count (string, optional, default="5")
        queryParam page (string, optional, default="2")
        queryParam max_id (string, optional, default="54321")
        queryParam since_id (string, optional, default="12345")
        queryParam trim_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
        queryParam include_rts (string, optional, default="true")
    

Returns tweet timeline for members of the specified list.

[http://dev.twitter.com/doc/get/:user/lists](http://dev.twitter.com/doc/get/:user/lists)

      operation GET
    
    resource lists/create.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_name (string, required, default="apigee")
        queryParam mode (string, optional, default="public")
        queryParam description (string, optional, default="")
    

Creates a new list for the authenticated user. Note that you can't create more than 20 lists per account.

[http://dev.twitter.com/doc/post/lists/create](http://dev.twitter.com/doc/post/lists/create)

      operation POST
    
    resource lists/update.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam slug (string, required, default="team")
    

Updates the specified list. The authenticated user must own the list to be able to update it.

[http://dev.twitter.com/doc/post/lists/update](http://dev.twitter.com/doc/post/lists/update)

      operation POST
    
    resource lists/destroy.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam slug (string, required, default="team")
    

Deletes the specified list. The authenticated user must own the list to be able to destroy it.

[http://dev.twitter.com/doc/post/lists/destroy](http://dev.twitter.com/doc/post/lists/destroy)

      operation POST
    
    resource lists/members.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam include_entities (string, optional, default="true")
        queryParam slug (string, required, default="team")
        queryParam cursor (string, optional, default="-1")
    

Returns the members of the specified list.
 Private list members will only be shown if the authenticated user owns the specified list.

[http://dev.twitter.com/doc/get/lists/members](http://dev.twitter.com/doc/get/lists/members)

      operation GET
    
    resource lists/members/show.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam include_entities (string, optional, default="true")
        queryParam slug (string, required, default="team")
        queryParam cursor (string, optional, default="-1")
    

Check if the specified user is a member of the specified list.

[http://dev.twitter.com/doc/get/lists/members](http://dev.twitter.com/doc/get/lists/members)

      operation GET
    
    resource lists/members/create.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam slug (string, required, default="team")
        queryParam cursor (string, optional, default="-1")
    

Add a member to a list. The authenticated user must own the list to be able to add members to it.

[http://dev.twitter.com/doc/post/lists/members/create](http://dev.twitter.com/doc/post/lists/members/create)

      operation POST
    
    resource lists/members/create_all.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam slug (string, required, default="team")
    

Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names.
 The authenticated user must own the list to be able to add members to it.

[http://dev.twitter.com/doc/post/lists/members/create](http://dev.twitter.com/doc/post/lists/members/create)

      operation POST
    
    resource lists/members/destroy.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam slug (string, required, default="team")
    

Removes the specified member from the list.
 The authenticated user must be the list's owner to remove members from the list.

[http://dev.twitter.com/doc/post/lists/members/destroy](http://dev.twitter.com/doc/post/lists/members/destroy)

      operation POST
    
    resource lists/subscribers.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam slug (string, required, default="team")
        queryParam cursor (string, optional, default="-1")
    

Returns the subscribers of the specified list.
 Private list subscribers will only be shown if the authenticated user owns the specified list.

[http://dev.twitter.com/doc/get/lists/subscribers](http://dev.twitter.com/doc/get/lists/subscribers)

      operation GET
    
    resource lists/subscribers/show.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam include_entities (string, optional, default="true")
        queryParam slug (string, required, default="team")
        queryParam cursor (string, optional, default="-1")
    

Check if the specified user is a subscriber of the specified list.

[http://dev.twitter.com/doc/get/lists/subscribers/show](http://dev.twitter.com/doc/get/lists/subscribers/show)

      operation GET
    
    resource lists/subscribers/create.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam slug (string, required, default="team")
        queryParam cursor (string, optional, default="-1")
    

Subscribes the authenticated user to the specified list.

[http://dev.twitter.com/doc/post/lists/subscribers/create](http://dev.twitter.com/doc/post/lists/subscribers/create)

      operation POST
    
    resource lists/subscribers/destroy.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (, required)
        queryParam slug (string, required, default="team")
        queryParam cursor (string, optional, default="-1")
    

Unsubscribes the authenticated user from the specified list.

[http://dev.twitter.com/doc/post/lists/subscribers/destroy](http://dev.twitter.com/doc/post/lists/subscribers/destroy)

      operation POST
    
    resource account/update_profile_colors.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam profile_text_color (, required, repeating)
    

Sets one or more hex values that control the color scheme of the authenticating user's profile
 page on twitter.com.

[http://dev.twitter.com/doc/post/account/update_profile_colors](http://dev.twitter.com/doc/post/account/update_profile_colors)

      operation POST
    
    resource account/update_profile_image.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        requestHeader image (string, required, default="")
    

Updates the authenticating user's profile image. Note that this method expects raw multipart
 data, not a URL to an image. Must be a valid GIF, JPG, or PNG image of less than 700 kilobytes in
 size. Images with width larger than 500 pixels will be scaled down.

[http://dev.twitter.com/doc/post/account/update_profile_image](http://dev.twitter.com/doc/post/account/update_profile_image)

      operation POST
    
    resource account/update_profile_background_image.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam tile (string, optional, default="false")
        requestHeader image (string, required)
    

Updates the authenticating user's profile background image. Note that this method expects raw
 multipart data, not a URL to an image. Must be a valid GIF, JPG, or PNG image of less than 800
 kilobytes in size. Images with width larger than 2048 pixels will be forceably scaled down.

[http://dev.twitter.com/doc/post/account/update_profile_background_image](http://dev.twitter.com/doc/post/account/update_profile_background_image)

      operation POST
    
    resource account/update_profile.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam url (, required, repeating)
    

Sets values that users are able to set under the "Account" tab of their settings page. Only the
 parameters specified will be updated.

[http://dev.twitter.com/doc/post/account/update_profile](http://dev.twitter.com/doc/post/account/update_profile)

      operation POST
    
    resource favorites.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid rss (application/rss+xml)
          valid atom (application/atom+xml)
        queryParam id (string, optional, default="apigee")
        queryParam page (string, optional, default="2")
    

Returns the 20 most recent favorite statuses for the authenticating user or user specified by the
 ID parameter in the requested format.

[http://dev.twitter.com/doc/get/favorites](http://dev.twitter.com/doc/get/favorites)

      operation GET
    
    resource favorites/create/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="11432445742")
    

Favorites the status specified in the ID parameter as the authenticating user. Returns the
 favorite status when successful.

[http://dev.twitter.com/doc/post/favorites/create](http://dev.twitter.com/doc/post/favorites/create)

      operation POST
    
    resource favorites/destroy/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="11432445742")
    

Un-favorites the status specified in the ID parameter as the authenticating user. Returns the
 un-favorited status in the requested format when successful.

[http://dev.twitter.com/doc/post/favorites/destroy](http://dev.twitter.com/doc/post/favorites/destroy)

      operation DELETE
    
    resource notifications/follow.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
    

Enables device notifications for updates from the specified user. Returns the specified user when
 successful.

[http://dev.twitter.com/doc/post/notifications/follow](http://dev.twitter.com/doc/post/notifications/follow)

      operation POST
    
    resource notifications/leave.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
    

Disables notifications for updates from the specified user to the authenticating user.
 Returns the specified user when successful.

[http://dev.twitter.com/doc/post/notifications/leave](http://dev.twitter.com/doc/post/notifications/leave)

      operation POST
    
    resource blocks/create.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
    

Blocks the user specified in the ID parameter as the authenticating user. Destroys a friendship
 to the blocked user if it exists. Returns the blocked user in the requested format when successful.
 You can find out more about blocking in the<a xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" href="http://help.twitter.com/forums/10711/entries/15355">Twitter Support Knowledge Base</a>
 .

[http://dev.twitter.com/doc/post/blocks/create](http://dev.twitter.com/doc/post/blocks/create)

      operation POST
    
    resource blocks/destroy.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
    

Un-blocks the user specified in the ID parameter for the authenticating user. Returns the
 un-blocked user in the requested format when successful.

[http://dev.twitter.com/doc/post/blocks/destroy](http://dev.twitter.com/doc/post/blocks/destroy)

      operation DELETE
    
    resource blocks/exists.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
    

Returns if the authenticating user is blocking a target user. Will return the blocked user's
 object if a block exists, and error with a HTTP 404 response code otherwise.

[http://dev.twitter.com/doc/get/blocks/exists](http://dev.twitter.com/doc/get/blocks/exists)

      operation GET
    
    resource blocks/blocking.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam page (string, optional, default="2")
    

Returns an array of user objects that the authenticating user is blocking.

[http://dev.twitter.com/doc/get/blocks/blocking](http://dev.twitter.com/doc/get/blocks/blocking)

      operation GET
    
    resource blocks/blocking/ids.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
    

Returns an array of numeric user ids the authenticating user is blocking.

[http://dev.twitter.com/doc/get/blocks/blocking/ids](http://dev.twitter.com/doc/get/blocks/blocking/ids)

      operation GET
    
    resource report_spam.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam user_id (, required)
    

The user specified in the id is blocked by the authenticated user and reported as a spammer.

[http://dev.twitter.com/doc/post/report_spam](http://dev.twitter.com/doc/post/report_spam)

      operation POST
    
    resource saved_searches.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
    

Returns the authenticated user's saved search queries.

[http://dev.twitter.com/doc/get/saved_searches](http://dev.twitter.com/doc/get/saved_searches)

      operation GET
    
    resource saved_searches/show/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="313006")
    

Retrieve the data for a saved search owned by the authenticating user specified by the given id.

[http://dev.twitter.com/doc/get/saved_searches/show](http://dev.twitter.com/doc/get/saved_searches/show)

      operation GET
    
    resource saved_searches/create.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam query (string, required, default="from:apigee OR from:sonoa")
    

Creates a saved search for the authenticated user.

[http://dev.twitter.com/doc/post/saved_searches/create](http://dev.twitter.com/doc/post/saved_searches/create)

      operation POST
    
    resource saved_searches/destroy/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="12345")
    

Destroys a saved search for the authenticated user. The search specified by id must be owned by
 the authenticating user.

[http://dev.twitter.com/doc/post/saved_searches/destroy](http://dev.twitter.com/doc/post/saved_searches/destroy)

      operation DELETE
    
    resource trends/available.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam lat (, required, repeating)
    

Returns the locations that Twitter has trending topic information for. The response is an array of "locations" that encode the location's WOEID and some other human-readable
 information such as a canonical name and country the location belongs in.

[http://dev.twitter.com/doc/get/trends/available](http://dev.twitter.com/doc/get/trends/available)

      operation GET
    
    resource trends/{woeid}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam woeid (string, required, default="2487956")
    

Returns the top 10 trending topics for a specific location Twitter has trending topic information
 for. The response is an array of "trend" objects that encode the name of the trending topic, the
 query parameter that can be used to search for the topic on Search, and the direct URL that can be
 issued against Search. This information is cached for five minutes, and therefore users are
 discouraged from querying these endpoints faster than once every five minutes.

[https://dev.twitter.com/docs/api/1/get/trends/%3Awoeid](https://dev.twitter.com/docs/api/1/get/trends/%3Awoeid)

      operation GET
    
    resource geo/search.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
        queryParam ip (string, optional, default="")
        queryParam long (string, optional, default="")
        queryParam max_results (string, optional, default="")
        queryParam granularity (string, optional, default="")
        queryParam lat (string, optional, default="")
        queryParam accuracy (string, optional, default="city")
    

Search for places that can be attached to a statuses/update.
 Given a latitude and a longitude pair, an IP address, or a name, this request will return a list of all the valid places that can be used as the place_id when updating a status.

[http://dev.twitter.com/doc/get/geo/search](http://dev.twitter.com/doc/get/geo/search)

      operation GET
    
    resource geo/similar_places.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
        queryParam long (string, required, default="")
        queryParam lat (string, required, default="")
        queryParam name (string, required, default="")
        queryParam attribute:street_address (string, optional, default="")
        queryParam callback (string, optional, default="")
    

Locates places near the given coordinates which are similar in name.

[http://dev.twitter.com/doc/get/geo/similar_places](http://dev.twitter.com/doc/get/geo/similar_places)

      operation GET
    
    resource geo/reverse_geocode.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
        queryParam lat (string, required, default="37.78215")
        queryParam max_results (string, optional, default="20")
        queryParam granularity (string, optional, default="city")
        queryParam long (string, required, default="-122.40060")
        queryParam accuracy (string, optional, default="500")
    

Given a latitude and a longitude, searches for up to 20 places that can be used as a place_id when updating a status.

[http://dev.twitter.com/doc/get/geo/reverse_geocode](http://dev.twitter.com/doc/get/geo/reverse_geocode)

      operation GET
    
    resource geo/id/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
        queryParam id (string, required, default="7695dd2ec2f86f2b")
    

Find out more details of a place that was returned from the geo/reverse_geocode method.

[http://dev.twitter.com/doc/get/geo/id](http://dev.twitter.com/doc/get/geo/id)

      operation GET
    
    resource geo/place.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
        queryParam name (string, required, default="7695dd2ec2f86f2b")
        queryParam lat (string, required, default="37.78215")
        queryParam token (string, required, default="36179c9bf78835898ebf521c1defd4be")
        queryParam contained_within (string, required, default="247f43d441defc03")
        queryParam long (string, required, default="-122.40060")
        queryParam attribute:street_address (string, optional, default="")
        queryParam callback (string, optional, default="")
    

Creates a new place at the given latitude and longitude.

[http://dev.twitter.com/doc/get/geo/id](http://dev.twitter.com/doc/get/geo/id)

      operation POST
    
    resource help/test.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
    

Returns the string "ok" in the requested format with a 200 OK HTTP status code.

[http://dev.twitter.com/doc/get/help/test](http://dev.twitter.com/doc/get/help/test)

      operation GET
    
    resource legal/tos.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
    

Returns Twitter's' Terms of Service in the requested format.
 These are not the same as the Developer Terms of Service.

[http://dev.twitter.com/doc/get/legal/tos](http://dev.twitter.com/doc/get/legal/tos)

      operation GET
    
    resource legal/privacy.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
    

Returns Twitter's Privacy Policy in the requested format.

[http://dev.twitter.com/doc/get/legal/tos](http://dev.twitter.com/doc/get/legal/tos)

      operation GET
    
    resource trends.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
    

Returns the top ten topics that are currently trending on Twitter. The response includes the time
 of the request, the name of each trend, and the url to the Twitter Search results page for that
 topic.

[http://dev.twitter.com/doc/get/trends](http://dev.twitter.com/doc/get/trends)

      operation GET
    
    resource trends/current.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
        queryParam exclude (string, optional, default="hashtags")
    

Returns the current top 10 trending topics on Twitter. The response includes the time of the
 request, the name of each trending topic, and query used on Twitter Search results page for that
 topic.

[http://dev.twitter.com/doc/get/trends/current](http://dev.twitter.com/doc/get/trends/current)

      operation GET
    
    resource trends/daily.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
        queryParam exclude (string, optional, default="hashtags")
        queryParam date (string, optional, default="2009-03-19")
    

Returns the top 20 trending topics for each hour in a given day.

[http://dev.twitter.com/doc/get/trends/daily](http://dev.twitter.com/doc/get/trends/daily)

      operation GET
    
    resource trends/weekly.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
        queryParam exclude (string, optional, default="hashtags")
        queryParam date (string, optional, default="2009-03-19")
    

Returns the top 30 trending topics for each day in a given week.

[http://dev.twitter.com/doc/get/trends/weekly](http://dev.twitter.com/doc/get/trends/weekly)

      operation GET
    
    resource statuses/friends_timeline.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid rss (application/rss+xml)
          valid atom (application/atom+xml)
        queryParam count (string, optional, default="5")
        queryParam page (string, optional, default="2")
        queryParam max_id (string, optional, default="54321")
        queryParam since_id (string, optional, default="12345")
        queryParam skip_user (string, optional, default="true")
        queryParam include_entities (string, optional, default="true")
        queryParam include_rts (string, optional, default="true")
    

<p xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">Returns the 20 most recent statuses posted by the authenticating user and that user's friends.
 This is the equivalent of /timeline/home on the Web.
 </p>
 <p xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
 <b>Note:</b>
 Retweets will not appear in the friends_timeline for backwards compatibility. If you want
 retweets included use Twitter REST API Method: statuses home_timeline.
 </p>

[http://dev.twitter.com/doc/get/statuses/friends_timeline](http://dev.twitter.com/doc/get/statuses/friends_timeline)

      operation GET
    
    resource {user}/lists.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam description (string, optional, default="They make APIs better.")
        queryParam name (string, required, default="API")
        queryParam user (string, required, default="apigee")
        queryParam mode (string, optional, default="public")
    

Creates a new list for the authenticated user. Accounts are limited to 20 lists.

[http://dev.twitter.com/doc/post/:user/lists](http://dev.twitter.com/doc/post/:user/lists)

      operation POST
    
    resource {user}/lists.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam cursor (string, optional, default="-1")
        queryParam user (string, required, default="apigee")
    

List the lists of the specified user. Private lists will be included if the authenticated users
 is the same as the user whose lists are being returned.

[http://dev.twitter.com/doc/get/:user/lists](http://dev.twitter.com/doc/get/:user/lists)

      operation GET
    
    resource {user}/lists/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam description (string, optional, default="They make APIs better.")
        queryParam name (string, required, default="APIs")
        queryParam user (string, required, default="apigee")
        queryParam id (string, required, default="apigee")
        queryParam mode (string, optional, default="public")
    

Updates the specified list.

[http://dev.twitter.com/doc/post/:user/lists/:id](http://dev.twitter.com/doc/post/:user/lists/:id)

      operation POST
    
    resource {user}/lists/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Show the specified list. Private lists will only be shown if the authenticated user owns the
 specified list.

[http://dev.twitter.com/doc/get/:user/lists/:id](http://dev.twitter.com/doc/get/:user/lists/:id)

      operation GET
    
    resource {user}/lists/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Deletes the specified list. Must be owned by the authenticated user.

[http://dev.twitter.com/doc/delete/:user/lists/:id](http://dev.twitter.com/doc/delete/:user/lists/:id)

      operation DELETE
    
    resource {user}/lists/{list_id}/statuses.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid atom (application/atom+xml)
        queryParam per_page (string, optional, default="200")
        queryParam page (string, optional, default="2")
        queryParam max_id (string, optional, default="54321")
        queryParam since_id (string, optional, default="12345")
        queryParam id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Show tweet timeline for members of the specified list.

[http://dev.twitter.com/doc/get/:user/lists/:id/statuses](http://dev.twitter.com/doc/get/:user/lists/:id/statuses)

      operation GET
    
    resource {user}/lists/memberships.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam cursor (string, optional, default="-1")
        queryParam user (string, required, default="apigee")
    

List the lists the specified user has been added to.

[http://dev.twitter.com/doc/get/:user/lists/memberships](http://dev.twitter.com/doc/get/:user/lists/memberships)

      operation GET
    
    resource {user}/lists/subscriptions.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam cursor (string, optional, default="-1")
        queryParam user (string, required, default="apigee")
    

List the lists the specified user follows.

[http://dev.twitter.com/doc/get/:user/lists/subscriptions](http://dev.twitter.com/doc/get/:user/lists/subscriptions)

      operation GET
    
    resource {user}/{list_id}/members.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam cursor (string, optional, default="-1")
        queryParam list_id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Returns the members of the specified list.

[http://dev.twitter.com/doc/get/:user/:list_id/members](http://dev.twitter.com/doc/get/:user/:list_id/members)

      operation GET
    
    resource {user}/{list_id}/members.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="apigee")
        queryParam list_id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Add a member to a list. The authenticated user must own the list to be able to add members to it.
 Lists are limited to having 500 members.

[http://dev.twitter.com/doc/post/:user/:list_id/members](http://dev.twitter.com/doc/post/:user/:list_id/members)

      operation POST
    
    resource {user}/{list_id}/members.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="earth2marsh")
        queryParam list_id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Removes the specified member from the list. The authenticated user must be the list's owner to
 remove members from the list.

[http://dev.twitter.com/doc/delete/:user/:id/members](http://dev.twitter.com/doc/delete/:user/:id/members)

      operation DELETE
    
    resource {user}/{list_id}/members/create_all.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="earth2marsh")
        queryParam list_id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Adds multiple members to a list, by specifying a comma-separated list of member ids or screen names. The authenticated user must own the list
 to be able to add members to it.

[http://dev.twitter.com/doc/delete/:user/:id/members](http://dev.twitter.com/doc/delete/:user/:id/members)

      operation POST
    
    resource {user}/{list_id}/members/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="apigee")
        queryParam list_id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Check if a user is a member of the specified list.

[http://dev.twitter.com/doc/get/:user/:list_id/members/:id](http://dev.twitter.com/doc/get/:user/:list_id/members/:id)

      operation GET
    
    resource {user}/{list_id}/subscribers.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam cursor (string, optional, default="-1")
        queryParam list_id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Returns the subscribers of the specified list.

[http://dev.twitter.com/doc/get/:user/:list_id/subscribers](http://dev.twitter.com/doc/get/:user/:list_id/subscribers)

      operation GET
    
    resource {user}/{list_id}/subscribers.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam list_id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Make the authenticated user follow the specified list.

[http://dev.twitter.com/doc/post/:user/:list_id/subscribers](http://dev.twitter.com/doc/post/:user/:list_id/subscribers)

      operation POST
    
    resource {user}/{list_id}/subscribers.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="earth2marsh")
        queryParam list_id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Unsubscribes the authenticated user form the specified list.

[http://dev.twitter.com/doc/delete/:user/:id/subscribers](http://dev.twitter.com/doc/delete/:user/:id/subscribers)

      operation DELETE
    
    resource {user}/{list_id}/subscribers/{id}.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam id (string, required, default="earth2marsh")
        queryParam list_id (string, required, default="team")
        queryParam user (string, required, default="apigee")
    

Check if the specified user is a subscriber of the specified list.

[http://dev.twitter.com/doc/get/:user/:list_id/subscribers/:id](http://dev.twitter.com/doc/get/:user/:list_id/subscribers/:id)

      operation GET
    
    resource geo/nearby_places.{format}
      request
        templateParam format (string, required, default="json")
          valid json (application/json)
        queryParam ip (string, optional, default="")
        queryParam long (string, optional, default="")
        queryParam max_results (string, optional, default="")
        queryParam granularity (string, optional, default="")
        queryParam lat (string, optional, default="")
        queryParam accuracy (string, optional, default="city")
    

Search for places (cities and neighborhoods) that can be attached to a statuses/update. Given a
 latitude and a longitude pair, or an IP address, return a list of all the valid cities and
 neighborhoods that can be used as a place_id when updating a status. Conceptually, a query can
 be made from the user's location, retrieve a list of places, have the user validate the location
 he or she is at, and then send the ID of this location up with a call to statuses/update.
 There are multiple granularities of places that can be returned -- "neighborhoods", "cities",
 etc. At this time, only United States data is available through this method.
 This is the recommended method to use find places that can be attached to statuses/update. Unlike
 geo/reverse_geocode which provides raw data access, this endpoint can potentially re-order
 places with regards to the user who is authenticated.

[http://dev.twitter.com/doc/get/geo/nearby_places](http://dev.twitter.com/doc/get/geo/nearby_places)

      operation GET
    
    resource account/end_session.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
    

Ends the session of the authenticating user, returning a null cookie. Use this method to sign
 users out of client-facing applications like widgets.

[http://dev.twitter.com/doc/post/account/end_session](http://dev.twitter.com/doc/post/account/end_session)

      operation POST
    
    resource account/update_delivery_device.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam device (string, required, default="sms")
    

Sets which device Twitter delivers updates to for the authenticating user. Sending none as the
 device parameter will disable IM or SMS updates.

[http://dev.twitter.com/doc/post/account/update_delivery_device](http://dev.twitter.com/doc/post/account/update_delivery_device)

      operation POST
    
    resource statuses/friends.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid atom (application/atom+xml)
        queryParam cursor (string, optional, default="-1")
        queryParam user_id (, required)
    

Returns a user's friends, each with current status inline. They are ordered by the order in which
 the user followed them, most recently followed first, 100 at a time. (Please note that the result
 set isn't guaranteed to be 100 every time as suspended users will be filtered out.) Use the cursor
 option to access older friends. With no user specified, request defaults to the authenticated user's
 friends. It's also possible to request another user's friends list via the id, screen_name or
 user_id parameter.

[http://dev.twitter.com/doc/get/statuses/friends](http://dev.twitter.com/doc/get/statuses/friends)

      operation GET
    
    resource statuses/followers.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
        queryParam cursor (string, optional, default="-1")
        queryParam user_id (, required)
    

Returns the authenticating user's followers, each with current status inline. They are ordered by
 the order in which they followed the user, 100 at a time. (Please note that the result set isn't
 guaranteed to be 100 every time as suspended users will be filtered out.) Use the cursor option to
 access earlier followers.

[http://dev.twitter.com/doc/get/statuses/followers](http://dev.twitter.com/doc/get/statuses/followers)

      operation GET
    
* tmp1

    Name = tmp1
    BasePath = http://search.twitter.com

    resource search.{format}
      request
        templateParam format (string, required, default="json")
          valid xml (application/xml)
          valid json (application/json)
          valid rss (application/rss+xml)
          valid atom (application/atom+xml)
    

Returns tweets that match a specified query.

[https://dev.twitter.com/docs/using-search](https://dev.twitter.com/docs/using-search)

      operation GET
    

#!/bin/sh

# Pull the "wadl-library" from github under the "apigee" acount
# CD to where you pulledit
# Run this script from where you pulled it and it'll upload
# all the WADLs there to the "apigee" organization

BASE=http://stage-apiapi.elasticbeanstalk.com/o/apigee/apis

upload() {
  status=`curl -s -o /dev/null -w '%{http_code}' ${BASE}/$1`
  if [ $status == "404" ]
  then
    echo "Uploading $1..."
    status=`curl -X POST -H "Content-Type: application/xml" "${BASE}?name=$1&format=wadl" -T $2 -o /dev/null -s -w "%{http_code}"`
    echo "Done with status $status"
  else
    echo "$1 has status $status"
  fi
}

upload apigee ./apigee/api.apigee.com.xml
upload bing ./bing/bing-wadl.xml
upload bitly ./bitly/bitly-wadl.xml
upload delicious ./delicious/delicious-wadl.xml
upload etsy ./etsy/etsy-wadl.xml
upload face-com ./face.com/face.com.wadl.xml
upload facebook ./facebook/facebook-wadl.xml
upload foursquare ./foursquare/foursquare-wadl.xml
upload freebase ./freebase/freebase.wadl.xml
upload github ./github/github-wadl.xml
upload google-prediction ./google-prediction/google-prediction.wadl.xml
upload google-webfonts ./google-webfonts/google-webfonts.wadl.xml
upload gowalla ./gowalla/gowalla-wadl.xml
upload groupon ./groupon_api2/groupon_api2-wadl.xml
upload instagram ./instagram/instagram-wadl.xml
upload last-fm ./last.fm/ws.audioscrobbler.com.xml
upload linkin ./linkedin/linkedin-wadl.xml
upload locomatix ./locomatix/api.locomatix.com.xml
upload nytimes ./nytimes/nytimes-wadl.xml
upload pagespeed ./pagespeed/pagespeed.wadl.xml
upload paypal ./paypal/paypal-wadl.xml
upload pivotaltracker ./pivotaltracker/pivotaltracker-wadl.xml
upload readability ./readability/readability.com.xml
upload reddit ./reddit/reddit-wadl.xml
upload rubygems ./rubygems/rubygems.wadl.xml
upload salesforce ./salesforce/salesforce-wadl.xml
upload salesforce-chatter ./salesforcechatter/salesforcechatter-wadl.xml
upload shopping-com ./shopping_com/shopping_com-wadl.xml
upload simplegeo ./simplegeo/simplegeo-wadl.xml
upload soundcloud ./soundcloud/soundcloud-wadl.xml
upload stackexchange ./stackexchange/stackexchange.wadl.xml
upload twilio ./twilio/twilio-wadl.xml
upload twitter-search ./twitter/search.twitter.com.xml
upload twitter ./twitter/twitter-wadl.xml
upload zappos ./zappos/zappos-wadl.xml

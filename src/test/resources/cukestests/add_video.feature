Feature: User can add new video to the database with adequate parameters

   Scenario: user can add new video with valid URL and title
      Given command "add video" is selected
      When  URL "www.address.com" is entered
      And   title "New video" is entered
       And   tag "tag" is entered
      Then  new video is added with URL "www.address.com" and title "New video"

    Scenario: user cannot add new video without URL
       Given command "add video" is selected
       When  URL "" is entered
       And   URL "uusi yritys" is entered
       And   title "Video title" is entered
       And   tag "tag" is entered
       Then  URL is asked again

    Scenario: user can add new video without title
       Given command "add video" is selected
       When  URL "www.videos.com" is entered
       And   title "" is entered
       And   tag "tag" is entered
       Then  new video is added with URL "www.videos.com" and title ""

    Scenario: user cannot add the same video twice with same URL and title
       Given command "add video" is selected
       When  URL "www.url.com" is entered
       And   title "title" is entered
       And   tag "tag" is entered
       And   command "add video" is selected
       And   URL "www.url.com" is entered
       And   title "title" is entered
       And   tag "tag" is entered
       Then  only one video is added with URL "www.url.com" and title "title"

    Scenario: user cannot add the same video twice with same URL and different title
       Given command "add video" is selected
       When  URL "www.url.com" is entered
       And   title "title" is entered
       And   tag "tag" is entered
       And   command "add video" is selected
       And   URL "www.url.com" is entered
       And   title "" is entered
       And   tag "tag" is entered
       Then  only one video is added with URL "www.url.com" and title "title"

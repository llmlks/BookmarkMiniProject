Feature: User can add new book to the database with adequate parameters

    Scenario: user can add new podcast with valid name, title, author and URL
       Given command "add podcast" is selected
       When  name "NewPodCastName" is entered
       And   author "NewPodcastAuthor" is entered
       And   title "NewPodcastTitle" is entered
       And   URL "https://www.podcast.com" is entered
       And   tag "tag" is entered
       Then  new podcast is added with name "NewPodCastName" and author "NewPodcastAuthor" and title "NewPodcastTitle" and URL "https://www.podcast.com"

    Scenario: user can add new podcast without URL
       Given command "add podcast" is selected
       When  name "NewPodcastName" is entered
       And   author "NewPodcastAuthor" is entered
       And   title "NewPodcastTitle" is entered
       And   URL "" is entered
       And   tag "tag" is entered
       Then  new podcast is added with name "NewPodcastName" and author "NewPodcastAuthor" and title "NewPodcastTitle" and URL ""
       
   Scenario: user cannot add new podcast without name
       Given command "add podcast" is selected
       When  name "" is entered
       And   name "toinen yritys" is entered
       And   author "NewPodcastAuthor" is entered
       And   title "NewPodcastTitle" is entered
       And   URL "https://www.podcast.com" is entered
       And   tag "tag" is entered
       Then  name is asked again
       
   Scenario: user cannot add new podcast without author
       Given command "add podcast" is selected
       When  name "NewPodcastName" is entered
       And   author "" is entered
       And   author "toinen yritys" is entered
       And   title "NewPodcastTitle" is entered
       And   URL "https://www.podcast.com" is entered
       And   tag "tag" is entered
       Then  author is asked again
       
   Scenario: user cannot add new podcast without title
       Given command "add podcast" is selected
       When  name "NewPodcastName" is entered
       And   author "NewPodcastAuthor" is entered
       And   title "" is entered
       And   title "uusi yritys" is entered
       And   URL "https://www.podcast.com" is entered
       And   tag "tag" is entered
       Then  title is asked again
       
   Scenario: user cannot add the same podcast twice with same name, author and title
       Given command "add podcast" is selected
       When  name "NewPodcastName" is entered
       And   author "NewPodcastAuthor" is entered
       And   title "NewPodcastTitle" is entered
       And   URL "https://www.podcast.com" is entered
       And   tag "tag" is entered
       And   command "add podcast" is selected
       And   name "NewPodcastName" is entered
       And   author "NewPodcastAuthor" is entered
       And   title "NewPodcastTitle" is entered
       And   URL "https://www.podcast.com" is entered
       And   tag "tag" is entered
       Then  only one podcast is added with name "NewPodcastName" and author "NewPodcastAuthor" and title "NewPodcastTitle" and URL "https://www.podcast.com"
       
   Scenario: user cannot add the same podcast twice with same name, author and title, and different url
       Given command "add podcast" is selected
       When  name "NewPodcastName" is entered
       And   author "NewPodcastAuthor" is entered
       And   title "NewPodcastTitle" is entered
       And   URL "https://www.podcast.com" is entered
       And   tag "tag" is entered
       And   command "add podcast" is selected
       And   name "NewPodcastName" is entered
       And   author "NewPodcastAuthor" is entered
       And   title "NewPodcastTitle" is entered
       And   URL "https://www.AnotherPodcastSite.com" is entered
       And   tag "tag" is entered
       Then  only one podcast is added with name "NewPodcastName" and author "NewPodcastAuthor" and title "NewPodcastTitle"
       
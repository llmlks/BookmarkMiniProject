Feature: User can search bookmarks by keywords

    Scenario: user can search bookmarks with existent keywords
        Given video with URL "www.google.com" and name "google" has been added
        And   command "search" is selected
        And   user enters "google.com"
        Then  video with title "google" and URL "www.google.com" is printed

    Scenario: user can search bookmarks with non-existent keywords
        Given video with URL "www.google.com" and name "google" has been added
        And   command "search" is selected
        And   user enters "yahoo"
        Then  empty list of videos is printed

Feature: User can filter unread bookmarks

    Scenario: user can filter unread bookmarks when browsing all bookmarks
        Given video with URL "www.google.com" and name "google" has been added
        And   book with title "Google tutuksi", author "Kirjailija234" and ISBN "" has been added
        And   book from row "1" has been marked as read
        And   command "browse -u" is selected
        Then  book is printed only once

    Scenario: user can browse bookmarks filtering only unread bookmarks
        Given video with URL "www.google.com" and name "google" has been added
        And   book with title "Google tutuksi", author "Kirjailija234" and ISBN "" has been added
        And   book from row "1" has been marked as read
        And   command "search -u" is selected
        When  user enters "Google"
        Then  book is printed only once
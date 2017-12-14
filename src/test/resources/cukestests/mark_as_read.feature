Feature: User can mark bookmark as read

    Scenario: User can mark book as read
        Given book with title "Kirja123", author "Kirjailija123" and ISBN "00-000-00" has been added
        And command "mark read" is selected
        When command "1" is selected
        Then user is notified that book is marked as read

    Scenario: User can mark podcast as listened
        Given podcast with name "podcast123" and author "author123" and title "title123" and URL "url123" has been added
        And command "mark listened" is selected
        When command "1" is selected
        Then user is notified that podcast is marked as listened

    Scenario: User can mark video as watched
        Given video with URL "www.jees.com" and name "Jees video" has been added
        And command "mark watched" is selected
        When command "1" is selected
        Then user is notified that video is marked as watched
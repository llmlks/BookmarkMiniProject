Feature: User can add new book to the database with adequate parameters

    Scenario: user can add new book with valid title, author and ISBN
       Given command "add book" is selected
       When  title "Uusi64646" is entered
       And   author "kirjailija864831" is entered
       And   ISBN "123-456-78912-3-4" is entered
       And   tag "tag" is entered
       Then  new book is added with title "Uusi64646" and author "kirjailija864831" and ISBN "123-456-78912-3-4"

    Scenario: user cannot add new book without title
       Given command "add book" is selected
       When  title "" is entered
       And   title "testi898" is entered
       And   author "testi565" is entered
       And   ISBN "123--123--123" is entered
       And   tag "tag" is entered
       Then  title is asked again

    Scenario: user cannot add new book without author
       Given command "add book" is selected
       When  title "kirja156483" is entered
       And   author "" is entered
       And   author "testi" is entered
       And   ISBN "123--123--123" is entered
       And   tag "tag" is entered
       Then  author is asked again

    Scenario: user can add new book without ISBN
       Given command "add book" is selected
       When  title "Uusi646746" is entered
       And   author "kirjailija8264831" is entered
       And   ISBN "" is entered
       And   tag "tag" is entered
       Then  new book is added with title "Uusi646746" and author "kirjailija8264831" and ISBN ""

    Scenario: user cannot add the same book twice with same author, title and ISBN
       Given command "add book" is selected
       When  title "Uusi38195" is entered
       And   author "kirjailija02835" is entered
       And   ISBN "111--111--111" is entered
       And   tag "tag" is entered
       And   command "add book" is selected
       And   title "Uusi38195" is entered
       And   author "kirjailija02835" is entered
       And   ISBN "111--111--111" is entered
       And   tag "tag" is entered
       Then  only one book is added with title "Uusi38195" and author "kirjailija02835" and ISBN "111--111--111"

    Scenario: user cannot add the same book twice with same author and title, and different ISBN
       Given command "add book" is selected
       When  title "Uusi12345" is entered
       And   author "kirjailija" is entered
       And   ISBN "111--111--111" is entered
       And   tag "tag" is entered
       And   command "add book" is selected
       And   title "Uusi12345" is entered
       And   author "kirjailija" is entered
       And   ISBN "" is entered
       And   tag "tag" is entered
       Then  only one book is added with title "Uusi12345" and author "kirjailija"

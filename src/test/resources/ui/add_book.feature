Feature: User can add new book to the database with adequate parameters

    Scenario: user can add new book with correct parameters
       Given command "add book" is selected
       When  title "Uusi Kirja" and author "Uusi Kirjailija" and ISBN "123-456-78912-3-4" are entered
       Then  system will respond with "Book added!"

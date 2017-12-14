Feature: User can edit the information of an existing book

   Scenario: user can edit the title of an existing book
      Given book with title "Kirja123456", author "Kirjailija123456" and ISBN "00-000-00" has been added
      And   command "edit book" is selected
      When  command "1" is selected
      And   title "Kirja789" is entered
      And   author "Kirjailija789" is entered
      And   ISBN "11-111-11" is entered
      And   command "browse" is selected
      Then  book with title "Kirja789" and author "Kirjailija789" and ISBN "11-111-11" is printed


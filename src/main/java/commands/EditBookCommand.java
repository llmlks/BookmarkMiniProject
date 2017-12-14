package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Book;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class EditBookCommand extends Command {

    public EditBookCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Book book = getEditable();

        if (book == null) {
            return;
        }

        Book newBook = getEdited(book);
        try {
            bookDAO.update(book, newBook);
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Book getEditable() {
        List<Book> books = browseBooks();
        System.out.println("\nWhich book do you want to edit?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(books.size());

        if (index == -1) {
            return null;
        }

        return books.get(index - 1);
    }

    private Book getEdited(Book book) {
        String title = getUserInput(
                "Enter new title (leave empty if no need to edit):");
        if (title.isEmpty()) {
            title = book.getTitle();
        }
        String author = getUserInput(
                "Enter new author (leave empty if no need to edit):");
        if (author.isEmpty()) {
            author = book.getAuthor();
        }
        String ISBN = getUserInput(
                "Enter new ISBN (leave empty if no need to edit):");
        if (ISBN.isEmpty()) {
            ISBN = book.getISBN();
        }

        return new Book(title, author, ISBN);
    }
}

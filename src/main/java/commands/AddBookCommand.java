package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Book;
import bookmarkmodels.BookmarkTag;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class AddBookCommand extends Command {

    public AddBookCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Book newBook = getNew();

        String[] tags = getTags();
        try {
            if (bookDAO.create(newBook) == null) {
                System.out.println("\nBook has already been added in the library");
            } else {
                addTags(tags, newBook);
                System.out.println("\nBook added!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Book getNew() {
        String title = getUserInputNotEmpty("\nTitle:",
                "Title cannot be empty. Enter title again:");
        String author = getUserInputNotEmpty("Author:",
                "Author cannot be empty. Enter author again:");
        String ISBN = getUserInput("ISBN:");

        return new Book(title, author, ISBN);
    }

    private String[] getTags() {
        String[] tags = getUserInput("Tags: (separate with empty space)")
                .split(" ");

        return tags;
    }

    private void addTags(String[] tags, Book book) throws SQLException {
        BookmarkTag newTag;
        for (String tag : tags) {
            newTag = new BookmarkTag(tag);
            tagDAO.create(newTag);
            bookTagDAO.create(book, newTag);
        }
    }
}

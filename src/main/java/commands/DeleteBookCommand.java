package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Book;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class DeleteBookCommand extends Command {

    public DeleteBookCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Book toDelete = getDeletable();

        if (toDelete == null) {
            return;
        }

        try {
            if (bookDAO.delete(toDelete)) {
                System.out.println("\nBook deleted!");
            } else {
                System.out.println("\nNon-existent book cannot be deleted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Book getDeletable() {
        List<Book> books = browseBooks();
        System.out.println("\nWhich book do you want to delete?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(books.size());

        if (index == -1) {
            return null;
        }

        return books.get(index - 1);
    }
}

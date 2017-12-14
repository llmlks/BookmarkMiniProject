package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Book;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class MarkReadCommand extends Command {

    public MarkReadCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Book toMark = getMarkable();

        if (toMark == null) {
            return;
        }

        try {
            bookDAO.markAsChecked(toMark);
            System.out.println("\nBook marked as read!");
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private Book getMarkable() {
        List<Book> books = browseBooks();
        System.out.println("\nWhich book do you want to mark as read?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(books.size());

        if (index == -1) {
            return null;
        }

        return books.get(index - 1);
    }
}

package ui;

import bookmarkdb.Connector;
import bookmarkdb.Database;
import java.sql.SQLException;

/**
 * Main class of project, contains logic for command-line interface.
 */
public class CLI {

    public static void main(String[] args) throws SQLException {
        Database database = new Database(new Connector("jdbc:sqlite:bookmarks.db"));
        database.init();

        UI ui = new UI(database);
        ui.run();
    }
}

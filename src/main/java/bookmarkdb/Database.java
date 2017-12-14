package bookmarkdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for taking care of database connections and executing queries using
 * those connections.
 */
public class Database implements AbstractDatabase {

    private boolean debug;
    private final AbstractConnector connector;

    public Database(AbstractConnector conn) {
        this.connector = conn;
    }

    /**
     * Initialises the database with tables defined in statements that are
     * acquired by calling {@link #sqliteStatements()}.
     */
    @Override
    public void init() {
        List<String> statements = sqliteStatements();

        try (Connection conn = connector.getConnection(); Statement st = conn.createStatement()) {
            for (String stmt : statements) {
                if (debug) {
                    System.out.println("Running command >> " + stmt);
                }
                st.executeUpdate(stmt);
            }

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }
    }

    /**
     * @param d Boolean value to which local variable {@link #debug} is set to.
     */
    @Override
    public void setDebugMode(boolean d) {
        debug = d;
    }

    /**
     * Prepares and executes an update statement with given query and arguments,
     * and returns the resulting {@link ResultSet}. If {@link #debug} is set to
     * {@code true}, also prints out the query being executed and the number of
     * rows that were affected by the update.
     *
     * @param updateQuery The update statement to be executed as a string
     * @param params Arguments that are inserted into the query
     * @return Results of the query as a {@link ResultSet}
     * @throws java.sql.SQLException
     */
    @Override
    public int update(String updateQuery, Object... params) throws SQLException {
        try (Connection conn = connector.getConnection(); PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            int changes = stmt.executeUpdate();

            if (debug) {
                System.out.println("---");
                System.out.println(updateQuery);
                System.out.println("Changed rows: " + changes);
                System.out.println("---");
            }
            return changes;
        }
    }

    /**
     * Prepares and executes a statement with given query and arguments, and
     * returns the results as a {@link Map} object. If {@link #debug} is set to
     * {@code true}, also prints out the query being executed.
     *
     * @param query The query to be executed as a string
     * @param params Arguments that are inserted into the query
     * @return Results of the query as a map of String to List of String, where
     * each column in the result of the query maps to a list of values in each
     * row.
     * @throws java.sql.SQLException
     */
    @Override
    public Map<String, List<String>> query(String query, Object... params) throws SQLException {

        Map<String, List<String>> results = new HashMap<>();
        try (Connection connection = connector.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs;

            if (debug) {
                System.out.println("---");
                System.out.println("Executing: " + query);
                System.out.println("---");
            }

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            rs = stmt.executeQuery();

            ResultSetMetaData metadata = rs.getMetaData();
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                results.put(metadata.getColumnName(i), new ArrayList<>());
            }

            while (rs.next()) {
                for (String column : results.keySet()) {
                    results.get(column).add(rs.getString(column));
                }
            }

            return results;
        }
    }

    /**
     * Method for generating all CREATE statements for setting up the database.
     *
     * @return List of CREATE statements for SQLite as strings
     */
    private List<String> sqliteStatements() {
        ArrayList<String> list = new ArrayList<>();

        // SQL statements for creating tables
        list.add(""
                + "CREATE TABLE IF NOT EXISTS Book"
                + "(title TEXT NOT NULL,"
                + " author TEXT NOT NULL,"
                + " ISBN TEXT,"
                + " checked INT NOT NULL DEFAULT 0)"
        );

        list.add(""
                + "CREATE TABLE IF NOT EXISTS Podcast"
                + "(name TEXT,"
                + " author TEXT NOT NULL,"
                + " title TEXT NOT NULL,"
                + " url TEXT,"
                + " checked INT NOT NULL DEFAULT 0)"
        );

        list.add(""
                + "CREATE TABLE IF NOT EXISTS Video"
                + "(URL TEXT NOT NULL,"
                + " title TEXT,"
                + " checked INT NOT NULL DEFAULT 0)"
        );

        list.add(""
                + "CREATE TABLE IF NOT EXISTS Tag"
                + "(name TEXT UNIQUE NOT NULL)");

        list.add(""
                + "CREATE TABLE IF NOT EXISTS BookTag"
                + "(tag INT(3) NOT NULL,"
                + " book INT(3) NOT NULL,"
                + " FOREIGN KEY (tag) REFERENCES Tag, " + "FOREIGN KEY (book) REFERENCES Book)");
        
        list.add("" 
                + "CREATE TABLE IF NOT EXISTS PodcastTag"
                + "(tag INT(3) NOT NULL,"
                + " podcast INT(3) NOT NULL,"
                + " FOREIGN KEY (tag) REFERENCES Tag,"
                + " FOREIGN KEY (podcast) REFERENCES Podcast)");
        
        list.add("" 
                + "CREATE TABLE IF NOT EXISTS VideoTag"
                + "(tag INT(3) NOT NULL,"
                + "video INT(3) NOT NULL,"
                + "FOREIGN KEY (tag) REFERENCES Tag,"
                + "FOREIGN KEY (video) REFERENCES Video)");

        return list;
    }
}

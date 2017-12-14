package bookmarkdb;

import bookmarkdb.Database;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rs;

    @Mock
    private Statement statement;
    
    @Mock
    private ResultSetMetaData metadata;


    private Database database;
    private ByteArrayOutputStream outContent;
    private PrintStream standardOut;
    private static final String CREATE_STMT = "CREATE TABLE IF NOT EXISTS Book";

    public DatabaseTest() {
    }

    @Before
    public void setUp() throws Exception {
        standardOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        when(metadata.getColumnCount()).thenReturn(1);
        when(metadata.getColumnName(any(Integer.class))).thenReturn("Title");
        when(rs.getMetaData()).thenReturn(metadata);
        when(rs.getString(any(String.class))).thenReturn("BookTitle");
        when(rs.next()).thenReturn(true, false);
        when(stmt.executeQuery()).thenReturn(rs);
        when(stmt.executeUpdate()).thenReturn(2);
        when(connection.prepareStatement(any(String.class))).thenReturn(stmt);
        when(connection.createStatement()).thenReturn(statement);

        database = new Database(() -> connection);
        database.setDebugMode(true);
    }

    @After
    public void cleanUpStreams() {
        System.setOut(standardOut);
    }

    @Test
    public void testInitCallsExecuteUpdateAndPrintsOutProcessedStatements()
            throws SQLException {
        database.init();
        String output = outContent.toString();

        verify(statement, times(5)).executeUpdate(any(String.class));
        assertTrue(output.contains(CREATE_STMT));
    }

    @Test
    public void testDebugModeSetToTrue() throws SQLException {
        database.setDebugMode(true);
        database.query(CREATE_STMT);
        database.update(CREATE_STMT);
        String output = outContent.toString();

        assertTrue(output.contains("Executing: "));
        assertTrue(output.contains("Changed rows: "));
    }

    @Test
    public void testDebugModeSetToFalse() throws SQLException {
        when(rs.next()).thenReturn(Boolean.FALSE);
        database.setDebugMode(false);
        database.query(CREATE_STMT);
        database.update(CREATE_STMT);
        String output = outContent.toString();

        assertFalse(output.contains("Executing: "));
        assertFalse(output.contains("Changed rows: "));
    }

    @Test
    public void testUpdateWithoutParams() throws Exception {
        int changes = database.update(CREATE_STMT);

        verify(stmt, never()).setObject(any(Integer.class), any(String.class));
        assertEquals(changes, 2);
    }

    @Test
    public void testUpdateWithTwoParams() throws Exception {
        String param = "Param";
        int param2 = 4;
        database.update(CREATE_STMT, param, param2);

        verify(stmt).setObject(1, param);
        verify(stmt).setObject(2, param2);
    }

    @Test
    public void testQueryWithoutParams() throws Exception {
        database.query(CREATE_STMT);

        verify(stmt, never()).setObject(any(Integer.class), any(String.class));
        verify(stmt).executeQuery();
    }

    @Test
    public void testQueryWithTwoParams() throws Exception {
        String param = "Param";
        int parami = 5;
        database.query(CREATE_STMT, param, parami);

        verify(stmt).setObject(1, param);
        verify(stmt).setObject(2, parami);
    }
}

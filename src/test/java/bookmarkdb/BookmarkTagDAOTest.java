package bookmarkdb;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import bookmarkmodels.Book;
import bookmarkmodels.BookmarkTag;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BookmarkTagDAOTest {

    private BookmarkTagDAO tagDAO;
    private Database database;
    private Map<String, List<String>> results;
    private BookmarkTag newTag;

    @Before
    public void setUp() throws Exception {
        database = mock(Database.class);
        tagDAO = new BookmarkTagDAO(database);
        results = new HashMap<>();

        newTag = new BookmarkTag("Name");
        results.put("name", Arrays.asList("Name"));

        when(database.query(any(String.class), any(String.class))).thenReturn(results);
   }

    @After
    public void tearDown() throws Exception {
        database.update("DROP TABLE Tag");
    }

    @Test
    public void testCreate() throws SQLException {
        BookmarkTag newerTag = new BookmarkTag("Name2");
        results.put("name", Arrays.asList("Name", "Name2"));
        when(database.query("SELECT name FROM Tag WHERE name=?", "Name2")).thenReturn(new HashMap<String, List<String>>());

        tagDAO.create(newerTag);

        verify(database).update(eq("INSERT INTO Tag(name) VALUES (?)"), eq("Name2"));
    }

    @Test
    public void testFindOne() throws SQLException {
        BookmarkTag found = tagDAO.findOne(newTag);

        verify(database).query("SELECT name FROM Tag WHERE name=?", found.getName());
    }
    
    @Test
    public void testGetRowId() throws SQLException {
        int rowId = tagDAO.getRowId(newTag);

        verify(database).query("SELECT rowid FROM Tag where name=?", newTag.getName());
    }
}

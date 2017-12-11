package bookmarkdb;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import bookmarkmodels.Podcast;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class PodcastDAOTest {

    private PodcastDAO podDAO;
    private Database database;
    private Map<String, List<String>> results;
    private Podcast newPodcast;

    @Before
    public void setup() throws Exception {
        database = mock(Database.class);
        podDAO = new PodcastDAO(database);
        results = new HashMap<>();

        newPodcast = new Podcast("Name", "Author", "Title", "Url");
        results.put("name", Arrays.asList("Name"));
        results.put("author", Arrays.asList("Author"));
        results.put("title", Arrays.asList("Title"));
        results.put("url", Arrays.asList("Url"));

        when(database.query("SELECT * FROM Podcast")).thenReturn(results);
        when(database.query(any(String.class), any(String.class), any(String.class))).thenReturn(results);
        when(database.query(any(String.class), any(String.class), any(String.class),
                any(String.class), any(String.class))).thenReturn(results);
    }

    @After
    public void tearDown() throws Exception {
        database.update("DROP TABLE Podcast");
    }

    @Test
    public void testCreate() throws SQLException {
        podDAO.create(newPodcast);

        Podcast newerPodcast = new Podcast("Name2", "Author2", "Title2", "Url2");

        podDAO.create(newerPodcast);
        
        verify(database).update(eq("INSERT INTO Podcast(name, title, author, url) VALUES (?, ?, ?, ?)"),
                eq("Name2"), eq("Title2"), eq("Author2"), eq("Url2"));
    }

    

    @Test
    public void testFindOne() throws SQLException {
        Podcast found = podDAO.findOne(newPodcast);
        
        verify(database).query("SELECT * FROM Podcast WHERE author=? AND title=?", found.getAuthor(), found.getTitle());
        
    }
    @Test
    public void testFindAll() throws SQLException {        
        podDAO.findAll();

        verify(database).query(eq("SELECT * FROM Podcast"));
    }

//    @Test
//    public void testUpdate()  {
//        
//    }
    @Test
    public void testDelete() throws SQLException {
        when(database.update(any(String.class), any(String.class),
                any(String.class), any(String.class))).thenReturn(1);
        
        boolean deleted = podDAO.delete(newPodcast);
        
        verify(database).update("DELETE FROM Podcast WHERE author=? AND title=? AND name=?",
                        newPodcast.getAuthor(), newPodcast.getTitle(), newPodcast.getName());
        
        assertTrue(deleted);
    }

    @Test
    public void testFindAllWithKeyword() throws SQLException {
        String s = "titl";
        podDAO.findAllWithKeyword(s);
        String keyword = "%" + s.toUpperCase() + "%";

        verify(database).query("SELECT * FROM Podcast"
                + " WHERE UPPER(author) LIKE ? OR UPPER(title) LIKE ? OR"
                + " UPPER(name) LIKE ? OR UPPER(url) LIKE ?",
                keyword, keyword, keyword, keyword);
    }
}
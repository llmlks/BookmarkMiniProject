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

import bookmarkmodels.Video;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class VideoDAOTest {

    private VideoDAO videoDAO;
    private Database database;
    private Map<String, List<String>> results;
    private Video newVideo;

    @Before
    public void setUp() throws Exception {
        database = mock(Database.class);
        videoDAO = new VideoDAO(database);
        results = new HashMap<>();

        newVideo = new Video("URL", "Title");
        results.put("URL", Arrays.asList("URL"));
        results.put("title", Arrays.asList("Title"));

        when(database.query(any(String.class))).thenReturn(results);
        when(database.query(any(String.class), any(String.class), any(String.class))).thenReturn(results);
    }

    @After
    public void tearDown() throws Exception {
        database.update("DROP TABLE Video");
    }

    @Test
    public void testCreate() throws SQLException {
        Video newerVideo = new Video("URL2", "Title2");

        videoDAO.create(newerVideo);

        verify(database).update(eq("INSERT INTO Video(URL, title) VALUES (?, ?)"), eq("URL2"),
                eq("Title2"));
    }

    @Test
    public void testFindOne() throws SQLException {
        Video found = videoDAO.findOne(newVideo);

        verify(database).query(eq("SELECT * FROM Video WHERE URL=? AND title=?"), eq(found.getURL()), eq(found.getTitle()));

    }

    @Test
    public void testFindAll() throws SQLException {
        videoDAO.findAll();

        verify(database).query(eq("SELECT * FROM Video"));
    }

    @Test
    public void testDelete() throws SQLException {
        when(database.update(any(String.class), any(String.class))).thenReturn(1);

        boolean deleted = videoDAO.delete(newVideo);

        verify(database).update("DELETE FROM Video WHERE URL=?", newVideo.getURL());

        assertTrue(deleted);
    }
    // @Test
    // public void testUpdate() {
    // fail("Not yet implemented");
    // }

    @Test
    public void testFindAllWithKeyword() throws SQLException {
        String s = "titl";
        videoDAO.findAllWithKeyword(s);
        String keyword = "%" + s.toUpperCase() + "%";

        verify(database).query("SELECT * FROM Video WHERE UPPER(url) LIKE ? OR"
                + " UPPER(title) LIKE ?", keyword, keyword);
    }
}

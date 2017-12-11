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
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BookDAOTest {

    private BookDAO bookDAO;
    private Database database;
    private Map<String, List<String>> results;
    private Book newBook;

    @Before
    public void setUp() throws Exception {
        database = mock(Database.class);
        bookDAO = new BookDAO(database);
        results = new HashMap<String, List<String>>();

        newBook = new Book("Title", "Author", "ISBN");
        results.put("title", Arrays.asList("Title"));
        results.put("author", Arrays.asList("Author"));
        results.put("ISBN", Arrays.asList("ISBN"));

        when(database.query(any(String.class))).thenReturn(results);
        when(database.query(any(String.class), any(String.class), any(String.class))).thenReturn(results);
        when(database.query(any(String.class), any(String.class), any(String.class),
                any(String.class))).thenReturn(results);
    }

    @After
    public void tearDown() throws Exception {
        database.update("DROP TABLE Book");
    }

    @Test
    public void testCreate() throws SQLException {
        Book newerBook = new Book("Title2", "Author2", "ISBN2");

        bookDAO.create(newerBook);

        verify(database).update(eq("INSERT INTO Book(title, author, ISBN) VALUES (?, ?, ?)"),
                eq("Title2"), eq("Author2"), eq("ISBN2"));
    }

    @Test
    public void testFindOne() throws SQLException {
        Book found = bookDAO.findOne(newBook);

        verify(database).query("SELECT * FROM Book WHERE author=? AND title=?", found.getAuthor(), found.getTitle());
    }

    @Test
    public void testFindAll() throws SQLException {
        bookDAO.findAll();

        verify(database).query(eq("SELECT * FROM Book"));
    }

    @Test
    public void testDelete() throws SQLException {
        when(database.update(any(String.class), any(String.class),
                any(String.class))).thenReturn(1);

        boolean deleted = bookDAO.delete(newBook);

        verify(database).update("DELETE FROM Book WHERE author=? AND title=?",
                newBook.getAuthor(), newBook.getTitle());

        assertTrue(deleted);
    }

    @Test
    public void testUpdate() throws SQLException {
        Book modifiedBook = new Book("Title2", "Author2", "ISBN2");
        bookDAO.update(newBook, modifiedBook);

        verify(database).update("UPDATE Book SET title=?, author=?, ISBN=? WHERE author=? AND title=?",
                modifiedBook.getTitle(), modifiedBook.getAuthor(), modifiedBook.getISBN(), newBook.getAuthor(), newBook.getTitle());
    }

    @Test
    public void testFindAllWithKeyword() throws SQLException {
        String s = "titl";
        bookDAO.findAllWithKeyword(s);
        String keyword = "%" + s.toUpperCase() + "%";

        verify(database).query("SELECT * FROM Book"
                + " WHERE UPPER(title) LIKE ? OR UPPER(author) LIKE ? OR"
                + " UPPER(ISBN) LIKE ?", keyword, keyword, keyword);
    }
}

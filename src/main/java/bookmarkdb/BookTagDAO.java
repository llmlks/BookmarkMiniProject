package bookmarkdb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import bookmarkmodels.Book;
import bookmarkmodels.BookmarkTag;

import java.util.stream.Collectors;

/**
 * Class for accessing database table for bookmarks of type 'Book'.
 */
public class BookTagDAO {

    private final AbstractDatabase database;
    private BookDAO bookDAO;
    private BookmarkTagDAO bookmarktagDAO;

    public BookTagDAO(AbstractDatabase db) {
        database = db;
    }

    public BookTagDAO(AbstractDatabase db, BookDAO bookDAO, BookmarkTagDAO tagDAO) {
    	database = db;
    	this.bookDAO = bookDAO;
    	this.bookmarktagDAO = tagDAO;
	}

	/**
     * Adds a book to the database table 'Book'
     *
     * @param book to be added
     * @return
     * @throws SQLException
     */
    public void create(Book book, BookmarkTag tag) throws SQLException {
    	database.update("INSERT INTO BookTag(tag, book) VALUES (?,?)", bookmarktagDAO.getRowId(tag), bookDAO.getRowId(book));
    }
    
    public String printTags(List<BookmarkTag> tags) {
    	StringBuilder s = new StringBuilder("");
        for (int i = 0; i < tags.size(); i++) {
        	s.append(tags.get(i).getName());
        	if (i < tags.size() - 1) {
        		s.append(" ");
        	}
        }
        return s.toString();
    }

    public List<BookmarkTag> findAll(Book book) throws SQLException {
			List<BookmarkTag> tags = new ArrayList<>();
			Map<String, List<String>> results = database.query("SELECT * FROM BookTag WHERE book IN"
					+ " (SELECT rowid from Book WHERE title=? and author=?)", book.getTitle(), book.getAuthor());

			for (int i = 0; i < results.get("tag").size(); i++) {
				int tagId = Integer.parseInt(results.get("tag").get(i));
				tags.add(new BookmarkTag(bookmarktagDAO.getTagName(tagId)));
			}
			return tags;
    }

    public void update(Book oldBook, Book newBook) throws SQLException {
    }

    public boolean delete(Book book) throws SQLException {
    	return false;
    }

    public List<Book> findAllWithKeyword(String s) throws SQLException {
        return null;
    }
}

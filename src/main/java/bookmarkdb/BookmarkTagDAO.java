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
public class BookmarkTagDAO implements AbstractDAO<BookmarkTag, Integer> {

    private final AbstractDatabase database;

    public BookmarkTagDAO(AbstractDatabase db) {
        database = db;
    }

    /**
     * Adds a bookmark to the database table 'Tag'
     *
     * @param newTag to be added
     * @return
     * @throws SQLException
     */
    @Override
    public BookmarkTag create(BookmarkTag tag) throws SQLException {
		if (!tagExists(tag)) {
			database.update("INSERT INTO Tag(name) VALUES (?)", tag.getName());
			return tag;
		} else {
			return null;
		}
	}

	public boolean tagExists(BookmarkTag tag) throws SQLException {
		return findOne(tag) != null;
	}
	
	@Override
	 public BookmarkTag findOne(BookmarkTag tag) throws SQLException {
		 Map<String, List<String>> results = database.query("SELECT name FROM Tag WHERE name=?", tag.getName());
		 
		 if (results.get("name") == null) return null;
		 
		 if (results.get("name").size() > 0 ) {
			 BookmarkTag found = new BookmarkTag();
			 for (String col : results.keySet()) {
	                if (col.equals("name")) {
	                	found.setName(results.get(col).get(0));
	                }
			 }
			 return found;
		 }
		 return null;
	 }

	public String getTagName(int tagId) throws SQLException {
		Map<String, List<String>> results = database.query("SELECT * FROM Tag WHERE rowid=?", tagId);
		for (String col : results.keySet()) {

			if (col.equals("name")) {
				if (results.get(col).isEmpty()) {
					continue;
				}
				return results.get(col).get(0);
			}
		}
		return "";
	}

	public int getRowId(BookmarkTag tag) throws SQLException {
		Map<String, List<String>> results = database.query("SELECT rowid FROM Tag where name=?", tag.getName());
		for (String col : results.keySet()) {
			if (col.equals("rowid")) {
				return Integer.parseInt(results.get(col).get(0));
			}
		}

		return -1;
	}

    @Override
    public List<BookmarkTag> findAll() throws SQLException {
        return null;
    }
    
	@Override
	public void update(BookmarkTag t, BookmarkTag s) throws SQLException {	
	}

	@Override
	public boolean delete(BookmarkTag t) throws SQLException {
		return false;
	}

	@Override
	public List<BookmarkTag> findAllWithKeyword(String s) throws SQLException {
		return null;
	}

	@Override
	public void markAsChecked(BookmarkTag t) throws SQLException {
		
	}

	@Override
	public List<BookmarkTag> filterOnlyUnchecked(List<BookmarkTag> t) {
		return null;
	}
}

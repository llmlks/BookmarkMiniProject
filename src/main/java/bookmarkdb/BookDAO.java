package bookmarkdb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import bookmarkmodels.Book;
import java.util.stream.Collectors;

/**
 * Class for accessing database table for bookmarks of type 'Book'.
 */
public class BookDAO implements AbstractDAO<Book, Integer> {

    private final AbstractDatabase database;

    public BookDAO(AbstractDatabase db) {
        database = db;
    }

    /**
     * Adds a book to the database table 'Book'
     *
     * @param book to be added
     * @return
     * @throws SQLException
     */
    @Override
    public Book create(Book book) throws SQLException {

        List<Book> allBooks = this.findAll();
        for (Book existingBook : allBooks) {
            if (book.getAuthor().equalsIgnoreCase(existingBook.getAuthor())
                    && book.getTitle().equalsIgnoreCase(existingBook.getTitle())) {
                return null;
            }
        }

        String query = "INSERT INTO Book(title, author, ISBN) VALUES (?, ?, ?)";
        database.update(
                query,
                book.getTitle(),
                book.getAuthor(),
                book.getISBN()
        );

        return book;
    }

    @Override
    public Book findOne(Book book) throws SQLException {
        String query = "SELECT * FROM Book WHERE author=? AND title=?";
        Map<String, List<String>> results = database.query(
                query,
                book.getAuthor(),
                book.getTitle()
        );

        if (results.get("title").size() > 0) {
            Book found = new Book();
            for (String col : results.keySet()) {
                if (col.equalsIgnoreCase("title")) {
                    found.setTitle(results.get(col).get(0));
                } else if (col.equalsIgnoreCase("author")) {
                    found.setAuthor(results.get(col).get(0));
                } else if (col.equalsIgnoreCase("ISBN")) {
                    found.setISBN(results.get(col).get(0));
                } else if (col.equalsIgnoreCase("checked")) {
                    found.setChecked(Integer.parseInt(results.get(col).get(0)));
                }
            }
            return found;
        }
        return null;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        Map<String, List<String>> results = database.query("SELECT * FROM Book");
        return getBookList(results);
    }

    private List<Book> getBookList(Map<String, List<String>> results) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < results.get("title").size(); i++) {
            Book book = new Book();
            for (String col : results.keySet()) {
                String value = results.get(col).get(i);
                switch (col.toLowerCase()) {
                    case "title":
                        book.setTitle(value);
                        break;
                    case "author":
                        book.setAuthor(value);
                        break;
                    case "isbn":
                        book.setISBN(value);
                        break;
                    case "checked":
                        book.setChecked(Integer.parseInt(value));
                        break;
                    default:
                        break;
                }
            }
            books.add(book);
        }

        return books;
    }

    @Override
    public void update(Book oldBook, Book newBook) throws SQLException {
        Book old = findOne(oldBook);
        if (old.getAuthor() != null) {
            String query = "UPDATE Book SET title=?, author=?, ISBN=? WHERE author=? AND title=?";
            database.update(
                    query,
                    newBook.getTitle(),
                    newBook.getAuthor(),
                    newBook.getISBN(),
                    old.getAuthor(),
                    old.getTitle()
            );
        }
    }

    @Override
    public boolean delete(Book book) throws SQLException {
        String query = "DELETE FROM Book WHERE author=? AND title=?";
        int deleted = database.update(
                query,
                book.getAuthor(),
                book.getTitle()
        );

        return deleted == 1;
    }

    @Override
    public List<Book> findAllWithKeyword(String s) throws SQLException {
        String keyword = "%" + s.toUpperCase() + "%";
        String query = ""
                + "SELECT * FROM Book"
                + " WHERE"
                + " UPPER(title) LIKE ?"
                + " OR UPPER(author) LIKE ?"
                + " OR UPPER(ISBN) LIKE ?";

        Map<String, List<String>> results = database.query(
                query,
                keyword,
                keyword,
                keyword
        );

        return getBookList(results);
    }

    @Override
    public void markAsChecked(Book b) throws SQLException {
        Book book = findOne(b);
        if (book.getAuthor() != null) {
            String query = "UPDATE Book SET checked=1 "
                    + "WHERE author=? "
                    + "AND title=?";
            database.update(
                    query,
                    book.getAuthor(),
                    book.getTitle()
            );
        }
    }

    @Override
    public List<Book> filterOnlyUnchecked(List<Book> books) {
        return books.stream().filter((book) -> (book.getChecked() == 0))
                .collect(Collectors.toList());
    }

    @Override
    public int getRowId(Book book) throws SQLException {
        Map<String, List<String>> results = database.query("SELECT rowid FROM Book where title=? AND author=?",
                book.getTitle(), book.getAuthor());
        for (String col : results.keySet()) {
            if (col.equals("rowid")) {
                return Integer.parseInt(results.get(col).get(0));
            }
        }
        return -1;
    }
}

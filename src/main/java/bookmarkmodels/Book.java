package bookmarkmodels;

/**
 * Class for representing bookmarks of type 'Book'.
 */
public class Book {

    private String title;
    private String author;
    private String ISBN;
    private int checked;

    public Book(String title, String author, String ISBN, int checked) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.checked = checked;
    }

    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Book() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Book: " + "title: " + title + ", author: " + author + ", ISBN: " + printOptionalField(ISBN) + ", " + isChecked();
    }

    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(Book.class)) {
            return false;
        }

        Book comp = (Book) o;
        return comp.getAuthor().equals(this.author)
                && comp.getISBN().equals(this.ISBN)
                && comp.getTitle().equals(this.title);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.title.hashCode();
        hash = 79 * hash + this.author.hashCode();
        hash = 79 * hash + this.ISBN.hashCode();
        return hash;
    }

    private String isChecked() {
        if (checked == 0) {
            return "not read";
        }
        return "read";
    }

    private String printOptionalField(String text) {
        if (text.isEmpty()) {
            return "<empty>";
        }
        return text;
    }
}

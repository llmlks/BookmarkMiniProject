package bookmarkmodels;

/**
 * Class for representing bookmarks of type 'Book'.
 */
public class BookmarkTag {

    private String name;

    public BookmarkTag(String name) {
        this.name = name;
    }


    public BookmarkTag() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(BookmarkTag.class)) {
            return false;
        }

        BookmarkTag comp = (BookmarkTag) o;
        return comp.getName().equals(this.name);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.name.hashCode();
        return hash;
    }
    
    @Override
    public String toString() {
    	return this.name;
    }
}

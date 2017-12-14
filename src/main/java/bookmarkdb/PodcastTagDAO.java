package bookmarkdb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import bookmarkmodels.BookmarkTag;
import bookmarkmodels.Podcast;

/**
 * Class for accessing database table for tags of bookmarks of type 'Podcast'.
 */
public class PodcastTagDAO {

    private final AbstractDatabase database;
    private PodcastDAO podcastDAO;
    private BookmarkTagDAO bookmarktagDAO;

    public PodcastTagDAO(AbstractDatabase db) {
        database = db;
    }

    public PodcastTagDAO(AbstractDatabase db, PodcastDAO podcastDAO, BookmarkTagDAO tagDAO) {
        database = db;
        this.podcastDAO = podcastDAO;
        this.bookmarktagDAO = tagDAO;
    }

    public void create(Podcast podcast, BookmarkTag tag) throws SQLException {
        database.update("INSERT INTO PodcastTag(tag, book) VALUES (?,?)",
                bookmarktagDAO.getRowId(tag), podcastDAO.getRowId(podcast));
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

    public List<BookmarkTag> findAll(Podcast podcast) throws SQLException {
        List<BookmarkTag> tags = new ArrayList<>();
        String query = "SELECT * FROM PodcastTag WHERE podcast IN"
                + " (SELECT rowid from Podcast WHERE title=? and author=?)";
        Map<String, List<String>> results = database.query(query,
                podcast.getTitle(), podcast.getAuthor());

        for (int i = 0; i < results.get("tag").size(); i++) {
            int tagId = Integer.parseInt(results.get("tag").get(i));
            tags.add(new BookmarkTag(bookmarktagDAO.getTagName(tagId)));
        }

        return tags;
    }

    public void update(Podcast podcast, Podcast newPod) throws SQLException {
    }

    public boolean delete(Podcast podcast) throws SQLException {
        return false;
    }

    public List<Podcast> findAllWithKeyword(String s) throws SQLException {
        return null;
    }
}

package bookmarkdb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import bookmarkmodels.Video;
import bookmarkmodels.BookmarkTag;

import java.util.stream.Collectors;

/**
 * Class for accessing database table for bookmarks of type 'Book'.
 */
public class VideoTagDAO {

    private final AbstractDatabase database;
    private VideoDAO videoDAO;
    private BookmarkTagDAO bookmarktagDAO;

    public VideoTagDAO(AbstractDatabase db) {
        database = db;
    }

    public VideoTagDAO(AbstractDatabase db, VideoDAO videoDAO, BookmarkTagDAO tagDAO) {
        database = db;
        this.videoDAO = videoDAO;
        this.bookmarktagDAO = tagDAO;
    }

    /**
     * Adds a video to the database table 'Video'
     *
     * @param video to be added
     * @return
     * @throws SQLException
     */
    public void create(Video video, BookmarkTag tag) throws SQLException {
        database.update("INSERT INTO VideoTag(tag, video) VALUES (?,?)", bookmarktagDAO.getRowId(tag), videoDAO.getRowId(video));
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

    public List<BookmarkTag> findAll(Video video) throws SQLException {
        List<BookmarkTag> tags = new ArrayList<>();
        Map<String, List<String>> results = database.query("SELECT * FROM VideoTag WHERE video IN"
                + " (SELECT rowid from Video WHERE URL=?)", video.getURL());

        for (int i = 0; i < results.get("tag").size(); i++) {
            int tagId = Integer.parseInt(results.get("tag").get(i));
            tags.add(new BookmarkTag(bookmarktagDAO.getTagName(tagId)));
        }
        return tags;
    }

    public void update(Video oldVideo, Video newVideo) throws SQLException {
    }

    public boolean delete(Video video) throws SQLException {
        return false;
    }

    public List<Video> findAllWithKeyword(String s) throws SQLException {
        return null;
    }
}

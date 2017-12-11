/**
 *
 */
package bookmarkdb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import bookmarkmodels.Video;
import java.util.stream.Collectors;

/**
 * @author Admin
 *
 */
public class VideoDAO implements AbstractDAO<Video, Integer> {

    private final AbstractDatabase database;

    public VideoDAO(AbstractDatabase db) {
        database = db;
    }

    /**
     * Adds a video to the database table 'Video'
     *
     * @param video to be added
     * @return
     * @throws SQLException
     */
    @Override
    public Video create(Video video) throws SQLException {

        List<Video> allVideos = this.findAll();
        for (Video existingVideo : allVideos) {
            if (video.getURL().equalsIgnoreCase(existingVideo.getURL())) {
                return null;
            }
        }

        String query = "INSERT INTO Video(URL, title) VALUES (?, ?)";
        database.update(query, video.getURL(), video.getTitle());

        return video;
    }

    @Override
    public Video findOne(Video video) throws SQLException {
        String query = "SELECT * FROM Video WHERE URL=? AND title=?";
        Map<String, List<String>> results = database.query(
                query,
                video.getURL(),
                video.getTitle()
        );

        if (results.get("URL").size() > 0) {
            Video found = new Video();
            for (String col : results.keySet()) {
                String value = results.get(col).get(0);
                if (col.equalsIgnoreCase("URL")) {
                    found.setURL(value);
                } else if (col.equalsIgnoreCase("title")) {
                    found.setTitle(value);
                } else if (col.equalsIgnoreCase("checked")) {
                    found.setChecked(Integer.parseInt(value));
                }
            }

            return found;
        }

        return null;
    }

    @Override
    public List<Video> findAll() throws SQLException {
        Map<String, List<String>> results = database.query("SELECT * FROM Video");

        return getVideoList(results);
    }

    @Override
    public void update(Video oldVideo, Video newVideo) throws SQLException {
        if (oldVideo.getURL() != null) {
            String query = "UPDATE Video SET URL=?, title=? WHERE URL=? AND title=?";
            database.update(
                    query,
                    newVideo.getURL(),
                    newVideo.getTitle(),
                    oldVideo.getURL(),
                    oldVideo.getTitle()
            );
        }
    }

    @Override
    public boolean delete(Video video) throws SQLException {
        int deleted = database.update("DELETE FROM Video WHERE URL=?", video.getURL());
        return deleted == 1;
    }

    @Override
    public List<Video> findAllWithKeyword(String s) throws SQLException {
        String keyword = "%" + s.toUpperCase() + "%";
        String query = ""
                + "SELECT * FROM Video"
                + " WHERE UPPER(url) LIKE ?"
                + " OR UPPER(title) LIKE ?";
        Map<String, List<String>> results = database.query(query, keyword, keyword);

        return getVideoList(results);
    }

    private List<Video> getVideoList(Map<String, List<String>> results) {
        List<Video> videos = new ArrayList<>();
        for (int i = 0; i < results.get("URL").size(); i++) {
            Video video = new Video();
            for (String col : results.keySet()) {
                String value = results.get(col).get(i);
                switch(col.toLowerCase()) {
                    case "title":
                        video.setTitle(value);
                        break;
                    case "url":
                        video.setURL(value);
                        break;
                    case "checked":
                        video.setChecked(Integer.parseInt(value));
                        break;
                    default:
                        break;
                }
            }
            videos.add(video);
        }

        return videos;
    }

    @Override
    public void markAsChecked(Video v) throws SQLException {
        Video video = findOne(v);
        if (video.getURL() != null) {
            String query = "UPDATE Video SET checked=1 "
                    + "WHERE URL=?";
            database.update(
                    query,
                    video.getURL()
            );
        }
    }

    @Override
    public List<Video> filterOnlyUnchecked(List<Video> videos) {
        return videos.stream().filter((video) -> (video.getChecked() == 0))
                .collect(Collectors.toList());
    }
}

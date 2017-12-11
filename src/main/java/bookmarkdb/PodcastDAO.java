package bookmarkdb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import bookmarkmodels.Podcast;

/**
 * Class for accessing database table for bookmarks of type 'Podcast'.
 */
public class PodcastDAO implements AbstractDAO<Podcast, Integer> {

    private final AbstractDatabase database;

    public PodcastDAO(AbstractDatabase database) {
        this.database = database;
    }

    @Override
    public Podcast create(Podcast podcast) throws SQLException {

        List<Podcast> allPodcasts = this.findAll();
        for (Podcast existingPod : allPodcasts) {
            if (podcast.getAuthor().equalsIgnoreCase(existingPod.getAuthor())
                    && podcast.getName().equalsIgnoreCase(existingPod.getName())
                    && podcast.getTitle().equalsIgnoreCase(existingPod.getTitle())) {
                return null;
            }
        }

        String query = "INSERT INTO Podcast(name, title, author, url) VALUES (?, ?, ?, ?)";
        database.update(
                query,
                podcast.getName(),
                podcast.getTitle(),
                podcast.getAuthor(),
                podcast.getUrl()
        );

        return podcast;
    }

    @Override
    public Podcast findOne(Podcast podcast) throws SQLException {
        String query = "SELECT * FROM Podcast WHERE author=? AND title=?";
        Map<String, List<String>> results = database.query(
                query,
                podcast.getAuthor(),
                podcast.getTitle()
        );

        if (results.get("title").size() > 0) {
            Podcast found = new Podcast();
            for (String col : results.keySet()) {
                if (col.equalsIgnoreCase("name")) {
                    found.setName(results.get(col).get(0));
                }
                if (col.equalsIgnoreCase("author")) {
                    found.setAuthor(results.get(col).get(0));
                }
                if (col.equalsIgnoreCase("title")) {
                    found.setTitle(results.get(col).get(0));
                }
                if (col.equalsIgnoreCase("url")) {
                    found.setUrl(results.get(col).get(0));
                } else if (col.equalsIgnoreCase("checked")) {
                    found.setChecked(Integer.parseInt(results.get(col).get(0)));
                }
            }
            return found;
        }
        return null;
    }

    @Override
    public List<Podcast> findAll() throws SQLException {
        List<Podcast> podcasts = new ArrayList<>();
        Map<String, List<String>> results = database.query("SELECT * FROM Podcast");

        for (int i = 0; i < results.get("title").size(); i++) {
            Podcast podcast = new Podcast();
            for (String col : results.keySet()) {
                if (col.equalsIgnoreCase("name")) {
                    podcast.setName(results.get(col).get(i));
                } else if (col.equalsIgnoreCase("author")) {
                    podcast.setAuthor(results.get(col).get(i));
                } else if (col.equalsIgnoreCase("title")) {
                    podcast.setTitle(results.get(col).get(i));
                } else if (col.equalsIgnoreCase("url")) {
                    podcast.setUrl(results.get(col).get(i));
                } else if (col.equalsIgnoreCase("checked")) {
                    podcast.setChecked(Integer.parseInt(results.get(col).get(i)));
                }
            }
            podcasts.add(podcast);
        }
        return podcasts;
    }

    @Override
    public void update(Podcast oldPod, Podcast newPod) throws SQLException {
        Podcast old = findOne(oldPod);
        if (old.getAuthor() != null) {
            String query = ""
                    + "UPDATE Podcast"
                    + " SET"
                    + " name=?, author=?, title=?, url=?"
                    + " WHERE"
                    + " author=? AND"
                    + " title=?";

            database.update(query,
                    newPod.getName(),
                    newPod.getAuthor(),
                    newPod.getTitle(),
                    newPod.getUrl(),
                    old.getAuthor(),
                    old.getTitle()
            );
        }
    }

    @Override
    public boolean delete(Podcast podcast) throws SQLException {
        String query = "DELETE FROM Podcast WHERE author=? AND title=? AND name=?";
        int deleted = database.update(
                query,
                podcast.getAuthor(),
                podcast.getTitle(),
                podcast.getName()
        );
        return deleted == 1;
    }

    @Override
    public List<Podcast> findAllWithKeyword(String s) throws SQLException {
        List<Podcast> podcasts = new ArrayList<>();
        String keyword = "\'%" + s.toUpperCase() + "\'%";
        String query = ""
                + "SELECT *"
                + " FROM Podcast"
                + " WHERE UPPER(author) LIKE ? OR UPPER(title) LIKE ? OR"
                + " UPPER(name) LIKE ? OR UPPER(url) LIKE ?";

        Map<String, List<String>> results = database.query(
                query,
                keyword,
                keyword,
                keyword,
                keyword
        );

        for (int i = 0; i < results.get("title").size(); i++) {
            Podcast podcast = new Podcast();
            for (String col : results.keySet()) {
                if (col.equalsIgnoreCase("name")) {
                    podcast.setName(results.get(col).get(i));
                } else if (col.equalsIgnoreCase("author")) {
                    podcast.setAuthor(results.get(col).get(i));
                } else if (col.equalsIgnoreCase("title")) {
                    podcast.setTitle(results.get(col).get(i));
                } else if (col.equalsIgnoreCase("url")) {
                    podcast.setUrl(results.get(col).get(i));
                } else if (col.equalsIgnoreCase("checked")) {
                    podcast.setChecked(Integer.parseInt(results.get(col).get(i)));
                }
            }
            podcasts.add(podcast);
        }
        return podcasts;
    }

    @Override
    public void markAsChecked(Podcast p) throws SQLException {
        Podcast podcast = findOne(p);
        if (podcast.getAuthor() != null) {
            String query = "UPDATE Podcast SET checked=1 "
                    + "WHERE name=? "
                    + "AND author=? "
                    + "AND title=?";
            database.update(
                    query,
                    podcast.getName(),
                    podcast.getAuthor(),
                    podcast.getTitle()
            );
        }
    }
}

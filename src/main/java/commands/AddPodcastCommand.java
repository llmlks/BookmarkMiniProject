package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.BookmarkTag;
import bookmarkmodels.Podcast;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddPodcastCommand extends Command {

    public AddPodcastCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Podcast newPodcast = getNew();
        String[] tags = getTags();

        try {

            if (podcastDAO.create(newPodcast) == null) {
                System.out.println("\nPodcast has already been added in the library");
            } else {
                addTags(tags, newPodcast);
                System.out.println("\nPodcast added!");
            }
        } catch (SQLException exe) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exe);
        }
    }

    private Podcast getNew() {
        String podName = getUserInputNotEmpty("\nName:",
                "Name cannot be empty. Enter name again:");
        String podAuthor = getUserInputNotEmpty("Author:",
                "Author cannot be empty. Enter author again:");
        String podTitle = getUserInputNotEmpty("Title:",
                "Title cannot be empty. Enter title again:");
        String podUrl = getUserInput("URL:");

        return new Podcast(podName, podAuthor, podTitle, podUrl);
    }

    private String[] getTags() {
        String[] tags = getUserInput("Tags: (separate with empty space)")
                .split(" ");

        return tags;
    }

    private void addTags(String[] tags, Podcast podcast) throws SQLException {
        BookmarkTag newTag;
        for (String tag : tags) {
            newTag = new BookmarkTag(tag);
            tagDAO.create(newTag);
            podTagDAO.create(podcast, newTag);
        }
    }
}

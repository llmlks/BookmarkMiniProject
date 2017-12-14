package commands;

import bookmarkdb.AbstractDatabase;
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

        try {

            if (podcastDAO.create(newPodcast) == null) {
                System.out.println("\nPodcast has already been added in the library");
            } else {
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
}

package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Podcast;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class EditPodcastCommand extends Command {

    public EditPodcastCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Podcast podcast = getEditable();

        if (podcast == null) {
            return;
        }

        Podcast newPodcast = getEdited(podcast);

        try {
            podcastDAO.update(podcast, newPodcast);
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Podcast getEditable() {
        List<Podcast> podcasts = browsePodcasts();

        System.out.println("\nWhich podcast do you want to edit?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(podcasts.size());

        if (index == -1) {
            return null;
        }

        return podcasts.get(index - 1);
    }

    private Podcast getEdited(Podcast podcast) {
        String name = getUserInput(
                "Enter new name (leave empty if no need to edit):");
        if (name.isEmpty()) {
            name = podcast.getName();
        }

        String author = getUserInput(
                "Enter new author (leave empty if no need to edit):");
        if (author.isEmpty()) {
            author = podcast.getAuthor();
        }

        String title = getUserInput(
                "Enter new title (leave empty if no need to edit):");
        if (title.isEmpty()) {
            title = podcast.getTitle();
        }

        String url = getUserInput(
                "Enter new url (leave empty if no need to edit):");
        if (url.isEmpty()) {
            url = podcast.getUrl();
        }

        return new Podcast(name, author, title, url);
    }
}

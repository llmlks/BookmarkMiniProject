package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Podcast;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class DeletePodcastCommand extends Command {

    public DeletePodcastCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Podcast toDelete = getDeletable();

        if (toDelete == null) {
            return; 
        }

        try {
            if (podcastDAO.delete(toDelete)) {
                System.out.println("\nPodcast deleted!");
            } else {
                System.out.println("\nNon-existent podcast cannot be deleted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Podcast getDeletable() {
        List<Podcast> podcasts = browsePodcasts();
        System.out.println("\nWhich podcast do you want to delete?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(podcasts.size());

        if (index == -1) {
            return null;
        }

        return podcasts.get(index - 1);
    }
}

package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Podcast;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class MarkListenedCommand extends Command {

    public MarkListenedCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Podcast toMark = getMarkable();

        if (toMark == null) {
            return;
        }

        try {
            podcastDAO.markAsChecked(toMark);
            System.out.println("\nPodcast marked as listened!");
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Podcast getMarkable() {
        List<Podcast> podcasts = browsePodcasts();
        System.out.println("\nWhich podcast do you want to mark as listened?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(podcasts.size());

        if (index == -1) {
            return null;
        }

        return podcasts.get(index - 1);
    }
}

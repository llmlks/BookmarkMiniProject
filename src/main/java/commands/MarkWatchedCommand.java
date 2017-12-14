package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Video;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class MarkWatchedCommand extends Command {

    public MarkWatchedCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Video toMark = getMarkable();

        if (toMark == null) {
            return;
        }

        try {
            videoDAO.markAsChecked(toMark);
            System.out.println("\nVideo marked as watched!");
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Video getMarkable() {
        List<Video> videos = browseVideos();
        System.out.println("\nWhich video do you want to mark as watched?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(videos.size());

        if (index == -1) {
            return null;
        }

        return videos.get(index - 1);
    }
}

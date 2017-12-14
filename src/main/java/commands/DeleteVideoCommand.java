package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Video;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class DeleteVideoCommand extends Command {

    public DeleteVideoCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Video toDelete = getDeletable();

        if (toDelete == null) {
            return;
        }

        try {
            if (videoDAO.delete(toDelete)) {
                System.out.println("\nVideo deleted!");
            } else {
                System.out.println("\nNon-existent video cannot be deleted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Video getDeletable() {
        List<Video> videos = browseVideos();
        System.out.println("\nWhich video do you want to delete?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(videos.size());

        if (index == -1) {
            return null;
        }

        return videos.get(index - 1);
    }
}

package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Video;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public class EditVideoCommand extends Command {

    public EditVideoCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Video video = getEditable();

        if (video == null) {
            return;
        }

        Video newVideo = getEdited(video);
        try {
            videoDAO.update(video, newVideo);
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Video getEditable() {
        List<Video> videos = browseVideos();
        System.out.println("\nWhich video do you want to edit?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(videos.size());

        if (index == -1) {
            return null;
        }

        return videos.get(index - 1);
    }

    private Video getEdited(Video video) {
        String title = getUserInput(
                "Enter new title (leave empty if no need to edit):");
        if (title.isEmpty()) {
            title = video.getTitle();
        }

        String url = getUserInput(
                "Enter new url (leave empty if no need to edit):");
        if (url.isEmpty()) {
            url = video.getURL();
        }

        return new Video(title, url);
    }
}

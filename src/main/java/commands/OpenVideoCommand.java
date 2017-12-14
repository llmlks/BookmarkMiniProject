package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Video;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

class OpenVideoCommand extends Command {

    private final Desktop desktop;

    public OpenVideoCommand(AbstractDatabase database, BufferedReader buff, Desktop dt) {
        super(database, buff);
        desktop = dt;
    }

    @Override
    public void run() {
        URI uri = getURIToOpen();
        try {
            desktop.browse(uri);
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private URI getURIToOpen() {
        List<Video> videos = browseVideos();
        System.out.println("\nWhich video would you like to open?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(videos.size());

        if (index == -1) {
            return null;
        }

        String url = videos.get(index - 1).getURL();
        url = formatURL(url);

        try {

            return new URI(url);
        } catch (URISyntaxException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String formatURL(String url) {
        if (url.startsWith("https://") || url.startsWith("http://")) {
            return url;
        }
        return "https://" + url;
    }
}

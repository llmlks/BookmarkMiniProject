package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkmodels.Video;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddVideoCommand extends Command {

    public AddVideoCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        Video newVideo = getNew();

        try {
            if (videoDAO.create(newVideo) == null) {
                System.out.println("\nVideo has already been added in the library");
            } else {
                System.out.println("\nVideo added!");
            }
        } catch (SQLException exe) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exe);
        }
    }

    private Video getNew(){
        String url = getUserInputNotEmpty("\nURL:",
                "Url cannot be empty. Enter URL again:");
        String name = getUserInput("Name:");

        return new Video(url, name);
    }
}

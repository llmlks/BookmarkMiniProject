package ui;

import bookmarkdb.Database;
import commands.Command;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import commands.CommandContainer;
import java.awt.Desktop;

public class UI implements Runnable {

    private final BufferedReader br;
    private final CommandContainer commands;

    public UI(Database database) {
        br = new BufferedReader(new InputStreamReader(System.in));
        commands = new CommandContainer(database, br, Desktop.getDesktop());
    }

    public UI(Database database, BufferedReader buff, Desktop desktop) {
        this.br = buff;
        commands = new CommandContainer(database, br, desktop);
    }

    @Override
    public void run() {

        while (true) {
            System.out.println("\nTo list all your bookmarks enter \"browse\".\n"
                    + "To search all your bookmarks enter \"search\"\n"
                    + "To search or browse only unread/not listened/not watched bookmarks type \"-u\" after search or browse command.\n"
                    + "To add a bookmark enter \"add <type>\".\n"
                    + "To edit a bookmark enter \"edit <type>\".\n"
                    + "To delete a bookmak enter \"delete <type>\".\n"
                    + "To mark bookmark as read/listened/watched enter \"mark <type> checked\". "
                    + "To open a video enter \"open video\".\n"
                    + "To quit the program enter \"quit\".\n\n"
                    + "Available types: book, podcast, video\n\n"
                    + "What to do?\n");
            String command;
            try {
                command = br.readLine();
                if (command.equals("quit")) {
                    break;
                } else {
                    Command executable = commands.getCommand(command);
                    if (executable == null) {
                        System.out.println("\nPlease enter a valid command\n");
                    } else {
                        executable.run();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

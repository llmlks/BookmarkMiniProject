package commands;

import bookmarkdb.Database;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private final Map<String, Command> commands;
    private final Database database;
    private final BufferedReader br;
    private final Desktop desktop;
    private BrowseCommand browse;

    public CommandContainer(Database db, BufferedReader buff, Desktop dt) {
        this.br = buff;
        this.database = db;
        this.commands = new HashMap<>();
        this.desktop = dt;
        initialiseCommands();
    }

    private void initialiseCommands() {
        browse = new BrowseCommand(database, br);
        commands.put("browse", browse);

        commands.put("add book", new AddBookCommand(database, br));
        commands.put("add podcast", new AddPodcastCommand(database, br));
        commands.put("add video", new AddVideoCommand(database, br));

        commands.put("edit book", new EditBookCommand(database, br));
        commands.put("edit podcast", new EditPodcastCommand(database, br));
        commands.put("edit video", new EditVideoCommand(database, br));

        commands.put("delete book", new DeleteBookCommand(database, br));
        commands.put("delete podcast", new DeletePodcastCommand(database, br));
        commands.put("delete video", new DeleteVideoCommand(database, br));

        commands.put("mark book checked", new MarkReadCommand(database, br));
        commands.put("mark podcast checked", new MarkListenedCommand(database, br));
        commands.put("mark video checked", new MarkWatchedCommand(database, br));

        commands.put("open video", new OpenVideoCommand(database, br, desktop));
    }

    /**
     * Searches {@link commands} for a Command that matches the String given as
     * parameter.
     * 
     * @param command String base on which the command is selected
     * @return Command to be executed based on parameter
     */
    public Command getCommand(String command) {
        if (command.contains("search") || command.contains("browse")) {
            return getBrowseCommand(command);
        }
        return commands.get(command);
    }

    private Command getBrowseCommand(String command) {
        boolean search = command.contains("search");
        boolean onlyUnchecked = command.contains("-u");

        browse.setOnlyUnchecked(onlyUnchecked);
        browse.setSearchWithKeyword(search);

        return browse;
    }
}

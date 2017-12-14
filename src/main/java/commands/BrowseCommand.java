package commands;

import bookmarkdb.AbstractDatabase;
import java.io.BufferedReader;

public class BrowseCommand extends Command {

    private boolean onlyUnchecked;
    private boolean search;

    public BrowseCommand(AbstractDatabase database, BufferedReader buff) {
        super(database, buff);
    }

    @Override
    public void run() {
        browse(search, onlyUnchecked);
    }

    private void browse(boolean search, boolean onlyUnchecked) {
        String keyword = "";

        if (search) {
            keyword = getUserInput("\nPlease enter a keyword: ");
        }

        browseBooks(keyword, onlyUnchecked);
        browsePodcasts(keyword, onlyUnchecked);
        browseVideos(keyword, onlyUnchecked);
    }

    protected void setSearchWithKeyword(boolean search) {
        this.search = search;
    }

    protected void setOnlyUnchecked(boolean unchecked) {
        this.onlyUnchecked = unchecked;
    }
}

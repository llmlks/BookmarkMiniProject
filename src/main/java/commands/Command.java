package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkdb.BookDAO;
import bookmarkdb.BookTagDAO;
import bookmarkdb.BookmarkTagDAO;
import bookmarkdb.PodcastDAO;
import bookmarkdb.VideoDAO;
import bookmarkmodels.Book;
import bookmarkmodels.Podcast;
import bookmarkmodels.Video;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.UI;

public abstract class Command {

    protected BufferedReader br;
    protected BookDAO bookDAO;
    protected VideoDAO videoDAO;
    protected PodcastDAO podcastDAO;
    protected BookmarkTagDAO tagDAO;
    protected BookTagDAO bookTagDAO;

    public Command(AbstractDatabase database, BufferedReader buff) {
        this.br = buff;
        this.bookDAO = new BookDAO(database);
        this.podcastDAO = new PodcastDAO(database);
        this.videoDAO = new VideoDAO(database);
        this.tagDAO = new BookmarkTagDAO(database);
        this.bookTagDAO = new BookTagDAO(database, bookDAO, tagDAO);
    }

    public abstract void run();

    protected List<Book> browseBooks() {
        return browseBooks("", false);
    }

    protected List<Book> browseBooks(String keyword, boolean onlyUnread) {
        System.out.println("");
        try {
            List<Book> books = keyword.isEmpty()
                    ? bookDAO.findAll()
                    : bookDAO.findAllWithKeyword(keyword);

            if (onlyUnread) {
                books = bookDAO.filterOnlyUnchecked(books);
            }

            for (int i = 0; i < books.size(); i++) {
                System.out.print((i + 1) + " " + books.get(i));
                System.out.println(", tags: "
                        + bookTagDAO.printTags(bookTagDAO.findAll(books.get(i))));
            }
            return books;
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected List<Podcast> browsePodcasts() {
        return browsePodcasts("", false);
    }

    protected List<Podcast> browsePodcasts(String keyword, boolean onlyUnread) {
        System.out.println("");
        try {
            List<Podcast> podcasts = keyword.isEmpty()
                    ? podcastDAO.findAll()
                    : podcastDAO.findAllWithKeyword(keyword);

            if (onlyUnread) {
                podcasts = podcastDAO.filterOnlyUnchecked(podcasts);
            }

            for (int i = 0; i < podcasts.size(); i++) {
                System.out.println((i + 1) + " " + podcasts.get(i));
            }
            return podcasts;
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected List<Video> browseVideos() {
        return browseVideos("", false);
    }

    protected List<Video> browseVideos(String keyword, boolean onlyUnread) {
        System.out.println("");
        try {
            List<Video> videos = keyword.isEmpty()
                    ? videoDAO.findAll()
                    : videoDAO.findAllWithKeyword(keyword);

            if (onlyUnread) {
                videos = videoDAO.filterOnlyUnchecked(videos);
            }

            for (int i = 0; i < videos.size(); i++) {
                System.out.println((i + 1) + " " + videos.get(i));
            }
            return videos;
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected int getRowNumber(int maxRowNumber) {
        boolean noRowNumber = true;
        int index = -1;
        while (noRowNumber) {
            try {
                String input = br.readLine();
                if (input.equals("cancel")) {
                    return -1;
                }
                index = Integer.parseInt(input);
                if (index <= 0 || index > maxRowNumber) {
                    System.out.println("\nThe row number entered was invalid");
                } else {
                    noRowNumber = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("\nPlease enter a number");
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        return index;
    }

    protected String getUserInputNotEmpty(String prompt, String rePrompt) {
        String input = "";
        try {
            System.out.println(prompt);
            input = br.readLine();
            while (input.isEmpty()) {
                System.out.println(rePrompt);
                input = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return input;
    }

    protected String getUserInput(String prompt) {
        String input = "";
        try {
            System.out.println(prompt);
            input = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return input;
    }
}

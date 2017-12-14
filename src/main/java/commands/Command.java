package commands;

import bookmarkdb.AbstractDatabase;
import bookmarkdb.BookDAO;
import bookmarkdb.BookTagDAO;
import bookmarkdb.BookmarkTagDAO;
import bookmarkdb.PodcastDAO;
import bookmarkdb.PodcastTagDAO;
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
    protected PodcastTagDAO podTagDAO;

    public Command(AbstractDatabase database, BufferedReader buff) {
        this.br = buff;
        this.bookDAO = new BookDAO(database);
        this.podcastDAO = new PodcastDAO(database);
        this.videoDAO = new VideoDAO(database);
        this.tagDAO = new BookmarkTagDAO(database);
        this.bookTagDAO = new BookTagDAO(database, bookDAO, tagDAO);
        this.podTagDAO = new PodcastTagDAO(database, podcastDAO, tagDAO);
    }

    /**
     * Runs the command.
     */
    public abstract void run();

    /**
     * Returns a list of all bookmarks of type book in the database by calling
     * {@link #browseBooks(java.lang.String, boolean)} with parameters "" and
     * {@code false}.
     * 
     * @return List of books
     */
    protected List<Book> browseBooks() {
        return browseBooks("", false);
    }

    /**
     * Searches all bookmarks of type book in the database and returns a list of
     * books based on the given parameters.
     * 
     * @param keyword Keyword based on which the books in the database are 
     * searched
     * @param onlyUnread {@code true} if the results should be filtered to only
     * include those books that haven't been marked read
     * @return List of books
     */
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

    /**
     * Returns a list of all bookmarks of type podcast in the database by calling
     * {@link #browsePodcasts(java.lang.String, boolean)} with parameters "" and
     * {@code false}.
     * 
     * @return List of podcasts
     */
    protected List<Podcast> browsePodcasts() {
        return browsePodcasts("", false);
    }

    /**
     * Searches all bookmarks of type podcast in the database and returns a list
     * of podcasts based on the given parameters.
     * 
     * @param keyword Keyword based on which the podcasts in the database are 
     * searched
     * @param onlyUnread {@code true} if the results should be filtered to only
     * include those podcasts that haven't been marked listened
     * @return List of podcasts
     */
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

    /**
     * Returns a list of all bookmarks of type video in the database by calling
     * {@link #browseVideos(java.lang.String, boolean)} with parameters "" and
     * {@code false}.
     * 
     * @return List of videos
     */
    protected List<Video> browseVideos() {
        return browseVideos("", false);
    }

    /**
     * Searches all bookmarks of type video in the database and returns a list 
     * of videos based on the given parameters.
     * 
     * @param keyword Keyword based on which the videos in the database are 
     * searched
     * @param onlyUnread {@code true} if the results should be filtered to only
     * include those videos that haven't been marked read
     * @return List of videos
     */
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

    /**
     * Returns a number formatted from user input, or keeps prompting for it 
     * until user either inputs "cancel" or a valid number, smaller than or 
     * equal to the given maximum.
     * 
     * @param maxRowNumber Number of rows from which to choose.
     * @return row number inputted by user, or -1 if user entered "cancel"
     */
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

    /**
     * Prompts the user for an input with the given {@link prompt}, and returns
     * the entered value, or (if entered value is empty) re-prompts the user 
     * until a non-empty value is entered.
     * 
     * @param prompt First prompt as String
     * @param rePrompt Re-prompt as String
     * @return Entered non-empty value
     */
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

    /**
     * Prompts the user for an input with the given {@link prompt}, and returns
     * the entered value.
     * 
     * @param prompt First prompt as String
     * @return Entered non-empty value
     */
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

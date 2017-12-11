package ui;

import bookmarkdb.BookDAO;
import bookmarkdb.Database;
import bookmarkdb.PodcastDAO;
import bookmarkdb.VideoDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import bookmarkmodels.Book;
import bookmarkmodels.Podcast;
import bookmarkmodels.Video;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class UI implements Runnable {

    private BufferedReader br;
    private BookDAO bookDAO;
    private PodcastDAO podcastDAO;
    private VideoDAO videoDAO;
    private Desktop desktop;

    public UI(Database database) {
        br = new BufferedReader(new InputStreamReader(System.in));
        bookDAO = new BookDAO(database);
        podcastDAO = new PodcastDAO(database);
        videoDAO = new VideoDAO(database);
        desktop = Desktop.getDesktop();
    }

    public UI(Database database, BufferedReader buff, Desktop dt) {
        this.br = buff;
        bookDAO = new BookDAO(database);
        podcastDAO = new PodcastDAO(database);
        videoDAO = new VideoDAO(database);
        desktop = dt;
    }

    @Override
    public void run() {

        while (true) {
            System.out.println("\nTo list all your bookmarks type \"browse\".\n"
                    + "To add a book type \"add book\".\n"
                    + "To edit a book type \"edit book\".\n"
                    + "To delete a book type \"delete book\".\n"
                    + "To mark book as read \"mark read\".\n"
                    + "To add a podcast type \"add podcast\".\n"
                    + "To edit a podcast type \"edit podcast\".\n"
                    + "To delete a podcast type \"delete podcast\".\n"
                    + "To mark podcast as listened \"mark listened\".\n"
                    + "To add a video type \"add video\".\n"
                    + "To edit a video type \"edit video\".\n"
                    + "To delete a video type \"delete video\".\n"
                    + "To open a video type \"open video\".\n"
                    + "To mark video as watched \"mark watched\".\n"
                    + "To quit the program type \"quit\".\n\n"
                    + "What to do?\n");

            String command;
            try {
                command = br.readLine();
                if (command.equals("quit")) {
                    break;
                } else if (command.equals("browse")) {
                    commandBrowse();
                } else if (command.equals("add book")) {
                    commandAddBook();
                } else if (command.equals("edit book")) {
                    commandEditBook();
                } else if (command.equals("delete book")) {
                    commandDeleteBook();
                } else if (command.equals("add podcast")) {
                    commandAddPodcast();
                } else if (command.equals("mark read")) {
                    commandMarkAsRead();
                } else if (command.equals("edit podcast")) {
                    commandEditPodcast();
                } else if (command.equals("delete podcast")) {
                    commandDeletePodcast();
                } else if (command.equals("mark listened")) {
                    commandMarkAsListened();
                } else if (command.equals("add video")) {
                    commandAddVideo();
                } else if (command.equals("edit video")) {
                    commandEditVideo();
                } else if (command.equals("delete video")) {
                    commandDeleteVideo();
                } else if (command.equals("open video")) {
                    commandOpenVideoURL();
                } else if (command.equals("mark watched")) {
                    commandMarkAsWatched();
                }
            } catch (IOException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void commandBrowse() throws IOException {
        browseBooks();
        browsePodcasts();
        browseVideos();
    }

    private List<Book> browseBooks() {
        System.out.println("");
        try {
            List<Book> books = bookDAO.findAll();
            for (int i = 0; i < books.size(); i++) {
                System.out.println((i + 1) + " " + books.get(i));
            }
            return books;
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Podcast> browsePodcasts() {
        System.out.println("");
        try {
            List<Podcast> podcasts = podcastDAO.findAll();
            for (int i = 0; i < podcasts.size(); i++) {
                System.out.println((i + 1) + " " + podcasts.get(i));
            }
            return podcasts;
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Video> browseVideos() {
        System.out.println("");
        try {
            List<Video> videos = videoDAO.findAll();
            for (int i = 0; i < videos.size(); i++) {
                System.out.println((i + 1) + " " + videos.get(i));
            }
            return videos;
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void commandAddBook() throws IOException {
        String author;
        String title;
        String ISBN;
        System.out.println("");
        System.out.println("Title:");
        title = br.readLine();
        while (title.isEmpty()) {
            System.out.println("Title cannot be empty. Enter title again:");
            title = br.readLine();
        }
        System.out.println("Author:");
        author = br.readLine();
        while (author.isEmpty()) {
            System.out.println("Author cannot be empty. Enter author again:");
            author = br.readLine();
        }
        System.out.println("ISBN:");
        ISBN = br.readLine();
        try {
            if (bookDAO.create(new Book(title, author, ISBN)) == null) {
                System.out.println("\nBook has already been added in the library");
            } else {
                System.out.println("\nBook added!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void commandEditBook() throws IOException {
        List<Book> books = browseBooks();
        System.out.println("\nWhich book do you want to edit?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(books.size());

        if (index == -1) {
            return;
        }

        Book toEdit = books.get(index - 1);
        editBook(toEdit);
    }

    private void editBook(Book book) throws IOException {
        System.out.println("Enter new title (leave empty if no need to edit):");
        String title = br.readLine();
        if (title.isEmpty()) {
            title = book.getTitle();
        }
        System.out.println("Enter new author (leave empty if no need to edit):");
        String author = br.readLine();
        if (author.isEmpty()) {
            author = book.getAuthor();
        }
        System.out.println("Enter new ISBN (leave empty if no need to edit):");
        String ISBN = br.readLine();
        if (ISBN.isEmpty()) {
            ISBN = book.getISBN();
        }
        try {
            bookDAO.update(book, new Book(title, author, ISBN));
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void commandDeleteBook() throws IOException {
        List<Book> books = browseBooks();
        System.out.println("\nWhich book do you want to delete?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(books.size());

        if (index == -1) {
            return;
        }

        Book toDelete = books.get(index - 1);

        try {
            if (bookDAO.delete(toDelete)) {
                System.out.println("\nBook deleted!");
            } else {
                System.out.println("\nNon-existent book cannot be deleted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void commandAddPodcast() throws IOException {
        String podName;
        String podAuthor;
        String podTitle;
        String podUrl;
        System.out.println("");
        System.out.println("Name:");
        podName = br.readLine();
        while (podName.isEmpty()) {
            System.out.println("Name cannot be empty. Enter name again:");
            podName = br.readLine();
        }
        System.out.println("Author:");
        podAuthor = br.readLine();
        while (podAuthor.isEmpty()) {
            System.out.println("Author cannot be empty. Enter author again:");
            podAuthor = br.readLine();
        }
        System.out.println("Title:");
        podTitle = br.readLine();
        while (podTitle.isEmpty()) {
            System.out.println("Title cannot be empty. Enter title again:");
            podTitle = br.readLine();
        }
        System.out.println("Url:");
        podUrl = br.readLine();
        try {
            if (podcastDAO.create(new Podcast(podName, podAuthor, podTitle, podUrl)) == null) {
                System.out.println("\nPodcast has already been added in the library");
            } else {
                System.out.println("\nPodcast added!");
            }
        } catch (SQLException exe) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, exe);
        }

    }

    private void commandEditPodcast() throws IOException {
        List<Podcast> podcasts = browsePodcasts();
        System.out.println("\nWhich podcast do you want to edit?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(podcasts.size());

        if (index == -1) {
            return;
        }

        Podcast toEdit = podcasts.get(index - 1);
        editPodcast(toEdit);
    }

    private void editPodcast(Podcast podcast) throws IOException {
        System.out.println("Enter new name (leave empty if no need to edit):");
        String name = br.readLine();
        if (name.isEmpty()) {
            name = podcast.getName();
        }
        System.out.println("Enter new author (leave empty if no need to edit):");
        String author = br.readLine();
        if (author.isEmpty()) {
            author = podcast.getAuthor();
        }
        System.out.println("Enter new title (leave empty if no need to edit):");
        String title = br.readLine();
        if (title.isEmpty()) {
            title = podcast.getTitle();
        }
        System.out.println("Enter new url (leave empty if no need to edit):");
        String url = br.readLine();
        if (url.isEmpty()) {
            url = podcast.getUrl();
        }
        try {
            podcastDAO.update(podcast, new Podcast(name, author, title, url));
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void commandDeletePodcast() throws IOException {
        List<Podcast> podcasts = browsePodcasts();
        System.out.println("\nWhich podcast do you want to delete?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(podcasts.size());

        if (index == -1) {
            return;
        }

        Podcast toDelete = podcasts.get(index - 1);

        try {
            if (podcastDAO.delete(toDelete)) {
                System.out.println("\nPodcast deleted!");
            } else {
                System.out.println("\nNon-existent podcast cannot be deleted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void commandOpenVideoURL() {
        System.out.println("");
        try {
            List<Video> videos = browseVideos();
            System.out.println("\nWhich video would you like to open?"
                    + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
            int index = getRowNumber(videos.size());

            if (index == -1) {
                return;
            }

            String url = videos.get(index - 1).getURL();
            url = formatURL(url);

            URI uri = new URI(url);
            desktop.browse(uri);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getRowNumber(int maxRowNumber) throws IOException {
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
            }
        }
        return index;
    }

    private String formatURL(String url) {
        if (url.startsWith("https://") || url.startsWith("http://")) {
            return url;
        }
        return "https://" + url;
    }

    private void commandAddVideo() throws IOException {
        String url;
        String name;
        System.out.println("\nUrl:");
        url = br.readLine();
        while (url.isEmpty()) {
            System.out.println("Url cannot be empty. Enter URL again:");
            url = br.readLine();
        }
        System.out.println("Name:");
        name = br.readLine();
        try {
            if (videoDAO.create(new Video(url, name)) == null) {
                System.out.println("\nVideo has already been added in the library");
            } else {
                System.out.println("\nVideo added!");
            }
        } catch (SQLException exe) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, exe);
        }

    }

    private void commandEditVideo() throws IOException {
        List<Video> videos = browseVideos();
        System.out.println("\nWhich video do you want to edit?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(videos.size());

        if (index == -1) {
            return;
        }

        Video toEdit = videos.get(index - 1);
        editVideo(toEdit);
    }

    private void editVideo(Video video) throws IOException {
        System.out.println("Enter new title (leave empty if no need to edit):");
        String title = br.readLine();
        if (title.isEmpty()) {
            title = video.getTitle();
        }
        System.out.println("Enter new url (leave empty if no need to edit):");
        String url = br.readLine();
        if (url.isEmpty()) {
            url = video.getURL();
        }
        try {
            videoDAO.update(video, new Video(url, title));
        } catch (SQLException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void commandDeleteVideo() throws IOException {
        List<Video> videos = browseVideos();
        System.out.println("\nWhich video do you want to delete?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(videos.size());

        if (index == -1) {
            return;
        }

        Video toDelete = videos.get(index - 1);

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

    private void commandMarkAsRead() throws IOException {
        List<Book> books = browseBooks();
        System.out.println("\nWhich book do you want to mark as read?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(books.size());

        if (index == -1) {
            return;
        }

        Book toMark = books.get(index - 1);
        bookDAO.marksAsChecked(toMark);
    }

    private void commandMarkAsWatched() throws IOException {
        List<Video> videos = browseVideos();
        System.out.println("\nWhich video do you want to mark as watched?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(videos.size());

        if (index == -1) {
            return;
        }

        Video toMark = videos.get(index - 1);
        videoDAO.marksAsChecked(toMark);
    }

    private void commandMarkAsListened() throws IOException {
        List<Podcast> podcasts = browsePodcasts();
        System.out.println("\nWhich podcast do you want to mark as listened?"
                + "\nPlease enter a row number or \"cancel\" to return to main menu: ");
        int index = getRowNumber(podcasts.size());

        if (index == -1) {
            return;
        }

        Podcast toMark = podcasts.get(index - 1);
        podcastDAO.marksAsChecked(toMark);
    }
}

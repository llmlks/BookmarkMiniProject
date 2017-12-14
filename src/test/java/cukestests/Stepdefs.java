package cukestests;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import bookmarkdb.BookDAO;
import bookmarkdb.Connector;
import bookmarkdb.Database;
import bookmarkdb.PodcastDAO;
import bookmarkdb.VideoDAO;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import static org.junit.Assert.*;
import java.util.List;
import bookmarkmodels.Book;
import bookmarkmodels.Podcast;
import bookmarkmodels.Video;

import java.awt.Desktop;
import java.net.URI;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import ui.UI;

public class Stepdefs {

    Database database;
    UI ui;
    BufferedReader buffer;
    BookDAO bookDao;
    PodcastDAO podcastDao;
    VideoDAO videoDao;
    ByteArrayInputStream inputStream;
    ByteArrayOutputStream outputStream;
    PrintStream standardOut;
    InputStream standardIn;
    String input;
    Desktop desktop;

    /**
     * Before test execution 
     * 1 standard system input and output are stored in class variables
     * {@link #standardIn} and {@link #standardOut}, respectively
     * 2 class variable {@link #input}, which is later used to simulate input, is
     * set to empty string
     * 3 new database file is created, a new {@link Database} object is
     * initialised with it and stored in variable {@link #database}.
     */
    @Before
    public void setUp() {
        input = "";
        standardIn = System.in;
        standardOut = System.out;
        database = new Database(new Connector("jdbc:sqlite:cukesTest.db"));
        database.init();
        bookDao = new BookDAO(database);
        podcastDao = new PodcastDAO(database);
        videoDao = new VideoDAO(database);
        desktop = mock(Desktop.class);
    }

    /**
     * After test execution system input and output are restored to their
     * original values and test database file is deleted.
     */
    @After
    public void tearDown() throws IOException {
        System.setIn(standardIn);
        System.setOut(standardOut);
        Files.deleteIfExists(FileSystems.getDefault().getPath("cukesTest.db"));
    }

    @Given("^command \"([^\"]*)\" is selected$")
    public void command_selected(String command) throws Throwable {
        addInputLine(command);
    }
    
    @Given("^book with title \"([^\"]*)\", author \"([^\"]*)\" and ISBN \"([^\"]*)\" has been added$")
    public void book_has_been_added(String title, String author, String ISBN) throws Throwable {
        addInputLine("add book");
        addInputLine(title);
        addInputLine(author);
        addInputLine(ISBN);
    }
    
    @Given("^podcast with name \"([^\"]*)\" and author \"([^\"]*)\" and title \"([^\"]*)\" and URL \"([^\"]*)\" has been added$")
    public void podcast_has_been_added(String name, String author, String title, String ISBN) throws Throwable {
        addInputLine("add podcast");
        addInputLine(name);
        addInputLine(author);
        addInputLine(title);
        addInputLine(ISBN);
    }

    @When("^title \"([^\"]*)\" is entered$")
    public void title_is_entered(String title) throws Throwable {
        addInputLine(title);
    }
    
    @Given ("^video with URL \"([^\"]*)\" and name \"([^\"]*)\" has been added$")
    public void video_has_been_added(String url, String name) throws Throwable {
        addInputLine("add video");
        addInputLine(url);
        addInputLine(name);
    }
    
    @When("^author \"([^\"]*)\" is entered$")
    public void author_is_entered(String author) throws Throwable {
        addInputLine(author);
    }
    
    @When("^ISBN \"([^\"]*)\" is entered$")
    public void ISBN_is_entered(String ISBN) throws Throwable {
        addInputLine(ISBN);
    }
    
    @When("^name \"([^\"]*)\" is entered$")
    public void name_is_entered(String name) throws Throwable {
    	addInputLine(name);
    }

    @When("^URL \"([^\"]*)\" is entered$")
    public void URL_is_entered(String url) {
        addInputLine(url);
    }
    
    @Then("^title is asked again$")
    public void title_is_asked_again() throws Throwable {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter title again"));
    }
    
    @Then("^author is asked again$")
    public void author_is_asked_again() throws Throwable {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter author again"));
    }
    
    @Then("^name is asked again$")
    public void name_is_asked_again() throws Throwable {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter name again"));
    }
    
    @Then("^URL is asked again$")
    public void URL_is_asked_again() throws Throwable {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Enter URL again"));
    }

    @When("^row number \"([^\"]*)\" is entered$")
    public void row_number_is_entered(String row) {
        addInputLine(row);
    }

    @When("^user enters \"([^\"]*)\"$")
    public void user_enters(String input) {
        addInputLine(input);
    }

    @Then("^new book is added with title \"([^\"]*)\" and author \"([^\"]*)\" and ISBN \"([^\"]*)\"$")
    public void new_book_is_added(String title, String author, String ISBN) throws Throwable {
        Book addedBook = new Book(title, author, ISBN);

        runApplication();

        List<Book> allBooks = bookDao.findAll();

        assertEquals(allBooks.size(), 1);
        assertEquals(allBooks.get(0), addedBook);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Book added!"));
    }

    @Then("^new podcast is added with name \"([^\"]*)\" and author \"([^\"]*)\" and title \"([^\"]*)\" and URL \"([^\"]*)\"$")
    public void new_podcast_is_added(String name, String author, String title, String URL) throws Throwable {
        Podcast addedPodcast = new Podcast(name, author, title, URL);

        runApplication();

        List<Podcast> allPodcasts = podcastDao.findAll();

        assertEquals(allPodcasts.size(), 1);
        assertEquals(allPodcasts.get(0), addedPodcast);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Podcast added!"));
    }
    
    @Then("^new video is added with URL \"([^\"]*)\" and title \"([^\"]*)\"")
    public void new_video_is_added(String url, String title) throws Throwable {
        Video addedVideo = new Video(url, title);

        runApplication();

        List<Video> allVideos = videoDao.findAll();

        assertEquals(allVideos.size(), 1);
        assertEquals(allVideos.get(0), addedVideo);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Video added!"));
    }
    
    @Then("^only one book is added with title \"([^\"]*)\" and author \"([^\"]*)\" and ISBN \"([^\"]*)\"$")
    public void only_one_book_is_added(String title, String author, String ISBN) throws Throwable {
        Book addedBook = new Book(title, author, ISBN);

        runApplication();

        List<Book> allBooks = bookDao.findAll();

        assertEquals(allBooks.size(), 1);
        assertEquals(allBooks.get(0), addedBook);
        String output = outputStream.toString();
        
        assertTrue(output.contains("Book added!"));
        assertTrue(output.contains("Book has already been added in the library"));
    }
    
    @Then("^only one podcast is added with name \"([^\"]*)\" and author \"([^\"]*)\" and title \"([^\"]*)\" and URL \"([^\"]*)\"$")
    public void only_one_podcast_is_added(String name, String author, String title, String URL) throws Throwable {
        Podcast addedPodcast = new Podcast(name, author, title, URL);

        runApplication();

        List<Podcast> allPodcasts = podcastDao.findAll();

        assertEquals(allPodcasts.size(), 1);
        assertEquals(allPodcasts.get(0), addedPodcast);
        String output = outputStream.toString();
        
        assertTrue(output.contains("Podcast added!"));
        assertTrue(output.contains("Podcast has already been added in the library"));
    }
    
    @Then("^only one podcast is added with name \"([^\"]*)\" and author \"([^\"]*)\" and title \"([^\"]*)\"$")
    public void only_one_podcast_is_added_with_name_and_author_and_title(String name, String author, String title) throws Throwable {
        runApplication();

        List<Podcast> allPodcasts = podcastDao.findAll();

        assertEquals(allPodcasts.size(), 1);
        assertEquals(allPodcasts.get(0).getName(), name);
        assertEquals(allPodcasts.get(0).getAuthor(), author);
        assertEquals(allPodcasts.get(0).getTitle(), title);
        String output = outputStream.toString();
        
        assertTrue(output.contains("Podcast added!"));
        assertTrue(output.contains("Podcast has already been added in the library"));
    }

    @Then("^only one book is added with title \"([^\"]*)\" and author \"([^\"]*)\"$")
    public void only_one_book_is_added_with_title_and_author(String title, String author) throws Throwable {
        runApplication();

        List<Book> allBooks = bookDao.findAll();

        assertEquals(allBooks.size(), 1);
        assertEquals(allBooks.get(0).getAuthor(), author);
        assertEquals(allBooks.get(0).getTitle(), title);
        String output = outputStream.toString();
        
        assertTrue(output.contains("Book added!"));
        assertTrue(output.contains("Book has already been added in the library"));
    }
    
    @Then("^only one video is added with URL \"([^\"]*)\" and title \"([^\"]*)\"$")
    public void only_one_video_is_added_with_URL_and_title(String URL, String title) throws Throwable {
        runApplication();

        List<Video> allVideos = videoDao.findAll();

        assertEquals(allVideos.size(), 1);
        assertEquals(allVideos.get(0).getURL(), URL);
        assertEquals(allVideos.get(0).getTitle(), title);
        String output = outputStream.toString();
        
        assertTrue(output.contains("Video added!"));
        assertTrue(output.contains("Video has already been added in the library"));
    }

    @Then("^book with title \"([^\"]*)\" and author \"([^\"]*)\" and ISBN \"([^\"]*)\" is not added$")
    public void book_without_title_or_author_is_not_added(String title, String author, String ISBN) throws Throwable {
        Book notAddedBook = new Book(title, author, ISBN);
        runApplication();

        List<Book> allBooks = bookDao.findAll();
        assertFalse(allBooks.contains(notAddedBook));

        String output = outputStream.toString();
        assertTrue(output.contains("Either title or author is not valid (cannot be empty)"));
    }
    
    @Then("^podcast with name \"([^\"]*)\" and author \"([^\"]*)\" and title \"([^\"]*)\" and URL \"([^\"]*)\" is not added$")
    public void podcast_without_name_or_author_or_title_is_not_added(String name, String author, String title, String URL) throws Throwable {
        Podcast notAddedPodcast = new Podcast(name, author, title, URL);
        runApplication();

        List<Podcast> allPodcasts = podcastDao.findAll();
        assertFalse(allPodcasts.contains(notAddedPodcast));

        String output = outputStream.toString();
        assertTrue(output.contains("Either name, author or title is invalid (all must be non-empty)"));
    }
    
    @Then("^video with URL \"([^\"]*)\" and title \"([^\"]*)\" is not added$")
    public void video_without_URL_is_not_added(String URL, String title) throws Throwable {
        Video notAddedVideo = new Video(URL, title);
        runApplication();

        List<Video> allVideos = videoDao.findAll();
        assertFalse(allVideos.contains(notAddedVideo));

        String output = outputStream.toString();
        assertTrue(output.contains("URL cannot be empty"));
    }
    
    @Then("^empty list of books is printed$")
    public void empty_list_of_books_is_printed() throws Throwable {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(!output.contains("Book:"));
    }
    
    @Then("^book with title \"([^\"]*)\" and author \"([^\"]*)\" and ISBN \"([^\"]*)\" is printed$")
    public void book_is_printed(String title, String author, String ISBN) throws Throwable {
        Book book = new Book(title, author, ISBN);

        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains(book.toString()));
    }
    
    @Then("^book with title \"([^\"]*)\" and book with title \"([^\"]*)\" is printed$")
    public void two_books_are_printed(String title1, String title2) throws Throwable {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains(title1) && output.contains(title2));
    }

    @Then("^podcast with title \"([^\"]*)\" and book with title \"([^\"]*)\" are printed$")
    public void podcast_and_book_are_printed(String podTitle, String bookTitle) {
        runApplication();

        String output = outputStream.toString();
        assertTrue(output.contains(podTitle) && output.contains(bookTitle));
    }

    @Then("^book with title \"([^\"]*)\" and author \"([^\"]*)\" and ISBN \"([^\"]*)\" is deleted$")
    public void added_book_is_deleted(String title, String author, String ISBN) throws Throwable {
        runApplication();

        String output = outputStream.toString();
        assertTrue(output.contains("Book deleted!"));
    }

    @Then("^podcast with name \"([^\"]*)\" and author \"([^\"]*)\" and title \"([^\"]*)\" and URL \"([^\"]*)\" is deleted$")
    public void added_podcast_is_deleted(String name, String author, String title, String URL) throws Throwable {
        runApplication();

        String output = outputStream.toString();
        assertTrue(output.contains("Podcast deleted!"));
    }
    
    @Then("^video with URL \"([^\"]*)\" and name \"([^\"]*)\" is deleted$")
    public void added_video_is_deleted(String URL, String name) throws Throwable {
        runApplication();

        String output = outputStream.toString();
        assertTrue(output.contains("Video deleted!"));
    }
    
    @Then("^no books are deleted$")
    public void no_books_are_deleted() throws Throwable {

        runApplication();

        List<Book> allBooks = bookDao.findAll();
        assertEquals(allBooks.size(), 1);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Non-existent book cannot be deleted"));
    }
    
    @Then("^no podcasts are deleted$")
    public void no_podcasts_are_deleted() throws Throwable {

        runApplication();

        List<Podcast> allPodcasts = podcastDao.findAll();
        assertEquals(allPodcasts.size(), 1);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Non-existent podcast cannot be deleted"));
    }

    @Then("^URL \"([^\"]*)\" is opened in browser$")
    public void URL_is_opened_in_browser(String url) throws Throwable {

        runApplication();

        String output = outputStream.toString();
        assertTrue(output.contains("Which video would you like to open?"));
        verify(desktop).browse(new URI(url));
    }

    @Then("^no URL is opened in browser$")
    public void no_URL_is_opened_in_browser() throws IOException {

        runApplication();

        verify(desktop, times(0)).browse(any(URI.class));
    }

    @Then("^user is notified the entered row number is invalid$")
    public void user_notified_of_invalid_row_number() {
        addInputLine("cancel");

        runApplication();

        String output = outputStream.toString();
        assertTrue(output.contains("The row number entered was invalid"));
    }

    @Then("^user is prompted for a number$")
    public void user_is_prompted_for_a_number() {
        addInputLine("cancel");

        runApplication();

        String output = outputStream.toString();
        assertTrue(output.contains("Please enter a number"));
    }
    
    @Then("^video with title \"([^\"]*)\" and URL \"([^\"]*)\" is printed$")
    public void video_with_title_and_url_is_printed(String title, String url) {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains(title) && output.contains(url));
    }

    @Then("^podcast with name \"([^\"]*)\" and author \"([^\"]*)\" and title \"([^\"]*)\" and URL \"([^\"]*)\" is displayed$")
    public void podcast_is_displayed(String name, String author, String title, String url) {
        addInputLine("cancel");
        Podcast podcast = new Podcast(name, author, title, url);

        runApplication();

        String output = outputStream.toString();
        assertTrue(output.contains(podcast.toString()));
    }

    @Then("^edit command is cancelled and main menu displayed$")
    public void edit_command_is_cancelled() throws Throwable {

        runApplication();

        String output = outputStream.toString();
        assertFalse(output.contains("Enter new "));
    }

    @Then("^podcast with name \"([^\"]*)\" and author \"([^\"]*)\" and title \"([^\"]*)\" and URL \"([^\"]*)\" is not in the library$")
    public void podcast_is_not_in_library(String name, String author, String title, String url) throws Throwable {
        Podcast podToTest = new Podcast(name, author, title, url);
        runApplication();

        assertFalse(podToTest.equals(podcastDao.findOne(podToTest)));
    }

    @Then("^podcast with name \"([^\"]*)\" and author \"([^\"]*)\" and title \"([^\"]*)\" and URL \"([^\"]*)\" is in the library$")
    public void podcast_is_in_library(String name, String author, String title, String url) throws Throwable {
        Podcast podToTest = new Podcast(name, author, title, url);
        runApplication();

        assertTrue(podToTest.equals(podcastDao.findOne(podToTest)));
    }
    
    @Then("^user is notified that book is marked as read$")
    public void user_is_notified_that_book_is_marked_as_read() throws Throwable {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Book marked as read!"));
    }
    
    @Then("^user is notified that podcast is marked as listened$")
    public void user_is_notified_that_podcast_is_marked_as_listened() throws Throwable {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Podcast marked as listened!"));
    }

    @Then("^user is notified that video is marked as watched$")
    public void user_is_notified_that_video_is_marked_as_watched() throws Throwable {
        runApplication();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Video marked as watched!"));
    }



    /**
     * Adds a line to the input, which simulates same behaviour as typing text
     * and pressing enter in the command line when it is read.
     */
    private void addInputLine(String string) {
        input += System.getProperty("line.separator") + string;
    }

    /**
     * Prepares System input and ouput, and runs the application. First a 
     * command "quit" is added to the input to make sure the application stops 
     * running. Then sets system input as a byte array, which represents 
     * variable {@link #input} and output as variable {@link #outputStream}.
     * Finally creates a new instance of {@link BufferedReader} and along with
     * {@link #database} passes it as parameter to create and run an instance 
     * of the user interface.
     */

    private void runApplication() {
        addInputLine("quit");

        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        buffer = new BufferedReader(new InputStreamReader(System.in));
        ui = new UI(database, buffer, desktop);
        ui.run();        
    }
}

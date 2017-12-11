
package bookmarkmodels;

public class Podcast {
    private String name;
    private String author;
    private String title;
    private String url;
    private int checked;

    public Podcast(String name, String author, String title, String url, int checked) {
        this.name = name;
        this.author = author;
        this.title = title;
        this.url = url;
        this.checked = checked;
    }

    public Podcast(String name, String author, String title, String url) {
        this.name = name;
        this.author = author;
        this.title = title;
        this.url = url;
    }
    public Podcast(String name, String author, String title) {
        this.name = name;
        this.author = author;
        this.title = title;
    }

    public Podcast() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getChecked() {
        return checked;
    }

    @Override
    public String toString() {
        return "Podcast: " + "name: " + name + ", title: " + title + ", author: " + author + ", url: " + printOptionalField(url) + ", " + isChecked();
    }
    
    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(Podcast.class)) {
            return false;
        }
        
        Podcast comp = (Podcast) o;
        return  comp.getName().equals(this.name)
        		&& comp.getAuthor().equals(this.author)
                && comp.getUrl().equals(this.url)
                && comp.getTitle().equals(this.title);
    }

    private String isChecked() {
        if (checked == 0) {
            return "not listened";
        }
        return "listened";
    }

    private String printOptionalField(String text) {
        if (text.isEmpty()) {
            return "<empty>";
        }
        return text;
    }
}

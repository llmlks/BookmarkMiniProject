/**
 * 
 */
package bookmarkmodels;

/**
 * @author Admin
 *
 */
public class Video {
	private String URL;
	private String title;
        private int checked; 

    public Video(String URL, String title, int checked) {
        this.URL = URL;
        this.title = title;
        this.checked = checked;
    }

    public Video(String URL, String title) {
    	this.URL = URL;
    	this.title = title;
    }

    public Video(String URL) {
        this.URL = URL;
    }

    public Video() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public String getTitle() {
        return title;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int newChecked) {
        this.checked = newChecked;
    }

    @Override
    public String toString() {
        return "Video: "  + "URL: " + URL + ", title: " + printOptionalField(title) + ", " + isChecked();
    }
    
    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(Video.class)) {
            return false;
        }
        
        Video comp = (Video) o;
        return comp.getURL().equals(this.URL);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.URL.hashCode();
        hash = 79 * hash + this.title.hashCode();
        return hash;
    }

    private String isChecked() {
        if (checked == 0) {
            return "not watched";
        }
        return "watched";
    }

    private String printOptionalField(String text) {
        if (text.isEmpty()) {
            return "<empty>";
        }
        return text;
    }
}
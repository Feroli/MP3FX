package MP3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Feroli on 11/04/15.
 */

public class musicItems {

    private  SimpleStringProperty artist;
    private SimpleStringProperty title;
    private  SimpleStringProperty duration;

    public musicItems(String aName, String tName, String dur) {
        this.artist = new SimpleStringProperty(aName);
        this.title = new SimpleStringProperty(tName);
        this.duration = new SimpleStringProperty(dur);
    }

    public String getArtist() {
        return artist.get();
    }

    public void setArtistName(String aName) {
        artist.set(aName);
    }

    public StringProperty artistProperty() {
        if (artist == null) artist = new SimpleStringProperty(this, "artist");
        return artist;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String tName) {
        title.set(tName);
    }

    public StringProperty titleProperty() {
        if (title == null) title = new SimpleStringProperty(this, "title");
        return title;
    }


    public String getDuration() {
        return duration.get();
    }

    public void setDuration(String dur) {
        duration.set(dur);}
}


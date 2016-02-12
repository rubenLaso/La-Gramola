package fernandoruben.mp3player;

/**
 * Created by ruben on 3/02/16.
 */
public class AudioFile {
    private String id;
    private String title;
    private String autor;
    private String disco;
    private String path;

    public AudioFile(String id, String title, String autor, String disco, String path) {
        this.id = id;
        this.title = title;
        this.autor = autor;
        this.disco = disco;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAutor() {
        return autor;
    }

    public String getDisco() {
        return disco;
    }

    public String getPath() {
        return path;
    }
}

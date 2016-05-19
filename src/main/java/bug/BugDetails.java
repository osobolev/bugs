package bug;

/**
 * Created by oleg on 12.05.2016.
 */
public class BugDetails {

    private int id;
    private String text;
    private String description;

    public BugDetails(int id, String text, String description) {
        this.id = id;
        this.text = text;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public String getText() {
        return text;
    }
    public String getDescription() {
        return description;
    }
}

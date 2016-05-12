package bug;

/**
 * Created by oleg on 12.05.2016.
 */
public class BugDetails {

    private int id;
    private String description;

    public BugDetails(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}

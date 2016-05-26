package bug;

import common.Status;

/**
 * Created by oleg on 12.05.2016.
 */
public class BugDetails {

    private int id;
    private String text;
    private String description;
    private Status status;

    public BugDetails(int id, String text, String description, Status status) {
        this.id = id;
        this.text = text;
        this.description = description;
        this.status = status;
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

    public String getStatus() {
        return status.translate();
    }
}

package bug;

import common.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by oleg on 02.06.2016.
 */
public class BugHistoryItem {

    private LocalDateTime date;
    private Status oldStatus;
    private Status newStatus;
    private String user;
    private String comment;

    public BugHistoryItem(LocalDateTime date, Status oldStatus, Status newStatus, String user, String comment) {
        this.date = date;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.user = user;
        this.comment = comment;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
    }

    public String getOldStatus() {
        return oldStatus.translate();
    }

    public String getNewStatus() {
        return newStatus.translate();
    }

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }
}

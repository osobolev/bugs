package bugs;

import common.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BugInList {

    private int id;
    private String text;
    private LocalDateTime createTime;
    private Status status;
    private int priority;

    public BugInList(int id, String text, LocalDateTime createTime, Status status, int priority) {
        this.id = id;
        this.text = text;
        this.createTime = createTime;
        this.status = status;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getCreateTime() {
        return createTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
    }

    public String getStatus() {
        return status.translate();
    }

    public int getPriority() {
        return priority;
    }
}

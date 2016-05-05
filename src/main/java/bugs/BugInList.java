package bugs;

import common.Status;

import java.time.LocalDateTime;

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Status getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }
}
